package Pokemon;
import java.text.SimpleDateFormat;
import java.util.Date;


// Classe Pokemon com os atributos, construtores, metodos de Set, Get,
// tamanho e uma função de máscara para imprimir um objeto

public class Pokemon { 
    private int id;
    private int number;
    private String name;
    private String type1;
    private String type2;
    private int total;
    private int hp;
    private int attack;
    private int defense;
    private int sp_attack;
    private int sp_defense;
    private int speed;
    private int generation;
    private Boolean legendary;
    private Date date_birth;

    public Pokemon() {
        id = 0;
        number = 0;
        name = "";
        type1 = "";
        type2 = "";
        total = 0;
        hp = 0;
        attack = 0;
        defense = 0;
        sp_attack = 0;
        sp_defense = 0;
        speed = 0;
        generation = 0;
        legendary = false;
        date_birth = new Date();
    }

    public Pokemon(int id, int number, String name, String type1, String type2, int total, int hp, int attack,
            int defense, int sp_attack,
            int sp_defense, int speed, int generation, Boolean legendary, Date date_birth) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.total = total;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.sp_attack = sp_attack;
        this.sp_defense = sp_defense;
        this.speed = speed;
        this.generation = generation;
        this.legendary = legendary;
        this.date_birth = date_birth;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setSp_attack(int sp_attack) {
        this.sp_attack = sp_attack;
    }

    public void setSp_defense(int sp_defense) {
        this.sp_defense = sp_defense;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public void setLegendary(Boolean legendary) {
        this.legendary = legendary;
    }

    public void setDate_birth(Date date_birth) {
        this.date_birth = date_birth;
    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getType1() {
        return type1;
    }

    public String getType2() {
        return type2;
    }

    public int getTotal() {
        return hp + attack + defense + sp_attack + sp_defense + speed;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSp_attack() {
        return sp_attack;
    }

    public int getSp_defense() {
        return sp_defense;
    }

    public int getSpeed() {
        return speed;
    }

    public int getGeneration() {
        return generation;
    }

    public Boolean getLegendary() {
        return legendary;
    }

    public Date getDate_birth() {
        return date_birth;
    }

    @Override
    public String toString() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date dataFortnite = getDate_birth();
        String dataFormatada = formato.format(dataFortnite);

        if (type2.equals(" ")) {
            return id + ", " + number + ", " + name + ", " + type1 + ", " + total + ", " + hp + ", " + attack +
                    ", " + defense + ", " + sp_attack + ", " + sp_defense + ", " + speed + ", " + generation + ", "
                    + legendary + ", " + dataFormatada;

        } else {
            return id + ", " + number + ", " + name + ", " + type1 + ", " + type2 + ", " + total + ", " + hp + ", "
                    + attack +
                    ", " + defense + ", " + sp_attack + ", " + sp_defense + ", " + speed + ", " + generation + ", "
                    + legendary + ", " + dataFormatada;
        }
    }

    public int getTamanho() {
        return 55 + name.length() + type1.length() + type2.length();
    }
}
