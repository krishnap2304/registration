package com.bootcamp.registration.service;

import com.bootcamp.registration.model.CaptchaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class CaptchaValidatorService {
    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private RestTemplate template;

    String recaptchaEndpoint= "https://www.google.com/recaptcha/api/siteverify";

    String recaptchaSecret= "your secret";
    public CaptchaValidatorService() {
        RestTemplateBuilder templateBuilder = new RestTemplateBuilder();
        template = templateBuilder.build();
    }

    public boolean validateCaptcha(final String captchaResponse, final String remoteip) {

        log.info("Going to validate the captcha response = {}", captchaResponse);
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", recaptchaSecret);
        params.add("response", captchaResponse);
        params.add("remoteip", remoteip);
        params.add("Content-Type","application/x-www-form-urlencoded");

        CaptchaResponse apiResponse = null;
        try {
            apiResponse = template.postForObject(recaptchaEndpoint, params, CaptchaResponse.class);
        } catch (final RestClientException e) {
            log.error("Some exception occurred while binding to the recaptcha endpoint.", e);
        }

        if (Objects.nonNull(apiResponse) && apiResponse.isSuccess()) {
            log.info("Captcha Verfication Successful API response = {}", apiResponse.toString());
            return true;
        } else {
            log.info("Captcha Verfication failed = {}", apiResponse.toString());
            return false;
        }

    }

//    public static void main(String[] args) {
//        CaptchaValidatorService service = new CaptchaValidatorService();
//        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("secret", "yoursecret");
//        RestTemplateBuilder templateBuilder = new RestTemplateBuilder();
//        RestTemplate template = templateBuilder.build();
//        CaptchaResponse apiResponse = null;
//        try {
//            apiResponse = template.postForObject("https://www.google.com/recaptcha/api/siteverify", params, CaptchaResponse.class);
//            ObjectMapper mapper = new ObjectMapper();
//            System.out.println(mapper.writeValueAsString(apiResponse));
//        } catch (JsonProcessingException ex) {
//            System.out.println("Error===>"+ex.getMessage());
//        }
//    }
}
