package com.universalcinemas.application.data.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.helpers.CrudService;

public class UserService extends CrudService<User, Integer>{

    private UserRepository repository;

    public UserService(@Autowired UserRepository repository) {
        this.repository = repository;
    }

    protected UserRepository getRepository() {
        return repository;
    }
}
