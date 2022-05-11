package org.ehrbase.fhirbridge.camel.processor;

import ca.uhn.fhir.rest.api.MethodOutcome;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.ehrbase.fhirbridge.camel.CamelConstants;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

@Component(PatientEHRProcessor.BEAN_ID)
public class PatientEHRProcessor implements Processor {

    public static final String BEAN_ID = "patientEHRProcessor";

    @Override
    public void process(Exchange exchange) throws Exception {
        MethodOutcome outcome = new MethodOutcome();
        System.out.println(exchange.getMessage());
        exchange.setProperty(CamelConstants.OUTCOME, outcome);
    }

}
