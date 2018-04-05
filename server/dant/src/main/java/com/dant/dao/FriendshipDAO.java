package com.dant.dao;

import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dant.entity.User;
import com.dant.exception.UserNotFoundException;
import com.dant.util.CryptoUtil;

public class FriendshipDAO {

	private final Connection connection = Init.getJDBC();

	public void setFriendship(String rkey, String akey, String bkey) throws SQLException{
		String sql="INSERT INTO friends values (0x"+rkey+",0x"+akey+",0x"+bkey+",b'1')";
		System.out.println(sql);
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			try (ResultSet req = ps.executeQuery(sql)) {
				System.out.println("Request done");
			}
		}



		//		String sql="{call SetFriendship(?,?,?)}";
		//		try (CallableStatement call = connection.prepareCall(sql)) { 
		//			String rkeyTmp = "x";
		//			call.setString(1, rkey); 
		//			call.setObject(2, akey);
		//			call.setObject(3, "0x"+bkey);
		//
		//			if(call.execute()){ 
		//				//Tout va bien
		//			}
		//			else{
		//				throw new SQLException();
		//			}
		//		}
	}

	public void deleteFriendship(String akey, String bkey) throws SQLException{
		String sql="DELETE FROM friends where `user_a`=0x"+akey+" AND `user_b`=0x"+bkey;
		System.out.println(sql);
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			try (ResultSet req = ps.executeQuery(sql)) {
				System.out.println("Friendship deleted.");
			}
		}
		
		
		
		
//		String sql="{call DeleteFriendship(?,?)}";
//		try (CallableStatement call = connection.prepareCall(sql)) {
//			call.setString(1, akey);
//			call.setString(2, bkey);
//
//			if(call.execute()){ 
//			}
//			else{
//				throw new SQLException();
//			}
//		}
	}

	public boolean getFriendship(String akey, String bkey) throws SQLException{
		String sql="SELECT COUNT(1) as nf FROM friends where `user_a`=0x"+akey+" AND `user_b`=0x"+bkey;

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			try (ResultSet req = ps.executeQuery(sql)) {
				if(req.next()){

				}

				if(Integer.parseInt((String) String.valueOf(req.getObject("nf")))>0){
					return true;
				}
				else{
					return false;
				}
			}
		}



		//		String sql="{call GetFriendship(?,?)}";
		//		try (CallableStatement call = connection.prepareCall(sql)) { 
		//			call.setString(1, akey);
		//			call.setString(2, bkey);
		//
		//			if(call.execute()){ 
		//				ResultSet rs = (ResultSet)call.getObject(1);
		//				if(rs.next()){
		//					return true;
		//				}
		//				else{
		//					return false;
		//				}
		//			}
		//			else{
		//				return false;
		//			}
		//		}
	}

	public String listFriends(String id) throws SQLException, UserNotFoundException, UnsupportedEncodingException{
		String sql="SELECT HEX(`user_b`) FROM friends where `user_a`=0x"+id;
		System.out.println(sql);
		String str="";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			try (ResultSet req = ps.executeQuery(sql)) {
				while(req.next()){
					if (0 < str.length()) {
						str += ",";
					}
					//TODO convert string to binary
					str += "\""+req.getNString("user_b")+"\"";


				}
				return str;
			}
		}




		//		String sql = "{call ListFriends("+id+")}";
		//
		//		String str = null;
		//		try (PreparedStatement ps = connection.prepareStatement(sql)) {
		//			try (ResultSet rs = ps.executeQuery(sql)) {
		//				if(rs.next()){
		//					while(rs.next()){
		//						//str+=rs.getString("fname"),rs.getString("lname"),rs.getString("email"),rs.getString("password");
		//					}
		//					return str;
		//				}
		//				else{
		//					throw new UserNotFoundException();
		//				}
		//			}
		//		}

	}


}
