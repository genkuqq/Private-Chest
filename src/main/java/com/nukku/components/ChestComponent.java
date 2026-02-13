package com.nukku.components;

import com.hypixel.hytale.server.core.inventory.ItemStack;

public class ChestComponent {

    public Integer chestSize;
    private ItemStack[] items;

    public ChestComponent() {}

    public void setItems(ItemStack[] items) {
        this.items = items;
    }

    public ItemStack[] getItems() {
        return items;
    }

    public void setChestSize(Integer size) {
        chestSize = size;
    }

    public Integer getChestSize() {
        return chestSize;
    }
}
