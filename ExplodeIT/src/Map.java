public class Map {
    private String name;
    private String color;

    private Block[] blocks;

    private Item[] items;

    private Character[] characters;

    public Map(String name, String color, Block[] blocks, Item[] items, Character[] characters) {
        this.name = name;
        this.color = color;
        this.blocks = blocks;
        this.items = items;
        this.characters = characters;
    }
}
