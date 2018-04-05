package com.dant.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.dant.entity.Session;

@Path("/test")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public class TestController {
	@GET
	public Response createSession() {
		new Session("00000000", "CBBC41DA;0.0;0.0;0");
		new Session("00000000", "CBBC41DA;127.5;59.3;12345789");

		return Response.status(200).type("application/json").entity("{\"c\":0}").build();
	}
}
