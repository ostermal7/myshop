package com.example.myshop.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.myshop.entity.User;
import com.example.myshop.security.MyShopUserDetails;
import com.example.myshop.service.FileUploadUtil;
import com.example.myshop.service.UserService;

@Controller
public class AccountController {

	@Autowired
	UserService service;
	
	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal MyShopUserDetails loggedUser, Model model) {
		String email = loggedUser.getUsername();
		
		User user = service.getByEmail(email);
		model.addAttribute("user", user);
		
		return "users/account_form";
	}
	
	 @PostMapping("/account/update")
	    public String saveUSer(User user,
	                           RedirectAttributes redirectAttributes,
	                           @AuthenticationPrincipal MyShopUserDetails loggedUser,
	                           @RequestParam("image")MultipartFile multipartFile) throws IOException {
	        if (!multipartFile.isEmpty()){
	            String fileName= StringUtils.cleanPath(multipartFile.getOriginalFilename());
	            user.setPhotos(fileName);
	            User savedUser=service.updateAccount(user);
	            String uploadDir="user-photos/"+savedUser.getId();
	            FileUploadUtil.cleanDir(uploadDir);
	            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
	        } else{
	            if (user.getPhotos().isEmpty())
	                user.setPhotos(null);
	            service.updateAccount(user);
	        }

	        loggedUser.setFirstName(user.getFirstName());
	        loggedUser.setLastName(user.getLastName());
	        //userService.save(user);
	        redirectAttributes.addFlashAttribute("message","Your account detail have been updated");

	        return "redirect:/account";
	    }
}
