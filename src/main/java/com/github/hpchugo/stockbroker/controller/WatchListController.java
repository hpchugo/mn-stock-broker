package com.github.hpchugo.stockbroker.controller;

import com.github.hpchugo.stockbroker.model.WatchList;
import com.github.hpchugo.stockbroker.store.InMemoryAccountStore;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/account/watchlist")
public class WatchListController {

    private static final Logger LOG = LoggerFactory.getLogger(WatchListController.class);

    private final InMemoryAccountStore store;
    static final UUID ACCOUNT_ID = UUID.randomUUID();

    public WatchListController(final InMemoryAccountStore store) {
        this.store = store;
    }

    @Get(
            value = "/",
            produces = MediaType.APPLICATION_JSON
    )
    public WatchList get(){
        LOG.debug("get - {}", Thread.currentThread().getName());
        return store.getWatchList(ACCOUNT_ID);
    }

    @Put(
            value = "/",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    public WatchList update(@Body WatchList watchList){
        return store.updateWatchList(ACCOUNT_ID, watchList);
    }

    @Delete(
            value = "/{accountID}",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    public WatchList delete(@PathVariable UUID accountID){
        return store.deleteWatchList(accountID);
    }
}
