package com.nukku.utils;

import com.google.gson.*;
import com.hypixel.hytale.server.core.inventory.ItemStack;

public final class ChestSerializer {

    private static final Gson GSON = new GsonBuilder().create();
    private static final int DATA_VERSION = 1;

    private ChestSerializer() {}

    // =========================
    // ItemStack[] → JSON
    // =========================
    public static String toJson(ItemStack[] items) {
        JsonObject root = new JsonObject();
        root.addProperty("version", DATA_VERSION);

        JsonArray array = new JsonArray();

        for (int slot = 0; slot < items.length; slot++) {
            ItemStack stack = items[slot];
            if (stack == null || stack.isEmpty()) continue;

            JsonObject obj = new JsonObject();
            obj.addProperty("slot", slot);
            obj.addProperty("id", stack.getItem().getId());
            obj.addProperty("count", stack.getQuantity());

            array.add(obj);
        }

        root.add("items", array);
        return GSON.toJson(root);
    }

    // =========================
    // JSON → ItemStack[]
    // =========================
    public static ItemStack[] fromJson(String json, int size) {
        ItemStack[] items = new ItemStack[size];

        if (json == null || json.isBlank()) {
            return items;
        }

        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        int version = root.has("version") ? root.get("version").getAsInt() : 1;

        if (version != DATA_VERSION) {
            // Future-proof: handle migrations here
            // For now, assume version 1
        }

        JsonArray array = root.getAsJsonArray("items");
        if (array == null) return items;

        for (JsonElement el : array) {
            JsonObject obj = el.getAsJsonObject();

            int slot = obj.get("slot").getAsInt();
            if (slot < 0 || slot >= size) continue;

            String itemId = obj.get("id").getAsString();
            int count = obj.get("count").getAsInt();
            items[slot] = new ItemStack(itemId, count);
        }

        return items;
    }
}
