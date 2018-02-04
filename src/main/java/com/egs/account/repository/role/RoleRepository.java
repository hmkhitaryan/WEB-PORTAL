package com.egs.account.repository.role;

import com.egs.account.model.Role;

import java.util.List;

public interface RoleRepository {

    List<Role> findAll();
}
