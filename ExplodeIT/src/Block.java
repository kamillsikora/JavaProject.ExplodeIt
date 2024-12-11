public abstract class Block {
    private String color;
    private boolean isDestructible;
    private int positionX;
    private int positionY;

    public Block(String color, boolean isDestructible) {
        this.color = color;
        this.isDestructible = isDestructible;
    }

    public String getColor() {
        return color;
    }

    public boolean isDestructible() {
        return isDestructible;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPosition(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public int getPositionY() {
        return positionY;
    }
}
