package com.nukku;

import com.google.gson.*;
import com.hypixel.hytale.server.core.inventory.ItemStack;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class DataManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FOLDER = new File("mods/PrivateChest/");

    static {
        if (!FOLDER.exists()) {
            FOLDER.mkdirs();
        }
    }

    public static void save(UUID uuid, ChestComponent chest) {
        File file = new File(FOLDER, uuid.toString() + ".json");
        JsonObject root = new JsonObject();
        root.addProperty("size", ChestComponent.CHEST_SIZE);
        JsonArray itemsArray = new JsonArray();
        ItemStack[] items = chest.getItems();
        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            if (item == null || item.isEmpty()) continue;
            JsonObject obj = new JsonObject();
            obj.addProperty("slot", i);
            obj.addProperty("item", item.getItem().getId());
            obj.addProperty("amount", item.getQuantity());
            itemsArray.add(obj);
        }
        root.add("items", itemsArray);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            GSON.toJson(root, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ChestComponent load(UUID uuid) {
        File file = new File(FOLDER, uuid.toString() + ".json");
        ChestComponent chest = new ChestComponent();
        if (!file.exists()) {
            return chest;
        }
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray itemsArray = root.getAsJsonArray("items");
            if (itemsArray != null) {
                for (JsonElement el : itemsArray) {
                    JsonObject obj = el.getAsJsonObject();
                    int slot = obj.get("slot").getAsInt();
                    String itemId = obj.get("item").getAsString();
                    int amount = obj.get("amount").getAsInt();
                    chest.setItem((short) slot, new ItemStack(itemId,amount));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chest;
    }
}
