/**
 * Copyright (c) 2024, Arc-i-Tech. All rights reserved.
 * visit www.arc-i-tech.in
 *
 */
package com.arcitech.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

/**
 * @author Ajay G
 * 
 */
@Entity
public class User extends PersonalDetails {

	@Column(unique = true, nullable = false)
	private String username;
	private LocalDate createdAt;

	private boolean enabled;
	
	@OneToOne(mappedBy = "user")
    private UserAuth userAuth;

    @OneToOne(mappedBy = "user")
    private ForgotPasswordToken forgotPasswordToken;
    
	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDate.now();
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the createdAt
	 */
	public LocalDate getCreatedAt() {
		return createdAt;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public UserAuth getUserAuth() {
		return userAuth;
	}

	public void setUserAuth(UserAuth userAuth) {
		this.userAuth = userAuth;
	}

	public ForgotPasswordToken getForgotPasswordToken() {
		return forgotPasswordToken;
	}

	public void setForgotPasswordToken(ForgotPasswordToken forgotPasswordToken) {
		this.forgotPasswordToken = forgotPasswordToken;
	}
}
