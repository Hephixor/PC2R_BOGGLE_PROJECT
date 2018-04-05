package com.dant.app;

import java.sql.SQLException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/api")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)

public class API {

	// ------ Collection /u/
	// --- GET
	// -- /u/?search
	// -- /u/search?&n

	// -- /u/{idUser}


	// -- /u/me
	@GET
	@Path("/u/me")
	public String listMetaData(){

		// Si connecté, on récupère les méta-données, puis HTTP 200 OK
		// Sinon, HTTP 404 Not Found
		//Session s = Session.getSess(...)
		//if (s == NULL) {
		// 404

		//	}
		return null;
	}

	// --- POST
	// - /u/

	// - /u/me/
	@POST
	@Path("/u/me")
	public String updateUser(
			@DefaultValue("Null") @FormParam("fname") String fname,
			@DefaultValue("Null") @FormParam("lname") String lname,
			@DefaultValue("Null") @FormParam("email") String email,
			@DefaultValue("Null") @FormParam("password") String password
			)
	{
		// Si connecté, alors on met à jour ses données.
		/*
		 * @DefaultValue("Null") @FormParam("fname") String fname,
			@DefaultValue("Null") @FormParam("lname") String lname,
			@DefaultValue("Null") @FormParam("email") String email,
			@DefaultValue("Null") @FormParam("password") String password
		 * */

		// Sinon, on le connecte.
		/*
			@DefaultValue("Null") @FormParam("email") String email,
			@DefaultValue("Null") @FormParam("password") String password
		 * */

		return "";
	}

	// ------ Collection /friends/
	// --- GET
	// - /friends
	@GET
	@Path("/friends")
	public void listFriends(){
		//Lister les amis de l'utilisateur connecté
	}


	// - /friends/{idUser}
	@GET
	@Path("/friends/{idUser}")
	public boolean isFriendWith(@PathParam("idUser") String idUser){
		//Retourne un boolean selon la relation en l'utilisateur connecté et idUser
		return false;
	}

	// --- POST
	// - /friends/{idUser}
	@POST
	@Path("/friends/{idUser}")
	public void requestFriendship(@PathParam("idUser") String idUser){
		//Faire requete amitié
	}

	// --- DELETE
	// - /friends/{idUser}
	@DELETE
	@Path("/friends/{iduser}")
	public void deleteFriend(@PathParam("idUser") String iduser){
		//Delete friendship
	}

	// ------ Collection /pos/
	// --- GET
	// - /pos/
	@GET
	@Path("/pos")
	public String getPosition(){
		//Récuperer position utilisateur
		return null;
	}

	// - /pos/friends
	@GET
	@Path("/pos/friends")
	public String getFriendsPositions(){
		//Récuperer les positions des amis de l'utilisateurs
		return null;
	}

	// - /pos/friends/{userFriendId}
	@GET
	@Path("/pos/friends/{n}")
	public String getFriendsPositionsP(@PathParam("n") int page){

		return null;
	}

	// - /pos/{idUser}
	@GET
	@Path("/pos/{idUser}")
	public String getFriendPosition(@PathParam("idUser") String id) throws SQLException{
		//JDBCCalls.getUserById(id);
		return null;
	}

	// --- POST
	// - /pos
	@POST
	@Path("/pos")
	public void updatePosition(){
		//Regle la nouvelle position du user à sa position actuelle
	}





}
