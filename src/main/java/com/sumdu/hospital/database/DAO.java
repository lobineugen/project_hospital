package com.sumdu.hospital.database;

import com.sumdu.hospital.model.Patient;

import java.sql.Connection;
import java.util.List;

public interface DAO {
    Connection getConnection();

    void closeConnection();

    List<Patient> getAllPatients();
}
