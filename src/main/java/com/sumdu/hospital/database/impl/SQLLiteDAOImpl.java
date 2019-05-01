package com.sumdu.hospital.database.impl;

import com.sumdu.hospital.database.DAO;
import com.sumdu.hospital.model.Patient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

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

    @Override
    public List<Patient> getAllPatients() {
        getConnection();
        List<Patient> result = new LinkedList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, full_name, age, address, diagnosis_main FROM sm_patients");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(new Patient(resultSet.getString("id"),
                        resultSet.getString("full_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("address"),
                        resultSet.getString("diagnosis_main")));
            }
        } catch (SQLException e) {
            LOGGER.debug("SQLException ", e);
        }
        closeConnection();
        return result;
    }
}
