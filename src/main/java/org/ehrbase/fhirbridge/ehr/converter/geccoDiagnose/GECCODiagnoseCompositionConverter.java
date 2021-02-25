package org.ehrbase.fhirbridge.ehr.converter.geccoDiagnose;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.generic.PartySelf;
import org.ehrbase.client.classgenerator.shareddefinition.Category;
import org.ehrbase.client.classgenerator.shareddefinition.Language;
import org.ehrbase.client.classgenerator.shareddefinition.Setting;
import org.ehrbase.client.classgenerator.shareddefinition.Territory;
import org.ehrbase.fhirbridge.camel.component.ehr.composition.CompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.BloodPressureCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.CommonData;
import org.ehrbase.fhirbridge.ehr.opt.geccodiagnosecomposition.GECCODiagnoseComposition;
import org.ehrbase.fhirbridge.ehr.opt.geccodiagnosecomposition.definition.AusgeschlosseneDiagnoseEvaluation;
import org.ehrbase.fhirbridge.ehr.opt.geccodiagnosecomposition.definition.AussageUeberDenAusschlussDefiningCode;
import org.ehrbase.fhirbridge.ehr.opt.geccodiagnosecomposition.definition.AussageUeberDieFehlendeInformationDefiningCode;
import org.ehrbase.fhirbridge.ehr.opt.geccodiagnosecomposition.definition.UnbekannteDiagnoseEvaluation;
import org.ehrbase.fhirbridge.ehr.opt.geccodiagnosecomposition.definition.VorliegendeDiagnoseEvaluation;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

public class GECCODiagnoseCompositionConverter implements CompositionConverter<GECCODiagnoseComposition, Condition> {

    private Optional<VorliegendeDiagnoseEvaluation> vorliegendeDiagnose = Optional.empty();

    private final String VERIFICATION_STATUS_PRESENT_CODE = "410605003";
    private final String VERIFICATION_STATUS_ABSENT_CODE = "410594000";
    private final String SNOMED_SYSTEM = "http://snomed.info/sct";

    @Override
    public Condition fromComposition(GECCODiagnoseComposition composition) {
        // TODO: Implement
        return null;
    }

    @Override
    public GECCODiagnoseComposition toComposition(Condition condition) {
        GECCODiagnoseComposition composition = new GECCODiagnoseComposition();

        // set feeder audit
        FeederAudit fa = CommonData.constructFeederAudit(condition);
        composition.setFeederAudit(fa);

        vorliegendeDiagnose = new VorliegendeDiagnoseConverter().map(condition);

        if (condition.getVerificationStatus().isEmpty()) {
            composition.setUnbekannteDiagnose(this.toUnbekannteDiagnose(condition));
        } else {
            Coding verficiationStatus = condition.getVerificationStatus().getCoding().get(
                    condition.getVerificationStatus().getCoding().size() - 1); // snomed code is the last element

            if (verficiationStatus.getSystem().equals(SNOMED_SYSTEM) &&
                    verficiationStatus.getCode().equals(VERIFICATION_STATUS_PRESENT_CODE)) {
                vorliegendeDiagnose.ifPresent(composition::setVorliegendeDiagnose);
            } else if (verficiationStatus.getSystem().equals(SNOMED_SYSTEM) &&
                    verficiationStatus.getCode().equals(VERIFICATION_STATUS_ABSENT_CODE)) {
                composition.setAusgeschlosseneDiagnose(this.toAusgeschlosseneDiagnose(condition));
            } else {
                throw new UnprocessableEntityException("Cant identify the verification status");
            }
        }

        Coding categoryCoding = condition.getCategory().get(0).getCoding().get(0);

        if (categoryCoding.getSystem().equals(SNOMED_SYSTEM) && GeccoDiagnoseCodeDefiningCodeMaps.getKategorieMap().containsKey(categoryCoding.getCode())) {
            composition.setKategorieDefiningCode(GeccoDiagnoseCodeDefiningCodeMaps.getKategorieMap().get(categoryCoding.getCode()));
        } else {
            throw new UnprocessableEntityException("Category not present");
        }


        composition.setStartTimeValue(condition.getRecordedDateElement().getValueAsCalendar().toZonedDateTime());


        // ======================================================================================
        // Required fields by API
        composition.setLanguage(Language.DE); // FIXME: we need to grab the language from the template
        composition.setLocation("test"); // FIXME: Location abfangen?
        composition.setSettingDefiningCode(Setting.SECONDARY_MEDICAL_CARE);
        composition.setTerritory(Territory.DE);
        composition.setCategoryDefiningCode(Category.EVENT);

        composition.setComposer(new PartySelf());

        return composition;
    }


    private UnbekannteDiagnoseEvaluation toUnbekannteDiagnose(Condition condition) {

        UnbekannteDiagnoseEvaluation unbekannteDiagnose = new UnbekannteDiagnoseEvaluation();

        unbekannteDiagnose.setAussageUeberDieFehlendeInformationDefiningCode(AussageUeberDieFehlendeInformationDefiningCode.UNKNOWN_QUALIFIER_VALUE);

        // Map problem
        Coding problem = condition.getCode().getCoding().get(0);
        if (problem.getSystem().equals(SNOMED_SYSTEM) &&
                GeccoDiagnoseCodeDefiningCodeMaps.getProblemDiagnoseMap().containsKey(problem.getCode())) {
            unbekannteDiagnose.setUnbekannteDiagnoseDefiningCode(GeccoDiagnoseCodeDefiningCodeMaps.getProblemDiagnoseMap().get(problem.getCode()));
        }

        unbekannteDiagnose.setSubject(new PartySelf());
        unbekannteDiagnose.setLanguage(Language.DE); // FIXME: we need to grab the language from the template

        return unbekannteDiagnose;
    }


    private AusgeschlosseneDiagnoseEvaluation toAusgeschlosseneDiagnose(Condition condition) {

        AusgeschlosseneDiagnoseEvaluation ausgeschlosseneDiagnose = new AusgeschlosseneDiagnoseEvaluation();

        ausgeschlosseneDiagnose.setAussageUeberDenAusschlussDefiningCode(AussageUeberDenAusschlussDefiningCode.KNOWN_ABSENT_QUALIFIER_VALUE);

        // Map problem
        Coding problem = condition.getCode().getCoding().get(0);
        if (problem.getSystem().equals(SNOMED_SYSTEM) &&
                GeccoDiagnoseCodeDefiningCodeMaps.getProblemDiagnoseMap().containsKey(problem.getCode())) {
            ausgeschlosseneDiagnose.setProblemDiagnoseDefiningCode( GeccoDiagnoseCodeDefiningCodeMaps.getProblemDiagnoseMap().get(problem.getCode()));
        }

        ausgeschlosseneDiagnose.setSubject(new PartySelf());
        ausgeschlosseneDiagnose.setLanguage(Language.DE); // FIXME: we need to grab the language from the template

        return ausgeschlosseneDiagnose;
    }
}
