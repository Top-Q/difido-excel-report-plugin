package il.co.topq.difido.controller;

import java.util.List;
import java.util.Map;

import il.co.topq.difido.model.Table;
import il.co.topq.report.business.elastic.ElasticsearchTest;

public interface Controller {

	Table process(Map<Integer,List<ElasticsearchTest>> testsPerExecution);

}
