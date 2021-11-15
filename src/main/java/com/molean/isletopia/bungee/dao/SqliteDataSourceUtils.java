package com.molean.isletopia.bungee.dao;

import com.molean.isletopia.bungee.IsletopiaBungee;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class SqliteDataSourceUtils {
    private static SQLiteDataSource dataSource = null;

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            dataSource = new SQLiteDataSource();
            boolean mkdir = IsletopiaBungee.getPlugin().getDataFolder().mkdir();
            dataSource.setUrl("jdbc:sqlite:" + IsletopiaBungee.getPlugin().getDataFolder() + "/storage.db");
        }
            return dataSource.getConnection();

    }
}
