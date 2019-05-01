package com.sumdu.hospital.database.impl;

import com.sumdu.hospital.database.DAO;
import com.sumdu.hospital.database.InitStructure;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import java.util.Objects;

@Repository
public class InitStructureImpl implements InitStructure {
    private static final Logger LOGGER = Logger.getLogger(SQLLiteDAOImpl.class);
    @Value("${structure.script.name}")
    private String structureScriptName;
    private DAO dao;

    @Autowired
    public InitStructureImpl(DAO dao) {
        this.dao = dao;
    }

    public void initStructure() {
        ScriptRunner scriptRunner = new ScriptRunner(dao.getConnection());
        try {
            URL url = this.getClass().getClassLoader().getResource(structureScriptName);
            Reader reader = new BufferedReader(new FileReader(Objects.requireNonNull(url).getFile()));
            scriptRunner.setSendFullScript(true);
            scriptRunner.runScript(reader);
            dao.closeConnection();
        } catch (FileNotFoundException e) {
            LOGGER.error("FileNotFoundException", e);
        }
    }
}
