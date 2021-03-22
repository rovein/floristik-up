package ua.nure.service;

import org.springframework.stereotype.Service;
import ua.nure.entity.Role;
import ua.nure.entity.value.UserRole;

public interface RoleService {

    Role findById(Long id);

    Role findByName(UserRole name);

}
