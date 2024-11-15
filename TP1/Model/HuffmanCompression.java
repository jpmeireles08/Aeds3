package Model;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

class HuffmanNode implements Comparable<HuffmanNode> {
    int frequency;
    byte value;
    HuffmanNode left, right;

    public HuffmanNode(byte value, int frequency) {
        this.value = value;
        this.frequency = frequency;
    }

    public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
        this.value = -1; // Indica que é um nó interno
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    @Override
    public int compareTo(HuffmanNode other) {
        return Integer.compare(this.frequency, other.frequency);
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }
}

public class HuffmanCompression {

    private Map<Byte, String> huffmanCodes = new HashMap<>();

    public void compress(String inputFilePath, String outputFilePath) throws IOException {
        byte[] fileData = readFile(inputFilePath);
        Map<Byte, Integer> frequencyMap = calculateFrequency(fileData);
        HuffmanNode root = buildHuffmanTree(frequencyMap);
        generateHuffmanCodes(root, "");

        byte[] compressedData = compressData(fileData);
        saveCompressedFile(outputFilePath, frequencyMap, compressedData);
    }

    private byte[] readFile(String filePath) throws IOException {
        return Files.readAllBytes(new File(filePath).toPath());
    }

    private Map<Byte, Integer> calculateFrequency(byte[] data) {
        Map<Byte, Integer> frequencyMap = new HashMap<>();
        for (byte b : data) {
            frequencyMap.put(b, frequencyMap.getOrDefault(b, 0) + 1);
        }
        return frequencyMap;
    }

    private HuffmanNode buildHuffmanTree(Map<Byte, Integer> frequencyMap) {
        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>();
        for (Map.Entry<Byte, Integer> entry : frequencyMap.entrySet()) {
            queue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (queue.size() > 1) {
            HuffmanNode left = queue.poll();
            HuffmanNode right = queue.poll();
            HuffmanNode parent = new HuffmanNode(left.frequency + right.frequency, left, right);
            queue.add(parent);
        }

        return queue.poll();
    }

    private void generateHuffmanCodes(HuffmanNode node, String code) {
        if (node.isLeaf()) {
            huffmanCodes.put(node.value, code);
        } else {
            generateHuffmanCodes(node.left, code + "0");
            generateHuffmanCodes(node.right, code + "1");
        }
    }

    private byte[] compressData(byte[] data) {
        StringBuilder binaryString = new StringBuilder();
        for (byte b : data) {
            binaryString.append(huffmanCodes.get(b));
        }

        int length = binaryString.length();
        byte[] compressedData = new byte[(length + 7) / 8];
        for (int i = 0; i < length; i++) {
            if (binaryString.charAt(i) == '1') {
                compressedData[i / 8] |= (1 << (7 - (i % 8)));
            }
        }

        return compressedData;
    }

    private void saveCompressedFile(String filePath, Map<Byte, Integer> frequencyMap, byte[] compressedData) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(filePath))) {
            dos.writeInt(frequencyMap.size());
            for (Map.Entry<Byte, Integer> entry : frequencyMap.entrySet()) {
                dos.writeByte(entry.getKey());
                dos.writeInt(entry.getValue());
            }

            dos.writeInt(compressedData.length);
            dos.write(compressedData);
        }
    }

    public void decompress(String compressedFilePath, String outputFilePath) throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(compressedFilePath))) {
            // Lê o mapa de frequências do arquivo compactado
            int frequencyMapSize = dis.readInt();
            Map<Byte, Integer> frequencyMap = new HashMap<>();
            for (int i = 0; i < frequencyMapSize; i++) {
                byte value = dis.readByte();
                int frequency = dis.readInt();
                frequencyMap.put(value, frequency);
            }
    
            // Reconstrói a árvore de Huffman
            HuffmanNode root = buildHuffmanTree(frequencyMap);
    
            // Lê os dados compactados
            int compressedDataLength = dis.readInt();
            byte[] compressedData = new byte[compressedDataLength];
            dis.readFully(compressedData);
    
            // Descompacta os dados
            byte[] decompressedData = decompressData(compressedData, root);
    
            // Salva os dados descompactados em um novo arquivo
            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                fos.write(decompressedData);
            }
        }
    }
    
    private byte[] decompressData(byte[] compressedData, HuffmanNode root) {
        StringBuilder binaryString = new StringBuilder();
        for (byte b : compressedData) {
            for (int i = 7; i >= 0; i--) {
                binaryString.append(((b >> i) & 1) == 1 ? '1' : '0');
            }
        }
    
        List<Byte> decompressedList = new ArrayList<>();
        HuffmanNode currentNode = root;
        for (char bit : binaryString.toString().toCharArray()) {
            currentNode = (bit == '0') ? currentNode.left : currentNode.right;
            if (currentNode.isLeaf()) {
                decompressedList.add(currentNode.value);
                currentNode = root;
            }
        }
    
        // Converte a lista de bytes para um array de bytes
        byte[] decompressedData = new byte[decompressedList.size()];
        for (int i = 0; i < decompressedData.length; i++) {
            decompressedData[i] = decompressedList.get(i);
        }
        return decompressedData;
    }
    

}
