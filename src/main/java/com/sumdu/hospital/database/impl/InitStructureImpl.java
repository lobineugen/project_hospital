package com.sumdu.hospital.database.impl;

import com.sumdu.hospital.database.DAO;
import com.sumdu.hospital.database.InitStructure;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.sqlite.util.StringUtils;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.*;

@Repository
public class InitStructureImpl implements InitStructure {
    private static final Logger LOGGER = Logger.getLogger(SQLLiteDAOImpl.class);
    @Value("${structure.script.name}")
    private String structureScriptName;
    @Value("${table.count}")
    private int tableCount;
    @Value("${table.list.name}")
    private String tableListName;
    private DAO dao;


    @Autowired
    public InitStructureImpl(DAO dao) {
        this.dao = dao;
    }

    public void initStructure() {
        if (checkStructure()) {
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

    private boolean checkStructure() {
        int count = 0;
        try {
            List<String> tableList = new ArrayList<>();
            URL url = this.getClass().getClassLoader().getResource(tableListName);
            BufferedReader reader = new BufferedReader(new FileReader(Objects.requireNonNull(url).getFile()));
            String line;
            while ((line = reader.readLine()) != null) {
                LOGGER.debug("Table name: " + line);
                tableList.add("'" + line + "'");
            }
            String checkQuery = "SELECT count(*) AS count FROM sqlite_master WHERE tbl_name IN (" + StringUtils.join(tableList, ",") + ") AND type = 'table'";
            PreparedStatement statement = dao.getConnection().prepareStatement(checkQuery);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            LOGGER.debug("Table count: " + count);
            dao.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQLException", e);
        } catch (FileNotFoundException e) {
            LOGGER.error("FileNotFoundException", e);
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
        return count != tableCount;
    }


}
