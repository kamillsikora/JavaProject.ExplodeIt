public class Item {
    private String name;
    private ItemLook look;
    private int timeOfEffect;

    private int dropProbability;

    private int charactersSpeed;

    private int charactersPower;

    private int explosionsSpeed;

    private int charactersHp;

    public Item(String name, ItemLook look, int timeOfEffect, int dropProbability, int charactersSpeed, int charactersPower, int exposionsSpeed, int charactersHp){
        this.name = name;
        this.look = look;
        this.timeOfEffect = timeOfEffect;
        this.dropProbability = dropProbability;
        this.charactersSpeed = charactersSpeed;
        this.charactersPower = charactersPower;
        this.explosionsSpeed = exposionsSpeed;
        this.charactersHp = charactersHp;
    }

}
