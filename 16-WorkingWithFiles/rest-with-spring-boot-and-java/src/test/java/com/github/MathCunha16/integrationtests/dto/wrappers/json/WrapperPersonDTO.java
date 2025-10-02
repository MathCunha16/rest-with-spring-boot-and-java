package com.github.MathCunha16.integrationtests.dto.wrappers.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class WrapperPersonDTO  implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private PersonEmbeddedDTO embeddedDTO;

    public WrapperPersonDTO() {
    }

    public PersonEmbeddedDTO getEmbededDTO() {
        return embeddedDTO;
    }

    public void setEmbeddedDTO(PersonEmbeddedDTO embeddedDTO) {
        this.embeddedDTO = embeddedDTO;
    }
}
