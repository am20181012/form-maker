package com.app.security2023.auth.model;

import com.app.security2023.model.Form;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * prvo sta Spring Security koristi jeste objekat UserDetails
 * UserDetails je interfejs koji nudi odredjene metode
 * stoga Spring Security bi trebalo da dobije objekat ovog tipa
 * kada implementiramo ovaj interfejs nad nasom klasom onda nas User postaje user Spring Security-ja
 *
 * takodje, Spring framework nudi svoju klasu User iz paketa org.springframework.security.core.userdetails, pa
 * mozemo implementirati UserDetails interfejs u svoju klasu ili naslediti User-a iz SpringBoot-a
 * naravno, ukoliko implementiramo sopstvenu klasu imamo vecu kontrolu nad njom
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User implements UserDetails {

    @Id private String id;
    @Field(name = "first_name") private String firstName;
    @Field(name = "last_name") private String lastName;
    @Indexed(unique = true) private String email;
    private String password;
    @DBRef private List<Form> forms;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void addForm(Form form) {
        if(forms == null) {
            forms = new ArrayList<>();
        }
        forms.add(form);
    }

    public void removeForm(String formId) {
        forms.removeIf(f -> f.getId().equals(formId));
    }
}
