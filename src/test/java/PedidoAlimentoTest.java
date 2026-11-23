import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

// Classe que contém os testes automatizados da classe PedidoAlimento.
public class PedidoAlimentoTest {

    // Declara um objeto do tipo PedidoAlimento.
    // Ele será utilizado em todos os testes desta classe.
    private PedidoAlimento pedido;

    // @BeforeEach: indica que este método será executado
    // antes de CADA teste.
    // Isso garante que cada teste comece com um pedido novo.
    @BeforeEach
    public void configurar() throws ValorInvalidoException {

        // Cria um pedido com:
        // 3 = identificador do pedido;
        // "Cliente" = nome do cliente;
        // 100 = valor original do pedido;
        // StatusPedido.PENDENTE = status inicial do pedido.
        pedido = new PedidoAlimento(
            3, "Cliente", 100,
            StatusPedido.PENDENTE
        );

        System.out.println("Iniciando teste de alimento...");
    }

    // @AfterEach: indica que este método será executado
    // depois de CADA teste.
    // Neste caso, mostra uma mensagem no terminal.
    @AfterEach
    public void finalizar() {
        System.out.println("Teste de alimento finalizado!");
    }

    // @Test: informa ao JUnit que este método é um teste.
    @Test
    public void testarCalculoValorFinal() {

        // assertEquals: compara o resultado esperado com o resultado obtido.
        //
        // 105 = valor esperado (100 + 5 de acréscimo).
        // pedido.calcularValorFinal() = valor calculado pelo programa.
        // 0.01 = margem de tolerância para diferenças decimais.
        //
        // Se a diferença entre os valores for maior que 0.01,
        // o teste falha.
        assertEquals(105, pedido.calcularValorFinal(), 0.01);
    }

    // Testa se o pedido muda corretamente de PENDENTE para PROCESSANDO.
    @Test
    public void testarProcessamentoPedido()
            throws PedidoInvalidoException {

        // Executa o método responsável por processar o pedido.
        pedido.processarPedido();

        // Verifica se o status atual é PROCESSANDO.
        //
        // Primeiro valor: status esperado.
        // Segundo valor: status atual do pedido.
        assertEquals(
            StatusPedido.PROCESSANDO,
            pedido.getStatus()
        );
    }

    // Testa se o sistema impede processar um pedido duas vezes.
    @Test
    public void testarPedidoJaProcessado() {

        // assertThrows: verifica se uma exceção específica acontece.
        //
        // PedidoInvalidoException.class = tipo de exceção esperado.
        // () -> { ... } = bloco de código que será executado.
        //
        // A primeira chamada processa o pedido.
        // A segunda tentativa deve lançar PedidoInvalidoException.
        assertThrows(PedidoInvalidoException.class, () -> {
            pedido.processarPedido();
            pedido.processarPedido();
        });
    }

    // Testa se não é possível alterar o status
    // de um pedido que já foi entregue.
    @Test
    public void testarAlteracaoPedidoEntregue()
            throws PedidoInvalidoException {

        // Altera o status do pedido para ENTREGUE.
        pedido.alterarStatus(StatusPedido.ENTREGUE);

        // Verifica se tentar mudar o status novamente
        // provoca a exceção PedidoInvalidoException.
        assertThrows(PedidoInvalidoException.class, () -> {
            pedido.alterarStatus(StatusPedido.CANCELADO);
        });
    }

    // Testa se o sistema rejeita pedidos com valores negativos.
    @Test
    public void testarValorInvalido() {

        // Verifica se criar um pedido com valor -50
        // provoca a exceção ValorInvalidoException.
        //
        // ValorInvalidoException.class = exceção esperada.
        // () -> { ... } = código que será executado pelo teste.
        assertThrows(ValorInvalidoException.class, () -> {
            new PedidoAlimento(
                4, "Cliente", -50,
                StatusPedido.PENDENTE
            );
        });
    }

    // Testa uma exceção nativa do Java.
    @Test
    public void testarExcecaoJava() {

        // Verifica se passar null como novo status
        // provoca uma NullPointerException.
        //
        // NullPointerException.class = exceção esperada.
        // pedido.alterarStatus(null) = operação que deve gerar o erro.
        assertThrows(NullPointerException.class, () -> {
            pedido.alterarStatus(null);
        });
    }
}
