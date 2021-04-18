package com.github.hpchugo.stockbroker.controller;

import com.github.hpchugo.stockbroker.error.CustomError;
import com.github.hpchugo.stockbroker.model.Quote;
import com.github.hpchugo.stockbroker.persistence.jpa.QuotesRepository;
import com.github.hpchugo.stockbroker.persistence.model.QuoteDTO;
import com.github.hpchugo.stockbroker.persistence.model.QuoteEntity;
import com.github.hpchugo.stockbroker.persistence.model.SymbolEntity;
import com.github.hpchugo.stockbroker.store.InMemoryStore;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Slice;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/quotes")
public class QuotesController {
    private final InMemoryStore store;
    private final QuotesRepository quotes;

    public QuotesController(InMemoryStore store, QuotesRepository quotes) {
        this.store = store;
        this.quotes = quotes;
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
    @Operation(summary = "Returns a quote for the given symbol.")
    @ApiResponse(
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @ApiResponse(responseCode = "400", description = "Invalid symbol specified")
    @Tag(name = "quotes")
    @Get("/jpa/{symbol}")
    public HttpResponse getQuoteViaJPA(@PathVariable String symbol){
        Optional<QuoteEntity> quote = quotes.findBySymbol(new SymbolEntity(symbol));
        if(quote.isEmpty()){
            final CustomError notFound = CustomError.builder()
                    .status(HttpResponse.notFound().getStatus().getCode())
                    .error(HttpResponse.notFound().getStatus().name())
                    .message("Quote for symbol not available in db")
                    .path("/quotes/jpa/" + symbol)
                    .build();
            return HttpResponse.notFound(notFound);
        }
        return HttpResponse.ok(quote.get());
    }
    @Operation(summary = "Returns a quote for the given symbol.")
    @ApiResponse(
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @ApiResponse(responseCode = "400", description = "Invalid symbol specified")
    @Tag(name = "quotes")
    @Get("/jpa")
    public List<QuoteEntity> getAllQuotesViaJPA(){
        return quotes.findAll();
    }

    @Get("/jpa/ordered/desc")
    public List<QuoteDTO> orderedDesc(){
        return quotes.listOrderByVolumeDesc();
    }
    @Get("/jpa/ordered/asc")
    public List<QuoteDTO> orderedAsc(){
        return quotes.listOrderByVolumeAsc();
    }

    @Get("/jpa/volume/{volume}")
    public List<QuoteDTO> volumeFilter(@PathVariable BigDecimal volume){
        return quotes.findByVolumeGreaterThan(volume);
    }

    @Get("/jpa/pagination{?page,volume}/")
    public List<QuoteDTO> volumeFilterPagination(@QueryValue Optional<Integer> page, @QueryValue Optional<BigDecimal> volume){
        int myPage = page.isEmpty() ? 0 : page.get();
        BigDecimal myVolume = volume.isEmpty() ? BigDecimal.ZERO : volume.get();
        return quotes.findByVolumeGreaterThan(myVolume, Pageable.from(myPage, 2));
    }

    @Get("/jpa/pagination/{page}")
    public Slice<QuoteDTO> volumeFilterPagination(@QueryValue Optional<Integer> page){
        int myPage = page.isEmpty() ? 0 : page.get();
        return quotes.list(Pageable.from(myPage, 2));
    }
}
