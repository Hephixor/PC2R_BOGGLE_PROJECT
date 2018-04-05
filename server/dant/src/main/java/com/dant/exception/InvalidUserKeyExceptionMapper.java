package com.dant.exception;

import javax.ws.rs.core.Response;

public class InvalidUserKeyExceptionMapper {

	public Response toResponse(InvalidUserKeyException e) {
		return Response.status(404).type("application/json").entity("{\"c\":6}").build();
	}
	
	
}
