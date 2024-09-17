package akshay.try_spring_integration_basic.Flows;

import akshay.try_spring_integration_basic.AppConfig;
import akshay.try_spring_integration_basic.dto.SOResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.function.Function;

@EnableIntegration
@Component
@RequiredArgsConstructor
public class Integrations {

    private final AppConfig config;

    @Bean
    public IntegrationFlow inbound() {
        return IntegrationFlow.from(Http.inboundGateway("/so/questions")
                        .requestMapping(m -> m.methods(HttpMethod.GET).params("tag"))
                        .requestPayloadType(String.class))
                .channel("makeRequest")
                .get();
    }

    @Bean
    public IntegrationFlow outbound() {
        String url = "https://api.stackexchange.com/2.3/questions?key="+config.getSokey()+"&site=stackoverflow&order=desc&sort=activity&filter=default&tagged={tagged}";
        return IntegrationFlow.from("makeRequest")
                .handle(Http.outboundGateway(url)
                        .httpMethod(HttpMethod.GET)
                        .uriVariable("tagged", f -> ((MultiValueMap<String, String>)f.getPayload()).get("tag"))
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
