package com.bootcamp.registration.service;

import com.bootcamp.registration.model.User;
import com.bootcamp.registration.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl {

    @Value("server.host")
    String server;
    @Value(("server.port"))
    String port;

    @Autowired
    UserRepository userRepository;

    static final int token_length = 20;
    static final boolean useLetters= true;
    static final boolean useNumbers = true;

    public String resetPasswordRequestUrl(String email){
        String token  = getResetPasswordToken();
        String resetPasswordUrl = getResetPasswordUrl(token);
        return resetPasswordUrl;
    }

    public String getResetPasswordToken(){
        return RandomStringUtils.random(token_length, useLetters, useNumbers);
    }

    public String getResetPasswordUrl(String token){
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("http://").append(server).append(":")
                .append(port).append("/reset-password?token"+token).toString();
    }

    public User saveResetPasswordToken(String token, String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.get();
        user.setResetPasswordToken(token);
       return  userRepository.save(user);
    }
}
