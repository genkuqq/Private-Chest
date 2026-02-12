package com.nukku.utils;

import com.google.gson.*;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import org.bson.BsonDocument;

public final class ChestSerializer {

    private static final Gson GSON = new GsonBuilder().create();
    private static final int DATA_VERSION = 1;

    private ChestSerializer() {}

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
            obj.addProperty("durability", stack.getDurability());
            obj.addProperty("maxDurability", stack.getMaxDurability());
            BsonDocument enchantDoc = stack.getFromMetadataOrNull("Enchantments", Codec.BSON_DOCUMENT);
            if (enchantDoc != null && !enchantDoc.isEmpty()) {
                BsonDocument wrapper = new BsonDocument();
                wrapper.put("Enchantments", enchantDoc);
                obj.addProperty("meta", wrapper.toJson());
            }
            array.add(obj);
        }

        root.add("items", array);
        return GSON.toJson(root);
    }
    public static ItemStack[] fromJson(String json, int size) {
        ItemStack[] items = new ItemStack[size];

        if (json == null || json.isBlank()) {
            return items;
        }

        JsonObject root = JsonParser.parseString(json).getAsJsonObject();

        JsonArray array = root.getAsJsonArray("items");
        if (array == null) return items;

        for (JsonElement el : array) {
            JsonObject obj = el.getAsJsonObject();

            int slot = obj.get("slot").getAsInt();
            if (slot < 0 || slot >= size) continue;

            String itemId = obj.get("id").getAsString();
            int count = obj.get("count").getAsInt();
            double durability = obj.get("durability").getAsDouble();
            double maxDurability = obj.get("maxDurability").getAsDouble();
            BsonDocument metadata = null;

            if (obj.has("meta")) {
                String metaJson = obj.get("meta").getAsString();

                if (metaJson != null && !metaJson.trim().isEmpty()) {
                    metadata = BsonDocument.parse(metaJson);
                }
            }
            items[slot] = new ItemStack(itemId, count, durability, maxDurability,metadata);
        }

        return items;
    }
}
