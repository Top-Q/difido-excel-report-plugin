package il.co.topq.difido;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.co.topq.difido.controller.Controller;
import il.co.topq.difido.controller.FailedTestsPerExecutionController;
import il.co.topq.difido.controller.SharedFailedTestsController;
import il.co.topq.difido.model.Table;
import il.co.topq.difido.model.TestsCollector;
import il.co.topq.difido.view.ExcelPopulater;
import il.co.topq.difido.view.Populater;
import il.co.topq.report.Configuration;
import il.co.topq.report.Configuration.ConfigProps;
import il.co.topq.report.business.elastic.ElasticsearchTest;
import il.co.topq.report.business.execution.ExecutionMetadata;
import il.co.topq.report.plugins.InteractivePlugin;

public class ReportsSummaryPlugin implements InteractivePlugin {

	private static final Logger log = LoggerFactory.getLogger(ReportsSummaryPlugin.class);

	private boolean enabled = true;

	private static int failedCount;

	public ReportsSummaryPlugin() {
		enabled = Configuration.INSTANCE.readBoolean(ConfigProps.ELASTIC_ENABLED);
	}

	@Override
	public String getName() {
		return "Ericsson Reports Summary plugin";
	}

	private static class Message {
		private static String disabledMessage() {
			return "<h3>Ericsson Reports Summary Plugin</h3><p>Plugin is disabled.</p><p>For more details please refer to the server log file.</p>";
		}

		private static String errorMessage(String reason) {
			return String.format(
					"<h3>Ericsson Reports Summary Plugin</h3><p>Error while running plugin.</p><p>%s</p><p>For more details please refer to the server log file.</p>",
					reason);
		}

		private static String successMessage(String fileName) {
			return "<h3>Ericsson Reports Summary Plugin</h3><p>Finished processing reports comparison</p><p>Excel report can be downloaded from this <a href=\""
					+ fileName + "\">link</a><p><br>";
		}

	}

	@Override
	public String executeInteractively(List<ExecutionMetadata> metaDataList, String params) {
		if (!enabled) {
			return Message.disabledMessage();
		}
		Map<Integer, List<ElasticsearchTest>> testsPerExecution = null;
		TestsCollector testsCollector = new TestsCollector(metaDataList);
		try {
			testsPerExecution = testsCollector.collect();
		} catch (IOException e) {
			log.error("Failed collecting tests from Elastic", e);
			failedCount++;
			return Message.errorMessage(e.getMessage());
		}
		final List<Controller> controllers = buildControllers();
		final List<Table> tables = new ArrayList<>();
		try {
			for (Controller controller : controllers) {
				tables.add(controller.process(testsPerExecution));
			}
		} catch (Exception e) {
			log.error("Failed processing document", e);
			failedCount++;
			return Message.errorMessage(e.getMessage());
		}
		final Populater populater = new ExcelPopulater();
		File outFile = null;
		try {
			outFile = populater.populate(tables);
		} catch (IOException e) {
			log.error("Failed populating tables in excel file ", e);
			failedCount++;
			return Message.errorMessage(e.getMessage());
		}

		return Message.successMessage(outFile.getName());
	}

	private List<Controller> buildControllers() {
		List<Controller> controllers = new ArrayList<>();
		controllers.add(new SharedFailedTestsController());
		controllers.add(new FailedTestsPerExecutionController());
		return controllers;
	}

	@Override
	public void execute(List<ExecutionMetadata> metaDataList, String params) {
		// unused
	}

}
