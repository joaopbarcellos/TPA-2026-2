package geradorArquivos;

import dominio.Contato;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

public class GeradorArquivosBalanceados {

    private static final int NUM_REGISTROS = 2_000_000;

    private static final String[] PRENOMES = { "Ana", "Bruno", "Carlos", "Daniela", "Eduardo", "Fernanda", "Gabriel", "Helena", "Isabela", "João", "Juliana", "Lucas", "Mariana", "Nathan", "Olivia", "Paulo", "Quésia", "Rafael", "Sofia", "Thiago", "Victor", "William", "Xavier", "Yasmin", "Zuleica", "Alfredo", "Beatriz", "Caio", "Denise", "Eliana", "Felipe", "Gustavo", "Heitor", "Igor", "Jéssica", "Kevin", "Larissa", "Mateus", "Natália", "Otávio", "Patrícia", "Renato", "Sandra", "Tadeu", "Ursula", "Vinícius", "Wellington", "Zilda", "Adriana", "Benício", "Cristina", "Davi", "Emanuel", "Flávia", "Geraldo", "Heloísa", "Ícaro", "Jaqueline", "Leonardo", "Marta", "Nelson", "Orlando", "Priscila", "Raquel", "Saulo", "Tatiane", "Ubirajara", "Vera", "Wesley", "Zenaide", "Alice", "Brenda", "Caetano", "Danilo", "Enzo", "Fabiana", "Gilberto", "Henrique", "Isadora", "José", "Kátia", "Lorena", "Maurício", "Natanael", "Osvaldo", "Pamela", "Regina", "Sandro", "Tânia", "Ulisses", "Vânia", "Wilson", "Yago", "Zélia", "Amélia", "Bernardo", "Celso", "Dulce", "Edson", "Fátima", "Gilmar", "Humberto", "Irene", "Jorge", "Kleber", "Luciana", "Marcelo", "Nadir", "Otacílio", "Paula", "Renata", "Aline", "Arthur", "Bianca", "Cauê", "Débora", "Diego", "Elaine", "Fabrício", "Giovana", "Hugo", "Ingrid", "Joaquim", "Karina", "Leandro", "Manuela", "Nicolas", "Paola", "Roberto", "Simone", "Tomás", "Valentina", "Wagner", "Yuri", "Amanda", "Bárbara", "César", "Daiane", "Douglas", "Ester", "Fábio", "Geovana", "Hélio", "Iara", "Jonas", "Kelly", "Luan", "Melissa", "Murilo", "Nicole", "Ramon", "Sabrina", "Samuel", "Talita", "Vanessa", "Washington", "Yasmim", "Adão", "Alana", "Augusto", "Brenda", "Camila", "Cristiano", "Débora", "Elisa", "Francisco", "Giulia", "Hélio", "Janaina", "Kaio", "Lívia", "Miguel", "Noah", "Raul", "Stella", "Theo", "Viviane", "Wallace", "Yohana", "Alex", "Brenda", "Clarice", "Danilo", "Elisa", "Frederico", "Gabriela", "Ítalo", "Júlia", "Lorenzo", "Mirela", "Nathalia", "Otto", "Rebeca", "Rodrigo", "Sarah", "Valter", "Yuri", "Adalberto", "Bernardo", "Cecília", "Diana", "Erick", "Evelyn", "Frederico", "Graziella", "Hugo", "Ivana", "Jean", "Lara", "Matheus", "Natasha", "Olavo", "Rafaela", "Serena", "Tales", "Vitor", "Weslley", "Yara", "Ari", "Breno" };

    private static final String[] SOBRENOMES = { "Almeida", "Barbosa", "Campos", "Dias", "Evangelista", "Ferreira", "Gomes", "Henrique", "Iglesias", "Junqueira", "Klein", "Lima", "Medeiros", "Nascimento", "Oliveira", "Pereira", "Queiroz", "Rodrigues", "Silva", "Teixeira", "Uchoa", "Vasconcelos", "Watanabe", "Ximenes", "Yamamoto", "Zanetti", "Araújo", "Borges", "Coelho", "Dantas", "Esteves", "Farias", "Guimarães", "Holanda", "Ivo", "Jardim", "Krieger", "Lacerda", "Monteiro", "Neves", "Oliveira", "Porto", "Quintana", "Ramos", "Sanches", "Torrico", "Urbano", "Vieira", "Wanderley", "Xavier", "Yunes", "Zampieri", "Abreu", "Barreto", "Coutinho", "Delgado", "Elias", "França", "Godoy", "Haddad", "Ibrahim", "Jacob", "Lopes", "Moura", "Nogueira", "Ortega", "Pinto", "Quaresma", "Reis", "Souto", "Torres", "Ubaldo", "Valente", "Weber", "Ximenes", "Yamaguchi", "Zanella", "Alvarenga", "Bittencourt", "Carvalho", "Duarte", "Espíndola", "Freitas", "Gonçalves", "Herrera", "Ishikawa", "Junqueira", "Lacerda", "Mancini", "Noronha", "Orsini", "Paz", "Quevedo", "Rangel", "Souza", "Tavares", "Uchoa", "Vilela", "Werneck", "Xisto", "Azevedo", "Batista", "Castro", "Domingues", "Estevão", "Figueiredo", "Garcia", "Horta", "Leite", "Macedo", "Nunes", "Peixoto", "Ribeiro", "Santos", "Teles", "Viana", "Andrade", "Bastos", "Cardoso", "Duarte", "Fonseca", "Freire", "Goulart", "Matos", "Moraes", "Pacheco", "Rezende", "Siqueira", "Soares", "Teodoro", "Valentim", "Aguiar", "Amaral", "Brito", "Cavalcante", "Chaves", "Corrêa", "Cunha", "Esteves", "Farias", "Fonseca", "Franco", "Guerra", "Lemos", "Machado", "Mendes", "Miranda", "Pires", "Prado", "Rosa", "Santana", "Serra", "Vargas", "Vasconcelos", "Amorim", "Antunes", "Bandeira", "Bezerra", "Braga", "Cabral", "Caldas", "Camargo", "Carvalho", "César", "Cordeiro", "Costa", "Couto", "Cunha", "Diniz", "Drummond", "Falcão", "Faria", "Galvão", "Gusmão", "Lopes", "Lourenço", "Magalhães", "Marques", "Meireles", "Mello", "Moraes", "Muniz", "Negrão", "Novaes", "Pimenta", "Quevedo", "Rangel", "Rezende", "Rios", "Rochа", "Rosa", "Sá", "Salazar", "Sampaio", "Santiago", "Saraiva", "Schmidt", "Silveira", "Tavares", "Toscano", "Varella", "Xavier", "Zanetti", "Assis", "Borges", "Bueno", "Câmara", "Carmo", "Carneiro", "Cerqueira", "Dourado", "Fagundes", "Farias", "Furtado", "Gouveia", "Lacerda", "Macedo", "Moraes", "Morais", "Moura", "Paes", "Pimentel", "Portela", "Ramos", "Rangel", "Saldanha", "Sampaio", "Seabra", "Tavares", "Torres", "Varela", "Vidal", "Vieira", "Xavier", "Zago" };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Escolha o tipo de balanceamento:");
        System.out.println("1 - Balanceado por nome");
        System.out.println("2 - Balanceado por telefone");
        System.out.print("Opção: ");

        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1:
                gerarArquivo(true);
                break;

            case 2:
                gerarArquivo(false);
                break;

            default:
                System.out.println("Opção inválida.");
        }

        scanner.close();
    }

    private static void gerarArquivo(boolean balancearPorNome) {

        Random random = new Random();
        ArrayList<Contato> contatos = new ArrayList<>(NUM_REGISTROS);

        System.out.println("Gerando contatos...");

        for (int i = 0; i < NUM_REGISTROS; i++) {

            String nome = gerarNomeAleatorio(random);
            String telefone = gerarTelefoneAleatorio(random);

            contatos.add(new Contato(nome, telefone));

            if ((i + 1) % 1_000_000 == 0) {
                System.out.println((i + 1) + " registros gerados...");
            }
        }

        System.out.println("Ordenando registros...");

        if (balancearPorNome) {
            contatos.sort(
                    Comparator.comparing(
                            Contato::getNome,
                            String.CASE_INSENSITIVE_ORDER
                    )
            );
        } else {
            contatos.sort(
                    Comparator.comparing(Contato::getTelefone)
            );
        }

        String nomeArquivo = balancearPorNome
                ? "contatosBalanceadosNome.txt"
                : "contatosBalanceadosTelefone.txt";

        System.out.println("Gerando arquivo balanceado...");

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(nomeArquivo, StandardCharsets.UTF_8))) {

            writer.write(NUM_REGISTROS + "\n");

            gerarArquivoBalanceado(
                    writer,
                    contatos,
                    0,
                    contatos.size() - 1
            );

            System.out.println("Arquivo gerado com sucesso: " + nomeArquivo);

        } catch (IOException e) {
            System.err.println(
                    "Erro ao escrever no arquivo: " + e.getMessage()
            );
        }
    }

    private static void gerarArquivoBalanceado(
            BufferedWriter writer,
            ArrayList<Contato> contatos,
            int inicio,
            int fim
    ) throws IOException {

        if (inicio > fim) {
            return;
        }

        int meio = inicio + (fim - inicio) / 2;

        Contato contato = contatos.get(meio);

        writer.write(
                contato.getNome()
                        + ";"
                        + contato.getTelefone()
                        + "\n"
        );

        gerarArquivoBalanceado(
                writer,
                contatos,
                inicio,
                meio - 1
        );

        gerarArquivoBalanceado(
                writer,
                contatos,
                meio + 1,
                fim
        );
    }

    private static String gerarNomeAleatorio(Random random) {
        String primeiroNome =
                PRENOMES[random.nextInt(PRENOMES.length)];

        String sobrenome =
                    SOBRENOMES[random.nextInt(SOBRENOMES.length)];

        return primeiroNome + " " + sobrenome;
    }

    private static String gerarTelefoneAleatorio(Random random) {

        int ddd = 11 + random.nextInt(89);
        int prefixo = 90000 + random.nextInt(10000);
        int numero = 1000 + random.nextInt(9000);

        return String.format(
                "(%02d) %05d-%04d",
                ddd,
                prefixo,
                numero
        );
    }
}
