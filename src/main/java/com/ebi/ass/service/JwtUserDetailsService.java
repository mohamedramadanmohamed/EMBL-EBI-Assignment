package com.ebi.ass.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author MohamedRamadan
 *
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Value("${jwt.username}")
	private String sysUsername;
	@Value("${jwt.password}")
	private String sysPassword;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (sysUsername.equals(username)) {
			return new User(sysUsername, "$2y$12$GwQ/XU7kR9BIfOzgXtoHfu/rdCq7sMljj1lKWdGDai75vvjCgmbFO",
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}

}