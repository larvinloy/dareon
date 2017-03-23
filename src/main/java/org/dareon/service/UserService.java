package org.dareon.service;

import org.dareon.domain.User;

public interface UserService {

	public User findByEmail(String email);
	
}
