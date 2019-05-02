package com.sumdu.hospital.service;

import com.sumdu.hospital.database.DAO;

import java.io.File;

public interface Export {
    void export(DAO dao, File file);
}
