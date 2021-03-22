package ua.nure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ua.nure.entity.Admin;
import ua.nure.entity.Role;
import ua.nure.entity.value.UserRole;
import ua.nure.repository.AdminRepository;

@SpringBootApplication
public class FloristikUpApplication {

    public static void main(String[] args) {
       SpringApplication.run(FloristikUpApplication.class, args);
    }

}
