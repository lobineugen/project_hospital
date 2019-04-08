package com.sumdu.hospital.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@EnableAutoConfiguration
public class SQLLiteDAOImpl implements DAO {
    private static final Logger LOGGER = Logger.getLogger(SQLLiteDAOImpl.class);
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.name}")
    private String dataBaseName;

    @Override
    public Connection getConnection() {

        try (Connection connection = DriverManager.getConnection(url + dataBaseName)) {
            if (!databaseExist()) {
                LOGGER.debug("db doesn't exist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean databaseExist() {
        File dbFile = new File(dataBaseName);
        return dbFile.exists();
    }
}
