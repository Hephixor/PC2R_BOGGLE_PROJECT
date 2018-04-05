package com.dant.exception;

import javax.ws.rs.core.Response;

public class InvalidEmailExceptionMapper {


	public Response toResponse(InvalidEmailException e) {
		return Response.status(200).type("application/json").entity("{\"c\":5}").build();
	}
	
}
