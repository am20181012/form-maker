package com.app.security2023.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "forms")
public class Form {

    @Id private String id;
    private String title;
    private String slug;
    @JsonProperty("image_url") @Field("image_url") private URL imageUrl;
    private boolean status;
    private String description;
    @JsonProperty("expire_date") @Field("expire_date") private Date expireDate;
    @JsonProperty("latest_update") @Field("latest_update") private Date latestUpdate;
    @JsonProperty("user_id") @Field("user_id") private String userId;
    private List<FormQuestion> questions;
    @DBRef private List<FormAnswer> answers;

    public void addAnswer(FormAnswer answer) {
        if(answers == null) {
            answers = new ArrayList<>();
        }
        answers.add(answer);
    }

}
