package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.entities.Role;
import org.apac.erp.cach.forecast.persistence.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;
   public Role findRole(Long id)
    {
        return  roleRepository.findOne(id);
    }
  public   Role saveRole(Role role)
    {
        return  roleRepository.save(role);
    }
   public List<Role> findAllRoles()
    {
        return roleRepository.findAll();
    }
}
