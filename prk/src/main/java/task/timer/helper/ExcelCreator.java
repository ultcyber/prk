package task.timer.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;

import javafx.collections.ObservableList;
import task.timer.model.AbstractEntity;
import task.timer.model.Record;


/**
 * Creates an Excel spreadsheet given an ObservableList of Records.
 * @author Mateusz Trybulec 
 */
public class ExcelCreator {
	

	private HSSFWorkbook wb;
	private HSSFSheet sheet;
    private CellStyle dateStyle;
    private CellStyle roundedStyle;
    private CreationHelper createHelper;
    private ObservableList<Record> dataArray;
    
    private byte[] outputData;
    
	/**
	 * Instantiates a new excel creator.
	 *
	 * @param dataRecords the data array
	 */
	public ExcelCreator(ObservableList<Record> dataRecords){
		this.dataArray = dataRecords;
		instantiateWorkbook();
		createHeadings();
		populateData();
		
		try {
			generateDocument();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

private void generateDocument() throws IOException {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    wb.write(baos);
    baos.close();
    wb.close();
    outputData = baos.toByteArray();
	}

private void populateData() {
		int rowNum = 0;
		for (AbstractEntity a : dataArray){
			Record rec = (Record) a;
			HSSFRow dataRow = sheet.createRow(++rowNum);
			dataRow.createCell(0).setCellValue(rec.getId());
			dataRow.createCell(1).setCellValue(rec.getProject().getName());
			dataRow.createCell(2).setCellValue(rec.getUser().getFirstName() + " " + rec.getUser().getLastName());
			
			HSSFCell date = dataRow.createCell(3);
			date.setCellValue(String.valueOf(rec.getDate()));
			date.setCellStyle(dateStyle);
			dataRow.createCell(4).setCellValue(rec.getDescription());
			
			HSSFCell timeStart = dataRow.createCell(5);
			timeStart.setCellValue(String.valueOf(rec.getTimeStart().toString()));
			timeStart.setCellStyle(dateStyle);
			
			HSSFCell timeStop = dataRow.createCell(6);
			timeStop.setCellValue(rec.getTimeStop() != null ? String.valueOf(rec.getTimeStop()) : "niezakończony");
			timeStop.setCellStyle(dateStyle);
			
		};
	}

	private void createHeadings() {
		String[] headings = { "ID pracy", "Projekt", "Pracownik", "Data rozpoczęcia zadania", "Opis zadania",
				"Godzina rozpoczęcia", "Godzina zakończenia" };
		HSSFRow headingsrow = sheet.createRow(0);
		for (int i = 0; i < 7; i++) {
			headingsrow.createCell(i).setCellValue(headings[i]);
		}

	}
	
	private void instantiateWorkbook(){
		wb = new HSSFWorkbook();
	    sheet = wb.createSheet();
	    dateStyle = wb.createCellStyle();
	    roundedStyle = wb.createCellStyle();
	    createHelper = wb.getCreationHelper();
	    dateStyle.setDataFormat(
	        createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
	    roundedStyle.setDataFormat(
	          createHelper.createDataFormat().getFormat("0.00"));
	}
	

	/**
	 * @return the outputData
	 */
	public byte[] getOutputData() {
		return outputData;
	}
	
		

	}
	
