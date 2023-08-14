package com.app.security2023.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormQuestion {

    private String id;
    private String type;
    private String question;
    private String description;
    @JsonProperty("data") private Options options;

}
