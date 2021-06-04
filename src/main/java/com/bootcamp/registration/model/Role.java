package com.bootcamp.registration.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="roles")
public class Role {
    @Id
    private String id;

    private RoleEnum name;



    public Role(){

    }
    public Role(RoleEnum name){
        this.name = name;
    }


    public RoleEnum getName() {
        return name;
    }

    public void setName(RoleEnum name) {
        this.name = name;
    }




    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return  this.id;
    }
}
