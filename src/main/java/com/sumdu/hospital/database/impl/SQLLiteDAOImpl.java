package com.sumdu.hospital.database.impl;

import com.sumdu.hospital.database.DAO;
import com.sumdu.hospital.model.Patient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
                return connection;
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException:", e);
        }
        return null;
    }

    @Override
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.error("Can't close connection: ", e);
        }
    }

    @Override
    public List<Patient> getPatientByName(String name) {
        getConnection();
        List<Patient> result = new LinkedList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT patientID,fullName,passportID,dateOfBirth,addressType, address , phoneNumber,workPlace  FROM sm_patients WHERE lower(fullName) LIKE lower(?)");
            preparedStatement.setString(1, "%" + name + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Patient patient = new Patient(resultSet.getInt("patientID"),
                        resultSet.getString("fullName"),
                        resultSet.getString("passportID"));
                patient.setDateOfBirth(resultSet.getDate("dateOfBirth"));
                patient.setAddress(resultSet.getString("address"));
                patient.setAddressType(resultSet.getString("addressType"));
                patient.setPhoneNumber(resultSet.getString("phoneNumber"));
                patient.setWorkPlace(resultSet.getString("workPlace"));
                result.add(patient);
            }
        } catch (SQLException e) {
            LOGGER.debug("SQLException ", e);
        }
        closeConnection();
        return result;
    }

    @Override
    public Map<String, String> getTableDefinition() {
        Map<String, String> result = new HashMap<>();
        getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM sm_patients LIMIT 1");
            ResultSet resultSet = preparedStatement.executeQuery();
            for (int i = 1; i < resultSet.getMetaData().getColumnCount(); i++) {
                result.put(resultSet.getMetaData().getColumnName(i),
                        resultSet.getMetaData().getColumnTypeName(i));
            }
        } catch (SQLException e) {
            LOGGER.debug("SQLException ", e);
        }

        closeConnection();
        return result;
    }

    @Override
    public void deleteByID(int patientID) {
        getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM sm_patients WHERE patientID = ?");
            preparedStatement.setInt(1, patientID);
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.debug("SQLException ", e);
        }
        closeConnection();
    }

    @Override
    public void createPatient(Patient patient) {
        getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO sm_patients VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, patient.getPatientID());
            ps.setString(2, patient.getFullName());
            ps.setString(3, patient.getPassportID());
            ps.setDate(4, patient.getDateOfBirth() != null ? new Date(patient.getDateOfBirth().getTime()) : null);
            ps.setString(5, patient.getAddressType());
            ps.setString(6, patient.getAddress());
            ps.setString(7, patient.getPhoneNumber());
            ps.setString(8, patient.getWorkPlace());
            ps.execute();
        } catch (SQLException e) {
            LOGGER.debug("SQLException ", e);
        }
        closeConnection();
    }

    @Override
    public void updatePatient(Patient patient) {
        LOGGER.debug("update patient");
        getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE sm_patients\n" +
                    "SET fullName = ?, passportID = ?, dateOfBirth = ?, addressType = ?, address = ?, phoneNumber = ?, workPlace = ?\n" +
                    "WHERE patientID = ?");
            ps.setString(1, patient.getFullName());
            ps.setString(2, patient.getPassportID());
            ps.setDate(3, patient.getDateOfBirth() != null ? new Date(patient.getDateOfBirth().getTime()) : null);
            ps.setString(4, patient.getAddressType());
            ps.setString(5, patient.getAddress());
            ps.setString(6, patient.getPhoneNumber());
            ps.setString(7, patient.getWorkPlace());
            ps.setInt(8, patient.getPatientID());
            ps.execute();
        } catch (SQLException e) {
            LOGGER.debug("SQLException ", e);
        }
        closeConnection();
    }

    @Override
    public int getID() {
        getConnection();
        int result = 0;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT id FROM sequence");
            ResultSet resultSet = ps.executeQuery();
            result = resultSet.getInt(1);
            ps = connection.prepareStatement("UPDATE sequence SET id = ?");
            if (result == 999999) {
                result = 1;
                ps.setInt(1, result);
                LOGGER.debug("Sequence updated successfully!");
            } else {
                ps.setInt(1, result + 1);
            }
            ps.execute();
        } catch (SQLException e) {
            LOGGER.debug("SQLException ", e);
        }
        closeConnection();
        return result;
    }
}

