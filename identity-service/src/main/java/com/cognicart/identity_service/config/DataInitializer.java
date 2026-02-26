package com.cognicart.identity_service.config;



import com.cognicart.identity_service.entity.Role;
import com.cognicart.identity_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(Role.builder().name("ROLE_USER").build());
        }

        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
        }

        if (roleRepository.findByName("ROLE_SELLER").isEmpty()) {
            roleRepository.save(Role.builder().name("ROLE_SELLER").build());
        }
    }
}