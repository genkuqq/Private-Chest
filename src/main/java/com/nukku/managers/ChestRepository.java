package com.nukku.managers;

import com.hypixel.hytale.server.core.util.Config;
import com.nukku.PrivateChest;
import com.nukku.config.ChestConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public final class ChestRepository {

    private static DatabaseManager db;
    PrivateChest privateChest = PrivateChest.get();

    public ChestRepository() {
        db = privateChest.getDatabase();
        Config<ChestConfig> config = privateChest.getConfig();
    }

    // WRITE (insert or update)
    public static void saveChest(UUID uuid, int page, String jsonData) {
        String sql = """
            INSERT INTO player_chest (uuid, page, data)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE data = VALUES(data)
            """;

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, uuid.toString());
            ps.setInt(2, page);
            ps.setString(3, jsonData);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ
    public static String loadChest(UUID uuid, int page) {
        String sql = "SELECT data FROM player_chest WHERE uuid = ? AND page = ?";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, uuid.toString());
            ps.setInt(2, page);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("data");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
