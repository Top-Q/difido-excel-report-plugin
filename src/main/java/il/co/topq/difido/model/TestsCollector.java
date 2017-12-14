package il.co.topq.difido.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.co.topq.elastic.ESClient;
import il.co.topq.report.Common;
import il.co.topq.report.Configuration;
import il.co.topq.report.Configuration.ConfigProps;
import il.co.topq.report.business.elastic.ElasticsearchTest;
import il.co.topq.report.business.execution.ExecutionMetadata;

public class TestsCollector {
	
	private static final Logger log = LoggerFactory.getLogger(TestsCollector.class);
	
	private final String HOST;
	
	private final int PORT;
	
	private final List<ExecutionMetadata> metaDataList;
	
	public TestsCollector(List<ExecutionMetadata> metaDataList) {
		HOST = Configuration.INSTANCE.readString(ConfigProps.ELASTIC_HOST);
		PORT = Configuration.INSTANCE.readInt(ConfigProps.ELASTIC_HTTP_PORT);
		this.metaDataList = metaDataList;

	}
	
	public Map<Integer,List<ElasticsearchTest>> collect() throws IOException{
		Map<Integer,List<ElasticsearchTest>> testsPerExecution = new HashMap<>();
		log.debug("About to collect tests from Elastic");
		try (ESClient client = new ESClient(HOST, PORT)) {
		    for (ExecutionMetadata metadata : metaDataList) {
		    	List<ElasticsearchTest> tests = client
		    			.index(Common.ELASTIC_INDEX)
		    			.document("test")
		    			.search()
		    			.byTerm("executionId", ""+metadata.getId())
		    			.asClass(ElasticsearchTest.class);
		    	log.debug("Collected " + tests.size() + " tests from Elastic for execution " + metadata.getId());
		    	testsPerExecution.put(metadata.getId(), tests);
		    }
		}
		return testsPerExecution;
		
	
	}
	
}
