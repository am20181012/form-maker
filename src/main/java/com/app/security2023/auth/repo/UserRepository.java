package com.app.security2023.auth.repo;

import com.app.security2023.auth.model.User;
import com.app.security2023.model.Form;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Klasa ili u ovom slucaju interfejs koji je odgovoran za komunikaciju sa bazom
 * MongoRepository ima vec neke od predefinisanih metoda kao sto su save(User u), findAll(), findById(String id)...
 */
public interface UserRepository extends MongoRepository<User, String> {

    /**
     *
     * @param email
     * @return
     *
     * vraca User-a na osnovu njegovog email-a
     * ovde ne moramo da pisemo query iako ova metoda ne postoji kao vec predefinisana
     * Spring ce na osnovu njeng imena znati sta treba da radi
     * findBy - query method koju obezbedjuje Spring
     * Email - atribut ili naziv field-a iz nase klase User
     */
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}
