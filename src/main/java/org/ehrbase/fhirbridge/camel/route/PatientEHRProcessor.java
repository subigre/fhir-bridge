package org.ehrbase.fhirbridge.camel.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

@Component(PatientEHRProcessor.BEAN_ID)
public class PatientEHRProcessor implements Processor, MessageSourceAware {

    public static final String BEAN_ID = "patientEHRProcessor";

    @Override
    public void process(Exchange exchange) throws Exception {

    }

    @Override
    public void setMessageSource(MessageSource messageSource) {

    }
}
