package com.dant.business;

import java.sql.SQLException;

import com.dant.dao.PositionDAO;
import com.dant.exception.UserNotFoundException;

public class PositionBusiness {

	private PositionDAO positionDAO = new PositionDAO();
	//private MemcacheDAO dao = new MemcacheDAO();
	private SessionManager sm = new SessionManager();
	
	public void updatePosition(String sessionId, String lat, String longi){
		positionDAO.updatePosition(sessionId,lat,longi);
	}
	
	public String getFriendsPositionsP(int page, String sessionId) throws SQLException, UserNotFoundException{
		return positionDAO.getFriendsPositions(page,getUser(sessionId));
	}
	
	public String getFriendPosition(String id) throws SQLException{
		return positionDAO.getPosition(id);
	}
	
	public String getPosition(String sessionId){
		return positionDAO.getPosition(getUser(sessionId));
	}

	public void getFriendsPositions(int i) {
		//positionDAO.getFriendsPositions();
		
	}
	
	public String getSession(String ukey){
		return sm.getSession(ukey);
	}
	
	public String getUser(String sessionId){
		return sm.getUserKey(sessionId);
	}
}
