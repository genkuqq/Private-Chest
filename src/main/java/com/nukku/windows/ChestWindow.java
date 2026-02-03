package com.nukku.windows;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import com.nukku.managers.ChestManager;
import com.nukku.managers.ChestRepository;
import com.nukku.managers.DataManager;
import com.nukku.managers.DatabaseManager;
import com.nukku.utils.ChestSerializer;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ChestWindow extends Window implements ItemContainerWindow{
    private final ItemContainer container;
    private final PlayerRef playerRef;
    private final Integer chestPage;
    public ChestWindow(ItemContainer container, PlayerRef playerRef, Integer chestPage) {
        super(WindowType.Container);
        this.container = container;
        this.playerRef = playerRef;
        this.chestPage = chestPage;
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
        UUID playerUUID = this.playerRef.getUuid();


        String t = ChestRepository.loadChest(playerUUID,chestPage);
        ChestComponent chestComponent = DataManager.load(playerUUID,chestPage);

        ItemStack[] items = chestComponent.getItems();
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
        assert this.getPlayerRef() != null;
        UUID playerUUID = this.getPlayerRef().getUuid();
        Player player = componentAccessor.getComponent(ref, Player.getComponentType());
        ChestComponent chestComponent = ChestManager.getChest(playerUUID,chestPage);
        ItemStack[] items = new ItemStack[chestComponent.getChestSize()];
        for (int i = 0; i < chestComponent.getChestSize(); i++){
            ItemStack item = this.container.getItemStack((short) i);
            if (item != null){
                items[i]=item;
            }
        }
        chestComponent.setItems(items);
        Kapa(items,chestPage);
        DataManager.save(playerUUID,chestComponent,chestPage);
    }

    private void Kapa(ItemStack[] items, Integer chestPage){
        String json = ChestSerializer.toJson(items);

        ChestRepository.saveChest(playerRef.getUuid(), chestPage, json);
    }


}
