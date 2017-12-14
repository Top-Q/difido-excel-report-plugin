package il.co.topq.difido.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import il.co.topq.report.business.elastic.ElasticsearchTest;

public class ControllerUtils {

	private ControllerUtils() {
		// Static class
	}

	/**
	 * Return list of all tests that are in <code>firstList</code> and not exist
	 * in <code>secondList</code><br>
	 * The comparison is done only by test and suite name.<br>
	 * 
	 * @param firstList
	 * @param secondList
	 * @return List with only tests that are in the first list
	 */
	public static List<ElasticsearchTest> findTestsNotInList(List<ElasticsearchTest> firstList,
			List<ElasticsearchTest> secondList) {
		return firstList.stream()
				.filter(test -> secondList.stream()
						.filter(otherTest -> otherTest.getName().equals(test.getName())
								&& otherTest.getExecution().equals(test.getExecution()))
						.count() == 0)
				.collect(Collectors.toList());

	}

	/**
	 * Return true if the <code>test</code> exists in the given
	 * <code>tests</code> list
	 * 
	 * @param tests
	 * @param aTest
	 * @return true if and only if the test exists in the list
	 */
	public static boolean isTestContainsInList(List<ElasticsearchTest> tests, ElasticsearchTest aTest) {
		return tests.stream().filter(
				test -> test.getName().equals(aTest.getName()) && test.getExecution().equals(aTest.getExecution()))
				.count() != 0;
	}

	public static Map<Integer, List<ElasticsearchTest>> filterOnlyFailedTests(
			Map<Integer, List<ElasticsearchTest>> testsPerExecution) {
		Map<Integer, List<ElasticsearchTest>> failedTestsPerExecution = new HashMap<>();
		for (int executionId : testsPerExecution.keySet()) {
			failedTestsPerExecution.put(executionId, testsPerExecution.get(executionId).stream()
					.filter(test -> !"success".equals(test.getStatus())).collect(Collectors.toList()));
		}
		return failedTestsPerExecution;
	}

}
