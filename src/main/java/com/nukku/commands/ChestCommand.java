package com.nukku.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.PageManager;
import com.hypixel.hytale.server.core.entity.entities.player.windows.Window;
import com.hypixel.hytale.server.core.inventory.container.SimpleItemContainer;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import com.nukku.config.ChestConfig;
import com.nukku.windows.ChestWindow;

import javax.annotation.Nonnull;
import java.util.Collection;

public class ChestCommand extends AbstractPlayerCommand {
    private final Config<ChestConfig> config;
    public ChestCommand(Config<ChestConfig> config) {
        super("chest", "Open your private Chest");
        requirePermission("privatechest.use");
        this.config = config;
        addUsageVariant(new ChestPlayerInspectCommand(config));
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        CommandSender sender = commandContext.sender();
        SimpleItemContainer simpleItemContainer = new SimpleItemContainer((short) config.get().getChestSize());
        ChestWindow chestWindow = new ChestWindow(simpleItemContainer,playerRef);
        if (sender instanceof Player  player){
            PageManager pageManager = player.getPageManager();
            pageManager.setPageWithWindows(ref, store, Page.Bench, true, new Window[]{chestWindow});
        }
    }

    private static class ChestPlayerInspectCommand extends AbstractPlayerCommand{
        private final Config<ChestConfig> config;
        private final RequiredArg<String> playerName;
        public ChestPlayerInspectCommand(Config<ChestConfig> config) {
            super("Inspect player's chests");
            this.config = config;
            requirePermission("privatechest.use.admin");
            playerName = withRequiredArg("name","Player Name",ArgTypes.STRING);
        }

        @Override
        protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
            CommandSender sender = commandContext.sender();
            if (playerName.get(commandContext) != null){
                Collection<PlayerRef> players = world.getPlayerRefs();
                for (PlayerRef playerRefer : players){
                    if (playerRefer.getUsername().equals(playerName.get(commandContext))){
                        SimpleItemContainer simpleItemContainer = new SimpleItemContainer((short) config.get().getChestSize());
                        ChestWindow chestWindow = new ChestWindow(simpleItemContainer,playerRef);
                        if (sender instanceof Player  player){
                            PageManager pageManager = player.getPageManager();
                            pageManager.setPageWithWindows(ref, store, Page.Bench, true, new Window[]{chestWindow});
                        }
                    }
                }
            }



        }
    }
}
