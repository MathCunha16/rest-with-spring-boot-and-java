package com.github.MathCunha16.integrationtests.dto.wrappers.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.MathCunha16.integrationtests.dto.PersonDTO;

import java.io.Serializable;
import java.util.List;

public class PersonEmbeddedDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("people")
    private List<PersonDTO> peopleDTO;

    public PersonEmbeddedDTO() {
    }

    public List<PersonDTO> getPeopleDTO() {
        return peopleDTO;
    }

    public void setPeopleDTO(List<PersonDTO> peopleDTO) {
        this.peopleDTO = peopleDTO;
    }
}
