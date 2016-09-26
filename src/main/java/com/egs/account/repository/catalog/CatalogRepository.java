package com.egs.account.repository.catalog;


import com.egs.account.model.Catalog;
import java.util.List;

public interface CatalogRepository {

    List<Catalog> findAll();

    Catalog findById(Long id);

    void save(Catalog document);

    List<Catalog> findAllByUserId(Long userId);

    void deleteById(Long id);
}