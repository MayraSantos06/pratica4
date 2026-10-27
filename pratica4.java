public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
    }
}

public enum StatusPedido{
    PENDENTE,
    PROCESSANDO,
    ENVIADO,
    ENTREGUE,
    CANCELADO
}

public enum Categoria{
    ELETRONICO,
    ROUPA,
    ALIMENTO
}

public interface Processavel{

    void processarPedido() throws PedidoInvalidoException;

    double calcularValorFinal();
} 

public interface Entregavel{
    double calcularFrete();
}



public class PedidoInvalidoException extends Exception{
    
    public PedidoInvalidoException (String mensagem){
        super(mensagem);
    }
}

public class ValorInvalidoException extends Exception{
    
    public ValorInvalidoException (String mensagem){
        super(mensagem);
    }
}

public abstract class Pedido implements Processavel{
    protected int id;
    protected String nomeCliente;
    protected double valor;
    protected StatusPedido status;
    protected Categoria categoria;
    protected  double frete;

    public Pedido( int id, String nomeCliente,double valor,StatusPedido status,Categoria categoria)
    throws ValorInvalidoException {

        if (valor <= 0){
            throw new ValorInvalidoException("valor invalido!");
        }
        
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.valor = valor;
        this.status = status;
        this.categoria = categoria;
    }

    public void processarPedido() throws PedidoInvalidoExeption{
        

        if (status != StatusPedido.PENDENTE){
            throw new PedidoInvalidoException("Pedido invalido");
        }

        status = StatusPedido.PROCESSANDO;

        System.out.println("Pedido em processamento");
    
    }

    public void alterarStatus(StatusPedido novoStatus){

        if(status == StatusPedido.ENTREGUE){
            throw new PedidoInvalidoException("Pedido entregue, status não pode ser alterado");
        }

        status = novoStatus;
    }

    public int getId(){
        return id;
    }

     public StatusPedido getStatus(){
        return status;
    }

}


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

public class PedidoAlimento extends Pedido{
 
    public PedidoAlimento(int id, String nomeCliente, double valor, StatusPedido status) throws ValorInvalidoException{

        super(id, nomeCliente, valor, status, Categoria.ROUPA);

    }

    @Override
    public double calcularValorFinal(){

        return valor + 5;
    }
    
}
