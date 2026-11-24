package dominio;

import apresentacao.TelaSistemaTransporte;
import servico.TransporteServico;

import javax.swing.SwingUtilities;
import java.io.File;

/**
 * Ponto de entrada do aplicativo de Roteamento de Transporte Público.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TransporteServico servico = new TransporteServico();
            TelaSistemaTransporte tela = new TelaSistemaTransporte(servico);
            tela.setVisible(true);

            // Se o arquivo padrão gerado existir na raiz do projeto, já oferece o carregamento inicial
            File arquivoPadrao = new File("malhaTransporteTeste.txt");
            if (arquivoPadrao.exists()) {
                servico.carregarDadosDoArquivo(arquivoPadrao.getPath());
            }
        });
    }
}