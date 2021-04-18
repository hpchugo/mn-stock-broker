package com.github.hpchugo.stockbroker.controller;

import com.github.hpchugo.stockbroker.model.Symbol;
import com.github.hpchugo.stockbroker.persistence.jpa.SymbolsRepository;
import com.github.hpchugo.stockbroker.persistence.model.SymbolEntity;
import com.github.hpchugo.stockbroker.store.InMemoryStore;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Single;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/markets")
public class MarketsController {

    private final InMemoryStore store;
    private final SymbolsRepository symbols;
    public MarketsController(final InMemoryStore store, final SymbolsRepository symbols) {
        this.store = store;
        this.symbols = symbols;
    }

    @Operation(summary = "Returns all available markets")
    @ApiResponse(
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Tag(name = "markets")
    @Get("/")
    public Single<List<Symbol>> all(){
        return Single.just(store.getAllSimbols());
    }


    @Operation(summary = "Returns all available markets from database using JPA")
    @ApiResponse(
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Tag(name = "markets")
    @Get("/jpa")
    public Single<List<SymbolEntity>>  allSymbolsViaJpa(){
        return Single.just(symbols.findAll());
    }

}
