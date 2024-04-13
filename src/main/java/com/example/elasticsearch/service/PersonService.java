package com.example.elasticsearch.service;

import com.example.elasticsearch.document.Person;
import com.example.elasticsearch.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void save(final Person person) {
        personRepository.save(person);
    }

    public Person findById(final String id) {
        return personRepository.findById(id).orElse(null);
    }
}
