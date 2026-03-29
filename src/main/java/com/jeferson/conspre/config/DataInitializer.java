package com.jeferson.conspre.config;

import com.jeferson.conspre.entity.Role;
import com.jeferson.conspre.entity.User;
import com.jeferson.conspre.repositories.RoleRepository;
import com.jeferson.conspre.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {

        return args -> {

            // 1. Criar ROLE_ADMIN se não existir
            Role adminRole = roleRepository.findByAuthority("ROLE_ADMIN");

            if (adminRole == null) {
                adminRole = new Role();
                adminRole.setAuthority("ROLE_ADMIN");
                roleRepository.save(adminRole);
            }

            // 2. Verificar se já existe usuário admin
            User admin = userRepository.findByLogin("admin");

            if (admin == null) {

                User user = new User();
                user.setName("Administrador");
                user.setLogin("admin");
                user.setPassword(passwordEncoder.encode("123456"));
                user.setAtivo(true);

                user.addRole(adminRole);

                userRepository.save(user);

                System.out.println("✅ Admin criado com sucesso!");
            } else {
                System.out.println("ℹ️ Admin já existe.");
            }
        };
    }
}