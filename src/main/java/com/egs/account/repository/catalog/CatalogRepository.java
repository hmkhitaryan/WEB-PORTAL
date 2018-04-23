package com.egs.account.repository.catalog;


import com.egs.account.model.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {
    @Query("select c from Catalog c where c.user.id = ?")
    List<Catalog> findAllByUserId(Long userId);
}