public class PedidoRoupa extends Pedido implements Entregavel{
 
    public PedidoRoupa(int id, String nomeCliente, double valor, StatusPedido status) throws ValorInvalidoException{

        super(id, nomeCliente, valor, status, Categoria.ROUPA);

    }

    @Override
    public double calcularFrete(){
        return 10 + (valor * 0.05);
    }

    @Override
    public double calcularValorFinal(){
        double desconto = 0;
        
        if (valor >200){
            desconto = (valor * 0.2);
        }

        frete = calcularFrete();

        return valor - desconto + frete;
    }
    
}