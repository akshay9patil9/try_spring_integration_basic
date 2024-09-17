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

	@Bean
	public IntegrationFlow inbound() {
		return IntegrationFlow.from(Http.inboundGateway("/so/questions")
						.requestMapping(m -> m.methods(HttpMethod.GET).params("tag"))
						.requestPayloadType(String.class))
				.channel("makeRequest")
				.get();
	}

	@Bean
	@ServiceActivator(inputChannel = "handleRequest", outputChannel = "makeRequest")
	public Function<Message<LinkedMultiValueMap>, LinkedMultiValueMap<String, String>> handler() {
		return new Function<>() {
			/**
			 * Applies this function to the given argument.
			 *
			 * @param message the function argument
			 * @return the function result
			 */
			@Override
			public  LinkedMultiValueMap<String, String> apply(Message<LinkedMultiValueMap> message) {
				System.out.println("myHandler: " + message.getPayload());
				System.out.println("myHandler: " + message.getHeaders());
				return message.getPayload();
			}

		};

	}

	@Bean
	public IntegrationFlow outbound() {
		String key = "";
		String url = "https://api.stackexchange.com/2.3/questions?key="+key+"&site=stackoverflow&order=desc&sort=activity&filter=default&tagged=";
		return IntegrationFlow.from("makeRequest")
				.handle((payload, header) -> Http.outboundGateway(url+((LinkedMultiValueMap<String, String>)payload).getOrDefault("tag", List.of("scala")).getFirst())
						.httpMethod(HttpMethod.GET)
						.expectedResponseType(SOResponse.class))
				.log()
				.channel("handleResponse")
				.get();
	}

	@Bean
	@ServiceActivator(inputChannel = "handleResponse")
	public Function<Message<SOResponse>, SOResponse> responseHandler() {
		return new Function<Message<SOResponse>, SOResponse>() {
			/**
			 * Applies this function to the given argument.
			 *
			 * @param message the function argument
			 * @return the function result
			 */
			@Override
			public SOResponse apply(Message<SOResponse> message) {
				System.out.println("myHandler2: " + message);
				System.out.println("myHandler2: " + message.getHeaders());
				return message.getPayload();
			}

		};

	}





}
