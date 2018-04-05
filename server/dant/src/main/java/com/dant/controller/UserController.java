package com.dant.controller;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.mariadb.jdbc.internal.util.dao.QueryException;

import com.dant.business.UserBusiness;
import com.dant.entity.User;
import com.dant.exception.EmailException;
import com.dant.exception.EmailExceptionMapper;
import com.dant.exception.EmptyEmailException;
import com.dant.exception.EmptyEmailExceptionMapper;
import com.dant.exception.EmptyNameException;
import com.dant.exception.EmptyNameExceptionMapper;
import com.dant.exception.EmptyPasswordException;
import com.dant.exception.EmptyPasswordExceptionMapper;
import com.dant.exception.HexadecimalException;
import com.dant.exception.HexadecimalExceptionMapper;
import com.dant.exception.InvalidEmailException;
import com.dant.exception.InvalidEmailExceptionMapper;
import com.dant.exception.InvalidTokenException;
import com.dant.exception.InvalidTokenExceptionMapper;
import com.dant.exception.InvalidUserKeyException;
import com.dant.exception.InvalidUserKeyExceptionMapper;
import com.dant.exception.QueryExceptionMapper;
import com.dant.exception.SQLExceptionMapper;
import com.dant.exception.UserFoundException;
import com.dant.exception.UserNotFoundException;
import com.dant.exception.UserNotFoundExceptionMapper;


@Path("/u")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)

public class UserController {

	private UserBusiness userBusiness = new UserBusiness();

	@POST
	public Response createUser(
			@DefaultValue("") @FormParam("fname") String fname,
			@DefaultValue("") @FormParam("lname") String lname,
			@DefaultValue("") @FormParam("email") String email,
			@DefaultValue("") @FormParam("password") String password)	{

		try {
			String sessionId = userBusiness.createUser(fname,lname,email,password);

			return Response.status(200).type("application/json").entity("{\"c\":0, \"data\":\""+sessionId+"\"}").build();
		}
		catch (EmptyNameException e) {
			EmptyNameExceptionMapper enem = new EmptyNameExceptionMapper();
			return enem.toResponse(e);
		}

		catch (EmailException e) {
			EmailExceptionMapper eem = new EmailExceptionMapper();
			return eem.toResponse(e);
		}

		catch(EmptyEmailException e){
			EmptyEmailExceptionMapper eeem = new EmptyEmailExceptionMapper();
			return eeem.toResponse(e);
		}

		catch(EmptyPasswordException e){
			EmptyPasswordExceptionMapper epem = new EmptyPasswordExceptionMapper();
			return epem.toResponse(e);
		}

		catch(InvalidEmailException e){
			InvalidEmailExceptionMapper ieem = new InvalidEmailExceptionMapper();
			return ieem.toResponse(e);
		}

		catch(SQLException e){
			SQLExceptionMapper sem = new SQLExceptionMapper();
			return sem.toResponse(e);
		}

	}


	@GET
	public Response searchP(@DefaultValue("") @QueryParam("search") String query, @DefaultValue("0") @QueryParam("n") int page) throws SQLException, UserFoundException{
		String users=null;
		
		try {
			users=userBusiness.searchUser(query, page);
			return Response.status(200).type("application/json").entity("{\"c\":0,\"data\":"+users+"}").build();

		}
		catch (SQLException e) {
			SQLExceptionMapper sem = new SQLExceptionMapper();
			return sem.toResponse(e);
		}


	}


	@Path("/test")
	@GET
	public Response test(){
		return null;
	}


	@Path("/{id}")
	@GET
	public Response listMetaDataForUser(@PathParam("id") String id, @DefaultValue("") @HeaderParam("x-token") String sessionId) {
		User tmp;
		if(id.equals("me")){
			id=userBusiness.getUser(sessionId);
			System.out.println("id: "+id +"sessionId: "+ sessionId);
		}
		try {
			tmp = userBusiness.getUserById(id);
			return Response.status(200).type("application/json").entity("{\"c\":0,\"data\":{\"fname\":\""+tmp.getFname()+"\",\"lname\":\""+tmp.getLname()+"\",\"email\":\""+tmp.getEmail()+"\"}}").build();
		} catch (HexadecimalException e) {
			HexadecimalExceptionMapper hem = new HexadecimalExceptionMapper();
			return hem.toResponse(e);
		} catch (InvalidUserKeyException e) {
			InvalidUserKeyExceptionMapper iukem = new InvalidUserKeyExceptionMapper();
			return iukem.toResponse(e);
		} catch (UserNotFoundException e) {
			UserNotFoundExceptionMapper iukem = new UserNotFoundExceptionMapper();
			return iukem.toResponse(e);
		} catch (SQLException e) {
			SQLExceptionMapper sem = new SQLExceptionMapper();
			return sem.toResponse(e);
		} 


	}


	@Path("/me")
	@POST
	//TODO x-token header
	public Response updateUser(
			@DefaultValue("") @FormParam("fname") String fname,
			@DefaultValue("") @FormParam("lname") String lname,
			@DefaultValue("") @FormParam("email") String email,
			@DefaultValue("") @FormParam("password") String password,
			@DefaultValue("") @HeaderParam("x-token") String sessionId
			)
	{
		try {
			
			String sessionKey = userBusiness.updateUser(sessionId, fname, lname, email, password);
			if(sessionKey==null){
			return Response.status(200).type("application/json").entity("{\"c\":0}").build();
			}
			else{
				return Response.status(200).type("application/json").entity("{\"c\":0, \"data\":\""+sessionKey+"\"}").build();
			}
		}
		catch (QueryException e) {
			QueryExceptionMapper qem = new QueryExceptionMapper();
			return qem.toResponse(e);
		}catch (SQLException e) {
			SQLExceptionMapper sem = new SQLExceptionMapper();
			return sem.toResponse(e);
		}catch (EmptyNameException e) {
			EmptyNameExceptionMapper enem = new EmptyNameExceptionMapper();
			return enem.toResponse(e);
		}
		catch(EmptyEmailException e){
			EmptyEmailExceptionMapper eeem = new EmptyEmailExceptionMapper();
			return eeem.toResponse(e);
		}
		catch(EmptyPasswordException e){
			EmptyPasswordExceptionMapper epem = new EmptyPasswordExceptionMapper();
			return epem.toResponse(e);
		}
		catch(InvalidEmailException e){
			InvalidEmailExceptionMapper ieem = new InvalidEmailExceptionMapper();
			return ieem.toResponse(e);
		} catch (HexadecimalException e) {
			HexadecimalExceptionMapper hem = new HexadecimalExceptionMapper();
			return hem.toResponse(e);
		} catch (InvalidUserKeyException e) {
			InvalidUserKeyExceptionMapper iukem = new InvalidUserKeyExceptionMapper();
			return iukem.toResponse(e);
		} catch (UserNotFoundException e) {
			UserNotFoundExceptionMapper unfem = new UserNotFoundExceptionMapper();
			return unfem.toResponse(e);
		} catch (InvalidTokenException e) {
			InvalidTokenExceptionMapper unfem = new InvalidTokenExceptionMapper();
			return unfem.toResponse(e);
		}
	}
}