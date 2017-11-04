package com.egs.account.repository.catalog;

import com.egs.account.model.Catalog;
import com.egs.account.repository.AbstractDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("catalogRepository")
public class CatalogRepoImpl extends AbstractDao<Long, Catalog> implements CatalogRepository {

    @SuppressWarnings("unchecked")
    public List<Catalog> findAll() {
	    final Criteria criteria = createEntityCriteria();

        return (List<Catalog>)criteria.list();
    }

    public void save(Catalog catalog) {
        persist(catalog);
    }

    public Catalog findById(Long id) {
        return getByKey(id);
    }

    @SuppressWarnings("unchecked")
    public List<Catalog> findAllByUserId(Long userId){
	    final Criteria criteria = createEntityCriteria();
	    final Criteria userCriteria = criteria.createCriteria("user");
	    userCriteria.add(Restrictions.eq("id", userId));

        return (List<Catalog>)criteria.list();
    }

    public void deleteById(Long id) {
	    final Catalog catalog = getByKey(id);
	    delete(catalog);
    }
}
