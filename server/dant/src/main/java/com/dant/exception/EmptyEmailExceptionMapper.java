package com.dant.exception;

import javax.ws.rs.core.Response;

public class EmptyEmailExceptionMapper {

	public Response toResponse(EmptyEmailException e) {
		return Response.status(200).type("application/json").entity("{\"c\":3}").build();
	}
	
}
