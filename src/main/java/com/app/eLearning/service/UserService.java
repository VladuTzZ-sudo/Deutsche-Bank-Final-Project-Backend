package com.app.eLearning.service;

import com.app.eLearning.dao.User;
import com.app.eLearning.dao.UserRole;
import com.app.eLearning.dto.LoginDTO;
import com.app.eLearning.dto.RegisterDTO;
import com.app.eLearning.exceptions.*;
import com.app.eLearning.repository.RoleRepository;
import com.app.eLearning.repository.UserRepository;
import com.app.eLearning.utils.BCryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserService
{

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;


	public Pair<User, String> loginUser(LoginDTO loginDTO) throws EmailTooShortException, EmailTooLongException,
			PasswordTooShortException, PasswordTooLongException,
			UnmatchedLoginCredentials, UserInactiveException
	{
		validateLoginDTO(loginDTO);

		User foundUser;

		try
		{
			foundUser = userRepository.findByEmail(loginDTO.getEmail()).get(0);
			BCryptUtils.checkEncryptedPassword(loginDTO.getPassword(), foundUser.getPassword());
		}
		catch (Exception e)
		{
			throw new UnmatchedLoginCredentials();
		}

		if (foundUser == null)
			throw new UnmatchedLoginCredentials();

		//check if user is active, if not throws exception
		if (foundUser.getActive() == null)
		{
			throw new UserInactiveException();
		}

		//daca userul este gasit si activ, ar trebui sa aiba rol
		if (foundUser != null)
		{
			Pair<User, String> foundUserAndRoleName = Pair.of(foundUser, roleRepository.findFirstById(foundUser.getUserRole().getRoleId()).getName());

			return foundUserAndRoleName;
		}
		else
		{
			throw new UnmatchedLoginCredentials();
		}

	}

	public boolean registerUser(RegisterDTO registerDTO) throws EmailTooLongException,
			PasswordTooShortException, EmailAlreadyRegisteredException,
			PasswordTooLongException, EmailTooShortException, InvalidEmailFormatException
	{

		validateRegisterDTO(registerDTO);

		User newUser = new User(registerDTO.getName(), registerDTO.getSurname(), registerDTO.getEmail(),
				BCryptUtils.hashPassword(registerDTO.getPassword()));
		newUser.setUserRole(new UserRole(roleRepository.findFirstByName(registerDTO.getRole()).getId()));

		try
		{
			userRepository.save(newUser);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

	}

	public boolean patternMatches(String emailAddress, String regexPattern)
	{
		return Pattern.compile(regexPattern)
				.matcher(emailAddress)
				.matches();
	}

	private void validateRegisterDTO(RegisterDTO registerDTO) throws EmailTooShortException, EmailTooLongException, PasswordTooShortException, PasswordTooLongException, EmailAlreadyRegisteredException, InvalidEmailFormatException
	{

		String email = registerDTO.getEmail();
		String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
				+ "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

		if (!patternMatches(email, regexPattern))
		{
			throw new InvalidEmailFormatException();
		}
		if (registerDTO.getEmail().length() < 6)
		{
			throw new EmailTooShortException();
		}
		if (registerDTO.getEmail().length() > 40)
		{
			throw new EmailTooLongException();
		}
		if (registerDTO.getPassword().length() < 5)
		{
			throw new PasswordTooShortException();
		}
		if (registerDTO.getPassword().length() > 20)
		{
			throw new PasswordTooLongException();
		}

		if (userRepository.findByEmail(registerDTO.getEmail()).size() > 0)
		{
			throw new EmailAlreadyRegisteredException();
		}
	}

	private void validateLoginDTO(LoginDTO loginDTO) throws EmailTooShortException, EmailTooLongException, PasswordTooShortException, PasswordTooLongException
	{
		if (loginDTO.getEmail().length() < 6)
		{
			throw new EmailTooShortException();
		}
		if (loginDTO.getEmail().length() > 40)
		{
			throw new EmailTooLongException();
		}
		if (loginDTO.getPassword().length() < 5)
		{
			throw new PasswordTooShortException();
		}
		if (loginDTO.getPassword().length() > 20)
		{
			throw new PasswordTooLongException();
		}
	}

}
