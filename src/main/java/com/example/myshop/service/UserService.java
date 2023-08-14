package com.example.myshop.service;

import com.example.myshop.entity.Role;
import com.example.myshop.entity.User;
import com.example.myshop.exceptions.UserNotFoundException;
import com.example.myshop.repository.RoleRepository;
import com.example.myshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {
    public static final int USERS_PER_PAGE=4;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public User getByEmail(String email) {
    	return userRepo.getUserByEmail(email);
    }

    public List<User> listAll(){
        return (List<User>) userRepo.findAll(Sort.by("firstName").ascending());
    }

    public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword){
        Sort sort=Sort.by(sortField);
        sort= sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable= PageRequest.of(pageNum-1,USERS_PER_PAGE, sort);

        if(keyword!=null){
            return userRepo.findAll(keyword,pageable);
        }
        return userRepo.findAll(pageable);
    }
    /*
    //метод для отображения постранично, заменяем на с возможностью сортировки
    public Page<User> listByPage(int pageNum){
        Pageable pageable= PageRequest.of(pageNum-1,USERS_PER_PAGE);
        return userRepo.findAll(pageable);
    }
     */

    public List<Role> listRoles(){
        return (List<Role>) roleRepo.findAll();
    }

    public User save(User user) {
        boolean isUpdatingUSer=(user.getId()!=null);

        if (isUpdatingUSer){
            User existingUser=userRepo.findById(user.getId()).get();

            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            }else {
                encodePassword(user);
            }
        }else {
            encodePassword(user);
        }


        return userRepo.save(user);
    }

    private void encodePassword(User user){
        String encodedPassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id,String email){
        User userByEmail=userRepo.getUserByEmail(email);
        if (userByEmail==null) return true;
        boolean isCreatingNew=(id==null);
        if (isCreatingNew){
            if (userByEmail !=null) return false;
        } else {
            if (userByEmail.getId() !=id){
                return false;
            }
        }
        return true;
    }

    public User getUserById(int id) throws UserNotFoundException {
        try {
            return userRepo.findById(id).get();
        }catch (NoSuchElementException e){
            throw new UserNotFoundException("Could not find any user with id= "+id);
        }
    }

    public void delete(Integer id) throws UserNotFoundException {
        Long countById=userRepo.countById(id);
        if (countById == null || countById==0){
            throw new UserNotFoundException("Could not find any user with id= "+id);
        }
        userRepo.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id,boolean enabled){
        userRepo.updateEnabledStatus(id,enabled);
    }
    
    public User updateAccount(User userInfo) {
    	User userInDB = userRepo.findById(userInfo.getId()).get();
    	
    	if (!userInfo.getPassword().isEmpty()) {
    		userInDB.setPassword(userInfo.getPassword());
    		encodePassword(userInDB);
    	}
    	
    	if (userInfo.getPhotos() != null)
    		userInDB.setPhotos(userInfo.getPhotos());
    	
    	userInDB.setFirstName(userInfo.getFirstName());
    	userInDB.setLastName(userInfo.getLastName());
    	
    	return userRepo.save(userInDB);
    }
}
