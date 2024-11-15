import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import Model.HuffmanCompression;
import Model.LZWCompression;
import Pokemon.*;
import Service.*;

public class Crud {

    public static void main(String[] args) throws ParseException, Exception {
        try {

            Scanner scanner = new Scanner(System.in);
            RandomAccessFile binFile = new RandomAccessFile("data.bin", "rw");
            Service binarioService = new Service(binFile, "treeFile", "hashFile1", "hashFile2");
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            int ultimoId = 0;
            int idBusca;
            int numCaminhos, numReg;
            String modo;

            System.out.println("Voce deseja recarregar o arquivo csv original?");
            modo = scanner.nextLine();

            if ((modo.toLowerCase()).equals("sim")) { // Recarregar CSV e arquivo binário
                RandomAccessFile csvFile = new RandomAccessFile("Pokemons.csv", "r");
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

            while (entrada != 7) {
                System.out.print("""
                        Bem vindo!
                        1 - Create
                        2 - Read
                        3 - Update
                        4 - Delete
                        5 - Comprimir
                        6 - Descomprimir
                        7 - Sair
                        """);
                System.out.print("Digite a operacao a ser realizada: ");

                entrada = scanner.nextInt();

                switch (entrada) {
                    case 1: // Create
                        Pokemon novoPokemon = Service.criarPokemon();
                        binarioService.create(novoPokemon);
                        break;

                    case 2: // Read
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
                        novoPokemon = Service.criarPokemonId();
                        if (binarioService.updatePokemon(novoPokemon)) {
                            System.out.println("Pokémon atualizado com sucesso!");
                        } else {
                            System.out.println("Erro ao atualizar Pokémon.");
                        }
                        break;

                    case 4: // Delete
                        System.out.print("Digite o Id a ser deletado: ");
                        idBusca = scanner.nextInt();
                        scanner.nextLine(); // Consome o \n
                        binarioService.deletePokemon(idBusca);
                        break;

                    case 9: // Opção extra
                        System.out.println("Entre com o número de caminhos");
                        numCaminhos = Integer.parseInt(scanner.nextLine());
                        System.out.println("Entre com o número máximo de registros por arquivo");
                        numReg = Integer.parseInt(scanner.nextLine());

                        Service.ordenar(numCaminhos, numReg);
                        break;

                    case 5:
                        HuffmanCompression compressor = new HuffmanCompression();
                        LZWCompression lzw = new LZWCompression();
                        String inputFilePath = "data.bin";
                        String outputFilePathHuff = "dataHuffmanComprimido.huf";
                        String outputFilePathLZW = "dataLZWComprimido.lzw";

                        try {
                            compressor.compress(inputFilePath, outputFilePathHuff);
                            lzw.compress(inputFilePath, outputFilePathLZW);
                            System.out.println("Arquivo compactado com sucesso!");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case 6:
                        compressor = new HuffmanCompression();
                        lzw = new LZWCompression();
                        outputFilePathHuff = "dataHuffmanComprimido.huf";
                        outputFilePathLZW = "dataLZWComprimido.lzw";
                        String decompressedFilePathHuff = "dataHuffman.bin";
                        String decompressedFilePathLZW = "dataLZW.bin";
    
                    try {
                        compressor.decompress(outputFilePathHuff, decompressedFilePathHuff);
                        lzw.decompress(outputFilePathLZW, decompressedFilePathLZW);
                        System.out.println("Arquivo descompactado com sucesso!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;


                    case 7: // Sair
                        System.out.println("Saindo do programa.");
                        break;

                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            }

            binFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
