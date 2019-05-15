package com.sumdu.hospital.database;

import com.sumdu.hospital.model.Card;
import com.sumdu.hospital.model.ExpertConsultation;
import com.sumdu.hospital.model.Patient;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface DAO {
    Connection getConnection();

    void closeConnection();

    List<Patient> getPatientByName(String name);

    Map<String, String> getTableDefinition();

    void deleteByID(int id, Object object);

    void createPatient(Patient patient);

    void updatePatient(Patient patient);

    void createCard(Card card);

    void updateCard(Card card);

    int getID();

    void createExpertConsultation(ExpertConsultation expertConsultation);

    void updateExpertConsultation(ExpertConsultation expertConsultation);

}
