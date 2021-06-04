package com.bootcamp.registration.service;

import com.bootcamp.registration.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class TokenServiceImpl {
    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private RestTemplate template;

    String recaptchaEndpoint= "http://localhost:8088/api/auth/token";

    public TokenServiceImpl() {
        RestTemplateBuilder templateBuilder = new RestTemplateBuilder();
        template = templateBuilder.build();
    }

    public ResponseEntity<Token> getToken(String username) {

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username",username);
        ResponseEntity<Token> apiResponse = null;
        try {
             apiResponse = template.getForEntity(recaptchaEndpoint, Token.class, params);

        } catch (final RestClientException e) {
            log.error("Some exception occurred while generating the token", e);
        }
        return apiResponse;

    }

}
