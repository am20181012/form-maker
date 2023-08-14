package com.app.security2023.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "answers")
public class FormAnswer {

    @Id private String id;
    @Field("created_at") private Date createdAt;
    @Field("form_id") private String formId;
    @Field("user_id") private String userId;
    private List<Answer> answers;

}
