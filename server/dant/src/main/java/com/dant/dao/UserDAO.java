package com.dant.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.mariadb.jdbc.internal.util.dao.QueryException;

import com.dant.business.SessionManager;
import com.dant.entity.User;
import com.dant.exception.UserNotFoundException;
import com.dant.util.CryptoUtil;

public class UserDAO {

	private final Connection connection = Init.getJDBC();
	private SessionManager sm = new SessionManager();

	public void createUser(User u) throws SQLException {
		String sql="{call CreateUser(0x" + u.getKey() + ",?,?,?, 0x" + CryptoUtil.encrypt(u.getPassword()) + ")}";

		try (CallableStatement call = connection.prepareCall(sql)) { 
			//e.g. passage de la chaîne userId comme valeur du premier paramètre 
			call.setString(1,u.getFname()); 
			call.setString(2,u.getLname()); 
			call.setString(3,u.getEmail()); 
			call.execute();

		}
	}

	public String getUserByCredentials(String email, String password) throws SQLException, QueryException {
		String sql="{call getUserByCredentials(?,0x" + CryptoUtil.encrypt(password)  + ")}";
		try (CallableStatement call = connection.prepareCall(sql)) {
			call.setString(1,email);
			call.registerOutParameter(1, Types.BINARY);
			if(call.execute()){ 
				//Tout va bien
				ResultSet rs = call.getResultSet();
				if(rs.next()){
					return rs.getString("key");
				}
				else{
				System.out.println("mauvais id");}
			}
			else{
				//Tout va mal
				throw new QueryException("Non");
			}
		}
		return null;
	}

	public User getUserById(String id) throws UserNotFoundException, SQLException{
		String sql="Select * from users where `key`=x'" + id +"'" ;
		System.out.println(sql);
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery(sql)) {
				if(rs.next()){
					return new User(rs.getString("fname"),rs.getString("lname"),rs.getString("email"),rs.getString("password"));
				}
			}
		}
		return null;
	
		
		
		
		
//		String sql="{call getUserById(?)}";
//		//String sql="select key from users;";
//		String str="";
//		System.out.println("avant try");
//		try (CallableStatement call = connection.prepareCall(sql)) {
//			System.out.println("apres try");
//			 call.setBytes(1,id.getBytes());
//			//call.setClob(1,("0x"+id).getBytes());
//			
////			System.out.println(call.getNString(1));
////			System.out.println(sql);
////			System.out.println(id);
//			if(call.execute()){ 
//				ResultSet rs = (ResultSet)call.getObject(1);
//				while(rs.next()){
//					
//					if (0 < str.length()) {
//						str += ",";
//					}
//					System.out.println(rs.getString("fname")+rs.getString("lname")+rs.getString("email")+rs.getString("password"));
//					str += new User(rs.getString("fname"),rs.getString("lname"),rs.getString("email"),rs.getString("password")).toJson();
//					System.out.println("str "+ str);
//					return str;
//				}
//
//				System.out.println(str);
//				return "[" + str + "]";
//			}
//		}
//		return str;

	}

	public String searchUser(String search_str, int offset_pos, int limit_l, boolean in_bool_mode) throws SQLException{

		String str="";
		User u1 = new User("toto","lepabo","a@b.cr","password");
		User u2 = new User("tatata","lep","d@b.cr","password");
		System.out.println("Users created");
		User[] usrs = new User[2];
		usrs[0] = u1;
		usrs[1] = u2;
		for(int i = 0 ; i<usrs.length; i++){
			if (0 < str.length()) {
				str += ",";
			}
			str += usrs[i].toJson();
		}


		return "[" + str + "]";
	}




	//		String sql="{call SearchUser(?,?,?,?)}";
	//		try (CallableStatement call = connection.prepareCall(sql);) { 
	//			call.setString(1, search_str);
	//			call.setInt(2, offset_pos);
	//			call.setInt(3, limit_l);
	//			call.setBoolean(4, in_bool_mode);
	//			if(call.execute()){ 
	//				//Tout va bien
	//				//Boucle json form�e depuis les users renvoy�s
	//				ResultSet rs = (ResultSet)call.getObject(1);
	//			
	//						
	//						while(rs.next()){
	//							if (0 < str.length()) {
	//								str += ",";
	//							}
	//							
	//							str += new User(rs.getString("fname"),rs.getString("lname"),rs.getString("email"),rs.getString("password")).toJson();
	//						}
	//						
	//						
	//						return "[" + str + "]";
	//					}
	//					else{
	//						
	//						throw new SQLException();
	//					}
	//	}

	public boolean keyAlreadyExists(String key) throws SQLException{
		String sql="Select 1 from users where `key`=0x"+key;
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			try (ResultSet req = ps.executeQuery(sql)) {
				return req.next();
			}
		}
	}

	public boolean emailAlreadyExists(String email) throws SQLException{
		String sql="Select 1 from users where `email`= '" + email + "'";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			try (ResultSet req = ps.executeQuery(sql)) {
				return req.next();
			}
		}
	}

	public void updateUser(String id, String fname, String lname, String email, String password) throws SQLException {
		String sql="UPDATE users set fname=\""+fname+"\",lname=\""+lname+"\",email=\""+email+"\",password=0x"+CryptoUtil.encrypt(password)+" where `key`=0x"+id;
		System.out.println(sql);
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			try (ResultSet req = ps.executeQuery(sql)) {
				System.out.println("Update done. uKey:0x"+id);
			}
		}
		//Requete mise à jour

		//throw new QueryException();
	}

}
