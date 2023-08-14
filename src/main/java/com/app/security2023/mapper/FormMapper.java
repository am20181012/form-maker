package com.app.security2023.mapper;

import com.app.security2023.dto.FormDto;
import com.app.security2023.model.Form;

public class FormMapper {

    public static Form mapToModelEntity(FormDto formDto) {
        return Form.builder()
                .id(formDto.id())
                .title(formDto.title())
                .status(formDto.status())
                .description(formDto.description())
                .expireDate(formDto.expireDate())
                .userId(formDto.userId())
                .questions(formDto.questions())
                .build();
    }
}
