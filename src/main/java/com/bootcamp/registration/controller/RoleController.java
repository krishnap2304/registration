package com.bootcamp.registration.controller;

import com.bootcamp.registration.model.Role;
import com.bootcamp.registration.model.RoleEnum;
import com.bootcamp.registration.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class RoleController {
    @Autowired
    RoleRepository repository;

    @GetMapping("/roles")
    public List<Role> getAllRoles(){
        return repository.findAll();
    }
    @GetMapping("/roles")
    public Role getARole(@RequestParam String name){
        if (name.equals("user")) {
            return repository.findByName(RoleEnum.ROLE_USER).get();
        } else if (name.equals("admin")){
          return repository.findByName(RoleEnum.ROLE_ADMIN).get();
        }else{
            return repository.findByName(RoleEnum.ROLE_MODERAROR).get();
        }

    }
}
