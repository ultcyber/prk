package task.timer.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import task.timer.model.AbstractEntity;
import task.timer.model.MEFactory;
import task.timer.model.Record;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;


// TODO: Auto-generated Javadoc
/**
 * The Class ExcelCreator.
 */
public class ExcelCreator {
	

	private HSSFWorkbook wb;
	private HSSFSheet sheet;
    private CellStyle dateStyle;
    private CellStyle roundedStyle;
    private CreationHelper createHelper;
    private ObservableList<AbstractEntity> dataArray;
    
    private byte[] outputData;
    
	/**
	 * Instantiates a new excel creator.
	 *
	 * @param dataArray the data array
	 */
	public ExcelCreator(ObservableList<AbstractEntity> dataArray){
		this.dataArray = dataArray;
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
    outputData = baos.toByteArray();
	}

private void populateData() {
		int rowNum = 0;
		for (AbstractEntity a : dataArray){
			Record rec = (Record) a;
			HSSFRow dataRow = sheet.createRow(rowNum++);
			dataRow.createCell(0).setCellValue(rec.getId());
			dataRow.createCell(1).setCellValue(rec.getProject().getId());
			HSSFCell date = dataRow.createCell(2);
			
			date.setCellValue(String.valueOf(rec.getDate()));
			date.setCellStyle(dateStyle);
			dataRow.createCell(3).setCellValue(rec.getDescription());
			
			HSSFCell timeStart = dataRow.createCell(4);
			timeStart.setCellValue(String.valueOf(rec.getTimeStart()));
			timeStart.setCellStyle(dateStyle);
			
			HSSFCell timeStop = dataRow.createCell(5);
			timeStop.setCellValue(String.valueOf(rec.getTimeStop()));
			timeStop.setCellStyle(dateStyle);
			
		};
	}

	private void createHeadings() {
		String[] headings = { "ID Pracy", "ID projektu", "Pracownik", "Data rozpoczęcia zadania", "Opis zadania",
				"Godzina rozpoczęcia", "Godzina zakończenia" };
		HSSFRow headingsrow = sheet.createRow((short) 0);
		for (int i = 0; i < 7; i++) {
			headingsrow.createCell(0).setCellValue(headings[i]);
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
	
	/**
     * The main method.
     *
     * @param args the arguments
	 * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
		List<AbstractEntity> testData = new MEFactory().getRecordEntityManager().list();
		
		ObservableList<AbstractEntity> dataRecords = 
				FXCollections.observableArrayList();
		
		for (AbstractEntity r : testData){
			dataRecords.add((Record) r);
		}
		
		byte[] data = new ExcelCreator(dataRecords).getOutputData();
		
		FileOutputStream fos = new FileOutputStream("testFile.xls");
		try {
			fos.write(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fos.close();
		

	}
	
}
