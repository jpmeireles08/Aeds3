import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import Pokemon.*;
import Service.*;


public class Crud {

    
    public static void main(String[] args) throws ParseException, Exception {
        try {
            
            Scanner sc = new Scanner(System.in);
            RandomAccessFile binFile = new RandomAccessFile("data.bin", "rw");
            Service binarioService = new Service(binFile, "treeFile", "hashFile1", "hashFile2");
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            int tamReg = 0;
            int pos = 0;
            int ultimoId = 0;
            int idBusca;
            int numCaminhos, numReg;
            String modo;
            

            System.out.println("Voce deseja recarregar o arquivo csv original?");
            modo = sc.nextLine();

            if ((modo.toLowerCase()).equals("sim")) { // Verifica se o usuário deseja recarregar o arquivo csv e o arquivo binário

                RandomAccessFile csvFile = new RandomAccessFile("Pokemons.csv", "r");
                Pokemon pokemons[] = new Pokemon[1072]; // Array com a quantidade de registros do arquivo csv original

                for (int i = 0; i < 1072; i++) {
                    pokemons[i] = new Pokemon();
                }

                String linha;

                csvFile.readLine();
                binFile.setLength(0);
                binFile.seek(0);
                binFile.writeInt(0);

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

                    tamReg = 55 + campos[2].length() + campos[3].length() + campos[4].length();

                    

                    binarioService.tree.create(Integer.parseInt(campos[0]), (int)binFile.getFilePointer());
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



            System.out.println("""
            Digite a operacao a ser realizada:
            1 - Create
            2 - Read
            3 - Update
            4 - Delete
            """);

            String entrada = sc.nextLine();

            System.out.println("");

            if ((entrada.toLowerCase()).equals("1")) {

                Pokemon novoPokemon = new Pokemon();

                novoPokemon = Service.criarPokemon();

                binarioService.create(novoPokemon);

            } else if ((entrada.toLowerCase()).equals("2")) {

                System.out.println("Digite o Id a ser buscado");
                idBusca = sc.nextInt();
                Service.read(idBusca);

            } else if ((entrada.toLowerCase()).equals("3")) {

                Pokemon novoPokemon = new Pokemon();

                novoPokemon = Service.criarPokemonId();

                binarioService.update(novoPokemon);

            } else if ((entrada.toLowerCase()).equals("4")) {

                System.out.println("Digite o Id a ser deletado");
                idBusca = sc.nextInt();
                Service.delete(idBusca);

            } else if ((entrada.toLowerCase()).equals("ordenar")) {
                System.out.println("Entre com o número de caminhos");
                numCaminhos = Integer.parseInt(sc.nextLine());
                System.out.println("Entre com o número máximo de registros por arquivo");
                numReg = Integer.parseInt(sc.nextLine());

                Service.ordenar(numCaminhos, numReg);
            }

            sc.close();
            binFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
