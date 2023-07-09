package com.example.myshop;

import com.example.myshop.entity.Role;
import com.example.myshop.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTests {

    @Autowired
    RoleRepository repo;

    @Test
    public void createFirstRole(){
        Role roleAdmin=new Role("Admin","Manage everything");
        Role savedRole= repo.save(roleAdmin);
        assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    void createRestRoles(){
        Role roleSalesPerson=new Role("Salesperson","manage product, price, customers, shipping, orders and sales report");
        Role roleEditor=new Role("Editor","manage products, categories, brands, articles and menus");
        Role roleShipper=new Role("Shipper","view products, view orders and update order status");
        Role roleAssistant=new Role("Assistant","manage questions and reviews");

        repo.saveAll(List.of(roleAssistant,roleEditor,roleShipper,roleSalesPerson));
    }
}
