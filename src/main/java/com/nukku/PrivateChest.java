package com.nukku;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

public class PrivateChest extends JavaPlugin {

    private static PrivateChest instance;
    public PrivateChest(JavaPluginInit init) {
        super(init);
    }
    public static PrivateChest get() {
        return instance;
    }

    @Override
    protected void setup() {
        super.setup();
        instance = this;
        this.getCommandRegistry().registerCommand(new ChestCommand());

    }
}
