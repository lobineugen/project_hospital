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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                InputStream in = getClass().getResourceAsStream("/" + structureScriptName);
                Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                scriptRunner.setSendFullScript(true);
                scriptRunner.runScript(reader);
                dao.closeConnection();
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("UnsupportedEncodingException ", e);
            }
        }
    }

    private boolean checkStructure() {
        int count = 0;
        try {
            InputStream in = getClass().getResourceAsStream("/" + tableListName);
            List<String> tableList = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
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
            LOGGER.error("SQLException", e);
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
        return count != tableCount;
    }


}
