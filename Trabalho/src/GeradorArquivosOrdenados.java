import dominio.Contato;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class GeradorArquivosOrdenados {
    private static final int NUM_REGISTROS = 1000;
    private static final String NOME_ARQUIVO = "entrada.txt";

    private static final String[] PRENOMES = {
            "Ana", "Bruno", "Carlos", "Daniela", "Eduardo", "Fernanda", "Gabriel", "Helena", "Isabela", "Joao",
            "Juliana", "Lucas", "Mariana", "Nathan", "Olivia", "Paulo", "Quezia", "Rafael", "Sofia", "Thiago",
            "Victor", "William", "Xavier", "Yasmin", "Zuleica", "Alfredo", "Beatriz", "Caio", "Denise", "Eliana",
            "Felipe", "Gustavo", "Heitor", "Igor", "Jessica", "Kevin", "Larissa", "Mateus", "Natalia", "Otavio",
            "Patricia", "Renato", "Sandra", "Tadeu", "Ursula", "Vinicius", "Wellington", "Zilda", "Adriana", "Benicio",
            "Cristina", "Davi", "Emanuel", "Flavia", "Geraldo", "Heloisa", "Icaro", "Jaqueline", "Leonardo", "Marta",
            "Nelson", "Orlando", "Priscila", "Raquel", "Saulo", "Tatiane", "Ubirajara", "Vera", "Wesley", "Zenaide",
            "Alice", "Brenda", "Caetano", "Danilo", "Enzo", "Fabiana", "Gilberto", "Henrique", "Isadora", "Jose",
            "Katia", "Lorena", "Mauricio", "Natanael", "Osvaldo", "Pamela", "Regina", "Sandro", "Tania", "Ulisses",
            "Vania", "Wilson", "Yago", "Zelia", "Amelia", "Bernardo", "Celso", "Dulce", "Edson", "Fatima", "Gilmar",
            "Humberto", "Irene", "Jorge", "Kleber", "Luciana", "Marcelo", "Nadir", "Otacilio", "Paula", "Renata"
    };

    private static final String[] SOBRENOMES = {
            "Almeida", "Barbosa", "Campos", "Dias", "Evangelista", "Ferreira", "Gomes", "Henrique", "Iglesias", "Junqueira",
            "Klein", "Lima", "Medeiros", "Nascimento", "Oliveira", "Pereira", "Queiroz", "Rodrigues", "Silva", "Teixeira",
            "Uchoa", "Vasconcelos", "Watanabe", "Ximenes", "Yamamoto", "Zanetti", "Araujo", "Borges", "Coelho", "Dantas",
            "Esteves", "Farias", "Guimaraes", "Holanda", "Ivo", "Jardim", "Krieger", "Lacerda", "Monteiro", "Neves",
            "Oliveira", "Porto", "Quintana", "Ramos", "Sanches", "Torrico", "Urbano", "Vieira", "Wanderley", "Xavier",
            "Yunes", "Zampieri", "Abreu", "Barreto", "Coutinho", "Delgado", "Elias", "Franca", "Godoy", "Haddad",
            "Ibrahim", "Jacob", "Lopes", "Moura", "Nogueira", "Ortega", "Pinto", "Quaresma", "Reis", "Souto",
            "Torres", "Ubaldo", "Valente", "Weber", "Ximenes", "Yamaguchi", "Zanella", "Alvarenga", "Bittencourt", "Carvalho",
            "Duarte", "Espindola", "Freitas", "Goncalves", "Herrera", "Ishikawa", "Junqueira", "Lacerda", "Mancini", "Noronha",
            "Orsini", "Paz", "Quevedo", "Rangel", "Souza", "Tavares", "Uchoa", "Vilela", "Werneck", "Xisto"
    };

    public static void main(String[] args) {
        gerarArquivo();
    }

    private static void gerarArquivo() {
        Random random = new Random();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO, StandardCharsets.UTF_8))) {
            writer.write(NUM_REGISTROS + "\n");

            for (int i = 1; i <= NUM_REGISTROS; i++) {
                String nome = gerarNomeAleatorio(random);
                String telefone = gerarTelefoneAleatorio(random);
                Contato contato = new Contato(nome, telefone);
                writer.write(contato.getNome() + ";" + contato.getTelefone() + "\n");
            }

            System.out.println("Arquivo gerado com sucesso: " + NOME_ARQUIVO);
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    private static String gerarNomeAleatorio(Random random) {
        String primeiroNome = PRENOMES[random.nextInt(PRENOMES.length)];
        String sobrenome = SOBRENOMES[random.nextInt(SOBRENOMES.length)];
        return primeiroNome + " " + sobrenome;
    }

    private static String gerarTelefoneAleatorio(Random random) {
        int ddd = 11 + random.nextInt(89);
        int prefixo = 90000 + random.nextInt(10000);
        int numero = 1000 + random.nextInt(9000);
        return String.format("(%02d) %05d-%04d", ddd, prefixo, numero);
    }
}