package com.github.hpchugo.stockbroker.controller;

import com.github.hpchugo.stockbroker.controller.account.JWTWatchListClient;
import com.github.hpchugo.stockbroker.model.Symbol;
import com.github.hpchugo.stockbroker.model.WatchList;
import com.github.hpchugo.stockbroker.store.InMemoryAccountStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.reactivex.Single;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.micronaut.http.HttpRequest.*;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class WatchListControllerReactiveTest {
    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerReactiveTest.class);
    private final UUID TEST_ACCOUNT_ID = WatchListControllerReactive.ACCOUNT_ID;


    @Inject
    EmbeddedApplication<?> application;

    @Inject
    @Client("/")
    JWTWatchListClient client;

    @Inject
    InMemoryAccountStore store;

    @Test
    void returnsEmptyWatchListForAccount() {
        final Single<WatchList> result = client.retrieveWatchlist(getAuthorizationHeader()).singleOrError();
        assertTrue(result.blockingGet().getSymbols().isEmpty());
        assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }

    @Test
    void returnsWatchListForAccount() {
        final List<Symbol> symbols = Stream.of("APPL", "AMZN", "FB", "GOOG", "MSFT", "NXFL", "TSLA")
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

        final WatchList result = client.retrieveWatchlist(getAuthorizationHeader()).singleOrError().blockingGet();
        assertEquals(7, result.getSymbols().size());
        assertEquals(7, store.getWatchList(TEST_ACCOUNT_ID).getSymbols().size());

    }

    @Test
    void returnsWatchListForAccountSingle() {
        final List<Symbol> symbols = Stream.of("APPL", "AMZN", "FB", "GOOG", "MSFT", "NXFL", "TSLA")
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);
        final WatchList result = client.retrieveWatchListAsSingle(getAuthorizationHeader()).blockingGet();
        assertEquals(7, result.getSymbols().size());
        assertEquals(7, store.getWatchList(TEST_ACCOUNT_ID).getSymbols().size());

    }

    @Test
    void returnsWatchListForAccountFlowable() {
        final List<Symbol> symbols = Stream.of("APPL", "AMZN", "FB", "GOOG", "MSFT", "NXFL", "TSLA")
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

     //   final WatchList result = client.toBlocking().retrieve("/flowable", WatchList.class);
     //   assertEquals(7, result.getSymbols().size());
     //   assertEquals(7, store.getWatchList(TEST_ACCOUNT_ID).getSymbols().size());

    }
    @Test
    void canUpdateWatchListForAccount() {
        final List<Symbol> symbols = Stream.of("APPL", "AMZN", "FB", "GOOG", "MSFT", "NXFL", "TSLA")
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        final HttpResponse<WatchList> result = client.updateWatchList(getAuthorizationHeader(), watchList);
        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals(watchList , store.getWatchList(TEST_ACCOUNT_ID));
    }

    @Test
    void canDeleteWatchListForAccount() {
        final List<Symbol> symbols = Stream.of("APPL", "AMZN", "FB", "GOOG", "MSFT", "NXFL", "TSLA")
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);
        assertFalse(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());

        final HttpResponse result = client.deleteWatchList(getAuthorizationHeader(), WatchListControllerReactive.ACCOUNT_ID);
        assertEquals(HttpStatus.OK, result.getStatus());
        assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }

    private String getAuthorizationHeader() {
        return "Bearer " + givenMyUserLoggedIn().getAccessToken();
    }

    private BearerAccessRefreshToken givenMyUserLoggedIn() {
        return client.login(new UsernamePasswordCredentials("my-user", "secret"));
    }
}
