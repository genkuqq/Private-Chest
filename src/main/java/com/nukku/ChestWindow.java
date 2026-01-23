package com.nukku;

import com.google.gson.JsonObject;
import com.hypixel.hytale.protocol.packets.window.WindowType;
import com.hypixel.hytale.server.core.entity.entities.player.windows.ItemContainerWindow;
import com.hypixel.hytale.server.core.entity.entities.player.windows.Window;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;

import javax.annotation.Nonnull;
import java.util.UUID;

public class ChestWindow extends Window implements ItemContainerWindow{
    private final ItemContainer container;
    public ChestWindow(ItemContainer container) {
        super(WindowType.Container);
        this.container = container;
    }

    @Nonnull
    @Override
    public ItemContainer getItemContainer() {
        return this.container;
    }

    @Nonnull
    @Override
    public JsonObject getData() {
        JsonObject data = new JsonObject();
        return data;
    }

    @Override
    protected boolean onOpen0() {
        UUID playerUUID = this.getPlayerRef().getUuid();
        ChestComponent chestComponent = DataManager.load(playerUUID);
        ItemStack[] items = chestComponent.getItems();
        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            if (item == null || item.isEmpty()) {
                continue;
            }
            container.addItemStackToSlot((short)i, item);
        }
        return true;
    }

    @Override
    protected void onClose0() {
        UUID playerUUID = this.getPlayerRef().getUuid();
        ChestComponent chestComponent = ChestManager.getChest(playerUUID);
        for (int i = 0; i < 26; i++){
            ItemStack item = this.container.getItemStack((short) i);
            if (item != null){
                chestComponent.setItem((short) i,item);
            }
        }
        DataManager.save(playerUUID,chestComponent);
    }
}
