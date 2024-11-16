package Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class KMP {
    public static void main(String[] args) {
        String filePath = "data.bin"; // Substitua pelo caminho do arquivo
        byte[] pattern = {0x01, 0x02, 0x03}; // Padrão a ser buscado no arquivo
        
        try {
            byte[] data = Files.readAllBytes(Paths.get(filePath));
            int occurrences = kmpSearch(data, pattern);
            System.out.println("Número de ocorrências do padrão: " + occurrences);
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    public static int kmpSearch(byte[] text, byte[] pattern) {
        int[] lps = computeLPSArray(pattern);
        int i = 0; // Índice para o texto
        int j = 0; // Índice para o padrão
        int count = 0;

        while (i < text.length) {
            if (pattern[j] == text[i]) {
                i++;
                j++;
            }

            if (j == pattern.length) {
                count++;
                j = lps[j - 1];
            } else if (i < text.length && pattern[j] != text[i]) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return count;
    }

    public static int[] computeLPSArray(byte[] pattern) {
        int length = 0; // Comprimento da maior prefixo-sufixo
        int i = 1;
        int[] lps = new int[pattern.length];
        lps[0] = 0;

        while (i < pattern.length) {
            if (pattern[i] == pattern[length]) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }
}

