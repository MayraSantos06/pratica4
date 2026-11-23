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

    public void processarPedido() throws PedidoInvalidoException{
        

        if (status != StatusPedido.PENDENTE){
            throw new PedidoInvalidoException("Pedido invalido");
        }

        status = StatusPedido.PROCESSANDO;

        System.out.println("Pedido em processamento");
    
    }

public void alterarStatus(StatusPedido novoStatus)
        throws PedidoInvalidoException {

    if (this.status == StatusPedido.ENTREGUE) {
        throw new PedidoInvalidoException(
            "Pedido já foi entregue"
        );
    }

    if (novoStatus == null) {
        throw new NullPointerException(
            "O novo status não pode ser nulo"
        );
    }

    this.status = novoStatus;
}

    public int getId(){
        return id;
    }

     public StatusPedido getStatus(){
        return status;
    }

}
