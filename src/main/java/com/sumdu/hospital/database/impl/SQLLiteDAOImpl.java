package com.sumdu.hospital.database.impl;

import com.sumdu.hospital.database.DAO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Repository
public class SQLLiteDAOImpl implements DAO {
    private static final Logger LOGGER = Logger.getLogger(SQLLiteDAOImpl.class);
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.name}")
    private String dataBaseName;
    private Connection connection;

    @Override
    public Connection getConnection() {
        LOGGER.debug("Get connection");
        try {
            connection = DriverManager.getConnection(url + dataBaseName);
            if (!connection.isClosed()) {
                LOGGER.info("Connection successful");
            }
            return connection;
        } catch (SQLException e) {
            LOGGER.error("SQLException:", e);
        }
        return null;
    }

    @Override
    public void closeConnection() {
        try {
            connection.close();
            LOGGER.debug("Connection successful closed!");
        } catch (SQLException e) {
            LOGGER.error("Can't close connection: ", e);
        }
    }
}
