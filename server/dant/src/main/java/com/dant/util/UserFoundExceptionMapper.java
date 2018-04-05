package com.dant.util;

import javax.ws.rs.core.Response;

import com.dant.entity.User;
import com.dant.exception.UserFoundException;
import com.dant.filter.GsonProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserFoundExceptionMapper {
	
	public Response toResponse(User u) {
		return Response.status(200).type("application/json").entity(u.toJson()).build();
	}
}
