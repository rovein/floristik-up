package ua.nure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.nure.entity.Admin;
import ua.nure.entity.FloristShop;
import ua.nure.repository.AdminRepository;
import ua.nure.repository.FloristShopRepository;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final FloristShopRepository floristShopRepository;

    private final AdminRepository adminRepository;

    @Autowired
    public UserDetailsServiceImpl(
            FloristShopRepository floristShopRepository,
            AdminRepository adminRepository
    ) {
        this.floristShopRepository = floristShopRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<FloristShop> floristShopUser = floristShopRepository.findByEmail(email);
        Optional<Admin> adminUser = adminRepository.findByEmail(email);

        if (floristShopUser.isPresent()) {
            return new UserDetailsImpl(floristShopUser.get());
        }

        if (adminUser.isPresent()) {
            return new UserDetailsImpl(adminUser.get());
        }

        throw new UsernameNotFoundException("User with email " + email + " not found");
    }
}
