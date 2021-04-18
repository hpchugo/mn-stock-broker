package com.github.hpchugo.stockbroker.persistence.jpa;

import com.github.hpchugo.stockbroker.persistence.model.QuoteDTO;
import com.github.hpchugo.stockbroker.persistence.model.QuoteEntity;
import com.github.hpchugo.stockbroker.persistence.model.SymbolEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Slice;
import io.micronaut.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuotesRepository extends CrudRepository<QuoteEntity, Integer> {

    @Override
    List<QuoteEntity> findAll();

    Optional<QuoteEntity> findBySymbol(SymbolEntity symbol);

    /**
     * @Ordering
     * @List<QuoteDTO>
     */
    //List<QuoteEntity> listOrderByVolumeDesc();
    //List<QuoteEntity> listOrderByVolumeAsc();
    List<QuoteDTO> listOrderByVolumeDesc();
    List<QuoteDTO> listOrderByVolumeAsc();


    /**
     * @Filtering
     * @List<QuoteDTO>
     */
    List<QuoteDTO> findByVolumeGreaterThan(BigDecimal volume);

    /**
     * @Page
     * @List<QuoteDTO>
     */
    List<QuoteDTO> findByVolumeGreaterThan(BigDecimal volume, Pageable pageable);
    Slice<QuoteDTO> list(Pageable pageable);

}
