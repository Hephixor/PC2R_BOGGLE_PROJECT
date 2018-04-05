package com.dant.exception;

import javax.ws.rs.core.Response;

public class InvalidTokenExceptionMapper {
	public Response toResponse(InvalidTokenException e) {
		return Response.status(200).type("application/json").entity("{\"c\":12}").build();
	}
}
