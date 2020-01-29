package fr.dsia.logex.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import fr.dsia.logex.model.RequestDto;

public class RequestItemProcessor implements ItemProcessor<RequestDto, RequestDto> {

	private static final Logger log = LoggerFactory.getLogger(RequestItemProcessor.class);

	public RequestDto process(final RequestDto request) throws Exception {
		final String date = request.getDate();
		final String info = request.getInfo();

		final RequestDto transformedRequest = new RequestDto();
		transformedRequest.setDate(date);
		transformedRequest.setInfo(info);

		log.info("Converting (" + request + ") into (" + transformedRequest + ")");

		return transformedRequest;
	}

}
