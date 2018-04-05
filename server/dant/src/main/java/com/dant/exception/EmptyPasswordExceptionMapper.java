package com.dant.exception;

import javax.ws.rs.core.Response;

public class EmptyPasswordExceptionMapper {

	public Response toResponse(EmptyPasswordException e) {
		return Response.status(200).type("application/json").entity("{\"c\":4}").build();
	}
	
}
