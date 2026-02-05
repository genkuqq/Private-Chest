package com.nukku.datamanagers;

import com.google.gson.*;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.util.Config;
import com.nukku.PrivateChest;
import com.nukku.components.ChestComponent;
import com.nukku.config.ChestConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class JsonDataManager implements IDataManager{
    private static Config<ChestConfig> config;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FOLDER = new File("mods/Nukku_PrivateChest/chests/");
    JsonDataManager(){
        config = PrivateChest.get().getChestConfig();
    }
    @Override
    public ChestComponent load(UUID userId, int page) {
        ChestComponent chest = new ChestComponent();
        chest.setChestSize(config.get().getChestSize());
        File file = new File(FOLDER, userId.toString() + ".json");
        if (!file.exists()) return chest;
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            JsonObject pages = root.getAsJsonObject("pages");
            if (pages == null) return chest;
            JsonObject pageObj = pages.getAsJsonObject(String.valueOf(page));
            if (pageObj == null) return chest;
            int size = pageObj.get("size").getAsInt();
            chest.setChestSize(size);
            ItemStack[] items = new ItemStack[size];
            JsonArray itemsArray = pageObj.getAsJsonArray("items");
            if (itemsArray != null) {
                for (JsonElement el : itemsArray) {
                    JsonObject obj = el.getAsJsonObject();
                    int slot = obj.get("slot").getAsInt();
                    String itemId = obj.get("item").getAsString();
                    int amount = obj.get("amount").getAsInt();
                    items[slot] = new ItemStack(itemId, amount);
                }
            }
            chest.setItems(items);
            return chest;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chest;
    }

    @Override
    public void save(UUID userId, int page, ChestComponent chestComponent) {
        File file = new File(FOLDER, userId.toString() + ".json");
        JsonObject root;
        if (file.exists()) {
            try (Reader r = new FileReader(file)) {
                root = JsonParser.parseReader(r).getAsJsonObject();
            } catch (Exception e) {
                root = new JsonObject();
            }
        } else {
            root = new JsonObject();
        }
        JsonObject pages = root.has("pages")
                ? root.getAsJsonObject("pages")
                : new JsonObject();
        JsonObject pageObj = new JsonObject();
        pageObj.addProperty("size", chestComponent.getChestSize());
        JsonArray itemsArray = new JsonArray();
        ItemStack[] items = chestComponent.getItems();
        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            if (item == null || item.isEmpty()) continue;
            JsonObject obj = new JsonObject();
            obj.addProperty("slot", i);
            obj.addProperty("item", item.getItem().getId());
            obj.addProperty("amount", item.getQuantity());
            itemsArray.add(obj);
        }
        pageObj.add("items", itemsArray);
        pages.add(String.valueOf(page), pageObj);
        root.add("pages", pages);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            GSON.toJson(root, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
