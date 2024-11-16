import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import Model.BoyerMoore;
import Model.HuffmanCompression;
import Model.KMP;
import Model.LZWCompression;
import Pokemon.*;
import Service.*;

public class Crud {

    public static void main(String[] args) throws ParseException, Exception {
        try {

            Scanner scanner = new Scanner(System.in);
            RandomAccessFile binFile = new RandomAccessFile("Files\\data.bin", "rw");
            Service binarioService = new Service(binFile, "Files\\treeFile", "Files\\hashFile1", "Files\\hashFile2");
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            int ultimoId = 0;
            int idBusca;
            int numCaminhos, numReg;
            String modo;

            System.out.println("Voce deseja recarregar o arquivo csv original?");
            modo = scanner.nextLine();

            if ((modo.toLowerCase()).equals("sim")) { // Recarregar CSV e arquivo binário
                RandomAccessFile csvFile = new RandomAccessFile("C:\\Users\\João\\Documents\\Aeds3\\TP1\\Database\\Pokemons.csv", "r");
                Pokemon[] pokemons = new Pokemon[1072]; // Array com a quantidade de registros do arquivo csv original

                for (int i = 0; i < 1072; i++) {
                    pokemons[i] = new Pokemon();
                }

                String linha;
                csvFile.readLine();
                binFile.setLength(0);
                binFile.seek(0);
                binFile.writeInt(0);

                int pos = 0;
                while ((linha = csvFile.readLine()) != null) {

                    String[] campos = linha.split(",");
                    pokemons[pos].setId(Integer.parseInt(campos[0]));
                    pokemons[pos].setNumber(Integer.parseInt(campos[1]));
                    pokemons[pos].setName(campos[2]);
                    pokemons[pos].setType1(campos[3]);
                    pokemons[pos].setType2(campos[4]);
                    pokemons[pos].setTotal(Integer.parseInt(campos[5]));
                    pokemons[pos].setHp(Integer.parseInt(campos[6]));
                    pokemons[pos].setAttack(Integer.parseInt(campos[7]));
                    pokemons[pos].setDefense(Integer.parseInt(campos[8]));
                    pokemons[pos].setSp_attack(Integer.parseInt(campos[9]));
                    pokemons[pos].setSp_defense(Integer.parseInt(campos[10]));
                    pokemons[pos].setSpeed(Integer.parseInt(campos[11]));
                    pokemons[pos].setGeneration(Integer.parseInt(campos[12]));
                    pokemons[pos].setLegendary(Boolean.parseBoolean(campos[13]));
                    Date data = formato.parse(campos[14]);
                    pokemons[pos].setDate_birth(data);

                    int tamReg = 55 + campos[2].length() + campos[3].length() + campos[4].length();

                    binarioService.tree.create(Integer.parseInt(campos[0]), (int) binFile.getFilePointer());
                    binarioService.hash.create(Integer.parseInt(campos[0]), binFile.getFilePointer());
                    binFile.writeByte(0);
                    binFile.writeInt(tamReg);
                    binFile.writeInt(Integer.parseInt(campos[0]));
                    binFile.writeInt(Integer.parseInt(campos[1]));
                    binFile.writeUTF(campos[2]);
                    binFile.writeUTF(campos[3]);
                    binFile.writeUTF(campos[4]);
                    binFile.writeInt(Integer.parseInt(campos[5]));
                    binFile.writeInt(Integer.parseInt(campos[6]));
                    binFile.writeInt(Integer.parseInt(campos[7]));
                    binFile.writeInt(Integer.parseInt(campos[8]));
                    binFile.writeInt(Integer.parseInt(campos[9]));
                    binFile.writeInt(Integer.parseInt(campos[10]));
                    binFile.writeInt(Integer.parseInt(campos[11]));
                    binFile.writeInt(Integer.parseInt(campos[12]));
                    binFile.writeBoolean(Boolean.parseBoolean(campos[13]));
                    binFile.writeLong(data.getTime());

                    ultimoId = Integer.parseInt(campos[0]);
                    pos++;
                }
                csvFile.close();
                binFile.seek(0);
                binFile.writeInt(ultimoId);
            }

            int entrada = 0;

            while (entrada != 9) {
                System.out.println();
                System.out.print("""
                        Bem vindo!
                        1 - Create
                        2 - Read
                        3 - Update
                        4 - Delete
                        5 - Comprimir
                        6 - Descomprimir
                        7 - Buscar Padrão (KMP)
                        8 - Buscar Padrão (Boyer Moore)
                        9 - Sair
                        """);
                        System.out.println();
                System.out.print("Digite a operacao a ser realizada: ");

                entrada = scanner.nextInt();

                switch (entrada) {
                    case 1: // Create
                    System.out.println();
                        Pokemon novoPokemon = Service.criarPokemon();
                        binarioService.create(novoPokemon);
                        break;

                    case 2: // Read
                    System.out.println();
                        System.out.print("Digite o Id a ser buscado: ");
                        idBusca = scanner.nextInt();
                        scanner.nextLine(); // Consome o \n

                        Pokemon[] pokemons = binarioService.readPokemonByTree(idBusca);
                        System.out.println("\nBusca pela árvore B+:");
                        for (Pokemon pokemon : pokemons) {
                            if (pokemon != null) {
                                System.out.println(pokemon.toString());
                            } else {
                                System.out.println("Id não encontrado");
                            }
                        }

                        System.out.println("\nBusca pela Hash:");
                        Pokemon controle = binarioService.readPokemonByHash(idBusca);
                        if (controle != null) {
                            System.out.println(controle.toString());
                        } else {
                            System.out.println("Id não encontrado");
                        }
                        System.out.println();
                        break;

                    case 3: // Update
                    System.out.println();
                        novoPokemon = Service.criarPokemonId();
                        if (binarioService.updatePokemon(novoPokemon)) {
                            System.out.println("Pokémon atualizado com sucesso!");
                        } else {
                            System.out.println("Erro ao atualizar Pokémon.");
                        }
                        break;

                    case 4: // Delete
                    System.out.println();
                        System.out.print("Digite o Id a ser deletado: ");
                        idBusca = scanner.nextInt();
                        scanner.nextLine(); // Consome o \n
                        binarioService.deletePokemon(idBusca);
                        break;

                    case 10: // Opção extra
                    System.out.println();
                        System.out.println("Entre com o número de caminhos");
                        numCaminhos = Integer.parseInt(scanner.nextLine());
                        System.out.println("Entre com o número máximo de registros por arquivo");
                        numReg = Integer.parseInt(scanner.nextLine());

                        Service.ordenar(numCaminhos, numReg);
                        break;

                    case 5:
                    System.out.println();
                        HuffmanCompression compressor = new HuffmanCompression();
                        LZWCompression lzw = new LZWCompression();
                        String inputFilePath = "Files\\data.bin";
                        String outputFilePathHuff = "Files\\dataHuffmanComprimido.huf";
                        String outputFilePathLZW = "Files\\dataLZWComprimido.lzw";
                        float tamData = binFile.length();
                        float taxaCompressaoHuffman = 0;
                        float taxaCompressaoLZW = 0;

                        try {
                            compressor.compress(inputFilePath, outputFilePathHuff);
                            lzw.compress(inputFilePath, outputFilePathLZW);
                            RandomAccessFile arqHuff = new RandomAccessFile("Files\\dataHuffmanComprimido.huf", "rw");
                            RandomAccessFile arqLZW = new RandomAccessFile("Files\\dataLZWComprimido.lzw", "rw");
                            taxaCompressaoHuffman = (arqHuff.length() / tamData) * 100;
                            taxaCompressaoLZW = (arqLZW.length() / tamData) * 100;


                            System.out.println("Arquivo compactado com sucesso!");
                            System.out.println("Taxa de compressao Huffman: " + (100 - taxaCompressaoHuffman) + "%");
                            System.out.println("Taxa de compressao LZW: " + (100 - taxaCompressaoLZW) + "%");
                            arqHuff.close();
                            arqLZW.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case 6:
                    System.out.println();
                        compressor = new HuffmanCompression();
                        lzw = new LZWCompression();
                        outputFilePathHuff = "Files\\dataHuffmanComprimido.huf";
                        outputFilePathLZW = "Files\\dataLZWComprimido.lzw";
                        String decompressedFilePathHuff = "Files\\dataHuffman.bin";
                        String decompressedFilePathLZW = "Files\\dataLZW.bin";

                        try {
                            compressor.decompress(outputFilePathHuff, decompressedFilePathHuff);
                            lzw.decompress(outputFilePathLZW, decompressedFilePathLZW);
                            System.out.println("Arquivo descompactado com sucesso!");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case 7:
                    System.out.println();
                        String filePath = "Files\\data.bin"; // Substitua pelo caminho do arquivo

                        System.out.println("Entre com o padrao a ser buscado:");
                        scanner.nextLine();
                        String padraoString = scanner.nextLine();
                        byte[] padrao = padraoString.getBytes(); // Padrão a ser buscado no arquivo

                        try {
                            byte[] arquivo = new byte[(int) binFile.length()];
                            arquivo = Files.readAllBytes(Paths.get(filePath));
                            int ocorrencias = KMP.kmpSearch(arquivo, padrao);
                            System.out.println("Número de ocorrências do padrao: " + ocorrencias);
                        } catch (IOException e) {
                            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
                        }

                        break;

                    case 8:
                    System.out.println();
                        String caminhoArquivo = "Files\\data.bin"; // Caminho para o arquivo binário
                        System.out.println("Entre com o padrao a ser buscado:");
                        scanner.nextLine();
                        String padraoString2 = scanner.nextLine();
                        byte[] padrao2 = padraoString2.getBytes();
                        try {
                            int ocorrencias = BoyerMoore.contarOcorrencias(caminhoArquivo, padrao2);
                            System.out.println("Número de ocorrências do padrao: " + ocorrencias);
                        } catch (IOException e) {
                            System.err.println("Erro ao processar o arquivo: " + e.getMessage());
                        }

                        break;

                    case 9: // Sair
                    System.out.println();
                        System.out.println("Saindo do programa.");
                        break;

                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
                System.out.println();
            }

            binFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}