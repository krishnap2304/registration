package com.bootcamp.registration.controller;

import com.bootcamp.registration.model.Role;
import com.bootcamp.registration.model.RoleEnum;
import com.bootcamp.registration.model.User;
import com.bootcamp.registration.request.SignupRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {

    @Autowired
    protected MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    protected void setUp() {

        DefaultMockMvcBuilder builder = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .dispatchOptions(true);
        this.mvc = builder.build();
    }



    @Test
    public void getUsers() throws Exception {
        String uri = "/api/auth/users";
        MvcResult mvcResult = null ;
        try {
            mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                    .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        }catch(Exception e){
            e.printStackTrace();
        }

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
    }
    @Test
    public void testSignUpSuccess()throws Exception{
        String uri = "/api/auth/signup";
        SignupRequest user = getMockedSignupReq();
        MvcResult mvcResult = null ;

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri,user)
                .header("Access-Control-Request-Method", "POST")
                .header("Origin", "*")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        System.out.println(status);
        System.out.println(mvcResult.getResponse().getErrorMessage());
        System.out.println(mvcResult.getResponse().getContentAsString());
        assertNotNull(status);

    }
    @Test
    public void testSignUpInvalid()throws Exception{
        String uri = "/api/auth/signup";

        User user = new User();
        user.setCaptchaResp("dfajdfalsdjflj");
        user.setUsername("testuser");
        user.setPassword("asdfadf");
        Set<Role> roles = new HashSet<Role>();
        roles.add(new Role(RoleEnum.ROLE_USER));
        user.setRoles(roles);
        MvcResult mvcResult = null ;
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri,user)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();

        assertEquals("400","400");
    }

    @Test
    public void getRoles() throws Exception {
        String uri = "/api/auth/roles";
        MvcResult mvcResult = null ;
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
    }

    @Test
    public void deRegisterUser() throws Exception {
        String uri = "/api/auth/de-register";
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("Test1234");
        user.setCaptchaResp("asdfasdfads");
        user.setEmail("testuser@yahoo.com");
        MvcResult mvcResult = null ;
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri,user)
                .header("Access-Control-Request-Method", "POST")
                .header("Origin", "*")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        System.out.println(status);
        assertNotNull(status);
    }

    private SignupRequest getMockedSignupReq(){
        SignupRequest user = new SignupRequest();
        user.setUsername("springtestuser");
        user.setPassword("Test1234");
        user.setEmail("springtestuser@yahoo.com");
        user.setCaptchaResp("adfadsfasdfsasdfs");
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        user.setRoles(roles);
        return user;
    }
}
