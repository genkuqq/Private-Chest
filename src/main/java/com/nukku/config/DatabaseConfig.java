package com.nukku.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class DatabaseConfig {
    private String databaseType = "Json";
    private String mysqlIp = "127.0.0.1";
    private String mysqlPort = "3306";
    private String mysqlTableName = "private_chest";
    private String mysqlUsername = "root";
    private String mysqlPassword = "root";
    public static final BuilderCodec<DatabaseConfig> CODEC = BuilderCodec.builder(
                    DatabaseConfig.class,
                    DatabaseConfig::new
            )
            .append(new KeyedCodec<>("Database Type", Codec.STRING),DatabaseConfig::setDatabaseType, DatabaseConfig::getDatabaseType).add()
            .append(new KeyedCodec<>("Mysql IP", Codec.STRING),DatabaseConfig::setMysqlIP,DatabaseConfig::getMysqlIP).add()
            .append(new KeyedCodec<>("Mysql Port", Codec.STRING),DatabaseConfig::setMysqlPort,DatabaseConfig::getMysqlPort).add()
            .append(new KeyedCodec<>("Mysql Table Name", Codec.STRING),DatabaseConfig::setMysqlTableName,DatabaseConfig::getMysqlTableName).add()
            .append(new KeyedCodec<>("Mysql Username", Codec.STRING),DatabaseConfig::setMysqlUsername,DatabaseConfig::getMysqlUsername).add()
            .append(new KeyedCodec<>("Mysql Password", Codec.STRING),DatabaseConfig::setMysqlPassword,DatabaseConfig::getMysqlPassword).add()
            .build();

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setMysqlIP(String mysqlIp) {
        this.mysqlIp = mysqlIp;
    }

    public String getMysqlIP() {
        return mysqlIp;
    }

    public void setMysqlPort(String mysqlPort) {
        this.mysqlPort = mysqlPort;
    }

    public String getMysqlPort() {
        return mysqlPort;
    }

    public void setMysqlTableName(String mysqlTableName) {
        this.mysqlTableName = mysqlTableName;
    }

    public String getMysqlTableName() {
        return mysqlTableName;
    }

    public void setMysqlUsername(String mysqlUsername) {
        this.mysqlUsername = mysqlUsername;
    }

    public String getMysqlUsername() {
        return mysqlUsername;
    }

    public void setMysqlPassword(String mysqlPassword) {
        this.mysqlPassword = mysqlPassword;
    }

    public String getMysqlPassword() {
        return mysqlPassword;
    }
}
