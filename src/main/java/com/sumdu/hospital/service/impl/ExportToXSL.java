package com.sumdu.hospital.service.impl;

import com.sumdu.hospital.database.DAO;
import com.sumdu.hospital.model.Patient;
import com.sumdu.hospital.service.Export;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExportToXSL implements Export {
    private static final Logger LOGGER = Logger.getLogger(ExportToXSL.class);

    @Override
    public void export(DAO dao, File file) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("База данных");
        List<Patient> list = dao.getAllByName("");
        int rownum = 0;
        Cell cell;
        Row row;
        row = sheet.createRow(rownum);

        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("ПІБ");

        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Номер документа");

        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Дата рождения");

        for (Patient patient : list) {
            rownum++;
            row = sheet.createRow(rownum);

            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(patient.getFullName());
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(patient.getId());
            cell = row.createCell(2, CellType.NUMERIC);
            cell.setCellValue(patient.getAge());
        }

        try (FileOutputStream outFile = new FileOutputStream(file)){
            workbook.write(outFile);
        } catch (FileNotFoundException e) {
            LOGGER.debug("File not found exception: ", e);
        } catch (IOException e) {
            LOGGER.debug("IO Exception: ", e);
        }

    }
}
