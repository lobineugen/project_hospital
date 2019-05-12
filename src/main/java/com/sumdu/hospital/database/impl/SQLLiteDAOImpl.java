package com.sumdu.hospital.database.impl;

import com.sumdu.hospital.database.DAO;
import com.sumdu.hospital.model.Card;
import com.sumdu.hospital.model.Patient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Repository
public class SQLLiteDAOImpl implements DAO {
    private static final Logger LOGGER = Logger.getLogger(SQLLiteDAOImpl.class);
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.name}")
    private String dataBaseName;
    private Connection connection;
    private static final String GET_PATIENT_QUERY = "SELECT p.patientID,\n" +
            "       fullName,\n" +
            "       passportID,\n" +
            "       dateOfBirth,\n" +
            "       addressType,\n" +
            "       address,\n" +
            "       phoneNumber,\n" +
            "       workPlace,\n" +
            "       cardID,\n" +
            "       cardNumber,\n" +
            "       dateIN,\n" +
            "       dateOUT,\n" +
            "       week," +
            "       mainDiagnosis," +
            "       complication," +
            "       pvt," +
            "       concomitant " +
            "FROM sm_patients p\n" +
            "LEFT OUTER JOIN (SELECT * FROM sm_cards GROUP BY patientID HAVING max(cardID)) c\n" +
            "ON p.patientID = c.patientID\n" +
            "WHERE lower(fullName) LIKE lower(?)";

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
            PreparedStatement preparedStatement = connection.prepareStatement(GET_PATIENT_QUERY);
            preparedStatement.setString(1, "%" + name + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int patientID = resultSet.getInt("patientID");
                Patient patient = new Patient(patientID,
                        resultSet.getString("fullName"),
                        resultSet.getString("passportID"));
                patient.setDateOfBirth(resultSet.getDate("dateOfBirth"));
                patient.setAddress(resultSet.getString("address"));
                patient.setAddressType(resultSet.getString("addressType"));
                patient.setPhoneNumber(resultSet.getString("phoneNumber"));
                patient.setWorkPlace(resultSet.getString("workPlace"));
                Card card = new Card(resultSet.getInt("cardID"),
                        resultSet.getString("cardNumber"),
                        resultSet.getDate("dateIn"),
                        resultSet.getDate("dateOut"),
                        resultSet.getString("week"));

                card.setMainDiagnosis(resultSet.getString("mainDiagnosis"));
                card.setComplication(resultSet.getString("complication"));
                card.setPvt(resultSet.getString("pvt"));
                card.setConcomitant(resultSet.getString("concomitant"));

                card.setPatientID(patientID);
                patient.setLastCard(card);

                List<Card> cardList = new ArrayList<>();
                PreparedStatement additionalStatement = connection.prepareStatement("SELECT * FROM sm_cards WHERE patientID = ? AND cardID != ?");
                additionalStatement.setInt(1, patientID);
                additionalStatement.setInt(2, card.getCardID());
                ResultSet additionalResult = additionalStatement.executeQuery();
                while (additionalResult.next()) {
                    Card newCard = new Card(additionalResult.getInt("cardID"),
                            additionalResult.getString("cardNumber"),
                            additionalResult.getDate("dateIn"),
                            additionalResult.getDate("dateOut"),
                            additionalResult.getString("week"));
                    newCard.setMainDiagnosis(additionalResult.getString("mainDiagnosis"));
                    newCard.setComplication(additionalResult.getString("complication"));
                    newCard.setPvt(additionalResult.getString("pvt"));
                    newCard.setConcomitant(additionalResult.getString("concomitant"));
                    cardList.add(newCard);
                }
                cardList.add(card);
                patient.setCardsList(cardList);
                result.add(patient);
            }


        } catch (SQLException e) {
            LOGGER.error("SQLException ", e);
        }
        closeConnection();
        return result;
    }

    @Override
    public Map<String, String> getTableDefinition() {
        Map<String, String> result = new LinkedHashMap<>();
        getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT p.patientID, fullName, passportID, dateOfBirth, addressType,address,phoneNumber, workPlace, cardID,cardNumber, week,dateIN, dateOUT, mainDiagnosis, complication, pvt,concomitant FROM sm_patients p, sm_cards LIMIT 1");
            ResultSet resultSet = preparedStatement.executeQuery();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                String name = resultSet.getMetaData().getColumnName(i);
                String type = resultSet.getMetaData().getColumnTypeName(i);
                result.put(name,
                        type);
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException ", e);
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
            LOGGER.error("SQLException ", e);
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
            LOGGER.error("SQLException ", e);
        }
        closeConnection();
    }

    @Override
    public void updatePatient(Patient patient) {
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
            LOGGER.error("SQLException ", e);
        }
        closeConnection();
    }

    @Override
    public void createCard(Card card) {
        getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO sm_cards (cardID, patientID, cardNumber,dateIN,dateOUT, week) VALUES (?,?,?,?,?, ?)");
            ps.setInt(1, card.getCardID());
            ps.setInt(2, card.getPatientID());
            ps.setString(3, card.getCardNumber());
            ps.setDate(4, card.getDateIn());
            ps.setDate(5, card.getDateOut());
            ps.setString(6, card.getWeek());
            ps.execute();
        } catch (SQLException e) {
            LOGGER.error("SQLException ", e);
        }
        closeConnection();
    }

    @Override
    public void updateCard(Card card) {
        getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE sm_cards SET mainDiagnosis = ? , complication = ? , pvt = ?, concomitant = ? WHERE cardID = ?");
            ps.setString(1, card.getMainDiagnosis());
            ps.setString(2, card.getComplication());
            ps.setString(3, card.getPvt());
            ps.setString(4, card.getConcomitant());
            ps.setInt(5, card.getCardID());
            ps.execute();
        } catch (SQLException e) {
            LOGGER.error("SQLException ", e);
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
            LOGGER.error("SQLException ", e);
        }
        closeConnection();
        return result;
    }
}

