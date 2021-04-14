package com.github.hpchugo.stockbroker.store;

import com.github.hpchugo.stockbroker.model.WatchList;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class InMemoryAccountStore {

    private final HashMap<UUID, WatchList> watchListsPerAccount = new HashMap<>();

    public WatchList getWatchList(final UUID accountId) {
        return watchListsPerAccount.getOrDefault(accountId, new WatchList());
    }

    public WatchList updateWatchList(final UUID accountId, final WatchList watchList) {
        watchListsPerAccount.put(accountId, watchList);
        return getWatchList(accountId);
    }

    public WatchList deleteWatchList(final UUID accountID) {
        return watchListsPerAccount.remove(accountID);
    }
}
