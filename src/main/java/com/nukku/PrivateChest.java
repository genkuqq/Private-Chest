package com.nukku;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import com.nukku.commands.ChestCommand;
import com.nukku.config.ChestConfig;
import com.nukku.managers.DataManager;

public class PrivateChest extends JavaPlugin {

    private static PrivateChest instance;
    private final Config<ChestConfig> config;
    private final DataManager dataManager;
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

    }
}
