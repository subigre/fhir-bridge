package org.ehrbase.fhirbridge.camel.route;

import groovy.util.logging.Slf4j;
import org.ehrbase.fhirbridge.camel.processor.PatientEHRProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PatientEHRRouteBuilder extends AbstractRouteBuilder {

    @Override
    public void configure() {
        rest("/patientEHR")
                .post();

        from("direct:patientEHR")
                .process(PatientEHRProcessor.BEAN_ID);

/*            from("direct:postError")
                    .unmarshal()
                    .json(JsonLibrary.Jackson)
                    .process(exchange -> {
                        log.info("Type of incoming body:{}", exchange.getIn().getBody().getClass().getName());
                        log.info("Incoming body:{}", exchange.getIn().getBody());
                    }).transform().constant("{'httpResponse:200':'OK'}");
        */
    }
}