/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ehrbase.fhirbridge.camel.route;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.camel.builder.RouteBuilder;
import org.ehrbase.fhirbridge.camel.FhirBridgeConstants;
import org.ehrbase.fhirbridge.fhir.bundle.converter.BloodGasPanelConverter;
import org.ehrbase.fhirbridge.fhir.bundle.converter.DiagnosticReportLabConverter;
import org.ehrbase.fhirbridge.fhir.bundle.validator.BloodGasPanelBundleValidator;
import org.ehrbase.fhirbridge.fhir.bundle.validator.DiagnosticReportLabBundleValidator;
import org.ehrbase.fhirbridge.fhir.common.Profile;
import org.ehrbase.fhirbridge.fhir.support.Bundles;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link RouteBuilder} that provides route definitions for transactions
 * linked to {@link org.hl7.fhir.r4.model.Bundle Bundle} resource.
 *
 * @since 1.0.0
 */
@Component
public class BundleRoutes extends AbstractRouteBuilder {

    @Override
    public void configure() throws Exception {
        // @formatter:off
        super.configure();

        // 'Provide Bundle' route definition
        from("bundle-provide:consumer?fhirContext=#fhirContext")
            .setHeader(FhirBridgeConstants.PROFILE, method(Bundles.class, "getTransactionProfile"))
            .choice()
                .when(header(FhirBridgeConstants.PROFILE).isEqualTo(Profile.BLOOD_GAS_PANEL))
                    .to("direct:process-blood-gas-panel-bundle")
                .when(header(FhirBridgeConstants.PROFILE).isEqualTo(Profile.DIAGNOSTIC_REPORT_LAB))
                    .to("direct:process-diagnostic-report-lab-bundle")
                .otherwise()
                    .throwException(new UnprocessableEntityException("Unsupported transaction: provided Bundle should have a resource that " +
                        "uses on of the following profiles: " + Profile.BLOOD_GAS_PANEL.getUri() + ", " + Profile.DIAGNOSTIC_REPORT_LAB.getUri()));

        // Internal routes definition
        from("direct:process-blood-gas-panel-bundle")
            .bean(BloodGasPanelBundleValidator.class)
            .bean(BloodGasPanelConverter.class, "convert")
            .to("direct:process-observation")
            .process("bundleResponseProcessor");

        from("direct:process-diagnostic-report-lab-bundle")
            .bean(DiagnosticReportLabBundleValidator.class)
            .bean(DiagnosticReportLabConverter.class, "convert")
            .to("direct:process-diagnostic-report")
            .process("bundleResponseProcessor");

        // @formatter:on
    }
}
