package com.nukku;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import com.nukku.commands.ChestCommand;
import com.nukku.config.ChestConfig;
import com.nukku.config.DatabaseConfig;
import com.nukku.datamanagers.DataManagers;
import com.nukku.datamanagers.mysql.MysqlDatabase;

public class PrivateChest extends JavaPlugin {

    private static PrivateChest instance;
    private final Config<ChestConfig> chestConfig;
    private final Config<DatabaseConfig> databaseConfig;

    public PrivateChest(JavaPluginInit init) {
        super(init);
        this.chestConfig = this.withConfig("chestConfig", ChestConfig.CODEC);
        this.databaseConfig = this.withConfig(
            "databaseConfig",
            DatabaseConfig.CODEC
        );
    }

    public static PrivateChest get() {
        return instance;
    }

    public Config<ChestConfig> getChestConfig() {
        return chestConfig;
    }

    public Config<DatabaseConfig> getDatabaseConfig() {
        return databaseConfig;
    }

    @Override
    protected void setup() {
        super.setup();
        instance = this;
        this.chestConfig.save();
        this.databaseConfig.save();
        this.getCommandRegistry().registerCommand(
            new ChestCommand(chestConfig)
        );
        DataManagers.init(this.databaseConfig);
    }

    @Override
    protected void shutdown() {
        if (DataManagers.get() != null) {
            MysqlDatabase.shutdown();
        }
    }
}
