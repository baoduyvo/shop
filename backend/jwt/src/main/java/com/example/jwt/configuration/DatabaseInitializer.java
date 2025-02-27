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
import java.util.Arrays;
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

            // Create permissions for Auth
            arr.add(new Permission("Logout", "/jwt/auth/logout", "POST", "AUTHENTICATION"));
            arr.add(new Permission("Refresh", "/jwt/auth/refresh", "POST", "AUTHENTICATION"));
            arr.add(new Permission("Logout", "/jwt/auth/logout", "POST", "AUTHENTICATION"));

            // Create permissions for Profile
            arr.add(new Permission("Update a profile user", "/jwt/profile/{id}", "PUT", "PROFILE"));
            arr.add(new Permission("Get a profile user by id", "/jwt/profile/{id}", "GET", "PROFILE"));

            // Create permissions for Users
            arr.add(new Permission("Create a user", "/jwt/users", "POST", "USERS"));
            arr.add(new Permission("Update a user", "/jwt/users/{id}", "PUT", "USERS"));
            arr.add(new Permission("Delete a user", "/jwt/users/{id}", "DELETE", "USERS"));
            arr.add(new Permission("Get a user by id", "/jwt/users/{id}", "GET", "USERS"));
            arr.add(new Permission("Get user with pagination", "/jwt/users", "GET", "USERS"));

            // Create permissions for Categories
            arr.add(new Permission("Create a category", "/shop/categories", "POST", "CATEGORIES"));
            arr.add(new Permission("Update a category", "/shop/categories/{id}", "PATCH", "CATEGORIES"));
            arr.add(new Permission("Delete a category", "/shop/categories/{id}", "DELETE", "CATEGORIES"));
            arr.add(new Permission("Get a category by id", "/shop/categories/{id}", "GET", "CATEGORIES"));
            arr.add(new Permission("Get a category by name", "/shop/categories/search", "POST", "CATEGORIES"));
            arr.add(new Permission("Get a category with pagination", "/shop/categories", "GET", "CATEGORIES"));

            // Create permissions for Products
            arr.add(new Permission("Create a product", "/shop/products", "POST", "PRODUCTS"));
            arr.add(new Permission("Update a product", "/shop/products/{id}", "PUT", "PRODUCTS"));
            arr.add(new Permission("Delete a product", "/shop/products/{id}", "DELETE", "PRODUCTS"));
            arr.add(new Permission("Get a product by id", "/shop/products/{id}", "GET", "PRODUCTS"));
            arr.add(new Permission("Get a product by name", "/shop/products/search", "POST", "PRODUCTS"));
            arr.add(new Permission("Get a product with pagination", "/shop/products", "GET", "PRODUCTS"));

            // Create permissions for Cart Detail
            arr.add(new Permission("Create a cart detail", "/shop/carts", "POST", "CARTDETAILS"));
            arr.add(new Permission("Update a cart detail", "/shop/carts/{id}", "PUT", "CARTDETAILS"));
            arr.add(new Permission("Delete a cart detail", "/shop/carts/{id}", "DELETE", "CARTDETAILS"));
            arr.add(new Permission("Get a cart detail by cart detail id", "/shop/carts/{id}", "GET", "CARTDETAILS"));
            arr.add(new Permission("Get a cart detail by user id", "/shop/carts/{id}", "GET", "CARTDETAILS"));
            arr.add(new Permission("Get a cart detail by product id", "/shop/carts/{id}", "GET", "CARTDETAILS"));
            arr.add(new Permission("Get a cart detail with pagination", "/shop/carts", "GET", "CARTDETAILS"));

            this.permissionRepository.saveAll(arr);
        }

        if (countRoles == 0) {
            List<Permission> allPermissions = (List<Permission>) permissionRepository.findAll();
            ArrayList<Role> arr = new ArrayList<>();
            arr.add(new Role(PredefinedRole.ADMIN_ROLE, "Admin thì full permissions", true, allPermissions));
            arr.add(new Role(PredefinedRole.EMPLOYEE_ROLE, "Employee thì full category, product", true, getRoleEmployeePermissions(allPermissions)));
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

    private List<Permission> getRoleEmployeePermissions(List<Permission> allPermissions) {
        List<Permission> employeePermissions = new ArrayList<>();

        for (Permission permission : allPermissions) {
            if ("AUTHENTICATION".equals(permission.getModule()) ||
                    "PROFILE".equals(permission.getModule()) ||
                    "CATEGORIES".equals(permission.getModule()) ||
                    "PRODUCTS".equals(permission.getModule())
            ) {
                employeePermissions.add(permission);
            }
        }

        return employeePermissions;
    }

    private List<Permission> getRoleClientPermissions(List<Permission> allPermissions) {
        List<Permission> productPermissions = new ArrayList<>();

        for (Permission permission : allPermissions) {
            if ("AUTHENTICATION".equals(permission.getModule()) ||
                    "CATEGORIES".equals(permission.getModule())
                            && ("GET".equals(permission.getMethod()) ||
                            "PATCH".equals(permission.getApiPath())) ||
                    "PRODUCTS".equals(permission.getModule())
            ) {
                productPermissions.add(permission);
            }
        }

        return productPermissions;
    }
}
