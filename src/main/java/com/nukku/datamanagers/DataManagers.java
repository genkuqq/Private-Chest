package com.nukku.datamanagers;

import com.hypixel.hytale.server.core.util.Config;
import com.nukku.config.ChestConfig;
import com.nukku.config.DatabaseConfig;

public final class DataManagers {

    private static IDataManager INSTANCE;

    public static void init(Config<DatabaseConfig> config) {
        INSTANCE = switch (config.get().getDatabaseType()) {
            case "Json" -> new JsonDataManager();
            case "Mysql" -> new MysqlDataManager();
            default -> null;
        };
    }

    public static IDataManager get() {
        return INSTANCE;
    }
}