package com.vasenin.workcube.services;

import com.vasenin.workcube.domains.ErrorMessage;
import com.vasenin.workcube.domains.User;
import com.vasenin.workcube.misc.ErrorType;
import com.vasenin.workcube.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AuthenticationService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        User user = userRepository.findByNickname(nickname);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getNickname(), user.getPassword(), Collections.singleton(user.getRole())
        );
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    public List<ErrorMessage> checkLogin(String nickname, String password){
        User user = new User();
        User targetUser;
        List<ErrorMessage> errorMessages = new ArrayList<>();
        ErrorMessage message = new ErrorMessage("Неверное имя пользователя или пароль", ErrorType.INVALID_AUTHENTICATION);

        user.setNickname(nickname);
        user.setPassword(password);

        if(!isUserExists(user) || isEmpty(nickname) || isEmpty(password)){
            errorMessages.add(message);
            return errorMessages;
        }


        return errorMessages;
    }

    public List<ErrorMessage> checkRegistration(String nickname, String name, String surname, String email, String password, String repeatPassword){
        User user = new User();

        List<ErrorMessage> errorMessages = new ArrayList<>();

        user.setNickname(nickname);
        user.setEmail(email);
        user.setPassword(password);

        if(isEmpty(name) || isEmpty(surname) || isEmpty(password)){
            ErrorMessage message = new ErrorMessage("Поля должны быть заполнены", ErrorType.FIELD_MUST_BE_FILLED_IN);
            errorMessages.add(message);
        }

        if(isEmpty(nickname) || isUserExists(user) || !isLatin(user.getNickname())){
            ErrorMessage message = new ErrorMessage("Пользователь уже существует или введены некорректные данные", ErrorType.USER_ALREADY_EXIST);
            errorMessages.add(message);
        }

        if(!isEmail(email)){
            ErrorMessage message = new ErrorMessage("Введен некорректный email", ErrorType.INVALID_EMAIL);
            errorMessages.add(message);
        }

        if(isNotEqualPasswords(password, repeatPassword) || isEmpty(password) || isEmpty(repeatPassword)){
            ErrorMessage message = new ErrorMessage("Пароли не совпадают или поля пустые", ErrorType.INVALID_PASSWORD);
            errorMessages.add(message);
        }

        return errorMessages;
    }

    private boolean isEmpty(String value){
        return value.length() == 0;
    }
    private boolean isUserExists(User user) {
        return userRepository.existsByName(user.getName()) || userRepository.existsByEmail(user.getEmail());
    }
    private boolean isNotEqualPasswords(String firstPassword, String secondPassword) {
        return !firstPassword.equals(secondPassword);
    }
    private boolean isEmail(String email) {
        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return email.matches(regexPattern);
    }

    private boolean isLatin(String data) {
        String regex = "\\w+";
        return data.matches(regex);
    }


}
