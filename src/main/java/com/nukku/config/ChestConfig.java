package com.nukku.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class ChestConfig {

    private Integer chestSize = 27;
    public static final BuilderCodec<ChestConfig> CODEC = BuilderCodec.builder(
        ChestConfig.class,
        ChestConfig::new
    )
        .append(
            new KeyedCodec<>("Chest Size", Codec.INTEGER),
            ChestConfig::setChestSize,
            ChestConfig::getChestSize
        )
        .add()
        .build();

    public ChestConfig chestConfig() {
        return this;
    }

    public void setChestSize(Integer chestSize) {
        this.chestSize = chestSize;
    }

    public int getChestSize() {
        return chestSize;
    }
}
