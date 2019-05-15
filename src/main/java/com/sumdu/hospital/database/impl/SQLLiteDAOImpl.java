package com.sumdu.hospital.database.impl;

import com.sumdu.hospital.database.DAO;
import com.sumdu.hospital.model.Card;
import com.sumdu.hospital.model.ExpertConsultation;
import com.sumdu.hospital.model.Patient;
import com.sumdu.hospital.service.Helper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import static com.sumdu.hospital.constants.Constants.EXPERT_LIST;

@Repository
public class SQLLiteDAOImpl implements DAO {
    private static final Logger LOGGER = Logger.getLogger(SQLLiteDAOImpl.class);
    private ApplicationContext context;
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
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(false);
        try {
            connection = DriverManager.getConnection(url + dataBaseName, config.toProperties());
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
            System.out.println("here");
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

                PreparedStatement additionalStatement = connection.prepareStatement("SELECT * FROM sm_cards WHERE patientID = ? ORDER BY cardID");
                additionalStatement.setInt(1, patientID);
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
                    PreparedStatement expertConsultationStatement = connection.prepareStatement("SELECT * FROM sm_expert_consultations WHERE cardID = ?");
                    expertConsultationStatement.setInt(1, newCard.getCardID());
                    ResultSet expertResultSet = expertConsultationStatement.executeQuery();
                    while (expertResultSet.next()) {
                        ExpertConsultation expertConsultation = new ExpertConsultation(expertResultSet.getInt("cons_id"),
                                expertResultSet.getDate("date"),
                                expertResultSet.getString("doctor"),
                                expertResultSet.getString("conclusion"));
                        expertConsultation.setCardID(newCard.getCardID());
                        newCard.addExpertConsultation(expertConsultation);
                    }
                    patient.addCard(newCard);
                }
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
    public void deleteByID(int id, Object object) {
        getConnection();
        try {
            PreparedStatement preparedStatement = null;
            if (object instanceof Patient) {
                preparedStatement = connection.prepareStatement("DELETE FROM sm_patients WHERE patientID = ?");
            } else if (object instanceof ExpertConsultation) {
                preparedStatement = connection.prepareStatement("DELETE FROM sm_expert_consultations WHERE cons_id = ?");
            }
            Objects.requireNonNull(preparedStatement).setInt(1, id);
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
        Helper helper = context.getBean(Helper.class);
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO sm_cards (cardID, patientID, cardNumber,dateIN,dateOUT, week) VALUES (?,?,?,?,?, ?)");
            ps.setInt(1, card.getCardID());
            ps.setInt(2, card.getPatientID());
            ps.setString(3, card.getCardNumber());
            ps.setDate(4, card.getDateIn());
            ps.setDate(5, card.getDateOut());
            ps.setString(6, card.getWeek());
            ps.execute();
            ps = connection.prepareStatement("INSERT INTO sm_expert_consultations (cons_id, cardID, doctor) VALUES (?,?,?)");
            for (String doctor : EXPERT_LIST) {
                ExpertConsultation expertConsultation = new ExpertConsultation(helper.getUniqueID(), null, doctor, null);
                expertConsultation.setCardID(card.getCardID());
                ps.setInt(1, expertConsultation.getConsID());
                ps.setInt(2, expertConsultation.getCardID());
                ps.setString(3, expertConsultation.getDoctor());
                ps.execute();
                card.addExpertConsultation(expertConsultation);
            }
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

    @Override
    public void createExpertConsultation(ExpertConsultation expertConsultation) {
        getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO sm_expert_consultations VALUES (?,?,?,?,?)");
            ps.setInt(1, expertConsultation.getConsID());
            ps.setInt(2, expertConsultation.getCardID());
            ps.setDate(3, expertConsultation.getDate());
            ps.setString(4, expertConsultation.getDoctor());
            ps.setString(5, expertConsultation.getConclusion());
            ps.execute();
        } catch (SQLException e) {
            LOGGER.error("SQLException ", e);
        }
        closeConnection();
    }

    @Override
    public void updateExpertConsultation(ExpertConsultation expertConsultation) {
        getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE sm_expert_consultations SET date = ? , doctor = ? , conclusion = ? WHERE cons_id = ?");
            ps.setDate(1, expertConsultation.getDate());
            ps.setString(2, expertConsultation.getDoctor());
            ps.setString(3, expertConsultation.getConclusion());
            ps.setInt(4, expertConsultation.getConsID());
            ps.execute();
        } catch (SQLException e) {
            LOGGER.error("SQLException ", e);
        }
        closeConnection();
    }

    @Autowired
    public void context(ApplicationContext context) {
        this.context = context;
    }


}

