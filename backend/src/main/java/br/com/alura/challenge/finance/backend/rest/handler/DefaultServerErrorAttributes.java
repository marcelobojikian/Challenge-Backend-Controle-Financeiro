package br.com.alura.challenge.finance.backend.rest.handler;

import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component
public class DefaultServerErrorAttributes extends DefaultErrorAttributes {

	Logger log = LoggerFactory.getLogger(DefaultServerErrorAttributes.class);

	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
		Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);

		String message = errorAttributes.get("error").toString();
		String detail = errorAttributes.get("message").toString();

		errorAttributes.clear();

		errorAttributes.put("message", message);
		errorAttributes.put("details", Arrays.asList(detail));
		errorAttributes.put("report", "http://reportApi");

		return errorAttributes;
	}

}
