package org.ehrbase.fhirbridge.ehr.converter.specific;

public enum CodeSystem {

    LOINC("http://loinc.org"),

    SNOMED("http://snomed.info/sct"),

    HL7_DATA_ABSENT_REASON("http://terminology.hl7.org/CodeSystem/data-absent-reason"),

    DIMDI_ATC("http://fhir.de/CodeSystem/dimdi/atc");

    private final String url;

    CodeSystem(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

}
