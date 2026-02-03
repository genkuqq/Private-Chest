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
        this.requirePermission("privatechest.use");
        this.config = config;
        this.addUsageVariant(new ChestSelfPage(config));
        this.addUsageVariant(new ChestPlayerPage(config));
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        CommandSender sender = commandContext.sender();
        SimpleItemContainer simpleItemContainer = new SimpleItemContainer((short) config.get().getChestSize());
        ChestWindow chestWindow = new ChestWindow(simpleItemContainer,playerRef,1);
        if (sender instanceof Player  player){
            PageManager pageManager = player.getPageManager();
            pageManager.setPageWithWindows(ref, store, Page.Bench, true, new Window[]{chestWindow});
        }
    }
    static class ChestSelfPage extends AbstractPlayerCommand {

        private final Config<ChestConfig> config;
        private final RequiredArg<Integer> page;

        ChestSelfPage(Config<ChestConfig> config) {
            super("");
            this.config = config;
            this.page = this.withRequiredArg("page", "Chest page", ArgTypes.INTEGER);
        }

        @Override
        protected void execute(
                @Nonnull CommandContext commandContext,
                @Nonnull Store<EntityStore> store,
                @Nonnull Ref<EntityStore> ref,
                @Nonnull PlayerRef playerRef,
                @Nonnull World world
        ) {
            CommandSender sender = commandContext.sender();
            if (!sender.hasPermission("privatechest.use."+commandContext.get(page))){
                // no perm message
                return;
            }
            SimpleItemContainer simpleItemContainer = new SimpleItemContainer((short) config.get().getChestSize());
            ChestWindow chestWindow = new ChestWindow(simpleItemContainer,playerRef,commandContext.get(page));
            if (sender instanceof Player  player){
                PageManager pageManager = player.getPageManager();
                pageManager.setPageWithWindows(ref, store, Page.Bench, true, new Window[]{chestWindow});
            }
        }
    }
    static class ChestPlayerPage extends AbstractPlayerCommand {

        private final Config<ChestConfig> config;
        private final RequiredArg<Integer> page;
        private final RequiredArg<String> player;

        ChestPlayerPage(Config<ChestConfig> config) {
            super("");
            this.config = config;
            this.requirePermission("privatechest.use.admin");
            this.page = this.withRequiredArg("page", "Chest page", ArgTypes.INTEGER);
            this.player = this.withRequiredArg("player", "Player", ArgTypes.STRING);
        }

        @Override
        protected void execute(
                @Nonnull CommandContext commandContext,
                @Nonnull Store<EntityStore> store,
                @Nonnull Ref<EntityStore> ref,
                @Nonnull PlayerRef playerRef,
                @Nonnull World world
        ) {
            CommandSender sender = commandContext.sender();
            Collection<PlayerRef> players = world.getPlayerRefs();
            for (PlayerRef playerRefer : players){
                if (playerRefer.getUsername().equals(player.get(commandContext))){
                    SimpleItemContainer simpleItemContainer = new SimpleItemContainer((short) config.get().getChestSize());
                    ChestWindow chestWindow = new ChestWindow(simpleItemContainer,playerRef,commandContext.get(page));
                    if (sender instanceof Player  player){
                        PageManager pageManager = player.getPageManager();
                        pageManager.setPageWithWindows(ref, store, Page.Bench, true, new Window[]{chestWindow});
                    }
                }
            }
        }
    }
}
