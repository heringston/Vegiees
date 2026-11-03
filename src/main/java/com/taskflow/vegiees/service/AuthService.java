package com.taskflow.vegiees.service;

import com.taskflow.vegiees.dto.LoginRequest;
import com.taskflow.vegiees.dto.LoginResponse;
import com.taskflow.vegiees.dto.RegisterRequest;
import com.taskflow.vegiees.dto.RegisterResponse;
import com.taskflow.vegiees.entity.User;
import com.taskflow.vegiees.exception.EmailAlreadyExistsException;
import com.taskflow.vegiees.exception.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.taskflow.vegiees.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository , PasswordEncoder passwordEncoder , JwtService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public boolean emailExists(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public RegisterResponse register(RegisterRequest request){
        if(emailExists(request.getEmail()))
            throw new EmailAlreadyExistsException("Email already exists");

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        RegisterResponse response = new RegisterResponse();

        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
        response.setMessage("Registration Successful");

        return response;

    }

    public LoginResponse login(LoginRequest request){

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid Email or Password"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException("Invalid Email or Password");
        }

        String token = jwtService.generateToken(user.getEmail());

        LoginResponse response = new LoginResponse();

        response.setToken(token);
        response.setEmail(user.getEmail());
        response.setId(user.getId());
        response.setName(user.getName());
        response.setMessage("Login Successful");

        return response;
    }
}
