package com.app.security2023.service;

import com.app.security2023.dto.DashboardDataDto;
import com.app.security2023.model.Answer;
import com.app.security2023.model.Form;
import com.app.security2023.model.FormAnswer;
import com.app.security2023.repository.FormAnswerRepository;
import com.app.security2023.repository.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MainService {

    private final FormRepository formRepository;
    private final FormAnswerRepository formAnswerRepository;

    @Autowired
    public MainService(FormRepository formRepository, FormAnswerRepository formAnswerRepository) {
        this.formRepository = formRepository;
        this.formAnswerRepository = formAnswerRepository;
    }

    public DashboardDataDto getDashboardData(String userId) {

        Long numberOfForms = formRepository.countByUserId(userId);
        Long numberOfAnswers = formAnswerRepository.countByUserId(userId);
        Form latestForm = formRepository.findTopByUserIdOrderByLatestUpdateDesc(userId).get();
        List<Form> forms = formRepository.findByUserIdOrderByLatestUpdateDesc(userId);
        //List<FormAnswer> answers = formAnswerRepository.findByUserIdOrderByCreatedAtDesc(userId);


        return DashboardDataDto.builder()
                .numberOfForms(numberOfForms)
                .numberOfAnswers(numberOfAnswers)
                .latestForm(latestForm)
                .forms(forms)
                //.answers(answers)
                .build();
    }
}
