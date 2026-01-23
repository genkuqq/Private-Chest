package com.nukku;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.PageManager;
import com.hypixel.hytale.server.core.entity.entities.player.windows.Window;
import com.hypixel.hytale.server.core.inventory.container.SimpleItemContainer;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class ChestCommand extends AbstractPlayerCommand {
    public ChestCommand() {
        super("chest", "Open your private Chest");
        this.addAliases("pc");
        this.requirePermission("privatechest.use");
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        CommandSender sender = commandContext.sender();
        SimpleItemContainer simpleItemContainer = new SimpleItemContainer((short) 27);
        ChestWindow chestWindow = new ChestWindow(simpleItemContainer);
        if (sender instanceof Player  player){
            PageManager pageManager = player.getPageManager();
            pageManager.setPageWithWindows(ref, store, Page.Bench, true, new Window[]{chestWindow});
        }


    }
}
