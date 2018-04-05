package com.dant.dao; 

import net.spy.memcached.MemcachedClient;

public class MemcacheDAO {

	private final MemcachedClient memcachedClient = Init.getMemcache();



//	public <T> T get(String key) throws UserNotFoundException {
//		if((T) memcachedClient.get(key)==null){
//			throw new UserNotFoundException();
//		}
//		else{
//			return (T) memcachedClient.get(key);
//		}
//
//	}
	

	
	
	// UserBySession
	public void createUserBySessionKey(String sessionId, String ukey) {
		memcachedClient.set(sessionId, 360000, ukey);
		
	}
	
	public String getUserBySessionKey(String sessionId){
		//System.out.println((String) memcachedClient.get(sessionId));
		return (String) memcachedClient.get(sessionId);
	}
	
	public void dropUserBySessionKey(String sessionId){
		memcachedClient.delete(sessionId);
	}
	
	//Session by user
	public void createSessionByUserKey(String ukey, String sessionData){
		memcachedClient.set(ukey, 360000, sessionData);
	}
	
	public String getSessionByUserKey(String ukey){
		return (String) memcachedClient.get(ukey);
		
	}
	
	public void dropSessionByUserKey(String ukey){
		memcachedClient.delete(ukey);
	}
	
	//Overall
	public void updateSessionPosition(String sessionId, String lat, String longi){
		String ukey = getUserBySessionKey(sessionId);
		String data = ""+sessionId+";"+lat+";"+longi+";"+System.currentTimeMillis();
		System.out.println("longi: "+longi);
		memcachedClient.set(ukey,360000,data);
		memcachedClient.set(sessionId, 360000, ukey);
		
	}


	
//	public void updateSessionByUserKey(String ukey, Session ob){
//		dropSessionByUserKey(ukey);
//		createSessionByUserKey(ukey, ob);
//	}



}
