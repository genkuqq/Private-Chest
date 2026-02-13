package com.nukku.datamanagers;

import com.nukku.components.ChestComponent;
import java.util.UUID;

public interface IDataManager {
    ChestComponent load(UUID userId, int page);
    void save(UUID userId, int page, ChestComponent chestComponent);
}
