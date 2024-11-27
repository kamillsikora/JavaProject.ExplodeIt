import java.util.ArrayList;
import java.util.List;

public class LuckyBlock extends Block{
    private int lootProbability;
    private List<Item> items;

    public LuckyBlock(String color, boolean isDestractible, int lootProbability, List<Item> items){
        super(color, isDestractible);
        this.lootProbability = lootProbability;
        this.items = items;
    }
}
