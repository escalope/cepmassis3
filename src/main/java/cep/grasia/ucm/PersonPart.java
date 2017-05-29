package cep.grasia.ucm;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PersonPart {
	@JsonProperty(value = "data")
	PartPosition data;
	String type;
	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public PersonPart() {
	}

	

	public PartPosition getData() {
		return data;
	}

	public void setData(PartPosition data) {
		this.data = data;
	}
}
