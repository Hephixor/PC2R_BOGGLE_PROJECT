package com.dant.business;

import com.dant.dao.MemcacheDAO;
import com.dant.entity.Session;

public class SessionManager {

	private MemcacheDAO dao = new MemcacheDAO();

//	public static Session getSession(String idUser) {
//		//Session session = new Session(idUser,(String)dao.get(idUser));
//	//	return session;
//
//	}

	public void setSession(Session s){

		//dao.updateSessionByUserKey(s.getIdUser(), s);
	}

	public void storeSession(Session s){
		dao.createSessionByUserKey(s.getIdUser(), s.toString());
	}

	public void storeUserSession(String ukey, String sessionId){
		dao.createUserBySessionKey(sessionId, ukey);
	}

	public void dropSession(String ukey){
		dao.dropSessionByUserKey(ukey);
	}

	public String getSession(String ukey){
		return dao.getSessionByUserKey(ukey);
	}

	public String getUserKey(String sessionId){
		return dao.getUserBySessionKey(sessionId);
	}

//	public void testTranscoder(Object str){
//		SerializingTranscoder transcoder = new SerializingTranscoder();
//		System.out.println(transcoder.encode(str));
//		System.out.println(transcoder.toString());
//	}

}
