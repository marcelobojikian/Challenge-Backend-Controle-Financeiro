package br.com.alura.challenge.finance.backend.rest.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Primary
@Component
@Profile("dev")
public class DevServerErrorAttributes extends DefaultErrorAttributes {

	Logger log = LoggerFactory.getLogger(DevServerErrorAttributes.class);

	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
		Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
		
		Throwable throwable = getError(webRequest);
        Throwable cause = throwable.getCause();
		log.info(throwable.getClass().getName());
		log.error("error", throwable);
		
        if (cause != null) {
            Map<Object, Object> causeErrorAttributes = new HashMap<>();
            causeErrorAttributes.put("exception", cause.getClass().getName());
            causeErrorAttributes.put("message", cause.getMessage());
            errorAttributes.put("cause", causeErrorAttributes);
        }
		
		errorAttributes.put("locale", webRequest.getLocale().toString());
		errorAttributes.remove("error");


		return errorAttributes;
	}

}
