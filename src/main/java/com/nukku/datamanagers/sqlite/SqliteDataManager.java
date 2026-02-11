package com.nukku.datamanagers.sqlite;

import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.nukku.PrivateChest;
import com.nukku.components.ChestComponent;
import com.nukku.config.DatabaseConfig;
import com.nukku.datamanagers.IDataManager;
import com.nukku.utils.ChestSerializer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.UUID;

public class SqliteDataManager implements IDataManager {
    SqliteDatabase sqliteDatabase;
    DatabaseConfig config = PrivateChest.get().getDatabaseConfig().get();
    public SqliteDataManager(){
        try {
            Path dbPath = Paths.get("mods/Nukku_PrivateChest/player_chests.db");
            Files.createDirectories(dbPath.getParent());

            sqliteDatabase = new SqliteDatabase(
                    "jdbc:sqlite:" + dbPath.toString()
            );
            String sql = "CREATE TABLE IF NOT EXISTS player_chests (uuid TEXT, page INTEGER, data TEXT);";
            try (Connection conn = sqliteDatabase.getConnection()){
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize SQLite database", e);
        }
    }

    @Override
    public ChestComponent load(UUID userId, int page) {
        String data = "";
        int chestSize = PrivateChest.get().getChestConfig().get().getChestSize();
        String sql = "SELECT data FROM player_chests WHERE uuid = ? AND page = ?;";

        try (Connection conn = sqliteDatabase.getConnection();
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

        String update = """
            UPDATE player_chests
            SET data = ?
            WHERE uuid = ? AND page = ?;
            """;
        String insert = """
            INSERT INTO player_chests (uuid, page, data)
            SELECT ?, ?, ?
            WHERE NOT EXISTS (
                SELECT 1 FROM player_chests WHERE uuid = ? AND page = ?
            );
            """;

        try (Connection conn = sqliteDatabase.getConnection()){
            int rowCount;
            try (PreparedStatement ps = conn.prepareStatement(update)) {
                ps.setString(1, itemsJson);
                ps.setString(2, userId.toString());
                ps.setInt(3, page);
                rowCount = ps.executeUpdate();
            }
            if (rowCount == 0){
                try (PreparedStatement insertPs = conn.prepareStatement(insert)) {
                    insertPs.setString(1, userId.toString());
                    insertPs.setInt(2, page);
                    insertPs.setString(3, itemsJson);
                    insertPs.setString(4, userId.toString());
                    insertPs.setInt(5, page);
                    insertPs.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
