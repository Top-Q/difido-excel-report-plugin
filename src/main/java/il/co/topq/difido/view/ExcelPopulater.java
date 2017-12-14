package il.co.topq.difido.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import il.co.topq.difido.model.Table;
import il.co.topq.difido.model.TableCell;
import il.co.topq.difido.model.TableRow;
import il.co.topq.report.Common;
import il.co.topq.report.Configuration;
import il.co.topq.report.Configuration.ConfigProps;

public class ExcelPopulater implements Populater {

	private static final String EXCEL_FILE_NAME = "summary_report.xlsx";
	
	private static final String TEMPLATE_FILE_NAME = "summaryReportTemplate.xlsx";

	public ExcelPopulater() {
		super();
	}

	@Override
	public File populate(List<Table> tables) throws IOException {
		final File templateFile = prepareTemplateFile();
		final File outFile = prepareOutputFile();
		final Workbook workbook = new XSSFWorkbook(new FileInputStream(templateFile));
		for (Table table : tables) {
			populateTable(workbook, table);
		}
		FileOutputStream out = new FileOutputStream(outFile);
		workbook.write(out);
		out.close();
		return outFile;

	}

	private File prepareOutputFile() throws IOException {
		File outFile = new File(Configuration.INSTANCE.readString(ConfigProps.DOC_ROOT_FOLDER), EXCEL_FILE_NAME);
		outFile.delete();
		try {
			if (!outFile.createNewFile()) {
				throw new IOException("Failed to create new output file");
			}
		} catch (IOException e) {
			throw new IOException("Failed to create new output file due to " + e.getMessage());
		}
		return outFile;
	}

	private File prepareTemplateFile() throws IOException {
		final File templateFile = new File(Common.CONFIUGRATION_FOLDER_NAME + File.separator + TEMPLATE_FILE_NAME);
		if (!templateFile.exists()) {
			throw new IOException("Template file " + templateFile.getAbsolutePath() + " was not found");
		}

		return templateFile;
	}

	private void populateTable(final Workbook workbook, final Table table) {
		CellStyle seperatorStyle = createSeperatorStyle(workbook);
		Sheet outSheet = workbook.getSheet(table.getName());
		for (TableRow tableRow : table.getRows()) {
			Row row = outSheet.createRow(tableRow.getRowNum());
			if (tableRow.isSeperator()) {
				row.setRowStyle(seperatorStyle);
			}
			for (TableCell tableCell : tableRow.getCells()) {
				Cell cell = row.createCell(tableCell.getColumn());
				switch (tableCell.getType()) {
				case STRING:
					cell.setCellType(1);
					cell.setCellValue((String) tableCell.getValue());
					break;
				case INT:
					cell.setCellType(0);
					cell.setCellValue((Integer) tableCell.getValue());
					break;
				}
			}
		}

	}

	private CellStyle createSeperatorStyle(Workbook workbook) {
		CellStyle seperatorStyle = workbook.createCellStyle();
		Font seperatorFont = workbook.createFont();
		seperatorFont.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
		seperatorStyle.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		seperatorStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		seperatorStyle.setFont(seperatorFont);
		return seperatorStyle;
	}

}
