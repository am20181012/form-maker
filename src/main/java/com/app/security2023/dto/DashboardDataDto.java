package com.app.security2023.dto;

import com.app.security2023.model.Form;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDataDto {

    private Long numberOfForms;
    private Long numberOfAnswers;
    private Form latestForm;
    private List<Form> forms;

}
