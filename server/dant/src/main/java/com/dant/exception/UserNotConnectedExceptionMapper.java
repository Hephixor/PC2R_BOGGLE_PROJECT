package com.dant.exception;

import javax.ws.rs.core.Response;

public class UserNotConnectedExceptionMapper {
	public Response toResponse(UserNotConnectedException e) {
		return Response.status(404).type("application/json").entity("{\"c\":11}").build();
	}
}
