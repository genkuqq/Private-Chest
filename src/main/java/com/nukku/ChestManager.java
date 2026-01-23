package com.nukku;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChestManager {
    private static final Map<UUID, ChestComponent> CHESTS = new ConcurrentHashMap<>();
    public static ChestComponent getChest(UUID uuid) {
        return CHESTS.computeIfAbsent(uuid, DataManager::load);
    }
}
