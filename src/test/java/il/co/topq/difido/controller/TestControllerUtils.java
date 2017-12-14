package il.co.topq.difido.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import il.co.topq.difido.controller.ControllerUtils;
import il.co.topq.report.business.elastic.ElasticsearchTest;

public class TestControllerUtils {

	@Test
	public void testFindTestsInList() {
		ElasticsearchTest test0 = new ElasticsearchTest();
		test0.setExecutionId(1);
		test0.setExecution("suite0");
		test0.setName("test0");
		test0.setDuration(1_212l);
		test0.setMachine("mymachine");

		ElasticsearchTest test1 = new ElasticsearchTest();
		test1.setExecutionId(1);
		test1.setExecution("suite1");
		test1.setStatus("failure");
		test1.setName("test1");
		test1.setDuration(1_212l);
		test1.setMachine("mymachine1");

		List<ElasticsearchTest> tests0 = new ArrayList<>();
		tests0.add(test0);
		tests0.add(test1);

		ElasticsearchTest test3 = new ElasticsearchTest();
		test3.setExecutionId(1);
		test3.setExecution("suite1");
		test3.setStatus("success");
		test3.setName("test1");
		test3.setDuration(1_212l);
		test3.setMachine("mymachine2");

		ElasticsearchTest test4 = new ElasticsearchTest();
		test4.setExecutionId(1);
		test4.setExecution("suite2");
		test4.setName("test2");
		test4.setDuration(1_22l);
		test4.setMachine("mymachine2");

		List<ElasticsearchTest> tests1 = new ArrayList<>();
		tests1.add(test3);
		tests1.add(test4);
		
		Assert.assertEquals(1,ControllerUtils.findTestsNotInList(tests0, tests1).size());

	}
	
	@Test
	public void testCombineList() {
		ElasticsearchTest test0 = new ElasticsearchTest();
		test0.setExecutionId(1);
		test0.setExecution("suite0");
		test0.setName("test0");
		test0.setDuration(1_212l);
		test0.setMachine("mymachine");

		ElasticsearchTest test1 = new ElasticsearchTest();
		test1.setExecutionId(1);
		test1.setExecution("suite1");
		test1.setStatus("failure");
		test1.setName("test1");
		test1.setDuration(1_212l);
		test1.setMachine("mymachine1");

		List<ElasticsearchTest> tests0 = new ArrayList<>();
		tests0.add(test0);
		tests0.add(test1);

		ElasticsearchTest test3 = new ElasticsearchTest();
		test3.setExecutionId(1);
		test3.setExecution("suite1");
		test3.setStatus("success");
		test3.setName("test1");
		test3.setDuration(1_212l);
		test3.setMachine("mymachine2");

		ElasticsearchTest test4 = new ElasticsearchTest();
		test4.setExecutionId(1);
		test4.setExecution("suite2");
		test4.setName("test2");
		test4.setDuration(1_22l);
		test4.setMachine("mymachine2");

		List<ElasticsearchTest> tests1 = new ArrayList<>();
		tests1.add(test3);
		tests1.add(test4);

		final List<ElasticsearchTest> uniqueTests = new ArrayList<ElasticsearchTest>(tests0);
		
		uniqueTests.addAll(tests1
				.stream()
				.filter(test -> uniqueTests
									.stream()
									.filter(otherTest -> otherTest.getName().equals(test.getName())).count() == 0)
				.collect(Collectors.toList()));
		Assert.assertEquals(3, uniqueTests.size());
		System.out.println(uniqueTests);

	}

	@Test
	public void testFindInList() {
		ElasticsearchTest test0 = new ElasticsearchTest();
		test0.setExecutionId(1);
		test0.setExecution("suite0");
		test0.setName("test0");
		test0.setDuration(1_212l);
		test0.setMachine("mymachine");

		ElasticsearchTest test1 = new ElasticsearchTest();
		test1.setExecutionId(1);
		test1.setExecution("suite0");
		test1.setName("test0");
		test1.setDuration(1_2122);
		test1.setMachine("mymachine1");

		List<ElasticsearchTest> tests = new ArrayList<ElasticsearchTest>();
		tests.add(test0);
		tests.add(test1);

		boolean find = tests.stream()
				.filter(test -> (test.getName().equals("test0") && test.getExecution().equals("suite0"))).findFirst()
				.isPresent();
		Assert.assertTrue(find);

	}

}
