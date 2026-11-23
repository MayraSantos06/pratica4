public class PedidoAlimento extends Pedido{
 
    public PedidoAlimento(int id, String nomeCliente, double valor, StatusPedido status) throws ValorInvalidoException{

        super(id, nomeCliente, valor, status, Categoria.ALIMENTO);

    }

    @Override
    public double calcularValorFinal(){

        return valor + 5;
    }
    
}