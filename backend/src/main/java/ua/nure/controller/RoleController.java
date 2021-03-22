package ua.nure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.entity.Role;
import ua.nure.repository.RoleRepository;

import java.util.List;

@RestController
@RequestMapping("/roles")
@Api(tags = "Role")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    @ApiOperation(value = "Returns a list of all roles", nickname = "getAllRoles")
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

}
