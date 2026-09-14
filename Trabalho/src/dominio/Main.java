package dominio;

import java.util.Scanner;
import colecao.IColecao;
import listaEncadeada.ListaEncadeada;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        IColecao<Contato> listaContatoPorTelefone, listaContatoPorNome;
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Qual lista encadeada deseja utilizar?");
            System.out.println("1 - Lista Encadeada Ordenada");
            System.out.println("2 - Lista Encadeada Não Ordenada");
            int escolha = lerInteiro(scanner);
            while (escolha != 1 && escolha != 2) {
                System.out.println("Escolha inválida. Digite 1 ou 2.");
                escolha = lerInteiro(scanner);
            }
            if (escolha == 1) {
                listaContatoPorTelefone = new ListaEncadeada<Contato>(new ComparadorContatoPorTelefone(), true);
                listaContatoPorNome = new ListaEncadeada<Contato>(new ComparadorContatoPorNome(), true);
            } else {
                listaContatoPorTelefone = new ListaEncadeada<Contato>(new ComparadorContatoPorTelefone(), false);
                listaContatoPorNome = new ListaEncadeada<Contato>(new ComparadorContatoPorNome(), false);
            }

            int op = 0;

            while (op != 7) {
                printMenu();
                op = lerInteiro(scanner);
                switch (op) {
                    case 1:
                        LeitorArquivo leitor = new LeitorArquivo();
                        leitor.lerArquivo(listaContatoPorNome, listaContatoPorTelefone);
                        System.out.println(listaContatoPorNome.quantidadeNos() + " contatos carregados com sucesso!");
                        break;
                    case 2:
                        System.out.println("Digite o nome do contato:");
                        String nome = lerLinha(scanner);
                        System.out.println("Digite o telefone do contato:");
                        String telefone = lerLinha(scanner);

                        long inicioAdicao = System.nanoTime();
                        Contato novoContato = new Contato(nome, telefone);
                        listaContatoPorNome.adicionar(novoContato);
                        listaContatoPorTelefone.adicionar(novoContato);
                        long tempoAdicao = System.nanoTime() - inicioAdicao;
                        double tempoEmSegundos = tempoAdicao / 1_000_000_000.0;
                        double tempoEmMilissegundos = tempoAdicao / 1_000_000.0;

                        System.out.println("Contato adicionado com sucesso!");
                        System.out.println("Tempo de adição: " + String.format("%.6f s | %.3f ms", tempoEmSegundos, tempoEmMilissegundos));
                        break;
                    case 3:
                        System.out.println("Digite o nome do contato a ser pesquisado:");
                        String nomePesquisa = lerLinha(scanner);
                        Contato contatoNome = new Contato(nomePesquisa, "");
                        Contato resultadoNome = listaContatoPorNome.pesquisar(contatoNome);
                        if (resultadoNome != null) {
                            System.out.println("Contato encontrado: " + resultadoNome);
                            break;
                        }
                        System.out.println("Contato não encontrado.");
                        break;
                    case 4:
                        System.out.println("Digite o telefone do contato a ser pesquisado:");
                        String telefonePesquisa = lerLinha(scanner);
                        Contato contatoTelefone = new Contato("", telefonePesquisa);
                        Contato resultadoTelefone = listaContatoPorTelefone.pesquisar(contatoTelefone);
                        if (resultadoTelefone != null) {
                            System.out.println("Contato encontrado: " + resultadoTelefone);
                            break;
                        }
                        System.out.println("Contato não encontrado.");
                        break;
                    case 5:
                        System.out.println("Digite o telefone do contato a ser removido:");
                        String telefoneRemover = lerLinha(scanner);
                        Contato contatoRemover = new Contato("", telefoneRemover);
                        Contato resultadoTelefone = listaContatoPorTelefone.pesquisar(contatoRemover);
                        if (resultadoTelefone == null) {
                            System.out.println("Contato não encontrado");
                            break;
                        }
                        boolean removido = listaContatoPorTelefone.remover(resultadoTelefone);
                        if (removido) {
                            listaContatoPorNome.remover(resultadoTelefone);
                            System.out.println("Contato removido com sucesso.");
                            break;
                        }
                        System.out.println("Contato não encontrado.");
                        break;
                    case 6:
                        System.out.println("Digite o nome do contato a ser alterado:");
                        String nomeAlterar = lerLinha(scanner);
                        Contato contatoAlterar = new Contato(nomeAlterar, "");
                        Contato resultadoAlterar = listaContatoPorNome.pesquisar(contatoAlterar);
                        if (resultadoAlterar != null) {
                            System.out.println("Contato encontrado: " + resultadoAlterar);
                            System.out.println("Digite o novo nome do contato:");
                            String novoNome = lerLinha(scanner);
                            System.out.println("Digite o novo telefone do contato:");
                            String novoTelefone = lerLinha(scanner);
                            listaContatoPorNome.remover(resultadoAlterar);
                            listaContatoPorTelefone.remover(resultadoAlterar);
                            Contato contatoAtualizado = new Contato(novoNome, novoTelefone);
                            listaContatoPorNome.adicionar(contatoAtualizado);
                            listaContatoPorTelefone.adicionar(contatoAtualizado);
                            System.out.println("Contato atualizado com sucesso.");
                            break;
                        }
                        System.out.println("Contato não encontrado.");
                        break;
                }
            }
            System.out.println("Quantidade total de contatos na lista: " + listaContatoPorNome.quantidadeNos());
            System.out.println("Programa encerrado.");
            scanner.close();
        } catch (Exception e) {
            scanner.close();
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static int lerInteiro(Scanner scanner) {
        String entrada = scanner.nextLine().trim();
        while (entrada.isEmpty()) {
            entrada = scanner.nextLine().trim();
        }
        return Integer.parseInt(entrada);
    }

    private static String lerLinha(Scanner scanner) {
        String entrada = scanner.nextLine().trim();
        while (entrada.isEmpty()) {
            entrada = scanner.nextLine().trim();
        }
        return entrada;
    }

    public static void printMenu() {
        System.out.println("O que deseja fazer?");
        System.out.println("1 - Carregar dados de arquivo");
        System.out.println("2 - Adicionar contato");
        System.out.println("3 - Pesquisar contato por nome");
        System.out.println("4 - Pesquisar contato por telefone");
        System.out.println("5 - Remover contato por telefone");
        System.out.println("6 - Alterar dados de contato");
        System.out.println("7 - Sair");
    }
}
