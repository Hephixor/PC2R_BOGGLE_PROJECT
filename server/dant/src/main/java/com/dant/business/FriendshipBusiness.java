package com.dant.business;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import com.dant.dao.FriendshipDAO;
import com.dant.exception.UserNotFoundException;
import com.dant.util.KeyGeneratorUtil;

public class FriendshipBusiness {
	private FriendshipDAO friendshipDAO = new FriendshipDAO();
	private SessionManager sm = new SessionManager();
	
	public String listFriends(String id) throws SQLException, UserNotFoundException, UnsupportedEncodingException{
		return friendshipDAO.listFriends(getUser(id));
	}
	
	public boolean getFriendship(String idA, String idB) throws SQLException{
		return friendshipDAO.getFriendship(idA, idB);
	}
	
	public void requestFriendship(String idA, String idB) throws SQLException{
		friendshipDAO.setFriendship(KeyGeneratorUtil.generateKey(4), getUser(idA), idB);
	}
	
	public void deleteFriend(String id, String idUser) throws SQLException{
		friendshipDAO.deleteFriendship(id, idUser);
	}
	
	public String getUser(String sessionId){
		return sm.getUserKey(sessionId);
	}
}
