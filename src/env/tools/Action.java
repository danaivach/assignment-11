package tools;

import ch.unisg.ics.interactions.wot.td.clients.TDHttpRequest;

public class Action {

  private final String relatedAffordanceType;
  private final String propertyType;
  private final Boolean propertyValue;
  private final TDHttpRequest request;

  private int applicableOnStateAxis;
  private int applicableOnStateValue;

  public Action(String relatedAffordanceType, String propertyType,
    Boolean propertyValue, TDHttpRequest request) {
      this.relatedAffordanceType = relatedAffordanceType;
      this.propertyType = propertyType;
      this.propertyValue = propertyValue;
      this.request = request;
    }

  public String getRelatedAffordanceType() {
    return this.relatedAffordanceType;
  }

  public String getPropertyType() {
    return this.propertyType;
  }

  public Boolean getPropertyValue() {
    return this.propertyValue;
  }

  public TDHttpRequest getRequest() {
    return this.request;
  }

  public int getApplicableOnStateAxis() {
    return this.applicableOnStateAxis;
  }

  public int getApplicableOnStateValue() {
    return this.applicableOnStateValue;
  }

  public void setApplicableOn(int stateAxis, int stateValue) {
    this.applicableOnStateAxis = stateAxis;
    this.applicableOnStateValue = stateValue;
  }
}
