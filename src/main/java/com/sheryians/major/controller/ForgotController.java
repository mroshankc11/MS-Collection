package com.sheryians.major.controller;

import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sheryians.major.model.User;
import com.sheryians.major.repository.UserRepository;
import com.sheryians.major.service.EmailService;

@Controller
public class ForgotController {
	
	Random random = new Random(1000);
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@RequestMapping("/forgot")
	public String openEmailForm() {
		
		return "forget_email_form";
	}
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email, HttpSession session) {
		
		System.out.println("Email "+email);
		
		//generating 4 digits OTP numbers
		
		
		int otp = random.nextInt(999999);
		
		System.out.println("OTP "+otp);
		
		//code for sending OTP code
		
		
		String subject = "OTP From MS Collections";
		String message = ""
				+"<div style='border:1px solid #e2e2e2; padding:20px;'></div>"
				+"<h1>"
				+"OTP is "
				+"<b>"+otp
				+"</b>"
				+"</h1>";
		String to = email;
		boolean flag = this.emailService.sendEmail(subject, message, to);
		
		if(flag) {
			
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			return "verify_otp";
		}
		else {
			
			
			session.setAttribute("message", "Check your email id");
			return "forgot_email_form";
			
			
			
		}
		
		
	}
	
	//Verify OTP
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp, HttpSession session) {
		
		int myOtp=(int)session.getAttribute("myotp");
		String email=(String)session.getAttribute("email");
		
		if(myOtp==otp) {
			
			//password change form
			
			Optional<User> user = this.userRepository.findUserByEmail(email);
			
			if(user==null) {
				
				//send error message
				session.setAttribute("message", "User doesn't exists with this email");
				return "forgot_email_form";
				
			}
			else {
				
				//change password using a form
			}
			
			return "password_change_form";
		}
		else {
			
			session.setAttribute("message", "You have entered wrong OTP");
			return "verify_otp";
		}
		
		
	}
	
	//change password
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newpassword") String newpassword, HttpSession session) {
		
		//String email=(String)session.getAttribute("email");
		//User user = this.userRepository.getUserByUserName(email);
		//user.setPassword(this.bcrypt.encode(newpassword));
		//this.userRepository.save(user);
		
		return "redirect:/login?change=password changed successfully";
		
	}
	
	
	
	//api to send email
	
//	@RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
//   public ResponseEntity<?> sendEmail(@RequestBody EmailRequest request) {
//		
//		
//   
//		
//		System.out.println(request);
//		boolean result = this.emailservice.sendEmail(request.getSubject(), request.getMessage(), request.getTo());
//		if(result) {
//			
//			return ResponseEntity.ok("Email Send Successfully");
//		}
//		else {
//			
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email not send");
//		}
//	   
//   }
}
