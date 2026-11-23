
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class App {

    // Lista que armazena todos os pedidos cadastrados
    private static ArrayList<Pedido> pedidos = new ArrayList<>();

    private static Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {

        int opcao;

        do {
            System.out.println("\n===== SISTEMA DE PEDIDOS =====");
            System.out.println("1 - Cadastrar pedido");
            System.out.println("2 - Alterar status de um pedido");
            System.out.println("3 - Processar pedidos");
            System.out.println("4 - Remover pedidos cancelados");
            System.out.println("5 - Exibir lista de pedidos");
            System.out.println("6 - Exibir pedidos entregáveis");
            System.out.println("7 - Encerrar");
            System.out.print("Escolha uma opção: ");

            opcao = Integer.parseInt(entrada.nextLine());

            switch (opcao) {

                case 1:
                    cadastrarPedido();
                    break;

                case 2:
                    alterarStatus();
                    break;

                case 3:
                    processarPedidos();
                    break;

                case 4:
                    removerCancelados();
                    break;

                case 5:
                    exibirPedidos();
                    break;

                case 6:
                    exibirEntregaveis();
                    break;

                case 7:
                    System.out.println("Sistema encerrado!");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 7);

        entrada.close();
    }

    // 1 - CADASTRAR PEDIDO
    private static void cadastrarPedido() {

        try {
            System.out.print("ID do pedido: ");
            int id = Integer.parseInt(entrada.nextLine());

            // Verifica se já existe um pedido com esse ID
            for (Pedido p : pedidos) {
                if (p.getId() == id) {
                    System.out.println("Já existe um pedido com esse ID!");
                    return;
                }
            }

            System.out.print("Nome do cliente: ");
            String nome = entrada.nextLine();

            System.out.print("Valor do pedido: ");
            double valor = Double.parseDouble(entrada.nextLine());

            System.out.println("\nCategoria:");
            System.out.println("1 - Eletrônico");
            System.out.println("2 - Roupa");
            System.out.println("3 - Alimento");
            System.out.print("Escolha: ");

            int categoria = Integer.parseInt(entrada.nextLine());

            Pedido pedido;

            // Cria o objeto de acordo com a categoria escolhida
            switch (categoria) {

                case 1:
                    pedido = new PedidoEletronico(
                        id, nome, valor, StatusPedido.PENDENTE
                    );
                    break;

                case 2:
                    pedido = new PedidoRoupa(
                        id, nome, valor, StatusPedido.PENDENTE
                    );
                    break;

                case 3:
                    pedido = new PedidoAlimento(
                        id, nome, valor, StatusPedido.PENDENTE
                    );
                    break;

                default:
                    System.out.println("Categoria inválida!");
                    return;
            }

            pedidos.add(pedido);

            System.out.println("Pedido cadastrado com sucesso!");

        } catch (NumberFormatException e) {
            System.out.println("Digite um número válido!");

        } catch (ValorInvalidoException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    // 2 - ALTERAR STATUS
    private static void alterarStatus() {

        System.out.print("Digite o ID do pedido: ");
        int id = Integer.parseInt(entrada.nextLine());

        Pedido pedido = buscarPedido(id);

        if (pedido == null) {
            System.out.println("Pedido não encontrado!");
            return;
        }

        System.out.println("\nEscolha o novo status:");

        StatusPedido[] status = StatusPedido.values();

        for (int i = 0; i < status.length; i++) {
            System.out.println((i + 1) + " - " + status[i]);
        }

        System.out.print("Opção: ");
        int escolha = Integer.parseInt(entrada.nextLine());

        if (escolha < 1 || escolha > status.length) {
            System.out.println("Status inválido!");
            return;
        }

        try {
            pedido.alterarStatus(status[escolha - 1]);

            System.out.println("Status alterado com sucesso!");

        } catch (PedidoInvalidoException e) {
            System.out.println("Não foi possível alterar: " + e.getMessage());
        }
    }

    // 3 - PROCESSAR TODOS OS PEDIDOS USANDO ITERATOR
    private static void processarPedidos() {

        Iterator<Pedido> iterator = pedidos.iterator();

        while (iterator.hasNext()) {

            Pedido pedido = iterator.next();

            try {
                pedido.processarPedido();

                System.out.println(
                    "Pedido " + pedido.getId() + " processado!"
                );

            } catch (PedidoInvalidoException e) {

                System.out.println(
                    "Pedido " + pedido.getId()
                    + " não foi processado: " + e.getMessage()
                );
            }
        }
    }

    // 4 - REMOVER PEDIDOS CANCELADOS USANDO ITERATOR
    private static void removerCancelados() {

        Iterator<Pedido> iterator = pedidos.iterator();

        int quantidadeRemovida = 0;

        while (iterator.hasNext()) {

            Pedido pedido = iterator.next();

            if (pedido.getStatus() == StatusPedido.CANCELADO) {

                iterator.remove();

                quantidadeRemovida++;
            }
        }

        System.out.println(
            quantidadeRemovida + " pedido(s) cancelado(s) removido(s)."
        );
    }

    // 5 - EXIBIR PEDIDOS, EXCETO OS CANCELADOS
    private static void exibirPedidos() {

        System.out.println("\n===== LISTA DE PEDIDOS =====");

        boolean encontrou = false;

        for (Pedido pedido : pedidos) {

            if (pedido.getStatus() != StatusPedido.CANCELADO) {

                System.out.println(
                    "ID: " + pedido.getId()
                    + " | Status: " + pedido.getStatus()
                    + " | Valor final: R$ "
                    + pedido.calcularValorFinal()
                );

                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhum pedido para exibir.");
        }
    }

    // 6 - EXIBIR PEDIDOS ENTREGÁVEIS
    private static void exibirEntregaveis() {

        System.out.println("\n===== PEDIDOS ENTREGÁVEIS =====");

        boolean encontrou = false;

        for (Pedido pedido : pedidos) {

            // Verifica se o pedido implementa a interface Entregavel
            if (pedido instanceof Entregavel) {

                if (pedido.getStatus() != StatusPedido.CANCELADO) {

                    Entregavel entregavel = (Entregavel) pedido;

                    System.out.println(
                        "ID: " + pedido.getId()
                        + " | Status: " + pedido.getStatus()
                        + " | Frete: R$ "
                        + entregavel.calcularFrete()
                    );

                    encontrou = true;
                }
            }
        }

        if (!encontrou) {
            System.out.println("Nenhum pedido entregável encontrado.");
        }
    }

    // MÉTODO AUXILIAR PARA BUSCAR PEDIDO PELO ID
    private static Pedido buscarPedido(int id) {

        for (Pedido pedido : pedidos) {

            if (pedido.getId() == id) {
                return pedido;
            }
        }

        return null;
    }
}
