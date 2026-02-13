package com.nukku.datamanagers;

import com.hypixel.hytale.server.core.util.Config;
import com.nukku.config.DatabaseConfig;
import com.nukku.datamanagers.mysql.MysqlDataManager;
import com.nukku.datamanagers.sqlite.SqliteDataManager;

public final class DataManagers {

    private static IDataManager INSTANCE;

    public static void init(Config<DatabaseConfig> config) {
        INSTANCE = switch (config.get().getDatabaseType()) {
            case "Sqlite" -> new SqliteDataManager();
            case "Mysql" -> new MysqlDataManager();
            default -> null;
        };
    }

    public static IDataManager get() {
        return INSTANCE;
    }
}
