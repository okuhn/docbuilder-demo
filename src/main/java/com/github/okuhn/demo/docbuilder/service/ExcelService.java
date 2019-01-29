package com.github.okuhn.demo.docbuilder.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.github.okuhn.demo.docbuilder.model.DemoDocument;

@Service
public class ExcelService implements DocumentCreationService {

	private static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	@Override
	public DemoDocument createDocument(final String name) {
		final DemoDocument document = new DemoDocument();
		document.setContent(createExcel(name));
		document.setName(name + ".xlsx");
		document.setMimeType(XLSX);
		return document;		
	}
	
	private byte[] createExcel(final String name) {
		try(final XSSFWorkbook wb = new XSSFWorkbook()) {
			
			final XSSFFont font0 = wb.createFont();
			font0.setBold(true);
			font0.setFontHeight(36);
			
			final XSSFCellStyle st0 = wb.createCellStyle();
			st0.setFont(font0);

			final XSSFFont font1 = wb.createFont();
			font1.setBold(true);
			font1.setColor(IndexedColors.RED.getIndex());
			
			final XSSFCellStyle st1 = wb.createCellStyle();
			st1.setFont(font1);
			
			final XSSFFont font2 = wb.createFont();
			font2.setItalic(true);
			font2.setColor(IndexedColors.BLUE.getIndex());

			final XSSFCellStyle st2 = wb.createCellStyle();
			st2.setFillBackgroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
			st2.setFont(font2);

			final XSSFSheet sheet1 = wb.createSheet("Sheet 1");
    		final XSSFSheet sheet2 = wb.createSheet("Sheet 2");
    		
    		final Row row0 = sheet1.createRow(0);
    	    final Cell cell = row0.createCell(0);
    	    cell.setCellValue("Test " + name);
    	    cell.setCellStyle(st0);
    		
    		addCells(sheet1, st1, st2);
    		addCells(sheet2, st2, st1);
    		
    		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
   			wb.write(stream);
   			return stream.toByteArray();
		} catch (IOException e) {
    		throw new RuntimeException("Error creating excel document: " + e.getMessage());
		}
	}

	private void addCells(final XSSFSheet sheet, CellStyle style1, CellStyle style2) {
		final Row row2 = sheet.createRow(2);
		setCell(row2, 0 , "a3", style1);
		setCell(row2, 1 , "b3", style1);
		setCell(row2, 2 , "c3", style1);
		final Row row3 = sheet.createRow(3);
		setCell(row3, 0 , "a4", style2);
		setCell(row3, 1 , "b4", style2);
		setCell(row3, 2 , "c4", style2);
	}

	private void setCell(final Row row, final int index, final String value, CellStyle style) {
	    final Cell cell = row.createCell(index);
	    cell.setCellValue(value);
	    cell.setCellStyle(style);
	}	
}
