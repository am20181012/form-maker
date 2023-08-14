package com.app.security2023.dto;

import com.app.security2023.model.FormQuestion;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;
public record FormDto(String id,
                      String title,
                      String slug,
                      String image,
                      boolean status,
                      String description,
                      @JsonProperty("expire_date") Date expireDate,
                      @JsonProperty("user_id") String userId,
                      List<FormQuestion> questions) {

}
