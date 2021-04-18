package com.github.hpchugo.stockbroker.persistence.jpa;

import com.github.hpchugo.stockbroker.model.Symbol;
import com.github.hpchugo.stockbroker.persistence.model.SymbolEntity;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface SymbolsRepository extends CrudRepository<SymbolEntity, String> {

    @Override
    List<SymbolEntity> findAll();
}
