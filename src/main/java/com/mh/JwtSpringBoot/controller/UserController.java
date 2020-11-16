package com.mh.JwtSpringBoot.controller;

import com.mh.JwtSpringBoot.config.WebSecurityConfig;
import com.mh.JwtSpringBoot.dao.UserRepo;
import com.mh.JwtSpringBoot.model.AuthenticationRequest;
import com.mh.JwtSpringBoot.model.AuthenticationResponse;
import com.mh.JwtSpringBoot.model.Users;
import com.mh.JwtSpringBoot.service.MyUserDetailsService;
import com.mh.JwtSpringBoot.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private UserRepo myUserRepo;

    @Autowired
    private WebSecurityConfig myWebSecurityConfig;

    @GetMapping({"/getData"})
    public String getData(){
        return "Hello jwt";
    }

    @GetMapping({"/hello"})
    public String sayHello(){return "Hello P long";}

    @PostMapping({"/send"})
    public ResponseEntity<Users> saveUser(@RequestBody Users users){
        System.out.println("user name " + users.getUserName());
        Users userobj = new Users();
        userobj.setUserName(users.getUserName());
        userobj.setActive(users.isActive());
        userobj.setPassword(myWebSecurityConfig.passwordEncoder().encode(users.getPassword()));
        userobj.setRoles(users.getRoles());
        Users user = myUserRepo.save(userobj);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }


        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }


}
