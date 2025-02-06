package com.example.jwt.configuration;

import com.example.jwt.entities.Permission;
import com.example.jwt.entities.Role;
import com.example.jwt.entities.User;
import com.example.jwt.repositories.PermissionRepository;
import com.example.jwt.repositories.RoleRepository;
import com.example.jwt.repositories.UserRepository;
import com.example.jwt.utils.constants.GenderEnum;
import com.example.jwt.utils.constants.PredefinedRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseInitializer implements CommandLineRunner {

    private RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private PermissionRepository permissionRepository;

    public DatabaseInitializer(
            RoleRepository _roleRepository,
            UserRepository _userRepository,
            PermissionRepository _permissionRepository,
            PasswordEncoder _passwordEncoder) {
        this.roleRepository = _roleRepository;
        this.userRepository = _userRepository;
        this.permissionRepository = _permissionRepository;
        this.passwordEncoder = _passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> START INIT DATABASE");
        long countRoles = this.roleRepository.count();
        long countUsers = this.userRepository.count();
        long countPermissions = this.permissionRepository.count();

        if (countPermissions == 0) {
            ArrayList<Permission> arr = new ArrayList<>();
            arr.add(new Permission("Create a user", "/jwt/users", "POST", "USERS"));
            arr.add(new Permission("Update a user", "/jwt/users", "PUT", "USERS"));
            arr.add(new Permission("Delete a user", "/jwt/users/{id}", "DELETE", "USERS"));
            arr.add(new Permission("Get a user by id", "/jwt/users/{id}", "GET", "USERS"));
            arr.add(new Permission("Get user with pagination", "/jwt/users", "GET", "USERS"));
            this.permissionRepository.saveAll(arr);
        }

        if (countRoles == 0) {
            ArrayList<Role> arr = new ArrayList<>();
            arr.add(new Role(PredefinedRole.ADMIN_ROLE, "Admin thì full permissions", true, (List<Permission>) permissionRepository.findAll()));
            arr.add(new Role(PredefinedRole.EMPLOYEE_ROLE, "Employee thì full product", true));
            arr.add(new Role(PredefinedRole.USER_ROLE, "User thì full cart", true));
            roleRepository.saveAll(arr);
        }

        if (countUsers == 0) {
            User adminUser = new User();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setPassword(passwordEncoder.encode("Passw0rd123!"));
            adminUser.setAge(25);
            adminUser.setAddress("Admin");
            adminUser.setGender(GenderEnum.MALE);

            Role role = roleRepository.fetchRoleByName(PredefinedRole.ADMIN_ROLE);
            if (role != null) {
                adminUser.setRole(role);
            }
            userRepository.save(adminUser);
        }

        if (countRoles > 0 && countUsers > 0 && countPermissions > 0)
            System.out.println(">>> SKIP INIT DATABASE ~ ALREADY HAVE DATA...");
        else
            System.out.println(">>> END INIT DATABASE");
    }
}
