package com.github.hpchugo.stockbroker.controller;

import static io.micronaut.http.HttpRequest.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import com.github.hpchugo.stockbroker.model.Symbol;
import com.github.hpchugo.stockbroker.model.WatchList;
import com.github.hpchugo.stockbroker.store.InMemoryAccountStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

@MicronautTest
class WatchListControllerTest {
    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerTest.class);
    private final UUID TEST_ACCOUNT_ID = WatchListController.ACCOUNT_ID;


    @Inject
    EmbeddedApplication<?> application;

    @Inject
    @Client("/account") RxHttpClient client;

    @Inject
    InMemoryAccountStore store;

    @Test
    void returnsEmptyWatchListForAccount() {
        final WatchList result = client.toBlocking().retrieve(GET("/watchlist/"), WatchList.class);
        assertTrue(result.getSymbols().isEmpty());
        assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }

    @Test
    void returnsWatchListForAccount() {
        final List<Symbol> symbols = Stream.of("APPL", "AMZN", "FB", "GOOG", "MSFT", "NXFL", "TSLA")
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

        final WatchList result = client.toBlocking().retrieve("/watchlist/", WatchList.class);
        assertEquals(7, result.getSymbols().size());
        assertEquals(7, store.getWatchList(TEST_ACCOUNT_ID).getSymbols().size());

    }

    @Test
    void canUpdateWatchListForAccount() {
        final List<Symbol> symbols = Stream.of("APPL", "AMZN", "FB", "GOOG", "MSFT", "NXFL", "TSLA")
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        final HttpResponse result = client.toBlocking().exchange(PUT("/watchlist/", watchList));
        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals(watchList , store.getWatchList(TEST_ACCOUNT_ID));
    }

    @Test
    void canDeleteWatchListForAccount() {
        final List<Symbol> symbols = Stream.of("APPL", "AMZN", "FB", "GOOG", "MSFT", "NXFL", "TSLA")
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        final HttpResponse result = client.toBlocking().exchange(DELETE("/watchlist/", watchList));
        assertEquals(HttpStatus.OK, result.getStatus());
        assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }
}
