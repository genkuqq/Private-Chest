package com.nukku.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.arguments.system.DefaultArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
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
    private final DefaultArg<Integer> chestPage;
    public ChestCommand(Config<ChestConfig> config) {
        super("chest", "Open your private Chest");
        this.requirePermission("privatechest.use");
        this.config = config;
        this.chestPage = this.withDefaultArg("page", "Chest page", ArgTypes.INTEGER,1,"First Page");
        this.addUsageVariant(new ChestPlayerInspectCommand(config));
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        CommandSender sender = commandContext.sender();
        if (chestPage.get(commandContext) == null) {
            return;
        }
        if (!sender.hasPermission("privatechest.use."+commandContext.get(chestPage))){
            // no perm message
            return;
        }
        SimpleItemContainer simpleItemContainer = new SimpleItemContainer((short) config.get().getChestSize());
        ChestWindow chestWindow = new ChestWindow(simpleItemContainer,playerRef,chestPage.get(commandContext));
        if (sender instanceof Player  player){
            PageManager pageManager = player.getPageManager();
            pageManager.setPageWithWindows(ref, store, Page.Bench, true, new Window[]{chestWindow});
        }
    }

    private static class ChestPlayerInspectCommand extends AbstractPlayerCommand{
        private final Config<ChestConfig> config;
        private final RequiredArg<String> playerName;
        private final DefaultArg<Integer> chestPage;

        public ChestPlayerInspectCommand(Config<ChestConfig> config) {
            super("Inspect player's chests");
            this.config = config;
            this.requirePermission("privatechest.use.admin");
            this.playerName = this.withRequiredArg("name","Player Name",ArgTypes.STRING);
            this.chestPage = this.withDefaultArg("page", "Chest page", ArgTypes.INTEGER,1,"First Page");
        }

        @Override
        protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
            CommandSender sender = commandContext.sender();
            if (playerName.get(commandContext) == null || chestPage.get(commandContext) == null) {
                return;
            }
            if (!sender.hasPermission("privatechest.use."+commandContext.get(chestPage))){
                // perm error
                return;
            }
            Collection<PlayerRef> players = world.getPlayerRefs();
            for (PlayerRef playerRefer : players){
                if (playerRefer.getUsername().equals(playerName.get(commandContext))){
                    SimpleItemContainer simpleItemContainer = new SimpleItemContainer((short) config.get().getChestSize());
                    ChestWindow chestWindow = new ChestWindow(simpleItemContainer,playerRef,commandContext.get(chestPage));
                    if (sender instanceof Player  player){
                        PageManager pageManager = player.getPageManager();
                        pageManager.setPageWithWindows(ref, store, Page.Bench, true, new Window[]{chestWindow});
                    }
                }
            }
        }
    }
}
