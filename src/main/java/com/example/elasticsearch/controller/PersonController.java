package com.example.elasticsearch.controller;

import com.example.elasticsearch.document.Person;
import com.example.elasticsearch.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/person")
public class PersonController {
    private final PersonService service;

    @Autowired
    public PersonController(PersonService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public void save(@RequestBody final Person person) {
        service.save(person);
    }

    @GetMapping("/find/{id}")
    public Person findById(@PathVariable final String id) {
        return service.findById(id);
    }
}
