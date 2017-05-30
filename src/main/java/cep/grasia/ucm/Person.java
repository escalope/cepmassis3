package cep.grasia.ucm;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Person {
    String entityId;
    String changeType;
    float timestamp;

    @JsonProperty(value = "components")
    PersonPart[] components;


    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public float getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(float timestap) {
        this.timestamp = timestap;
    }

    public PersonPart[] getComponents() {
        return components;
    }

    public void setComponents(PersonPart[] components) {
        this.components = components;
    }


}
