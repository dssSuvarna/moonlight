package com.builderbackend.app.configs.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.models.User;

@Service
public class MyDatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

	//UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
         
        // 1. Load the user from the users table by userId. If not found, throw UsernameNotFoundException.
         User user = userRepository.getReferenceByUserName(userName);
         if(user == null) {
            throw new UsernameNotFoundException(userName);
         }

         // 2. Convert/wrap the user to a UserDetails object and return it.
        return new UserWrapper(user);
    }


}