
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class PedidoEletronicoTest {

    private PedidoEletronico pedido;

    @BeforeEach
    public void configurar() throws ValorInvalidoException {
        pedido = new PedidoEletronico(
            1, "Cliente", 150,
            StatusPedido.PENDENTE
        );

        System.out.println("Iniciando teste...");
    }

    @AfterEach
    public void finalizar() {
        System.out.println("Teste finalizado");
    }

    @Test
    public void testarCalculoValorFinal() {
        // 150 - 15 de desconto + 35 de frete = 170
        assertEquals(170, pedido.calcularValorFinal(), 0.01);
    }

    @Test
    public void testarCalculoFrete() {
        assertEquals(35, pedido.calcularFrete(), 0.01);
    }

    @Test
    public void testarProcessamentoPedido()
            throws PedidoInvalidoException {

        pedido.processarPedido();

        assertEquals(
            StatusPedido.PROCESSANDO,
            pedido.getStatus()
        );
    }

    @Test
    public void testarPedidoJaProcessado() {
        assertThrows(PedidoInvalidoException.class, () -> {
            pedido.processarPedido();
            pedido.processarPedido();
        });
    }

    @Test
    public void testarAlteracaoPedidoEntregue()
            throws PedidoInvalidoException {

        pedido.alterarStatus(StatusPedido.ENTREGUE);

        assertThrows(PedidoInvalidoException.class, () -> {
            pedido.alterarStatus(StatusPedido.CANCELADO);
        });
    }

    @Test
    public void testarValorInvalido() {
        assertThrows(ValorInvalidoException.class, () -> {
            new PedidoEletronico(
                2, "Cliente", -50,
                StatusPedido.PENDENTE
            );
        });
    }

    @Test
    public void testarExcecaoJava() {
        assertThrows(NullPointerException.class, () -> {
            pedido.alterarStatus(null);
        });
    }
}
