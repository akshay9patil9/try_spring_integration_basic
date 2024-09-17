package akshay.try_spring_integration_basic;

import akshay.try_spring_integration_basic.dto.SOQuestion;
import akshay.try_spring_integration_basic.dto.SOResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.GenericHandler;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.http.config.HttpOutboundGatewayParser;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.HeaderMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@EnableIntegration
@SpringBootApplication
public class TrySpringIntegrationBasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrySpringIntegrationBasicApplication.class, args);
	}
}
