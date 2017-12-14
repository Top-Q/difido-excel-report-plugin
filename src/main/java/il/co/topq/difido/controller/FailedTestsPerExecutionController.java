package il.co.topq.difido.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import il.co.topq.difido.model.Table;
import il.co.topq.difido.model.TableCell;
import il.co.topq.difido.model.TableRow;
import il.co.topq.difido.model.TableCell.ValueType;
import il.co.topq.report.business.elastic.ElasticsearchTest;

public class FailedTestsPerExecutionController implements Controller {

	@Override
	public Table process(Map<Integer, List<ElasticsearchTest>> testsPerExecution) {
		final Map<Integer, List<ElasticsearchTest>> failedTestsPerExecution = ControllerUtils
				.filterOnlyFailedTests(testsPerExecution);
		List<ElasticsearchTest> uniqueTests = new ArrayList<>();
		for (List<ElasticsearchTest> testList : failedTestsPerExecution.values()) {
			uniqueTests.addAll(ControllerUtils.findTestsNotInList(testList, uniqueTests));
		}

		final Table table = new Table("Failed Tests Per Execution");
		int rowNum = 0;
		int colNum = 0;
		TableRow row = table.createRow(rowNum++);
		TableCell cell = row.createCell(colNum++);
		cell.setValue("Suite Name");
		cell = row.createCell(colNum++);
		cell.setValue("Test Name");
		for (int executionId : failedTestsPerExecution.keySet()) {
			cell = row.createCell(colNum++);
			cell.setType(ValueType.INT);
			cell.setValue(executionId);
		}

		for (ElasticsearchTest test : uniqueTests) {
			colNum = 0;
			row = table.createRow(rowNum++);
			cell = row.createCell(colNum++);
			cell.setValue(test.getExecution());
			cell = row.createCell(colNum++);
			cell.setValue(test.getName());
			for (int executionId : failedTestsPerExecution.keySet()) {
				cell = row.createCell(colNum++);
				cell.setType(ValueType.INT);
				if (ControllerUtils.isTestContainsInList(failedTestsPerExecution.get(executionId), test)) {
					cell.setValue(1);
				} else {
					cell.setValue(0);
				}
			}
		}
		return table;
	}

}
