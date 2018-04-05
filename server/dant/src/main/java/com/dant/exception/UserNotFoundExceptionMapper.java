package com.dant.exception;

import javax.ws.rs.core.Response;

public class UserNotFoundExceptionMapper {

	public Response toResponse(UserNotFoundException e) {
		return Response.status(404).type("application/json").entity("{\"c\":8}").build();
	}
}
