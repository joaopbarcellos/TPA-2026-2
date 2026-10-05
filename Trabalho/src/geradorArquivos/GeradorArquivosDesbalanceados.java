package geradorArquivos;

import dominio.Contato;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class GeradorArquivosDesbalanceados {

    private static final int NUM_REGISTROS = 50000;
    private static final String NOME_ARQUIVO = "arq50kDesbalanceado.txt";

    private static final String[] PRENOMES = {
            "Ana", "Bruno", "Carlos", "Daniela", "Eduardo", "Fernanda", "Gabriel", "Helena",
            "Isabela", "João", "Juliana", "Lucas", "Mariana", "Nathan", "Olivia", "Paulo",
            "Quésia", "Rafael", "Sofia", "Thiago", "Victor", "William", "Xavier", "Yasmin",
            "Zuleica", "Alfredo", "Beatriz", "Caio", "Denise", "Eliana", "Felipe", "Gustavo",
            "Heitor", "Igor", "Jéssica", "Kevin", "Larissa", "Mateus", "Natália", "Otávio",
            "Patrícia", "Renato", "Sandra", "Tadeu", "Ursula", "Vinícius", "Wellington",
            "Zilda", "Adriana", "Benício", "Cristina", "Davi", "Emanuel", "Flávia", "Geraldo",
            "Heloísa", "Ícaro", "Jaqueline", "Leonardo", "Marta", "Nelson", "Orlando",
            "Priscila", "Raquel", "Saulo", "Tatiane", "Ubirajara", "Vera", "Wesley",
            "Zenaide", "Alice", "Brenda", "Caetano", "Danilo", "Enzo", "Fabiana", "Gilberto",
            "Henrique", "Isadora", "José", "Kátia", "Lorena", "Maurício", "Natanael", "Osvaldo",
            "Pamela", "Regina", "Sandro", "Tânia", "Ulisses", "Vânia", "Wilson", "Yago",
            "Zélia", "Amélia", "Bernardo", "Celso", "Dulce", "Edson", "Fátima", "Gilmar",
            "Humberto", "Irene", "Jorge", "Kleber", "Luciana", "Marcelo", "Nadir", "Otacílio",
            "Paula", "Renata", "Aline", "Arthur", "Bianca", "Cauê", "Débora", "Diego",
            "Elaine", "Fabrício", "Giovana", "Hugo", "Ingrid", "Joaquim", "Karina", "Leandro",
            "Manuela", "Nicolas", "Paola", "Roberto", "Simone", "Tomás", "Valentina", "Wagner",
            "Yuri", "Amanda", "Bárbara", "César", "Daiane", "Douglas", "Ester", "Fábio",
            "Geovana", "Hélio", "Iara", "Jonas", "Kelly", "Luan", "Melissa", "Murilo", "Nicole",
            "Ramon", "Sabrina", "Samuel", "Talita", "Vanessa", "Washington", "Yasmim", "Adão",
            "Alana", "Augusto", "Camila", "Cristiano", "Elisa", "Francisco", "Giulia", "Janaina",
            "Kaio", "Lívia", "Miguel", "Noah", "Raul", "Stella", "Theo", "Viviane", "Wallace",
            "Yohana", "Alex", "Clarice", "Frederico", "Gabriela", "Ítalo", "Júlia", "Lorenzo",
            "Mirela", "Nathalia", "Otto", "Rebeca", "Rodrigo", "Sarah", "Valter", "Adalberto",
            "Cecília", "Diana", "Erick", "Evelyn", "Graziella", "Ivana", "Jean", "Lara",
            "Matheus", "Natasha", "Olavo", "Rafaela", "Serena", "Tales", "Vitor", "Weslley",
            "Yara", "Ari", "Breno"
    };

    private static final String[] SOBRENOMES = {
            "Almeida", "Barbosa", "Campos", "Dias", "Evangelista", "Ferreira", "Gomes",
            "Henrique", "Iglesias", "Junqueira", "Klein", "Lima", "Medeiros", "Nascimento",
            "Oliveira", "Pereira", "Queiroz", "Rodrigues", "Silva", "Teixeira", "Uchoa",
            "Vasconcelos", "Watanabe", "Ximenes", "Yamamoto", "Zanetti", "Araújo", "Borges",
            "Coelho", "Dantas", "Esteves", "Farias", "Guimarães", "Holanda", "Ivo", "Jardim",
            "Krieger", "Lacerda", "Monteiro", "Neves", "Porto", "Quintana", "Ramos", "Sanches",
            "Torrico", "Urbano", "Vieira", "Wanderley", "Xavier", "Yunes", "Zampieri", "Abreu",
            "Barreto", "Coutinho", "Delgado", "Elias", "França", "Godoy", "Haddad", "Ibrahim",
            "Jacob", "Lopes", "Moura", "Nogueira", "Ortega", "Pinto", "Quaresma", "Reis", "Souto",
            "Torres", "Ubaldo", "Valente", "Weber", "Yamaguchi", "Zanella", "Alvarenga",
            "Bittencourt", "Carvalho", "Duarte", "Espíndola", "Freitas", "Gonçalves", "Herrera",
            "Ishikawa", "Mancini", "Noronha", "Orsini", "Paz", "Quevedo", "Rangel", "Souza",
            "Tavares", "Vilela", "Werneck", "Xisto", "Azevedo", "Batista", "Castro",
            "Domingues", "Estevão", "Figueiredo", "Garcia", "Horta", "Leite", "Macedo", "Nunes",
            "Peixoto", "Ribeiro", "Santos", "Teles", "Viana", "Andrade", "Bastos", "Cardoso",
            "Fonseca", "Freire", "Goulart", "Matos", "Moraes", "Pacheco", "Rezende", "Siqueira",
            "Soares", "Teodoro", "Valentim", "Aguiar", "Amaral", "Brito", "Cavalcante", "Chaves",
            "Corrêa", "Cunha", "Franco", "Guerra", "Lemos", "Machado", "Mendes", "Miranda",
            "Pires", "Prado", "Rosa", "Santana", "Serra", "Vargas", "Amorim", "Antunes",
            "Bandeira", "Bezerra", "Braga", "Cabral", "Caldas", "Camargo", "César", "Cordeiro",
            "Costa", "Couto", "Diniz", "Drummond", "Falcão", "Faria", "Galvão", "Gusmão",
            "Lourenço", "Magalhães", "Marques", "Meireles", "Mello", "Muniz", "Negrão", "Novaes",
            "Pimenta", "Portela", "Saldanha", "Sampaio", "Seabra", "Toscano", "Varela", "Vidal",
            "Assis", "Bueno", "Câmara", "Carmo", "Carneiro", "Cerqueira", "Dourado", "Fagundes",
            "Furtado", "Gouveia", "Morais", "Paes", "Pimentel", "Rios", "Salazar", "Santiago",
            "Saraiva", "Schmidt", "Silveira", "Varella", "Zago"
    };

    public static void main(String[] args) {
        gerarArquivo();
    }

    private static void gerarArquivo() {

        Random random = new Random();

        ArrayList<Contato> contatos = new ArrayList<>(NUM_REGISTROS);

        System.out.println("Gerando " + NUM_REGISTROS + " registros...");

        for (int i = 0; i < NUM_REGISTROS; i++) {

            String nome = gerarNomeAleatorio(random);
            String telefone = gerarTelefoneAleatorio(random);

            contatos.add(new Contato(nome, telefone));
        }

        System.out.println("Ordenando por telefone...");

        contatos.sort(
                Comparator.comparing(Contato::getTelefone)
        );

        System.out.println("Gravando arquivo...");

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(NOME_ARQUIVO, StandardCharsets.UTF_8))) {

            writer.write(NUM_REGISTROS + "\n");

            for (Contato contato : contatos) {

                writer.write(
                        contato.getNome()
                                + ";"
                                + contato.getTelefone()
                                + "\n"
                );
            }

            System.out.println(
                    "Arquivo gerado com sucesso: " + NOME_ARQUIVO
            );

        } catch (IOException e) {
            System.err.println(
                    "Erro ao escrever no arquivo: " + e.getMessage()
            );
        }
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
