package Model;
import java.io.FileInputStream;
import java.io.IOException;

public class BoyerMoore {

    // Criação da tabela de deslocamento do algoritmo Boyer-Moore
    public static int[] criarTabelaDeslocamento(byte[] padrao) {
        int[] tabela = new int[256]; // Tabela para todos os bytes possíveis (0 a 255)
        int tamanhoPadrao = padrao.length;

        // Inicializa a tabela com o tamanho do padrão
        for (int i = 0; i < tabela.length; i++) {
            tabela[i] = tamanhoPadrao;
        }

        // Atualiza os deslocamentos com base nas ocorrências dos bytes no padrão
        for (int i = 0; i < tamanhoPadrao - 1; i++) {
            tabela[padrao[i] & 0xFF] = tamanhoPadrao - 1 - i;
        }

        return tabela;
    }

    // Método para contar as ocorrências do padrão no arquivo
    public static int contarOcorrencias(String caminhoArquivo, byte[] padrao) throws IOException {
        int[] tabelaDeslocamento = criarTabelaDeslocamento(padrao);
        int ocorrencias = 0;
        int tamanhoPadrao = padrao.length;

        try (FileInputStream fis = new FileInputStream(caminhoArquivo)) {
            byte[] buffer = fis.readAllBytes(); // Carrega o arquivo inteiro na memória
            int tamanhoArquivo = buffer.length;

            int i = 0; // Índice no buffer do arquivo
            while (i <= tamanhoArquivo - tamanhoPadrao) {
                int j = tamanhoPadrao - 1;

                // Comparação de trás para frente
                while (j >= 0 && padrao[j] == buffer[i + j]) {
                    j--;
                }

                // Se o padrão foi encontrado
                if (j < 0) {
                    ocorrencias++;
                    i += tamanhoPadrao; // Avança o índice após o padrão
                } else {
                    // Calcula o deslocamento baseado na tabela
                    i += Math.max(1, tabelaDeslocamento[buffer[i + j] & 0xFF]);
                }
            }
        }

        return ocorrencias;
    }

    // Método principal para testar o código
    public static void main(String[] args) {
        String caminhoArquivo = "arquivo.bin"; // Caminho para o arquivo binário
        byte[] padrao = {0x01, 0x02, 0x03};   // Exemplo de padrão em bytes

        try {
            int ocorrencias = contarOcorrencias(caminhoArquivo, padrao);
            System.out.println("Número de ocorrências do padrão: " + ocorrencias);
        } catch (IOException e) {
            System.err.println("Erro ao processar o arquivo: " + e.getMessage());
        }
    }
}
