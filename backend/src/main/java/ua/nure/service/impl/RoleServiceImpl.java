package ua.nure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.entity.Role;
import ua.nure.entity.value.UserRole;
import ua.nure.repository.RoleRepository;
import ua.nure.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role findByName(UserRole name) {
        return roleRepository.findByName(name).orElse(null);
    }
}
