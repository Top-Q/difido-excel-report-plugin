package il.co.topq.difido.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.co.topq.difido.model.Table;
import il.co.topq.difido.model.TableCell;
import il.co.topq.difido.model.TableRow;
import il.co.topq.difido.model.TableCell.ValueType;
import il.co.topq.report.business.elastic.ElasticsearchTest;

public class SharedFailedTestsController implements Controller {

	private static final Logger log = LoggerFactory.getLogger(SharedFailedTestsController.class);

	@Override
	public Table process(Map<Integer, List<ElasticsearchTest>> testsPerExecution) {
		final Table sheet = new Table("Failed Tests");
		int rowCounter = 0;
		addRowSeperator(sheet, ++rowCounter, "Shared failed tests");
		List<ElasticsearchTest> sharedFailedTests = findSharedFailedTests(testsPerExecution);
		for (ElasticsearchTest test : sharedFailedTests) {
			addSharedTestRow(sheet, ++rowCounter, test);
		}
		for (int executionId : testsPerExecution.keySet()) {
			addRowSeperator(sheet, ++rowCounter, "Execution " + executionId);
			for (ElasticsearchTest test : testsPerExecution.get(executionId)) {
				if ("success".equals(test.getStatus())) {
					continue;
				}
				if (sharedFailedTests.stream().filter(sharedTest -> sharedTest.getName().equals(test.getName())
						&& sharedTest.getExecution().equals(test.getExecution())).findFirst().isPresent()) {
					// We already added this test to the shared failed tests
					// section
					continue;
				}
				addTestRow(sheet, ++rowCounter, executionId, test);
				log.debug("Adding to row " + rowCounter + " test " + test.getName());
			}
		}
		return sheet;
	}

	private int addRowSeperator(final Table sheet, int rowCounter, String description) {
		TableRow row = sheet.createRow(rowCounter);
		row.setSeperator(true);
		TableCell cell = row.createCell(0);
		cell.setValue(description);
		return rowCounter;
	}

	private void addSharedTestRow(Table sheet, int rowCounter, ElasticsearchTest test) {
		TableRow row = sheet.createRow(rowCounter);
		TableCell cell = row.createCell(0);
		cell.setType(ValueType.STRING);
		cell.setValue(" ");
		cell = row.createCell(1);
		cell.setValue(test.getExecution());
		cell = row.createCell(2);
		cell.setValue(test.getName());
	}

	private void addTestRow(Table sheet, int rowCounter, Integer executionId, ElasticsearchTest test) {
		TableRow row = sheet.createRow(rowCounter);
		TableCell cell = row.createCell(0);
		cell.setType(ValueType.INT);
		cell.setValue(executionId);
		cell = row.createCell(1);
		cell.setValue(test.getExecution());
		cell = row.createCell(2);
		cell.setValue(test.getName());
	}

	private List<ElasticsearchTest> findSharedFailedTests(Map<Integer, List<ElasticsearchTest>> testsPerExecution) {
		List<List<ComparableTest>> failedTests = new ArrayList<>();
		for (List<ElasticsearchTest> tests : testsPerExecution.values()) {
			// Collect all the failed tests from all the executions
			failedTests.add(tests.stream().filter(test -> !"success".equals(test.getStatus()))
					.map(test -> new ComparableTest(test)).collect(Collectors.toList()));
		}
		List<ComparableTest> sharedFailedTests = new ArrayList<>(failedTests.get(0));
		Set<ComparableTest> testsToRemove = new HashSet<ComparableTest>();
		for (ComparableTest test : sharedFailedTests) {
			for (List<ComparableTest> executionFailedTest : failedTests) {
				if (!executionFailedTest.contains(test)) {
					testsToRemove.add(test);
				}
			}
		}
		sharedFailedTests.removeAll(testsToRemove);
		return sharedFailedTests.stream().map(test -> test.getTest()).collect(Collectors.toList());

	}

	static class ComparableTest {

		private final ElasticsearchTest test;

		ComparableTest(ElasticsearchTest test) {
			this.test = test;
		}

		@Override
		public boolean equals(Object other) {
			if (null == other) {
				return false;
			}
			if (!(other instanceof ComparableTest)) {
				return false;
			}
			return other.hashCode() == hashCode();
		}
		
		@Override
		public int hashCode() {
			int result = 31;
			if (test.getName() != null) {
				result = 31 * result + test.getName().hashCode();
			}
			if (test.getExecution() != null) {
				result = 31 * result + test.getExecution().hashCode();
			}
			return result;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("Execution: ").append(test.getExecution()).append("Name: ").append(test.getName());
			return sb.toString();
		}


		ElasticsearchTest getTest() {
			return test;
		}

	}

}
