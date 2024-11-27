public abstract class Block {
    private String color;
    private boolean isDestractible;

    public Block(String color, boolean isDestractible) {
        this.color = color;
        this.isDestractible = isDestractible;
    }
}
