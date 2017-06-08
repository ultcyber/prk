package task.timer.helper;

import javafx.collections.ObservableList;
import task.timer.model.AbstractEntity;
import task.timer.model.Record;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

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
		createRows();
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

private void createRows() {
	String[] headings = {"ID Pracy", "ID projektu", "Pracownik", "Data rozpoczęcia zadania", "Opis zadania", "Godzina rozpoczęcia", "Godzina zakończenia"};
	HSSFRow headingsrow = sheet.createRow((short)0);
	for (int i = 0; i < 7; i++ ){
		headingsrow.createCell(0).setCellValue(headings[i]);
    }
		
	}

//CREATE ROWS FOR EACH ELEMENT IN VALUESMAP
    /*
    dataArray.forEach(element -> {
    HSSFRow dataRow = sheet.createRow(1);
    createXlsCell(dataRow, 0, it.submissionTrackingNumber);
    createXlsCell(dataRow, 1, it.country);
    createXlsCell(dataRow, 2, it.legalEntity);;
       createXlsCell(dataRow, 3, it.campaignStartDate, dateStyle)
    createXlsCell(dataRow, 5, it.campaignType);
    createXlsCell(dataRow, 6, it.dateSubmitted, dateStyle);
    createXlsCell(dataRow, 8, it.changesByCustomer);
    createXlsCell(dataRow, 9, it.referrer);
    createXlsCell(dataRow, 10, it.taskAccessed);
    createXlsCell(dataRow, 11, it.taskSubmitted);
    createXlsCell(dataRow, 12, it.submissionTime, dateStyle);

    HSSFCell sTime = dataRow.createCell(9);
    sTime.setCellValue(it.submissionTime);
    sTime.setCellStyle(dateStyle);


	});

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    wb.write(baos);
    baos.close();
    byte[] bytes = baos.toByteArray();
	}
	*/
	
	
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
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
}
