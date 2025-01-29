package com.browxy.wrapper.db.service.impl;

import com.browxy.wrapper.db.repository.GenericRepository;
import com.browxy.wrapper.model.User;

public class UserServiceImpl extends GenericServiceImpl<User, Long> {
	public UserServiceImpl(GenericRepository<User, Long> repository) {
		super(repository);
	}
}
