package com.bootcamp.registration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class CaptchaTokenService {
    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private RestTemplate template;


    String siteKey = "6LeZmfEaAAAAACBI5Ld-TPg6uhRMkZ6foarbHk0U";
    String recaptchaEndpoint = "https://www.google.com/recaptcha/api.js?render=" + siteKey;

    public CaptchaTokenService() {
        RestTemplateBuilder templateBuilder = new RestTemplateBuilder();
        template = templateBuilder.build();
    }

    public String getToken() {


        String apiResponse = null;
        try {
            apiResponse = template.getForObject(recaptchaEndpoint, String.class);
            System.out.println(apiResponse);
        } catch (final RestClientException e) {
            log.error("Some exception occurred while binding to the recaptcha endpoint.", e);
        }
        return apiResponse;

    }

    public static void main(String[] args) {
        CaptchaTokenService service = new CaptchaTokenService();
        service.getToken();
    }
}
