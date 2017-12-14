package il.co.topq.difido.controller;

import org.junit.Assert;
import org.junit.Test;

import il.co.topq.difido.controller.SharedFailedTestsController.ComparableTest;
import il.co.topq.report.business.elastic.ElasticsearchTest;

public class TestComparableTest {
	
	@Test
	public void testEquals() {
		ElasticsearchTest test0 = new ElasticsearchTest();
		test0.setName("test0");
		test0.setStatus("success");
		test0.setExecution("execution0");
		ElasticsearchTest test1 = new ElasticsearchTest();
		test1.setName("test0");
		test1.setStatus("success");
		test1.setExecution("execution0");

		ComparableTest cTest0 = new ComparableTest(test0);
		ComparableTest cTest1 = new ComparableTest(test1);
		
		Assert.assertTrue(cTest0.equals(cTest1));
	}
	
	@Test
	public void testNotEqualsByName() {
		ElasticsearchTest test0 = new ElasticsearchTest();
		test0.setName("test0");
		test0.setStatus("success");
		test0.setExecution("execution0");
		ElasticsearchTest test1 = new ElasticsearchTest();
		test1.setName("test1");
		test1.setStatus("success");
		test1.setExecution("execution0");

		ComparableTest cTest0 = new ComparableTest(test0);
		ComparableTest cTest1 = new ComparableTest(test1);
		
		Assert.assertFalse(cTest0.equals(cTest1));
	}
	
	@Test
	public void testNotEqualsByStatus() {
		ElasticsearchTest test0 = new ElasticsearchTest();
		test0.setName("test0");
		test0.setStatus("success");
		test0.setExecution("execution0");
		ElasticsearchTest test1 = new ElasticsearchTest();
		test1.setName("test0");
		test1.setStatus("error");
		test1.setExecution("execution0");

		ComparableTest cTest0 = new ComparableTest(test0);
		ComparableTest cTest1 = new ComparableTest(test1);
		
		Assert.assertTrue(cTest0.equals(cTest1));
	}
	
	@Test
	public void testNotEqualsByExecution() {
		ElasticsearchTest test0 = new ElasticsearchTest();
		test0.setName("test0");
		test0.setStatus("success");
		test0.setExecution("execution0");
		ElasticsearchTest test1 = new ElasticsearchTest();
		test1.setName("test0");
		test1.setStatus("success");
		test1.setExecution("execution1");

		ComparableTest cTest0 = new ComparableTest(test0);
		ComparableTest cTest1 = new ComparableTest(test1);
		
		Assert.assertFalse(cTest0.equals(cTest1));
	}



	
}
