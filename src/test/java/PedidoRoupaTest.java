import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class PedidoRoupaTest {

    private PedidoRoupa pedido;

    @BeforeEach 
    public void configurar() throws ValorInvalidoException{
        pedido = new PedidoRoupa(2, "Cliente", 300, StatusPedido.PENDENTE);

        System.out.print("Iniciando teste de roupa...");
    }

    @AfterEach
    public void finalizar() {
        System.out.println("Teste de roupa finalizado!");
    }

    @Test
    public void testarCalculoValorFinal() {
        // 300 - 60 de desconto + 25 de frete = 265
        assertEquals(265, pedido.calcularValorFinal(), 0.01);
    }

    @Test
    public void testarCalculoFrete() {
        // 10 + 5% de 300 = 25
        assertEquals(25, pedido.calcularFrete(), 0.01);
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
            new PedidoRoupa(
                3, "Cliente", -50,
                StatusPedido.PENDENTE
            );
        });
    }
}
    
