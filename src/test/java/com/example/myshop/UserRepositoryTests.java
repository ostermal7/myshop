package com.example.myshop;

import com.example.myshop.entity.Role;
import com.example.myshop.entity.User;
import com.example.myshop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNewUserWithOneRole(){
        Role roleAdmin=entityManager.find(Role.class,1);
        User userDenis=new User("axesaspwde@yandex.ru","12345","Denis","Panasenko");
        userDenis.addRole(roleAdmin);

        User savedUser=userRepo.save(userDenis);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRole(){

        User userLarisa=new User("larisa@ya.ru","lara95","Larisa","Larskaya");
        userLarisa.addRole(new Role(3));
        userLarisa.addRole(new Role(5));

        User savedUser=userRepo.save(userLarisa);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void ListAllUsersTest(){
        Iterable<User> allUsers=userRepo.findAll();
        allUsers.forEach(System.out::println);
    }

    @Test
    public void testUpdateUserDetails(){
        User userDenis=userRepo.findById(1).get();
        userDenis.setEnabled(true);
        userRepo.save(userDenis);
    }

    @Test
    public void testUpdateUserRoles(){
        User userLara=userRepo.findById(4).get();
        userLara.getRoles().remove(new Role(3));
        userLara.addRole(new Role(2));

        userRepo.save(userLara);
    }

    @Test
    public void testGetUserByEmail(){
        String email="axesaspwde@yandex.ru";
        User user=userRepo.getUserByEmail(email);
        assertThat(user).isNotNull();
    }

    @Test
    public void testCountById(){
        Integer id=1;
        Long countById=userRepo.countById(id);
        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testEnableUser(){
        Integer id=33;
        userRepo.updateEnabledStatus(id,false);
    }

    @Test
    public void testListFirstPage(){
        int pageNumber=0;
        int pageSize=4;
        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Page<User> page=userRepo.findAll(pageable);

        List<User> listUsers=page.getContent();
        listUsers.forEach(user -> System.out.println(user));

        assertThat(listUsers.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUsers(){
        String keyword="bruce";

        int pageNumber=0;
        int pageSize=4;

        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Page<User> page=userRepo.findAll(keyword, pageable);

        List<User> listUsers=page.getContent();
        listUsers.forEach(user -> System.out.println(user));

        assertThat(listUsers.size()).isGreaterThan(0);
    }
}
