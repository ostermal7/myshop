package com.example.myshop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.myshop.entity.User;
import com.example.myshop.repository.UserRepository;

public class MyshopUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.getUserByEmail(email);
		
		if(user != null)
			return new MyShopUserDetails(user);
		
		throw new UsernameNotFoundException("Could not find user with email:" + email);
	}
//this class is called when spring security is performing authentication
}
