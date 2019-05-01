package com.sumdu.hospital.database;

import java.sql.Connection;

public interface DAO {
    Connection getConnection();

    void closeConnection();
}
