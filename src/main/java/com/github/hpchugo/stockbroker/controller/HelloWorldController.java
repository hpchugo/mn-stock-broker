package com.github.hpchugo.stockbroker.controller;

import com.github.hpchugo.stockbroker.config.GreetingConfig;
import com.github.hpchugo.stockbroker.model.Greeting;
import com.github.hpchugo.stockbroker.service.HelloWorldService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("${hello.controller.path}")
public class HelloWorldController {

    private final HelloWorldService service;
    private final GreetingConfig config;

    public HelloWorldController(HelloWorldService service, GreetingConfig config) {
        this.service = service;
        this.config = config;
    }

    @Get("/")
    public String index(){
        return service.sayHi();
    }

    @Get("/de")
    public String greetInGerman(){
        return config.getDe();
    }

    @Get("/en")
    public String greetInEnglish(){
        return config.getEn();
    }

    @Get(value = "/json", produces = MediaType.APPLICATION_JSON)
    public Greeting json(){
        return new Greeting();
    }


}
