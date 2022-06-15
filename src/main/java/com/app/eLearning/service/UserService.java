package com.app.eLearning.service;

import com.app.eLearning.dao.User;
import com.app.eLearning.dao.UserRole;
import com.app.eLearning.dto.LoginDTO;
import com.app.eLearning.dto.RegisterDTO;
import com.app.eLearning.exceptions.*;
import com.app.eLearning.repository.RoleRepository;
import com.app.eLearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    public Pair<User, String> loginUser(LoginDTO loginDTO) throws EmailTooShortException, EmailTooLongException, PasswordTooShortException, PasswordTooLongException, UnmatchedLoginCredentials, UserInactiveException {
        validateLoginDTO(loginDTO);

        User foundUser;

        try {
            foundUser = userRepository.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword()).get(0);
        } catch (Exception e) {
            throw new UnmatchedLoginCredentials();
        }

        //check if user is active, if not throws exception
        if (foundUser.getActive() == null) {
            throw new UserInactiveException();
        }

        //daca userul este gasit si activ, ar trebui sa aiba rol
        if (foundUser != null) {
            Pair<User, String> foundUserAndRoleName = Pair.of(foundUser, roleRepository.findFirstById(foundUser.getUserRole().getRoleId()).getName());

            return foundUserAndRoleName;
        } else {
            throw new UnmatchedLoginCredentials();
        }

    }

    public boolean registerUser(RegisterDTO registerDTO) throws EmailTooLongException, PasswordTooShortException, EmailAlreadyRegisteredException, PasswordTooLongException, EmailTooShortException {

        validateRegisterDTO(registerDTO);

        User newUser = new User(registerDTO.getName(), registerDTO.getSurname(), registerDTO.getEmail(), registerDTO.getPassword());
        newUser.setUserRole(new UserRole(roleRepository.findFirstByName(registerDTO.getRole()).getId()));

        try {
            userRepository.save(newUser);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private void validateRegisterDTO(RegisterDTO registerDTO) throws EmailTooShortException, EmailTooLongException, PasswordTooShortException, PasswordTooLongException, EmailAlreadyRegisteredException {
        if (registerDTO.getEmail().length() < 6) {
            throw new EmailTooShortException();
        }
        if (registerDTO.getEmail().length() > 40) {
            throw new EmailTooLongException();
        }
        if (registerDTO.getPassword().length() < 5) {
            throw new PasswordTooShortException();
        }
        if (registerDTO.getPassword().length() > 20) {
            throw new PasswordTooLongException();
        }

        if (userRepository.findByEmail(registerDTO.getEmail()).size() > 0) {
            throw new EmailAlreadyRegisteredException();
        }
    }

    private void validateLoginDTO(LoginDTO loginDTO) throws EmailTooShortException, EmailTooLongException, PasswordTooShortException, PasswordTooLongException {
        if (loginDTO.getEmail().length() < 6) {
            throw new EmailTooShortException();
        }
        if (loginDTO.getEmail().length() > 40) {
            throw new EmailTooLongException();
        }
        if (loginDTO.getPassword().length() < 5) {
            throw new PasswordTooShortException();
        }
        if (loginDTO.getPassword().length() > 20) {
            throw new PasswordTooLongException();
        }
    }
}
