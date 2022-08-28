package br.com.alura.challenge.finance.test.integration.profile;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.test.context.ContextConfiguration;

import br.com.alura.challenge.finance.backend.ControleFinanceiroApplication;
import br.com.alura.challenge.finance.backend.rest.handler.DevServerErrorAttributes;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { ControleFinanceiroApplication.class })
public class DefaultProfileTest {
	
	@Autowired
	DefaultErrorAttributes errorAttributes;

    @Test
    public void testSpringProfiles() {
    	assertThat(errorAttributes).isInstanceOf(DevServerErrorAttributes.class);
    }

}
