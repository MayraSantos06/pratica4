public class PedidoEletronico extends Pedido implements Entregavel{
 
    public PedidoEletronico(int id, String nomeCliente, double valor, StatusPedido status) throws ValorInvalidoException{

        super(id, nomeCliente, valor, status, Categoria.ELETRONICO);

    }

    @Override
    public double calcularFrete(){
        return 20 + (valor * 0.1);
    }

    @Override
    public double calcularValorFinal(){
        double desconto = 0;
        
        if (valor >100){
            desconto = (valor * 0.1);
        }

        frete = calcularFrete();

        return valor - desconto + frete;
    }
    
}