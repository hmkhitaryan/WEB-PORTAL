package com.egs.account.repository.role;

import com.egs.account.model.Role;
import com.egs.account.repository.AbstractDao;
import com.egs.account.repository.user.UserRepositoryImpl;
import org.hibernate.Criteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Hayk_Mkhitaryan
 */
@Repository("roleRepository")
public class RoleRepoImpl extends AbstractDao<Long, Role> implements RoleRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Override
    public List<Role> findAll() {
        final Criteria criteria = createEntityCriteria();
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//To avoid duplicates.
        final List<Role> users = (List<Role>) criteria.list();
        LOGGER.info("Retrieved all roles...");

        return users;
    }
}
