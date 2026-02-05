package com.nukku.datamanagers;

import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.nukku.PrivateChest;
import com.nukku.components.ChestComponent;
import com.nukku.config.DatabaseConfig;
import com.nukku.utils.ChestSerializer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MysqlDataManager implements IDataManager{
    MysqlDatabase mysqlDatabase;
    DatabaseConfig config = PrivateChest.get().getDatabaseConfig().get();
    MysqlDataManager(){
        mysqlDatabase = new MysqlDatabase(
                "jdbc:mysql://"+config.getMysqlIP()+":"+config.getMysqlPort()+"/"+config.getMysqlTableName()+"?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                config.getMysqlUsername(),
                config.getMysqlPassword()
        );
    }

    @Override
    public ChestComponent load(UUID userId, int page) {
        String data = "";
        int chestSize = PrivateChest.get().getChestConfig().get().getChestSize();
        String sql = "SELECT data FROM player_chest WHERE uuid = ? AND page = ?";

        try (Connection conn = mysqlDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId.toString());
            ps.setInt(2, page);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getString("data");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        ItemStack[] items = ChestSerializer.fromJson(data, chestSize);
        ChestComponent chest = new ChestComponent();
        chest.setChestSize(chestSize);
        chest.setItems(items);
        return chest;
    }

    @Override
    public void save(UUID userId, int page, ChestComponent chestComponent) {
        String itemsJson = ChestSerializer.toJson(chestComponent.getItems());
        String sql = """
            INSERT INTO player_chest (uuid, page, data)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE data = ?
            """;

        try (Connection conn = mysqlDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId.toString());
            ps.setInt(2, page);
            ps.setString(3, itemsJson);
            ps.setString(4, itemsJson);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
