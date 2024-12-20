/**
 * Copyright (c) 2024, Arc-i-Tech. All rights reserved.
 * visit www.arc-i-tech.in
 *
 */
package com.arcitech.service;

import com.arcitech.model.ForgotPasswordToken;

/**
 * @author AJ
 * 
 */

public interface ForgotPasswordService {

	public void updateRestPasswordToken(String token, String email);
	public ForgotPasswordToken validateToken(String token);
	public void   updatePassword(ForgotPasswordToken forgotPasswordToken, String newPassword);
}
