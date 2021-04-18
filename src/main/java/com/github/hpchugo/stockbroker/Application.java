package com.github.hpchugo.stockbroker;

import com.github.hpchugo.stockbroker.auth.jwt.AuthenticationProviderUserPassword;
import com.github.hpchugo.stockbroker.controller.HelloWorldController;
import com.github.hpchugo.stockbroker.service.HelloWorldService;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@OpenAPIDefinition(
    info = @Info(
            title = "mn-stock-broker",
            version = "0.1",
            description = "Udemy Micronaut Course",
            license = @License(name = "MIT")
    )
)
public class Application {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationProviderUserPassword.class);

    public static void main(String[] args) {
        final ApplicationContext context = Micronaut.run(Application.class);
        final HelloWorldService service = context.getBean(HelloWorldService.class);
        LOG.info(service.sayHi());
    }
}
