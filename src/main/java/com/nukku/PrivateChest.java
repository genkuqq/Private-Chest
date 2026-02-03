package com.nukku;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import com.nukku.commands.ChestCommand;
import com.nukku.config.ChestConfig;
import com.nukku.managers.ChestRepository;
import com.nukku.managers.DataManager;
import com.nukku.managers.DatabaseManager;

import java.sql.Connection;
import java.sql.DriverManager;


public class PrivateChest extends JavaPlugin {

    Connection conn = null;
    private static PrivateChest instance;
    private final Config<ChestConfig> config;
    private final DataManager dataManager;
    private DatabaseManager database;
    private ChestRepository chestRepository;
    public PrivateChest(JavaPluginInit init) {
        super(init);
        this.config = this.withConfig("chestconfig", ChestConfig.CODEC);
        this.dataManager = new DataManager(getConfig());
    }


    public static PrivateChest get() {
        return instance;
    }

    public Config<ChestConfig> getConfig() {
        return config;
    }

    @Override
    protected void setup() {
        super.setup();
        instance = this;
        this.config.save();
        this.getCommandRegistry().registerCommand(new ChestCommand(config));
        database = new DatabaseManager(
                "jdbc:mysql://127.0.0.1:3306/test" +
                        "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "root",
                "root"
        );

        chestRepository = new ChestRepository(database);

    }

    @Override
    protected void shutdown() {
        if (database != null) {
            database.shutdown();
        }
    }

    private void connectMysql() {
        try {
            database = new DatabaseManager(
                    "jdbc:mysql://127.0.0.1:3306/test?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "root"
            );
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public ChestRepository getChestRepository() {
        return chestRepository;
    }
}
