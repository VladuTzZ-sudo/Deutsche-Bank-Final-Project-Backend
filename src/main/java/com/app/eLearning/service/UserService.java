package com.app.eLearning.service;

import com.app.eLearning.dao.User;
import com.app.eLearning.dto.LoginDTO;
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
            Pair<User, String> foundUserAndRoleName = Pair.of(foundUser, roleRepository.findFirstById(foundUser.getId()).getName());

            return foundUserAndRoleName;
        } else {
            throw new UnmatchedLoginCredentials();
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
