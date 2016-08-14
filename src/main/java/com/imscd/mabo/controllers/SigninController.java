package com.imscd.mabo.controllers;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Christian Sperandio on 14/08/2016.
 */
@Controller
public class SigninController {
    @RequestMapping("/signin")
    public String signin(@RequestParam(required = false, defaultValue = "") String login,
                         @RequestParam(required = false, defaultValue = "") String pwd) {

        if (login.isEmpty() || pwd.isEmpty()) {
            return "signin";
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.MULTIPART_FORM_DATA.toString());
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("Authorization", "4b21f7db-b0a1-47a3-9007-a0547c7104cd");

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("login", login);
        requestBody.add("pwd", pwd);

        HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange("http://localhost:3333/signin", HttpMethod.POST,
                    formEntity, Map.class);


            String redirectUrl = "http://localhost:1234/?token=" + response.getBody().get("token");
            return "redirect:" + redirectUrl;

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) { return "errors/401"; }
            else throw e;
        }
    }
}

