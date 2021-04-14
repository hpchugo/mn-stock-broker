package com.github.hpchugo.stockbroker.store;

import com.github.hpchugo.stockbroker.model.Quote;
import com.github.hpchugo.stockbroker.model.Symbol;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class InMemoryStore {
    private final List<Symbol> symbols;
    private final Map<String, Quote> cachedQuotes = new HashMap<>();
    private final ThreadLocalRandom current = ThreadLocalRandom.current();

    public InMemoryStore() {
        this.symbols = Stream.of("APPL", "AMZN", "FB", "GOOG", "MSFT", "NXFL", "TSLA")
                .map(Symbol::new)
                .collect(Collectors.toList());
        this.symbols.forEach(symbol -> cachedQuotes.put(symbol.getValue(), randomQuote(symbol)));

    }

    private Quote randomQuote(Symbol symbol) {
        return Quote.builder().symbol(symbol)
                .bid(randomValue())
                .ask(randomValue())
                .lastPrice(randomValue())
                .volume(randomValue())
                .build();
    }

    public List<Symbol> getAllSimbols() {
        return symbols;
    }

    public Optional<Quote> fetchQuote(final String symbol) {
        return Optional.ofNullable(cachedQuotes.get(symbol));
    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(current.nextDouble(1, 100));
    }

    public void update(final Quote quote) {
        cachedQuotes.put(quote.getSymbol().getValue(), quote);
    }
}
