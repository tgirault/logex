package fr.dsia.logex.batch.processor;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dsia.logex.model.CsvLineDto;
import fr.dsia.logex.model.LogLineDto;

public class RequestItemProcessor implements ItemProcessor<LogLineDto, CsvLineDto> {

	private static final Logger log = LoggerFactory.getLogger(RequestItemProcessor.class);

	public CsvLineDto process(final LogLineDto logLine) throws Exception {
		final String responseDateTime = logLine.getDate();
		final String headers = logLine.getHeaders();

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(headers);
//		String timestamp = jsonNode.get("body").get("timestamp").asText();
		String requestDateTime = jsonNode.get("dateTime").asText();
//		String duration = jsonNode.get("responseTime").asText();
//		String status = jsonNode.get("body").get("type").asText();
//		String params = jsonNode.get("body").asText();
//		String uri = jsonNode.get("body").get("body").get("pathInfo").asText();
				
		final CsvLineDto csvLine = new CsvLineDto();
		csvLine.setRequestDateTime(requestDateTime);
		csvLine.setResponseDateTime(responseDateTime);
//		csvLine.setDuration(duration);
//		csvLine.setStatus(status);
//		csvLine.setParams(Arrays.asList(params));
//		csvLine.setUri(uri);

		log.info("Converting (" + logLine + ") into (" + csvLine + ")");

		return csvLine;
	}

}
