package org.ehrbase.fhirbridge.camel.route;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.ehrbase.fhirbridge.camel.FhirBridgeConstants;
import org.ehrbase.fhirbridge.fhir.bundle.BundleValidator.BloodGasPanelBundleValidator;
import org.ehrbase.fhirbridge.fhir.bundle.BloodGasPanelConverter;
import org.ehrbase.fhirbridge.fhir.bundle.BundleValidator.DiagnosticReportLabBundleValidator;
import org.ehrbase.fhirbridge.fhir.bundle.DiagnosticReportLabConverter;
import org.ehrbase.fhirbridge.fhir.common.Profile;
import org.ehrbase.fhirbridge.fhir.util.FhirUtils;
import org.springframework.stereotype.Component;

@Component
public class BundleRoutes extends RouteBuilder {

    @Override
    public void configure() {
        // @formatter:off
        from("fhir-provide-bundle:consumer?fhirContext=#fhirContext")
            .setHeader(FhirBridgeConstants.PROFILE, method(FhirUtils.class, "getBundleProfile"))
            .choice()
                .when(header(FhirBridgeConstants.PROFILE).isEqualTo(Profile.BLOOD_GAS_PANEL))
                    .to("direct:process-blood-gas-panel-bundle")
                .when(header(FhirBridgeConstants.PROFILE).isEqualTo(Profile.DIAGNOSTIC_REPORT_LAB))
                    .to("direct:process-diagnostic-report-lab-bundle")
                .otherwise()
                    .throwException(new UnprocessableEntityException("Unsupported transaction: provided Bundle should have a resource that " +
                            "uses on of the following profiles: " + Profile.BLOOD_GAS_PANEL.getUri() + ", "  + Profile.DIAGNOSTIC_REPORT_LAB.getUri()));

        from("direct:process-blood-gas-panel-bundle")
            .bean(BloodGasPanelBundleValidator.class)
            .bean(BloodGasPanelConverter.class, "convert")
            .to("direct:process-observation-resource")
            .log(LoggingLevel.DEBUG, "Completed"); // TODO

        from("direct:process-diagnostic-report-lab-bundle")
            .bean(DiagnosticReportLabBundleValidator.class)
            .bean(DiagnosticReportLabConverter.class, "convert")
            .to("direct:process-diagnostic-report-resource")
            .log(LoggingLevel.DEBUG, "Completed"); // TODO
        // @formatter:on
    }
}
