package Service;

import java.io.*;
import java.text.ParseException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import Model.ArvoreBMaisPokemon;
import Model.HashExtensivelPokemon;
import Pokemon.Pokemon;

public class Service {

    public final RandomAccessFile raf; // atributo do tipo raf para manipulacao do arquivo

    public final ArvoreBMaisPokemon tree;

    public final HashExtensivelPokemon hash;

    public Service(RandomAccessFile raf, String treeFile, String HashFile1, String HashFile2) throws Exception { // construtor
                                                                                                                 // da
        this.raf = raf; // classe
        this.tree = new ArvoreBMaisPokemon(8, treeFile);
        this.hash = new HashExtensivelPokemon(3000, HashFile1, HashFile2);
    }

    public void create(Pokemon pomekon) throws Exception { // Função para criar um novo pokemon no arquivo
                                                           // binário
        try {
            RandomAccessFile binFile = new RandomAccessFile("data.bin", "rw"); // Abertura do arquivo binário
            Scanner sc = new Scanner(System.in);
            int ultimoId;
            int tamReg;
            long tamFile = binFile.length();

            binFile.seek(0);
            ultimoId = binFile.readInt();
            binFile.seek(0);
            binFile.writeInt(ultimoId + 1);

            pomekon.setId(ultimoId + 1);

            tamReg = pomekon.getTamanho();

            binFile.seek(tamFile);
            binFile.writeByte(0);
            binFile.writeInt(tamReg);
            binFile.writeInt(pomekon.getId());
            binFile.writeInt(pomekon.getNumber());
            binFile.writeUTF(pomekon.getName());
            binFile.writeUTF(pomekon.getType1());
            binFile.writeUTF(pomekon.getType2());
            binFile.writeInt(pomekon.getTotal());
            binFile.writeInt(pomekon.getHp());
            binFile.writeInt(pomekon.getAttack());
            binFile.writeInt(pomekon.getDefense());
            binFile.writeInt(pomekon.getSp_attack());
            binFile.writeInt(pomekon.getSp_defense());
            binFile.writeInt(pomekon.getSpeed());
            binFile.writeInt(pomekon.getGeneration());
            binFile.writeBoolean(pomekon.getLegendary());
            binFile.writeLong(pomekon.getDate_birth().getTime());
            tree.create(pomekon.getId(), (int) tamFile);
            hash.create(pomekon.getId(), tamFile);

            System.out.println("O Pokemon foi adicionado no fim do arquivo");
            binFile.close();
            sc.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeInFile(long pos, Pokemon pokemon) throws Exception { // metodo para escrever no arquivo, recebe a posicao para escrita e um objeto pokemon para escrever
        raf.seek(pos); // seta o ponteiro na posicao desejada
        raf.writeByte(0); // escreve a lapide
        raf.writeInt(pokemon.getTamanho()); // tamanho do registro
        raf.writeInt(pokemon.getId());
        raf.writeInt(pokemon.getNumber());
        raf.writeUTF(pokemon.getName());
        raf.writeUTF(pokemon.getType1());
        raf.writeUTF(pokemon.getType2());
        raf.writeInt(pokemon.getTotal());
        raf.writeInt(pokemon.getHp());
        raf.writeInt(pokemon.getAttack());
        raf.writeInt(pokemon.getDefense());
        raf.writeInt(pokemon.getSp_attack());
        raf.writeInt(pokemon.getSp_defense());
        raf.writeInt(pokemon.getSpeed());
        raf.writeInt(pokemon.getGeneration());
        raf.writeBoolean(pokemon.getLegendary());
        raf.writeLong(pokemon.getDate_birth().getTime());
        tree.create(pokemon.getId(), (int)pos);
        hash.create(pokemon.getId(), pos);
    }

    private void writePokemonByTam(long pos, Pokemon pokemon, int tam) throws Exception { // metodo para escrever a pokemon, mas mantendo o tamanho do arquivo anterior, metodo exclusivo do update
        raf.seek(pos); // seta o ponteiro na posicao desejada
        raf.writeByte(0); // escreve a lapide
        raf.writeInt(tam); // tamanho do registro
        raf.writeInt(pokemon.getId());
        raf.writeInt(pokemon.getNumber());
        raf.writeUTF(pokemon.getName());
        raf.writeUTF(pokemon.getType1());
        raf.writeUTF(pokemon.getType2());
        raf.writeInt(pokemon.getTotal());
        raf.writeInt(pokemon.getHp());
        raf.writeInt(pokemon.getAttack());
        raf.writeInt(pokemon.getDefense());
        raf.writeInt(pokemon.getSp_attack());
        raf.writeInt(pokemon.getSp_defense());
        raf.writeInt(pokemon.getSpeed());
        raf.writeInt(pokemon.getGeneration());
        raf.writeBoolean(pokemon.getLegendary());
        raf.writeLong(pokemon.getDate_birth().getTime());
        tree.create(pokemon.getId(), (int)pos);
        hash.create(pokemon.getId(), pos);
    }

    public static void read(int idBusca) throws ParseException { // Função para ler um pokemon do arquivo binário
        try {
            RandomAccessFile binFile = new RandomAccessFile("data.bin", "rw");
            Scanner sc = new Scanner(System.in);
            Pokemon pomekon = new Pokemon();
            int tamReg;
            long pos;
            byte lapide;

            binFile.seek(4);

            while (binFile.getFilePointer() < binFile.length()) {
                lapide = binFile.readByte();
                if (lapide == 0) {
                    binFile.readInt();
                    pomekon.setId(binFile.readInt());
                    pomekon.setNumber(binFile.readInt());
                    pomekon.setName(binFile.readUTF());
                    pomekon.setType1(binFile.readUTF());
                    pomekon.setType2(binFile.readUTF());
                    pomekon.setTotal(binFile.readInt());
                    pomekon.setHp(binFile.readInt());
                    pomekon.setAttack(binFile.readInt());
                    pomekon.setDefense(binFile.readInt());
                    pomekon.setSp_attack(binFile.readInt());
                    pomekon.setSp_defense(binFile.readInt());
                    pomekon.setSpeed(binFile.readInt());
                    pomekon.setGeneration(binFile.readInt());
                    pomekon.setLegendary(binFile.readBoolean());
                    Date data = new Date(binFile.readLong());
                    pomekon.setDate_birth(data);

                    if (idBusca == pomekon.getId()) {
                        System.out.println(pomekon);
                        sc.close();
                        binFile.close();
                        return;
                    }

                } else {
                    tamReg = binFile.readInt();
                    pos = tamReg + binFile.getFilePointer();
                    binFile.seek(pos);
                }
            }
            System.out.println("Id nao encontrado");
            sc.close();
            binFile.close();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deletePokemon(int id) throws Exception {
        int[] posicoes;
        posicoes = tree.read(id);
        for (int i = 0; i < posicoes.length; i++) {
            raf.seek(posicoes[i]);
            raf.writeByte(1);
            tree.delete(id, posicoes[i]);
        }
        hash.delete(id);
    }

    public boolean updatePokemon(Pokemon pokemon) throws Exception{
        long pos = hash.read(pokemon.getId());
        if(pos != -1){
            raf.seek(pos);
            if(!raf.readBoolean()){
                int tam = raf.readInt();
                Pokemon temp = readFromFile(pos);
                if(pokemon.getTamanho() == temp.getTamanho()){
                    writeInFile(pos, pokemon);
                    return true;
                } else if(pokemon.getTamanho() < temp.getTamanho()){
                    writePokemonByTam(pos, pokemon, tam);
                    return true;
                } else{
                    deletePokemon(temp.getId());
                    writeInFile(raf.length(), pokemon);
                    return true;
                }
            }
        }
        return false;
    }

    public static Pokemon criarPokemon() throws ParseException { // Função que recebe um pokemon do teclado e transforma
                                                                 // em um objeto
        Scanner sc = new Scanner(System.in);
        Pokemon pomekon = new Pokemon();
        String condicional;
        Date data;
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); // Formatação da data

        System.out.println("Entre com o numero do Pokemon:");
        pomekon.setNumber(Integer.parseInt(sc.nextLine()));
        System.out.println("Entre com o nome do Pokemon:");
        pomekon.setName(sc.nextLine());
        System.out.println("Entre com o tipo do Pokemon:");
        pomekon.setType1(sc.nextLine());
        System.out.println("O Pokemon tem 2 tipos?");
        condicional = sc.nextLine();
        if ((condicional.toLowerCase()).equals("sim")) {
            System.out.println("Entre com o segundo tipo do Pokemon:");
            pomekon.setType2(sc.nextLine());
        } else {
            pomekon.setType2(" "); // Verificação se o pokemon tem dois tipos
        }
        System.out.println("Entre com os pontos de vida do Pokemon:");
        pomekon.setHp(Integer.parseInt(sc.nextLine()));
        System.out.println("Entre com o ataque do Pokemon:");
        pomekon.setAttack(Integer.parseInt(sc.nextLine()));
        System.out.println("Entre com a defesa do Pokemon:");
        pomekon.setDefense(Integer.parseInt(sc.nextLine()));
        System.out.println("Entre com o ataque especial do Pokemon:");
        pomekon.setSp_attack(Integer.parseInt(sc.nextLine()));
        System.out.println("Entre com a defesa especial do Pokemon:");
        pomekon.setSp_defense(Integer.parseInt(sc.nextLine()));
        System.out.println("Entre com a velocidade do Pokemon:");
        pomekon.setSpeed(Integer.parseInt(sc.nextLine()));
        System.out.println("Entre com a geracao do Pokemon:");
        pomekon.setGeneration(Integer.parseInt(sc.nextLine()));
        System.out.println("O Pokemon e lendario?");
        condicional = sc.nextLine();
        if ((condicional.toLowerCase()).equals("sim")) {
            pomekon.setLegendary(true);
        } else {
            pomekon.setLegendary(false);
        }
        System.out.println("Entre com a data do Pokemon:");
        data = formato.parse(sc.nextLine());
        pomekon.setDate_birth(data);

        sc.close();
        return pomekon;

    }

    public static Pokemon criarPokemonId() throws ParseException { // Função utilizada pelo Update para modificar um
                                                                   // pokemon
        Scanner sc = new Scanner(System.in);
        Pokemon pomekon = new Pokemon();
        String condicional;
        Date data;
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("Entre com o Id do Pokemon:");
        pomekon.setId(Integer.parseInt(sc.nextLine()));
        System.out.println("Entre com o numero do Pokemon:");
        pomekon.setNumber(Integer.parseInt(sc.nextLine()));
        System.out.println("Entre com o nome do Pokemon:");
        pomekon.setName(sc.nextLine());
        System.out.println("Entre com o tipo do Pokemon:");
        pomekon.setType1(sc.nextLine());
        System.out.println("O Pokemon tem 2 tipos?");
        condicional = sc.nextLine();
        if ((condicional.toLowerCase()).equals("sim")) {
            System.out.println("Entre com o segundo tipo do Pokemon:");
            pomekon.setType2(sc.nextLine());
        } else {
            pomekon.setType2(" ");
        }
        System.out.println("Entre com os pontos de vida do Pokemon:");
        pomekon.setHp(Integer.parseInt(sc.nextLine()));
        System.out.println("Entre com o ataque do Pokemon:");
        pomekon.setAttack(Integer.parseInt(sc.nextLine()));
        System.out.println("Entre com a defesa do Pokemon:");
        pomekon.setDefense(Integer.parseInt(sc.nextLine()));
        System.out.println("Entre com o ataque especial do Pokemon:");
        pomekon.setSp_attack(Integer.parseInt(sc.nextLine()));
        System.out.println("Entre com a defesa especial do Pokemon:");
        pomekon.setSp_defense(Integer.parseInt(sc.nextLine()));
        System.out.println("Entre com a velocidade do Pokemon:");
        pomekon.setSpeed(Integer.parseInt(sc.nextLine()));
        System.out.println("Entre com a geracao do Pokemon:");
        pomekon.setGeneration(Integer.parseInt(sc.nextLine()));
        System.out.println("O Pokemon e lendario?");
        condicional = sc.nextLine();
        if ((condicional.toLowerCase()).equals("sim")) {
            pomekon.setLegendary(true);
        } else {
            pomekon.setLegendary(false);
        }
        System.out.println("Entre com a data do Pokemon:");
        data = formato.parse(sc.nextLine());
        pomekon.setDate_birth(data);

        sc.close();
        return pomekon;

    }

    public String readString(long pos) throws IOException { // metodo para ler string de tamanho variavel
        raf.seek(pos);
        short temp = raf.readShort(); // le o tamanho da string, que e escrito no inicio dela no arquivo
        byte[] newString = new byte[temp]; // cria o array de bytes do tamanho da string
        for (int i = 0; i < temp; i++) { // preenche o array
            newString[i] = raf.readByte();
        }
        return new String(newString, StandardCharsets.UTF_8); // utiliza um construtor de string para construir a
                                                              // string, em UTF-8

    }

    public Pokemon readFromFile(long pos) throws IOException { // metodo para ler do arquivo, passando a posicao como
                                                               // paranetro

        if (pos > 0) {
            raf.seek(pos);
            Pokemon temp = new Pokemon();
            raf.readByte(); // ignorando lapide, pois nos metodos usados a lapide ja eh considerada
            raf.readInt(); // lendo tamanho
            temp.setId(raf.readInt());
            temp.setNumber(raf.readInt());
            temp.setName(readString(raf.getFilePointer()));
            temp.setType1(readString(raf.getFilePointer()));
            temp.setType2(readString(raf.getFilePointer()));
            temp.setTotal(raf.readInt());
            temp.setHp(raf.readInt());
            temp.setAttack(raf.readInt());
            temp.setDefense(raf.readInt());
            temp.setSp_attack(raf.readInt());
            temp.setSp_defense(raf.readInt());
            temp.setSpeed(raf.readInt());
            temp.setGeneration(raf.readInt());
            temp.setLegendary(raf.readBoolean());
            Date data = new Date(raf.readLong());
            temp.setDate_birth(data);

            return temp;
        }
        return null;
    }

    public Pokemon[] readPokemonByTree(int id) throws IOException { // metodo para Read da musica no arquivo binario
        int[] posicoes;
        Pokemon[] pokemons;
        posicoes = tree.read(id);
        pokemons = new Pokemon[posicoes.length];

        for (int i = 0; i < posicoes.length; i++) { // enquanto arquivo nao acabar
            long pos = posicoes[i];
            raf.seek(pos); // pos = posicao da lapide
            if (!raf.readBoolean()) { // checagem dupla da lápide
                pokemons[i] = readFromFile(pos);
            }
        }

        return pokemons;
    }

    public Pokemon readPokemonByHash(int id) throws Exception {
        long data = hash.read(id);

        return readFromFile(data);
    }

    /* Ordenação de bosta externa DEU ERRADO!!!!!!! */
    //
    ////////////////////////
    ////////////////////////
    ////////////////////////
    ////////////////////////
    ////////////////////////
    ////////////////////////
    ////////////////////////

    public static void ordenar(int numCaminhos, int numReg) { // Tentativa da função de ordenação externa
        try {
            RandomAccessFile binFile = new RandomAccessFile("data.bin", "rw");
            String fileName;
            RandomAccessFile binArq[] = new RandomAccessFile[numCaminhos * 2]; // Tamanho variável dos arquivos que
                                                                               // serão abertos
            Pokemon pomekon[] = new Pokemon[numReg]; // Array de pokemons com o total de pokemons registrados no arquivo
                                                     // binário
            int totalReg = 0;
            int cont;

            for (int i = 0; i < numReg; i++) {
                pomekon[i] = new Pokemon();
            }

            for (int i = 0; i < numCaminhos * 2; i++) { // Abertura dos arquivos temporários
                fileName = "binArq" + i + ".bin";
                binArq[i] = new RandomAccessFile(fileName, "rw");
            }

            binFile.readInt();

            // Distribuição

            while (binFile.getFilePointer() < binFile.length()) {
                for (int i = 0; i < numCaminhos; i++) {
                    cont = 0;

                    for (int j = 0; j < numReg; j++) {
                        if (binFile.getFilePointer() < binFile.length()) {
                            if (binFile.readByte() == 0) {
                                binFile.readInt();
                                pomekon[j].setId(binFile.readInt());
                                pomekon[j].setNumber(binFile.readInt());
                                pomekon[j].setName(binFile.readUTF());
                                pomekon[j].setType1(binFile.readUTF());
                                pomekon[j].setType2(binFile.readUTF());
                                pomekon[j].setTotal(binFile.readInt());
                                pomekon[j].setHp(binFile.readInt());
                                pomekon[j].setAttack(binFile.readInt());
                                pomekon[j].setDefense(binFile.readInt());
                                pomekon[j].setSp_attack(binFile.readInt());
                                pomekon[j].setSp_defense(binFile.readInt());
                                pomekon[j].setSpeed(binFile.readInt());
                                pomekon[j].setGeneration(binFile.readInt());
                                pomekon[j].setLegendary(binFile.readBoolean());
                                Date data = new Date(binFile.readLong());
                                pomekon[j].setDate_birth(data);
                                cont++;
                            } else {
                                binFile.seek(binFile.getFilePointer() + binFile.readInt());
                                j--;
                            }
                        }
                    }

                    quickSort(pomekon, cont); // Ordenação na distribuição

                    for (int j = 0; j < cont; j++) { // Escrita do bloco no arquivo binário
                        binArq[i].writeByte(0);
                        binArq[i].writeInt(pomekon[j].getTamanho());
                        binArq[i].writeInt(pomekon[j].getId());
                        binArq[i].writeInt(pomekon[j].getNumber());
                        binArq[i].writeUTF(pomekon[j].getName());
                        binArq[i].writeUTF(pomekon[j].getType1());
                        binArq[i].writeUTF(pomekon[j].getType2());
                        binArq[i].writeInt(pomekon[j].getTotal());
                        binArq[i].writeInt(pomekon[j].getHp());
                        binArq[i].writeInt(pomekon[j].getAttack());
                        binArq[i].writeInt(pomekon[j].getDefense());
                        binArq[i].writeInt(pomekon[j].getSp_attack());
                        binArq[i].writeInt(pomekon[j].getSp_defense());
                        binArq[i].writeInt(pomekon[j].getSpeed());
                        binArq[i].writeInt(pomekon[j].getGeneration());
                        binArq[i].writeBoolean(pomekon[j].getLegendary());
                        binArq[i].writeLong(pomekon[j].getDate_birth().getTime());
                        totalReg++;
                    }
                }
            }

            for (int i = 0; i < numCaminhos; i++) { // Posicionando o ponteiro para o inicio dos arquivos
                binArq[i].seek(0);
            }

            int tamanhoDoBloco = numReg;
            int debug = 0;
            int condicional = 0;

            while (tamanhoDoBloco < totalReg) { // Tentativa da Intercalação / Obs: não funcionou

                System.out.println(debug++);
                Pokemon pokemons[] = new Pokemon[numCaminhos * tamanhoDoBloco]; // Array com o tamanho variável
                                                                                // dependendo do tamanho de cada bloco
                                                                                // das intercalações
                int pokemonsLength;
                int incremento;

                for (int i = 0; i < numCaminhos * tamanhoDoBloco; i++) {
                    pokemons[i] = new Pokemon();
                }

                if (condicional == 0) {
                    System.out.println(
                            "intercalaçao 1 - tamanho do bloco: " + tamanhoDoBloco + " - condicional: " + condicional);
                    for (int i = numCaminhos; i < (numCaminhos * 2)
                            && binArq[0].getFilePointer() < binArq[0].length(); i++) { // começo for intercalação //
                        incremento = 0;
                        pokemonsLength = 0;
                        for (int j = 0; j < numCaminhos; j++) { // começo for setor

                            for (int k = 0; k < tamanhoDoBloco; k++) {
                                if (binArq[j].getFilePointer() < binArq[j].length()) {
                                    binArq[j].readByte();
                                    binArq[j].readInt();
                                    pokemons[k + incremento].setId(binArq[j].readInt());
                                    pokemons[k + incremento].setNumber(binArq[j].readInt());
                                    pokemons[k + incremento].setName(binArq[j].readUTF());
                                    pokemons[k + incremento].setType1(binArq[j].readUTF());
                                    pokemons[k + incremento].setType2(binArq[j].readUTF());
                                    pokemons[k + incremento].setTotal(binArq[j].readInt());
                                    pokemons[k + incremento].setHp(binArq[j].readInt());
                                    pokemons[k + incremento].setAttack(binArq[j].readInt());
                                    pokemons[k + incremento].setDefense(binArq[j].readInt());
                                    pokemons[k + incremento].setSp_attack(binArq[j].readInt());
                                    pokemons[k + incremento].setSp_defense(binArq[j].readInt());
                                    pokemons[k + incremento].setSpeed(binArq[j].readInt());
                                    pokemons[k + incremento].setGeneration(binArq[j].readInt());
                                    pokemons[k + incremento].setLegendary(binArq[j].readBoolean());
                                    Date data = new Date(binArq[j].readLong());
                                    pokemons[k + incremento].setDate_birth(data);
                                    pokemonsLength++;
                                }
                            }
                            incremento = incremento + tamanhoDoBloco;
                        } // fim for setor //

                        quickSort(pokemons, pokemonsLength); // Ordenação do Setor //
                        for (int j = 0; j < pokemonsLength; j++) {
                            binArq[i].writeByte(0);
                            binArq[i].writeInt(pokemons[j].getTamanho());
                            binArq[i].writeInt(pokemons[j].getId());
                            binArq[i].writeInt(pokemons[j].getNumber());
                            binArq[i].writeUTF(pokemons[j].getName());
                            binArq[i].writeUTF(pokemons[j].getType1());
                            binArq[i].writeUTF(pokemons[j].getType2());
                            binArq[i].writeInt(pokemons[j].getTotal());
                            binArq[i].writeInt(pokemons[j].getHp());
                            binArq[i].writeInt(pokemons[j].getAttack());
                            binArq[i].writeInt(pokemons[j].getDefense());
                            binArq[i].writeInt(pokemons[j].getSp_attack());
                            binArq[i].writeInt(pokemons[j].getSp_defense());
                            binArq[i].writeInt(pokemons[j].getSpeed());
                            binArq[i].writeInt(pokemons[j].getGeneration());
                            binArq[i].writeBoolean(pokemons[j].getLegendary());
                            binArq[i].writeLong(pokemons[j].getDate_birth().getTime());
                        }

                        for (int PAULO = 0; PAULO < numCaminhos * tamanhoDoBloco; PAULO++) {
                            pokemons[PAULO] = new Pokemon();
                        }

                        if (i == (numCaminhos * 2) - 1) {
                            i = numCaminhos;
                        }
                    } // fim for intercalação //
                    condicional = 1;
                    tamanhoDoBloco = tamanhoDoBloco * numCaminhos;
                    for (int i = 0; i < numCaminhos * 2; i++) {
                        binArq[i].seek(0);
                    }

                    // for(int i = 0; i < numCaminhos; i++){
                    // binArq[i].setLength(0);
                    // }
                } else {
                    System.out.println(
                            "intercalaçao 2 - tamanho do bloco: " + tamanhoDoBloco + " - condicional: " + condicional);
                    for (int i = 0; i < numCaminhos
                            && binArq[numCaminhos].getFilePointer() < binArq[numCaminhos].length(); i++) { // começo for
                        // intercalação
                        // //
                        incremento = 0;
                        pokemonsLength = 0;
                        for (int j = 0; j < numCaminhos; j++) { // começo for setor //
                            for (int k = 0; k < tamanhoDoBloco; k++) {
                                if (binArq[j + numCaminhos].getFilePointer() < binArq[j + numCaminhos].length()) {
                                    binArq[j + numCaminhos].readByte();
                                    binArq[j + numCaminhos].readInt();
                                    pokemons[k + incremento].setId(binArq[j + numCaminhos].readInt());
                                    pokemons[k + incremento].setNumber(binArq[j + numCaminhos].readInt());
                                    pokemons[k + incremento].setName(binArq[j + numCaminhos].readUTF());
                                    pokemons[k + incremento].setType1(binArq[j + numCaminhos].readUTF());
                                    pokemons[k + incremento].setType2(binArq[j + numCaminhos].readUTF());
                                    pokemons[k + incremento].setTotal(binArq[j + numCaminhos].readInt());
                                    pokemons[k + incremento].setHp(binArq[j + numCaminhos].readInt());
                                    pokemons[k + incremento].setAttack(binArq[j + numCaminhos].readInt());
                                    pokemons[k + incremento].setDefense(binArq[j + numCaminhos].readInt());
                                    pokemons[k + incremento].setSp_attack(binArq[j + numCaminhos].readInt());
                                    pokemons[k + incremento].setSp_defense(binArq[j + numCaminhos].readInt());
                                    pokemons[k + incremento].setSpeed(binArq[j + numCaminhos].readInt());
                                    pokemons[k + incremento].setGeneration(binArq[j + numCaminhos].readInt());
                                    pokemons[k + incremento].setLegendary(binArq[j + numCaminhos].readBoolean());
                                    Date data = new Date(binArq[j + numCaminhos].readLong());
                                    pokemons[k + incremento].setDate_birth(data);
                                    System.out.println(
                                            pokemons[k + incremento] + " " + i + " " + j + " " + k + " " + incremento);
                                    pokemonsLength++;
                                }
                            }
                            incremento = incremento + tamanhoDoBloco;
                        } // fim for setor //

                        quickSort(pokemons, pokemonsLength); // Ordenação do Setor //

                        for (int j = 0; j < pokemonsLength; j++) {
                            binArq[i].writeByte(0);
                            binArq[i].writeInt(pokemons[j].getTamanho());
                            binArq[i].writeInt(pokemons[j].getId());
                            binArq[i].writeInt(pokemons[j].getNumber());
                            binArq[i].writeUTF(pokemons[j].getName());
                            binArq[i].writeUTF(pokemons[j].getType1());
                            binArq[i].writeUTF(pokemons[j].getType2());
                            binArq[i].writeInt(pokemons[j].getTotal());
                            binArq[i].writeInt(pokemons[j].getHp());
                            binArq[i].writeInt(pokemons[j].getAttack());
                            binArq[i].writeInt(pokemons[j].getDefense());
                            binArq[i].writeInt(pokemons[j].getSp_attack());
                            binArq[i].writeInt(pokemons[j].getSp_defense());
                            binArq[i].writeInt(pokemons[j].getSpeed());
                            binArq[i].writeInt(pokemons[j].getGeneration());
                            binArq[i].writeBoolean(pokemons[j].getLegendary());
                            binArq[i].writeLong(pokemons[j].getDate_birth().getTime());
                        }

                        for (int PAULO = 0; PAULO < numCaminhos * tamanhoDoBloco; PAULO++) {
                            pokemons[PAULO] = new Pokemon();
                        }

                        if (i == (numCaminhos) - 1) {
                            i = 0;
                        }
                    } // fim for intercalação //
                    condicional = 0;
                    tamanhoDoBloco = tamanhoDoBloco * numCaminhos;
                    for (int i = 0; i < numCaminhos * 2; i++) {
                        binArq[i].seek(0);
                    }
                    // for(int i = numCaminhos; i < numCaminhos * 2; i++){
                    // binArq[i].setLength(0);
                    // }
                }
            }

            for (int i = 0; i < numCaminhos * 2; i++) {
                binArq[i].close();
            }
            binFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void quickSort(Pokemon[] pomekon, int numReg) {
        quickSort(0, numReg - 1, pomekon);
    }

    public static void quickSort(int esq, int dir, Pokemon[] pomekon) {
        int i = esq;
        int j = dir;
        int pivo = pomekon[(dir + esq) / 2].getId();

        while (i <= j) {
            while (pomekon[i].getId() < pivo) {
                i++;
            }

            while (pomekon[j].getId() > pivo) {
                j--;
            }

            if (i <= j) {
                Pokemon temp = pomekon[i];
                pomekon[i] = pomekon[j];
                pomekon[j] = temp;
                i++;
                j--;
            }
        }
        if (esq < dir) {
            quickSort(esq, j, pomekon);
        }
        if (i < dir) {
            quickSort(i, dir, pomekon);
        }
    }

}
