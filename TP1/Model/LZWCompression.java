package Model;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class LZWCompression {

    // Compacta um arquivo binário
    public void compress(String inputFilePath, String outputFilePath) throws IOException {
        // Lê os dados do arquivo
        byte[] fileData = readFile(inputFilePath);

        // Compacta os dados usando LZW
        List<Integer> compressedData = compressData(fileData);

        // Salva o arquivo compactado
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFilePath))) {
            for (int code : compressedData) {
                dos.writeInt(code);
            }
        }
    }

    // Descompacta um arquivo LZW
    public void decompress(String compressedFilePath, String outputFilePath) throws IOException {
        // Lê os dados compactados
        List<Integer> compressedData = new ArrayList<>();
        try (DataInputStream dis = new DataInputStream(new FileInputStream(compressedFilePath))) {
            while (dis.available() > 0) {
                compressedData.add(dis.readInt());
            }
        }

        // Descompacta os dados
        byte[] decompressedData = decompressData(compressedData);

        // Salva o arquivo descompactado
        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            fos.write(decompressedData);
        }
    }

    // Lê o arquivo em bytes
    private byte[] readFile(String filePath) throws IOException {
        return Files.readAllBytes(new File(filePath).toPath());
    }

    // Compacta os dados usando LZW
    private List<Integer> compressData(byte[] data) {
        Map<String, Integer> dictionary = new HashMap<>();
        int dictSize = 256;

        // Inicializa o dicionário com todos os bytes únicos
        for (int i = 0; i < 256; i++) {
            dictionary.put("" + (char) i, i);
        }

        String w = "";
        List<Integer> result = new ArrayList<>();

        for (byte b : data) {
            char c = (char) (b & 0xFF);
            String wc = w + c;

            if (dictionary.containsKey(wc)) {
                w = wc;
            } else {
                result.add(dictionary.get(w));
                dictionary.put(wc, dictSize++);
                w = "" + c;
            }
        }

        // Adiciona o restante
        if (!w.isEmpty()) {
            result.add(dictionary.get(w));
        }

        return result;
    }

    // Descompacta os dados usando LZW
    private byte[] decompressData(List<Integer> compressedData) {
        Map<Integer, String> dictionary = new HashMap<>();
        int dictSize = 256;

        // Inicializa o dicionário com todos os bytes únicos
        for (int i = 0; i < 256; i++) {
            dictionary.put(i, "" + (char) i);
        }

        String w = "" + (char) (int) compressedData.remove(0);
        StringBuilder result = new StringBuilder(w);

        for (int k : compressedData) {
            String entry;
            if (dictionary.containsKey(k)) {
                entry = dictionary.get(k);
            } else if (k == dictSize) {
                entry = w + w.charAt(0);
            } else {
                throw new IllegalArgumentException("Código inválido no arquivo compactado.");
            }

            result.append(entry);

            // Adiciona w+entrada[0] ao dicionário
            dictionary.put(dictSize++, w + entry.charAt(0));
            w = entry;
        }

        return result.toString().getBytes();
    }

    public static void main(String[] args) {
        LZWCompression lzw = new LZWCompression();

        String inputFilePath = "input.bin";
        String compressedFilePath = "compressed.lzw";
        String decompressedFilePath = "decompressed.bin";

        try {
            // Compacta o arquivo
            lzw.compress(inputFilePath, compressedFilePath);
            System.out.println("Arquivo compactado com sucesso!");

            // Descompacta o arquivo
            lzw.decompress(compressedFilePath, decompressedFilePath);
            System.out.println("Arquivo descompactado com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

