package com.github.hpchugo.stockbroker.controller;

import com.github.hpchugo.stockbroker.model.WatchList;
import com.github.hpchugo.stockbroker.store.InMemoryAccountStore;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Controller("/account/watchlist-reactive")
public class WatchListControllerReactive {

    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerReactive.class);
    static final UUID ACCOUNT_ID = UUID.randomUUID();

    private final InMemoryAccountStore store;
    private final Scheduler scheduler;

    public WatchListControllerReactive(final InMemoryAccountStore store, @Named(TaskExecutors.IO) ExecutorService executorService) {
        this.store = store;
        this.scheduler = Schedulers.from(executorService);
    }

    @Get(
            value = "/",
            produces = MediaType.APPLICATION_JSON
    )
    @ExecuteOn(TaskExecutors.IO)
    public WatchList get() {
        LOG.debug("get - {}", Thread.currentThread().getName());
        return store.getWatchList(ACCOUNT_ID);
    }

    @Get(
            value = "/single",
            produces = MediaType.APPLICATION_JSON
    )
    @ExecuteOn(TaskExecutors.IO)
    public Single<WatchList> getAsSingle() {
        return Single.fromCallable(() -> {
                    LOG.debug("get - {}", Thread.currentThread().getName());
                    return store.getWatchList(ACCOUNT_ID);
                }
        ).subscribeOn(scheduler);

    }

    @Get(
            value = "/flowable",
            produces = MediaType.APPLICATION_JSON
    )
    @ExecuteOn(TaskExecutors.IO)
    public Flowable<WatchList> getAsFlowable() {
        return Single.fromCallable(() -> {
                    LOG.debug("get - {}", Thread.currentThread().getName());
                    return store.getWatchList(ACCOUNT_ID);
                }
        ).toFlowable().subscribeOn(scheduler);

    }

    @Put(
            value = "/",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ExecuteOn(TaskExecutors.IO)
    public WatchList update(@Body WatchList watchList) {
        return store.updateWatchList(ACCOUNT_ID, watchList);
    }

    @Delete(
            value = "/{accountID}",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ExecuteOn(TaskExecutors.IO)
    public WatchList delete(@PathVariable UUID accountID) {
        return store.deleteWatchList(accountID);
    }
}
