package org.ehrbase.fhirbridge.ehr.opt.intensivmedizinischesmonitoringkorpertemperaturcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import java.lang.String;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Archetype;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.LocatableEntity;
import org.ehrbase.client.classgenerator.shareddefinition.NullFlavour;

@Entity
@Archetype("openEHR-EHR-CLUSTER.case_identification.v0")
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2021-03-09T11:54:08.566669+01:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.3.0"
)
public class FallidentifikationCluster implements LocatableEntity {
  /**
   * Path: Bericht/context/Fallidentifikation/Fall-Kennung
   * Description: Der Bezeichner/die Kennung dieses Falls.
   */
  @Path("/items[at0001]/value|value")
  private String fallKennungValue;

  /**
   * Path: Bericht/context/Tree/Fallidentifikation/Fall-Kennung/null_flavour
   */
  @Path("/items[at0001]/null_flavour|defining_code")
  private NullFlavour fallKennungNullFlavourDefiningCode;

  /**
   * Path: Bericht/context/Fallidentifikation/feeder_audit
   */
  @Path("/feeder_audit")
  private FeederAudit feederAudit;

  public void setFallKennungValue(String fallKennungValue) {
     this.fallKennungValue = fallKennungValue;
  }

  public String getFallKennungValue() {
     return this.fallKennungValue ;
  }

  public void setFallKennungNullFlavourDefiningCode(
      NullFlavour fallKennungNullFlavourDefiningCode) {
     this.fallKennungNullFlavourDefiningCode = fallKennungNullFlavourDefiningCode;
  }

  public NullFlavour getFallKennungNullFlavourDefiningCode() {
     return this.fallKennungNullFlavourDefiningCode ;
  }

  public void setFeederAudit(FeederAudit feederAudit) {
     this.feederAudit = feederAudit;
  }

  public FeederAudit getFeederAudit() {
     return this.feederAudit ;
  }
}
