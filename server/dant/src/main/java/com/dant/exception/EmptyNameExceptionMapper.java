package com.dant.exception;

import javax.ws.rs.core.Response;

public class EmptyNameExceptionMapper {
	
	public Response toResponse(EmptyNameException e) {
		return Response.status(200).type("application/json").entity("{\"c\":1}").build();
	}
}
