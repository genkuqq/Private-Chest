package com.nukku;

import com.hypixel.hytale.server.core.inventory.ItemStack;

public class ChestComponent{
    public static final short CHEST_SIZE = 27;
    private static final ItemStack[] items = new ItemStack[CHEST_SIZE];

    public void setItem(short slot, ItemStack item){
        if (slot < 0 || slot >= CHEST_SIZE) return;
        items[slot] = item;
    }
    public ItemStack[] getItems() {
        return items;
    }
}