package com.app.eLearning.utils;

import com.app.eLearning.exceptions.UnmatchedLoginCredentials;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptUtils
{
	public static String hashPassword(String plainPw){
		return BCrypt.hashpw(plainPw, BCrypt.gensalt());
	}

	public static boolean checkEncryptedPassword(String plainPw, String encryptedPw) throws UnmatchedLoginCredentials
	{
		if(!BCrypt.checkpw(plainPw, encryptedPw))
			throw new UnmatchedLoginCredentials();
		return true;
	}

}
