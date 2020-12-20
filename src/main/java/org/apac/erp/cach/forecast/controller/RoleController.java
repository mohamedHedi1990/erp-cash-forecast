package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.persistence.entities.Role;
import org.apac.erp.cach.forecast.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;
    @CrossOrigin
    @GetMapping("/findAllRoles")
    List<Role> findAllRoles()
    {
        return  roleService.findAllRoles();
    }
    @CrossOrigin
    @GetMapping("/findRole/{id}")
    Role findRole(@PathVariable Long id)
    {
        return roleService.findRole(id);
    }
    @CrossOrigin
    @PostMapping("/saveRole")
    Role saveRole(@RequestBody Role role)
    {
        return roleService.saveRole(role);
    }
}
