package com.nukku.managers;

import com.nukku.PrivateChest;
import com.nukku.components.ChestComponent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChestManager {
    private static final Map<UUID, Map<Integer, ChestComponent>> CHESTS =
            new ConcurrentHashMap<>();

    public static ChestComponent getChest(UUID uuid, int page) {
        return CHESTS
                .computeIfAbsent(uuid, u -> new ConcurrentHashMap<>())
                .computeIfAbsent(page, p -> DataManager.load(uuid, p));
    }

}