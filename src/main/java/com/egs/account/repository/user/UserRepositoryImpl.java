package com.egs.account.repository.user;

import com.egs.account.model.User;
import com.egs.account.repository.AbstractDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userRepository")
public class UserRepositoryImpl extends AbstractDao<Long, User> implements UserRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryImpl.class);

    public User findById(Long id) {
        User user = getByKey(id);
        return user;
    }

    public User findByUsername(String username) {
        LOGGER.info("entered user with username: {}", username);
        Criteria crit = createEntityCriteria();
        crit.add(Restrictions.eq("username", username));
        User user = (User) crit.uniqueResult();
        return user;
    }

    @SuppressWarnings("unchecked")
    public List<User> findAllUsers() {
        Criteria criteria = createEntityCriteria().addOrder(Order.asc("firstName"));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//To avoid duplicates.
        List<User> users = (List<User>) criteria.list();
        LOGGER.info("Retrieved all users...");
        return users;
    }

    public void save(User user) {
        persist(user);
        LOGGER.info("persisted user {}", user.getUsername());
    }

    public void deleteById(Long id) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("id", id));
        User user = (User) criteria.uniqueResult();
        delete(user);
        LOGGER.info("deleted successfully user with id : {}", user.getId());
    }

}
