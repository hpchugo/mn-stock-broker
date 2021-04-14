package com.github.hpchugo.stockbroker;

import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class MnStockBrokerTest {
    private static final Logger LOG = LoggerFactory.getLogger(MnStockBrokerTest.class);

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    @Client("/") RxHttpClient client;

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

    @Test
    void testHelloResponse() {
        final String result = client.toBlocking().retrieve("/hello");
        assertEquals("Hello from service", result);
    }

    @Test
    void returnsGermanGreeting() {
        final String result = client.toBlocking().retrieve("/hello/de");
        assertEquals("Hallo", result);
    }

    @Test
    void returnsEnglishGreeting() {
        final String result = client.toBlocking().retrieve("/hello/en");
        assertEquals("Hello", result);
    }

    @Test
    void returnsGreetingAsJson() {
        final ObjectNode result = client.toBlocking().retrieve("/hello/json", ObjectNode.class);
        LOG.debug(result.toString());
    }
}
