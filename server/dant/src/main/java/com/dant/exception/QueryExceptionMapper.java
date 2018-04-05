package com.dant.exception;

import javax.ws.rs.core.Response;

import org.mariadb.jdbc.internal.util.dao.QueryException;

public class QueryExceptionMapper {

	public Response toResponse(QueryException e) {
		return Response.status(404).type("application/json").entity("{\"c\":9}").build();
	}

}
