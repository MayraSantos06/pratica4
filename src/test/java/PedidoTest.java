import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

// Classe responsável por testar as regras da classe Pedido.
public class PedidoTest {

    // Declara uma variável do tipo Pedido.
    // Como Pedido é abstrata, utilizamos uma classe filha
    // (PedidoRoupa) para criar o objeto.
    private Pedido pedido;

    // @BeforeEach: executa este método antes de cada teste.
    // Assim, cada teste começa com um pedido novo.
    @BeforeEach
    public void configurar() throws ValorInvalidoException {

        pedido = new PedidoRoupa(
            5, "Cliente", 300,
            StatusPedido.PENDENTE
        );

        System.out.println("Iniciando teste da classe Pedido...");
    }

    // @AfterEach: executa este método depois de cada teste.
    // Aqui, mostramos uma mensagem informando o encerramento.
    @AfterEach
    public void finalizar() {
        System.out.println("Teste da classe Pedido finalizado!");
    }

    // Testa se o processamento altera corretamente o status.
    @Test
    public void testarProcessamentoPedido()
            throws PedidoInvalidoException {

        // Executa o processamento do pedido.
        pedido.processarPedido();

        // assertEquals compara o valor esperado com o obtido.
        // Primeiro parâmetro: status esperado.
        // Segundo parâmetro: status atual do pedido.
        assertEquals(
            StatusPedido.PROCESSANDO,
            pedido.getStatus()
        );
    }

    // Testa se não é possível processar o mesmo pedido duas vezes.
    @Test
    public void testarPedidoJaProcessado() {

        // assertThrows verifica se uma exceção específica acontece.
        // PedidoInvalidoException.class: exceção esperada.
        // () ->: função lambda que contém o código a ser executado.
        assertThrows(PedidoInvalidoException.class, () -> {
            pedido.processarPedido();
            pedido.processarPedido();
        });
    }

    // Testa a regra que impede alterações em pedidos entregues.
    @Test
    public void testarAlteracaoPedidoEntregue()
            throws PedidoInvalidoException {

        // Altera o status para ENTREGUE.
        pedido.alterarStatus(StatusPedido.ENTREGUE);

        // Tenta alterar novamente para CANCELADO.
        // A operação deve gerar PedidoInvalidoException.
        assertThrows(PedidoInvalidoException.class, () -> {
            pedido.alterarStatus(StatusPedido.CANCELADO);
        });
    }

    // Testa se o construtor impede a criação de pedidos
    // com valores negativos.
    @Test
    public void testarValorInvalido() {

        // Verifica se um valor de -50 provoca
        // a exceção ValorInvalidoException.
        assertThrows(ValorInvalidoException.class, () -> {
            new PedidoRoupa(
                6, "Cliente", -50,
                StatusPedido.PENDENTE
            );
        });
    }

    // Testa uma exceção nativa do Java.
    @Test
    public void testarExcecaoJava() {

        // Passar null como novo status deve provocar
        // uma NullPointerException.
        assertThrows(NullPointerException.class, () -> {
            pedido.alterarStatus(null);
        });
    }
}
