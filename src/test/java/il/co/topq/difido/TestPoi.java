package il.co.topq.difido;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestPoi {
	
	private File template;
	
	@Before
	public void setup() throws IOException {
		template = File.createTempFile("template_test_file", ".xlsx");
		String content = resourceToString("ReportExample.xlsx");
		FileUtils.write(template, content);
	}
	
	@Test
	public void testPoi() throws Exception {
		Workbook workbook = new XSSFWorkbook(new FileInputStream(template));
		Sheet sheet = workbook.getSheet("Failed Tests");
		for (int i = 1; i < 10; i++) {
			Row row = sheet.createRow(i);
			Cell cell = row.createCell(0);
			cell.setCellValue("And the number is " + i);
		}
		FileOutputStream out = new FileOutputStream("Report Output.xlsx");
		workbook.write(out);
		out.close();

	}
	
	@After
	public void teardown() {
		if (template.exists()) {
			template.delete();
		}
	}
	
	private String resourceToString(final String resourceName) throws IOException {
		try (Scanner s = new Scanner(getClass().getClassLoader().getResourceAsStream(resourceName))){
			s.useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		}
}

}
