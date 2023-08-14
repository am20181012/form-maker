package com.app.security2023.mapper;

import com.app.security2023.dto.FormAnswerDto;
import com.app.security2023.dto.FormDto;
import com.app.security2023.model.Answer;
import com.app.security2023.model.Form;
import com.app.security2023.model.FormAnswer;

import java.util.ArrayList;
import java.util.List;

public class FormAnswerMapper {

    public static FormAnswer mapToModelEntity(FormAnswerDto formAnswerDto) {
        List<Answer> answers = new ArrayList<>();
        formAnswerDto.answers().forEach((a) -> {
            Answer answer = Answer.builder()
                    .uuid(a.uuid())
                    .answer(a.answer())
                    .build();
            answers.add(answer);
        });
        return FormAnswer.builder()
                .answers(answers)
                .build();
    }
}
