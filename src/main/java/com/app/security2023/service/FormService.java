package com.app.security2023.service;

import com.app.security2023.auth.model.User;
import com.app.security2023.auth.repo.UserRepository;
import com.app.security2023.dto.FormAnswerDto;
import com.app.security2023.dto.FormDto;
import com.app.security2023.mapper.FormAnswerMapper;
import com.app.security2023.mapper.FormMapper;
import com.app.security2023.model.Form;
import com.app.security2023.model.FormAnswer;
import com.app.security2023.repository.FormAnswerRepository;
import com.app.security2023.repository.FormRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
public class FormService {

    private final FormRepository formRepository;
    private final UserRepository userRepository;
    private final FormAnswerRepository answerRepository;

    @Value("${image.path}")
    private static String IMAGE_PATH;

    @Autowired
    public FormService(FormRepository formRepository, UserRepository userRepository, FormAnswerRepository answerRepository) {
        this.formRepository = formRepository;
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
    }

    // CREATE
    // sa fronta stize: FormDto (title, description, expire_date, status, questions)
    public Form save(FormDto formDto) {
        Form form = FormMapper.mapToModelEntity(formDto);

        form.setLatestUpdate(new Date());
        String slug = form.getTitle().replace(" ", "-");
        form.setSlug(this.generateSlug(slug));

        if(formDto.image() != null) {
            try {
                URL imageUrl = getImageUrl(formDto.image());
                form.setImageUrl(imageUrl);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Can not save the image.");
            }
        }

        Form repoForm = formRepository.save(form);

        User user = userRepository.findById(repoForm.getUserId())
                .orElseThrow(() -> new RuntimeException("Unknown user!"));

        user.addForm(repoForm);
        userRepository.save(user);

        return repoForm;
    }

    public FormAnswer saveAnswer(String id, FormAnswerDto formAnswerDto) {
        FormAnswer formAnswer = FormAnswerMapper.mapToModelEntity(formAnswerDto);
        formAnswer.setCreatedAt(new Date());
        formAnswer.setFormId(id);

        Form form = formRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not save answer for this form"));
        formAnswer.setUserId(form.getUserId());

        FormAnswer repoFormAnswer = answerRepository.save(formAnswer);

        form.addAnswer(repoFormAnswer);
        formRepository.save(form);

        return repoFormAnswer;
    }


    // READ
    public List<Form> getAll() {
        return formRepository.findAll();
    }
    public Optional<Form> get(String id) { return formRepository.findById(id); }
    public List<Form> getByUserId(String id) { return formRepository.findAllByUserId(id); }
    public Optional<Form> getBySlug(String slug) { return formRepository.findBySlug(slug); }


    // UPDATE
    public Form update(String id, FormDto formDto) {
        Form form = FormMapper.mapToModelEntity(formDto);

        form.setId(id);
        form.setLatestUpdate(new Date());
        form.setSlug(formDto.slug()); // ?????

        Form repoForm = formRepository.findById(form.getId())
                .orElseThrow(() -> new RuntimeException("Can not upload current form."));
        form.setAnswers(repoForm.getAnswers());

        try {
            if(formDto.image() != null) {
                if(!formRepository.existsByImageUrl(formDto.image())) {
                    URL imageUrl = getImageUrl(formDto.image());
                    form.setImageUrl(imageUrl);
                    //Form f = repository.findById(formDto.id()).get();
                    deleteImage(repoForm.getImageUrl());
                } else {
                    form.setImageUrl(new URL(formDto.image()));
                }
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Can not save the image.");
        }
        return formRepository.save(form);
    }


    // DELETE
    public void delete(String id) {
        Form repoForm = formRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not delete current form."));

        User repoUser = userRepository.findById(repoForm.getUserId())
                .orElseThrow(() -> new RuntimeException("Unknown user"));

        repoUser.removeForm(id);

        deleteImage(repoForm.getImageUrl());

        if(repoForm.getAnswers() != null && !repoForm.getAnswers().isEmpty()) {
            answerRepository.deleteAll(repoForm.getAnswers());
        }

        formRepository.deleteById(id);
        userRepository.save(repoUser);
    }

    public List<Form> search(String userId, String title) {
        return formRepository.findByUserIdAndTitleLikeIgnoreCase(userId, title);
    }



    // ... private methods

    private String generateSlug(String slug) {
        slug = slug.toLowerCase();
        if(!formRepository.existsBySlug(slug)) {
            return slug;
        }
        Random randomChar = new Random();
        char suffix = (char) ('a' + randomChar.nextInt(26));
        slug = slug + "-" + suffix;
        return generateSlug(slug);
    }

    private URL getImageUrl(String image) throws MalformedURLException {
        String path = saveImage(image);
        return new URL("http", "localhost", 8080, path);
    }

    private String saveImage(String image) {
        //String base64 = image.split(",")[0];

        String extension = image
                .split(",")[0]
                .split(";")[0]
                .split("/")[1];

        String randomString = RandomStringUtils.randomAlphanumeric(15);

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] imageByte = decoder.decode(image.split(",")[1]);

        String path = "src/main/resources/static/images/" + randomString + "." + extension;

        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path))) {
            outputStream.write(imageByte);
            return "/images/" + randomString + "." + extension;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void deleteImage(URL url) {
        if(url != null) {
            String path = "src/main/resources/static/images/" + url.toString().split("/")[4];
            new File(path).delete();
        }
    }

}
