/**
 * Copyright (c) 2024, Arc-i-Tech. All rights reserved.
 * visit www.arc-i-tech.in
 *
 */
package com.arcitech.controllers;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.arcitech.exception.UserNotFoundException;
import com.arcitech.helper.SiteURL;
import com.arcitech.model.ForgotPasswordToken;
import com.arcitech.model.UserAuth;
import com.arcitech.service.ForgotPasswordService;

import net.bytebuddy.utility.RandomString;

/**
 * @author AJ
 * 
 */
@Controller
public class ForgotPasswordController {

	@Autowired
	private ForgotPasswordService forgotPasswordService;

	@Autowired
	JavaMailSender mailSender;

	@PostMapping("/forgot_password/{email}/{username}")
	public ResponseEntity<String> processForgotPassword(@PathVariable("email") String email,
			@PathVariable("username") String username, HttpServletRequest request) {
		try {
			String token = RandomString.make(45);
			forgotPasswordService.updateRestPasswordToken(token, username);

			String resetPasswordLink = SiteURL.getSiteURL(request) + "/reset_password?token=" + token + "&password=";

			sendEmail(email, resetPasswordLink);

			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("Failed to process forgot password request", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public void sendEmail(String email, String resetPasswordLink)
			throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom("support@arc-i-tech.in", "Arc-i-Tech Support");
		helper.setTo(email);
		String subject = "Here's the link to reset your password";
		String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
				+ "<p>Click the link below to change your password:</p>" + "<p><b><a href=\"" + resetPasswordLink
				+ "\">Change my Password</a></b></p>"
				+ "<p>Ignore this email if you did not request a password reset.</p>";
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}

	@GetMapping("/reset_password/{token}")
	public ResponseEntity<String> resetPasswordProcess(@PathVariable("token") String token) {
		ForgotPasswordToken tokens = forgotPasswordService.validateToken(token);

		if (tokens!=null &&token.isEmpty()) {
			return new ResponseEntity<>("pasword change success", HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>("pasword not change ", HttpStatus.BAD_GATEWAY);
		}

	}

	@GetMapping("/reset_password")
	public ResponseEntity<String> resetPasswordProcess(HttpServletRequest request) {
		try {
			String token = request.getParameter("token");
			String password = request.getParameter("password");

			if (token == null || token.isEmpty() || password == null || password.isEmpty()) {
				return new ResponseEntity<>("Token and password must be provided.", HttpStatus.BAD_REQUEST);
			}

			ForgotPasswordToken forgotPasswordToken = forgotPasswordService.validateToken(token);

		
			forgotPasswordService.updatePassword(forgotPasswordToken, password);

			return new ResponseEntity<>("Password has been reset successfully.", HttpStatus.OK);

		} catch (UserNotFoundException e) {
			return new ResponseEntity<>("Invalid token or user not found: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>("An error occurred while resetting the password.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
