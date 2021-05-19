package org.ehrbase.fhirbridge.ehr.converter.specific.PatientenAufenthalt;

import org.ehrbase.fhirbridge.ehr.converter.generic.EncounterToCompositionConverter;
import org.ehrbase.fhirbridge.ehr.opt.patientenaufenthaltcomposition.PatientenaufenthaltComposition;
import org.ehrbase.fhirbridge.ehr.opt.patientenaufenthaltcomposition.definition.AbteilungsfallCluster;
import org.ehrbase.fhirbridge.ehr.opt.patientenaufenthaltcomposition.definition.FachlicheOrganisationseinheitCluster;
import org.ehrbase.fhirbridge.ehr.opt.patientenaufenthaltcomposition.definition.StandortCluster;
import org.ehrbase.fhirbridge.ehr.opt.patientenaufenthaltcomposition.definition.VersorgungsaufenthaltAdminEntry;
import org.ehrbase.fhirbridge.ehr.opt.patientenaufenthaltcomposition.definition.VersorgungsfallCluster;
import org.ehrbase.fhirbridge.ehr.opt.patientenaufenthaltcomposition.definition.VersorgungstellenkontaktCluster;
import org.ehrbase.fhirbridge.fhir.support.KontaktebeneDefiningCode;
import org.ehrbase.client.classgenerator.shareddefinition.Language;
import static org.ehrbase.fhirbridge.ehr.converter.specific.CodeSystem.KONTAKT_EBENE;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Identifier;
import org.springframework.lang.NonNull;
import com.nedap.archie.rm.generic.PartySelf;

import java.time.OffsetDateTime;
import java.util.ArrayList;


public class PatientenAufenthaltCompositionConverter extends EncounterToCompositionConverter<PatientenaufenthaltComposition> {

    private final String FACH_ABTEILUNGS_SCHLUESSEL_CODE_SYSTEM = "https://www.medizininformatik-initiative.de/fhir/core/modul-fall/CodeSystem/Fachabteilungsschluessel";

    @Override
    public PatientenaufenthaltComposition convertInternal(@NonNull Encounter encounter) {

        PatientenaufenthaltComposition retVal = new PatientenaufenthaltComposition();

        if (encounter.getIdentifier() != null && encounter.getIdentifier().size() > 0) {

            Identifier encounterIdentifier = encounter.getIdentifier().get(0);

            if (encounter.getType() != null && encounter.getType().size() > 0
            && encounter.getType().get(0).getCoding().get(0).getSystem().equals(KONTAKT_EBENE.getUrl())) {

                String typeCode = encounter.getType().get(0).getCoding().get(0).getCode();

                if (typeCode.equals(KontaktebeneDefiningCode.EINRICHTUNGS_KONTAKT.getCode())) {

                    VersorgungsfallCluster versorgungsfallCluster = new VersorgungsfallCluster();
                    versorgungsfallCluster.setZugehoerigerVersorgungsfallKennungValue(encounterIdentifier.getValue());
                    retVal.setVersorgungsfall(versorgungsfallCluster);
                } else if (typeCode.equals(KontaktebeneDefiningCode.ABTEILUNGS_KONTAKT.getCode())) {

                    AbteilungsfallCluster abteilungsfallCluster = new AbteilungsfallCluster();
                    abteilungsfallCluster.setZugehoerigerAbteilungsfallKennungValue(encounterIdentifier.getValue());
                    retVal.setAbteilungsfall(abteilungsfallCluster);
                } else if (typeCode.equals(KontaktebeneDefiningCode.VERSORGUNGS_STELLEN_KONTAKT.getCode())) {

                    VersorgungstellenkontaktCluster versorgungstellenkontaktCluster = new VersorgungstellenkontaktCluster();
                    versorgungstellenkontaktCluster.setZugehoerigerVersorgungsstellenkontaktKennungValue(encounterIdentifier.getValue());
                    retVal.setVersorgungstellenkontakt(versorgungstellenkontaktCluster);
                } else{
                    throw new IllegalStateException("Invalid Code " + typeCode +
                            " or Code System as 'Kontaktebene', valid codes are einrichtungskontakt, abteilungskontakt, versorgungsstellenkontakt.");
                }
            }
        }

        // Mapping for Versorgungsaufenthalt
        VersorgungsaufenthaltAdminEntry versorgungsaufenthaltAdminEntry = new VersorgungsaufenthaltAdminEntry();

        versorgungsaufenthaltAdminEntry.setSubject(new PartySelf());
        versorgungsaufenthaltAdminEntry.setLanguage(Language.DE);

        if(encounter.getLocation() != null
                && encounter.getLocation().size() > 0) {

            Encounter.EncounterLocationComponent location = encounter.getLocation().get(0);

            if (location.getPeriod() != null) {
                OffsetDateTime begin = OffsetDateTime.from(location.getPeriod().getStartElement().getValueAsCalendar().toZonedDateTime());
                versorgungsaufenthaltAdminEntry.setBeginnValue(begin);

                if (location.getPeriod().hasEndElement()) {
                    OffsetDateTime end = OffsetDateTime.from(location.getPeriod().getEndElement().getValueAsCalendar().toZonedDateTime());
                    versorgungsaufenthaltAdminEntry.setEndeValue(end);
                }
            }

            // location physical type / name -> standort
            String locationPhysicalType = location.getPhysicalType().getCoding().get(0).getCode();
            //String locationName = location.getLocationTarget().getName(); // todo: get Location from Reference
            String locationName = location.getPhysicalType().getText();
            StandortCluster standortCluster = new StandortCluster();

            if (locationName != null && !locationName.isEmpty()) {

                switch (locationPhysicalType) {
                    case "si":
                        standortCluster.setCampusValue(locationName);
                        break;
                    case "bu":
                        standortCluster.setGebaeudegruppeValue(locationName);
                        break;
                    case "lvl":
                        standortCluster.setEbeneValue(locationName);
                        break;
                    case "wa":
                        standortCluster.setStationValue(locationName);
                        break;
                    case "ro":
                        standortCluster.setZimmerValue(locationName);
                        break;
                    case "bd":
                        standortCluster.setBettplatzValue(locationName);
                        break;
                    default: // other types aren't needed by EHR Composition
                        throw new IllegalStateException("unexpected location physical type " + locationPhysicalType +
                                " by EHR composition.");
                }
            }

            /* TODO: get Location from Reference
            if (location.getLocationTarget().getDescription() != null
                    && !location.getLocationTarget().getDescription().isEmpty()) {

                standortCluster.setZusaetzlicheBeschreibungValue(location.getLocationTarget().getDescription());
            }*/

            versorgungsaufenthaltAdminEntry.setStandort(standortCluster);

            versorgungsaufenthaltAdminEntry.setKommentarValue(location.getLocation().getDisplay());
        }

        if (versorgungsaufenthaltAdminEntry.getFachlicheOrganisationseinheit() == null) {
            versorgungsaufenthaltAdminEntry.setFachlicheOrganisationseinheit(new ArrayList<>());
        }

        if (encounter.getServiceType() != null
                && encounter.getServiceType().getCoding() != null) {

            for(Coding fachAbteilungsSchluessel : encounter.getServiceType().getCoding()) {

                FachlicheOrganisationseinheitCluster fachlicheOrganisationseinheitCluster = new FachlicheOrganisationseinheitCluster();

                if (fachAbteilungsSchluessel.getSystem().equals(FACH_ABTEILUNGS_SCHLUESSEL_CODE_SYSTEM)
                        && FachAbteilungsSchluesselDefiningCodeMap.getFachAbteilungsSchluesselMap().containsKey(fachAbteilungsSchluessel.getCode())) {

                    fachlicheOrganisationseinheitCluster.setFachabteilungsschluesselDefiningCode(FachAbteilungsSchluesselDefiningCodeMap.getFachAbteilungsSchluesselMap().get(fachAbteilungsSchluessel.getCode()));
                } else {
                    throw new IllegalStateException("Invalid Code " + fachAbteilungsSchluessel.getCode() +
                            " or Code System for 'Fachabteilungsschlüssel'.");
                }

                versorgungsaufenthaltAdminEntry.getFachlicheOrganisationseinheit().add(fachlicheOrganisationseinheitCluster);
            }
        }

        retVal.setVersorgungsaufenthalt(versorgungsaufenthaltAdminEntry);

        return retVal;
    }
}
