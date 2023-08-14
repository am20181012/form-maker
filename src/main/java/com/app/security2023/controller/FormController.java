package com.app.security2023.controller;

import com.app.security2023.dto.FormAnswerDto;
import com.app.security2023.dto.FormDto;
import com.app.security2023.service.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

@RestController
@RequestMapping(value = "/api/v1/forms")
public class FormController {

    private final FormService service;

    @Autowired
    public FormController(FormService service) {
        this.service = service;
    }


    // CREATE
    // POST localhost:8080/api/v1/forms
    @PostMapping
    public ResponseEntity<?> save(@RequestBody FormDto form) {
        try{
            return ResponseEntity.ok(service.save(form));
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    // POST localhost:8080/api/v1/forms/answer/{id}
    @PostMapping("/answer/{id}")
    public ResponseEntity<?> saveAnswer(@PathVariable String id, @RequestBody FormAnswerDto formAnswerDto) {
        try{
            return ResponseEntity.ok(service.saveAnswer(id, formAnswerDto));
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }


    // READ
    // GET localhost:8080/api/v1/forms
    // ovaj se ne koristi
    @GetMapping
    public ResponseEntity<?> get() {
        try{
            return ResponseEntity.ok(service.getAll());
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    // GET localhost:8080/api/v1/forms/{id}
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        try{
            return ResponseEntity.ok(service.get(id));
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    // GET localhost:8080/api/v1/forms/user/{id}
    @GetMapping(value = "/user/{id}")
    public ResponseEntity<?> getByUserId(@PathVariable String id) {
        try{
            return ResponseEntity.ok(service.getByUserId(id));
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    // GET localhost:8080/api/v1/forms/form-by-slug/{slug}
    @GetMapping(value = "/form-by-slug/{slug}")
    public ResponseEntity<?> getBySlug(@PathVariable String slug) {
        try{
            return ResponseEntity.ok(service.getBySlug(slug));
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/search/{id}")
    public ResponseEntity<?> search(@PathVariable String id, @RequestParam String title) {
        try{
            return ResponseEntity.ok(service.search(id, title));
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }


    // UPDATE
    // PUT localhost:8080/api/v1/forms/{id}
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody FormDto form) {
        try{
            //return new ResponseEntity<>(new Exception("Test trenutno ne moze biti azuriran"), HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(service.update(id, form));
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }


    // DELETE
    // DELETE localhost:8080/api/v1/forms/{id}
    // todo uokviriti nekako u ResponseEntity i try catch blok
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable String id) {service.delete(id);}

}
