package com.dant.controller;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.dant.business.PositionBusiness;
import com.dant.exception.SQLExceptionMapper;
import com.dant.exception.UserNotFoundException;
import com.dant.exception.UserNotFoundExceptionMapper;


@Path("/pos")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public class PositionController {

	private PositionBusiness positionBusiness = new PositionBusiness();

	@POST
	public Response updatePosition(@DefaultValue("") @HeaderParam("x-token") String sessionId,@DefaultValue("") @HeaderParam("x-lat") String lat,@DefaultValue("") @HeaderParam("x-longi") String longi){
		if(positionBusiness.getUser(sessionId).length()==0){
			return Response.status(404).build();
		}
		else{		
			positionBusiness.updatePosition(sessionId,lat,longi);
			return Response.status(200).type("application/json").entity("{\"c\":0}").build();
			//Regle la nouvelle position du user à sa position actuelle
		}
	}

	@GET
	public Response getPosition(@DefaultValue("") @HeaderParam("x-token") String sessionId){
		try {
			String str = positionBusiness.getFriendPosition(sessionId);
			return Response.status(200).type("application/json").entity("{\"c\":0,\"data\":"+str+"}").build();
		} catch (SQLException e) {
			SQLExceptionMapper sem = new SQLExceptionMapper();
			return sem.toResponse(e);
		}
		
		//Récuperer position utilisateur

	}

	// - /pos/friends
	@GET
	@Path("/friends")
	public Response getFriendsPositions(@DefaultValue("") @HeaderParam("x-token") String sessionId){
		try {
			String str = positionBusiness.getFriendsPositionsP(0,sessionId);
			return Response.status(200).type("application/json").entity("{\"c\":0,\"data\":"+str+"}").build();
		} catch (SQLException e) {
			SQLExceptionMapper sem = new SQLExceptionMapper();
			return sem.toResponse(e);
		} catch (UserNotFoundException e) {
			UserNotFoundExceptionMapper unfem = new UserNotFoundExceptionMapper();
			return unfem.toResponse(e);
		}
		//Récuperer les positions des amis de l'utilisateurs
	
	}

	// - /pos/friends/{userFriendId}
	@GET
	@Path("/friends/{n}")
	public Response getFriendsPositionsP(@PathParam("n") int page, @DefaultValue("") @HeaderParam("x-token") String id){
		try {
			String str = positionBusiness.getFriendsPositionsP(page, id);
			return Response.status(200).type("application/json").entity("{\"c\":0,\"data\":"+str+"}").build();
		} catch (SQLException e) {
			SQLExceptionMapper sem = new SQLExceptionMapper();
			return sem.toResponse(e);
		} catch (UserNotFoundException e) {
			UserNotFoundExceptionMapper unfem = new UserNotFoundExceptionMapper();
			return unfem.toResponse(e);
		}
	}

	// - /pos/{idUser}
	@GET
	@Path("/{idUser}")
	public Response getFriendPosition(@PathParam("idUser") String id) throws SQLException{
		String str = positionBusiness.getFriendPosition(id);
		return Response.status(200).type("application/json").entity("{\"c\":0,\"data\":"+str+"}").build();
	}

	// --- POST
	// - /pos


}
