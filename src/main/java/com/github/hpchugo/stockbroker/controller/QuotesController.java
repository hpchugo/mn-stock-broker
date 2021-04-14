package com.github.hpchugo.stockbroker.controller;

import com.github.hpchugo.stockbroker.error.CustomError;
import com.github.hpchugo.stockbroker.model.Quote;
import com.github.hpchugo.stockbroker.store.InMemoryStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;

@Controller("/quotes")
public class QuotesController {
    private final InMemoryStore store;

    public QuotesController(InMemoryStore store) {
        this.store = store;
    }
    @Operation(summary = "Returns a quote for the given symbol.")
    @ApiResponse(
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @ApiResponse(responseCode = "400", description = "Invalid symbol specified")
            @Tag(name = "quotes")
    @Get("/{symbol}")
    public HttpResponse getQuote(@PathVariable String symbol){
        Optional<Quote> quote = store.fetchQuote(symbol);
        if(quote.isEmpty()){
            final CustomError notFound = CustomError.builder()
                    .status(HttpResponse.notFound().getStatus().getCode())
                    .error(HttpResponse.notFound().getStatus().name())
                    .message("Quote for symbol not available")
                    .path("/quotes/" + symbol)
                    .build();
            return HttpResponse.notFound(notFound);
        }
        return HttpResponse.ok(quote.get());
    }

}
