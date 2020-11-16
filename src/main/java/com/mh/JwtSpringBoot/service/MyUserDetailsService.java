package com.mh.JwtSpringBoot.service;


import com.mh.JwtSpringBoot.dao.UserRepo;
import com.mh.JwtSpringBoot.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepo myUserRepo;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

       Optional<Users> user = myUserRepo.findByUserName(s);
       System.out.println("user -> " + user.get().getUserName());

       user.orElseThrow(() -> new UsernameNotFoundException("Not found" + s));

     //  return new User("foo", "foo",
      //         new ArrayList<>());

       return user.map(MyUserDetails::new).get();


    }
}