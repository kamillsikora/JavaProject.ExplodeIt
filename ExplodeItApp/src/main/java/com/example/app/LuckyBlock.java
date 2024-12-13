package com.example.app;

import java.util.List;

public class LuckyBlock extends Block {
    private int lootProbability;
    private List<Item> items;

    public LuckyBlock(String color, boolean isDestructible, int lootProbability, List<Item> items) {
        super(color, isDestructible);
        this.lootProbability = lootProbability;
        this.items = items;
    }

    public int getLootProbability() {
        return lootProbability;
    }

    public List<Item> getItems() {
        return items;
    }
}
