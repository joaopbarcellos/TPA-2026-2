package dominio;

import colecao.IColecao;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class LeitorArquivo {
    private static final String NOME_ARQUIVO = "entrada.txt";

    public void lerArquivo(IColecao<Contato> listaNome, IColecao<Contato> listaTelefone) {
        long inicio = System.nanoTime();
        try (BufferedReader reader = Files.newBufferedReader(Path.of(NOME_ARQUIVO), StandardCharsets.UTF_8)) {
            int numRegistros = Integer.parseInt(reader.readLine().trim());
            String linha;
            Contato contato;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) {
                    continue;
                }
                contato = processarLinha(linha);
                if (contato != null) {
                    listaNome.adicionar(contato);
                    listaTelefone.adicionar(contato);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        long tempo = System.nanoTime() - inicio;
        double tempoEmSegundos = tempo / 1_000_000_000.0;
        double tempoEmMilissegundos = tempo / 1_000_000.0;
        System.out.println("Tempo de leitura do arquivo e adição na lista: "
                + String.format("%.6f s | %.3f ms", tempoEmSegundos, tempoEmMilissegundos));
    }

    private Contato processarLinha(String linha) {
        String[] partes = linha.split(";");
        if (partes.length != 2) {
            System.err.println("Linha inválida: " + linha);
            return null;
        }

        String nome = partes[0].trim();
        String telefone = partes[1].trim();
        Contato contato = new Contato(nome, telefone);
        return contato;
    }
}
