package task.timer.helper;

import javafx.collections.ObservableList;
import task.timer.model.AbstractEntity;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;


public class ExcelCreator {
	
	
	
	public ExcelCreator(ObservableList<AbstractEntity> dataArray){

	HSSFWorkbook wb = new HSSFWorkbook();
    HSSFSheet sheet = wb.createSheet();
    CellStyle dateStyle = wb.createCellStyle();
    CellStyle roundedStyle = wb.createCellStyle();
    CreationHelper createHelper = wb.getCreationHelper();
    dateStyle.setDataFormat(
        createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
    roundedStyle.setDataFormat(
          createHelper.createDataFormat().getFormat("0.00"));

//CREATE HEADER
    HSSFRow row = sheet.createRow((short)0);
    
    Field[] fields = dataArray.get(0).getClass().getFields();
        
    for (int i = 0; i < fields.length; i++ ){
    	String heading = fields[i].getName();
    	
    }
	}
    
    /*
    def headings = serviceParameters["headings"].split("\\|")
    headings.eachWithIndex { it, index ->
        row.createCell(index).setCellValue(it);
    }
    */
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
}
