package com.nukku.windows;

import com.google.gson.JsonObject;
import com.hypixel.hytale.component.ComponentAccessor;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.window.WindowType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.windows.ItemContainerWindow;
import com.hypixel.hytale.server.core.entity.entities.player.windows.Window;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.nukku.components.ChestComponent;
import com.nukku.datamanagers.DataManagers;

import javax.annotation.Nonnull;
import java.io.*;

public class ChestWindow extends Window implements ItemContainerWindow{
    private final ItemContainer container;
    private final PlayerRef playerRef;
    private final Integer chestPage;
    private final ChestComponent chest;
    public ChestWindow(ItemContainer container, PlayerRef playerRef, Integer chestPage,ChestComponent chest) {
        super(WindowType.Container);
        this.container = container;
        this.playerRef = playerRef;
        this.chestPage = chestPage;
        this.chest = chest;
    }

    @Nonnull
    @Override
    public ItemContainer getItemContainer() {
        return this.container;
    }

    @Nonnull
    @Override
    public JsonObject getData() {
        return new JsonObject();
    }

    @Override
    protected boolean onOpen0(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store) {
        container.clear();
        ItemStack[] items = chest.getItems();
        if (items != null){
            for (int i = 0; i < items.length; i++) {
                ItemStack item = items[i];
                if (item == null || item.isEmpty()) {
                    continue;
                }
                container.addItemStackToSlot((short)i, item);
            }
        }
        return true;
    }


    @Override
    protected void onClose0(@Nonnull Ref<EntityStore> ref, @Nonnull ComponentAccessor<EntityStore> componentAccessor) {
        ItemStack[] items = new ItemStack[chest.getChestSize()];
        for (int i = 0; i < chest.getChestSize(); i++){
            ItemStack item = this.container.getItemStack((short) i);
            if (item != null){
                items[i]=item;
            }
        }
        chest.setItems(items);
        DataManagers.get().save(playerRef.getUuid(),chestPage,chest);
    }
}
