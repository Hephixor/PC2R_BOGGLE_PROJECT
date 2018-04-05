/*package com.dant.dao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.codec.binary.Hex;

import com.dant.entity.User;
import com.dant.util.KeyGeneratorUtil;

public class JDBCCalls {
	static Connection conn=null;
	static Statement stmt=null;
	ResultSet req = null;

	public JDBCCalls(){
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			conn = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/imap_contacts", "root","root");
			System.out.println("Connexion effectuee");
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//			ResultSet req = stmt.executeQuery("Select * from users");
	//			while(req.next()){
	//				int id = req.getInt(1);
	//				String nom = req.getString(2);
	//				double prix = req.getDouble(3);
	//				java.sql.Date date = req.getDate(4);



	public static void CreateUser(User u) throws SQLException, NoSuchAlgorithmException{
		String sql="{call CreateUser(0x" + u.getKey() + ",?,?,?, 0x" + Hex.encodeHexString(MessageDigest.getInstance("SHA-256").digest(u.getPassword().getBytes(StandardCharsets.UTF_8))) + ")}";
		CallableStatement call = conn.prepareCall(sql);
		//e.g. passage de la chaîne userId comme valeur du premier paramètre
		call.setString(1,u.getFname());
		call.setString(2,u.getLname());
		call.setString(3,u.getEmail());
		call.execute();

		if (1 != call.getUpdateCount()) {
			System.out.println("UpdateCount : " + call.getUpdateCount());

		}
		System.out.println("UpdateCount : " + call.getUpdateCount());
	}

	public static void getUserByCredentials(String email, String password) throws SQLException, NoSuchAlgorithmException{
		String sql="{call getUserByCredentials(?,0x" + Hex.encodeHexString(MessageDigest.getInstance("SHA-256").digest(password.getBytes(StandardCharsets.UTF_8))) + ")}";
		CallableStatement call = conn.prepareCall(sql);
		call.setString(1,email);
		if(call.execute()){
			//Tout va bien
		}
		else{
			//Tout va mal
		}
	}

	public static void getUserById(String id) throws SQLException{
		boolean isHexa=true;
		if(id.length()==8){
			for(int i=0; i<8;i++){
				if(Character.digit(id.charAt(i),16)==-1)
					isHexa=false;
					break;
				//pas hexa
			}
			if(isHexa){
				String sql="{call getUserById(?)}";
				CallableStatement call = conn.prepareCall(sql);
				call.setString(1,id);
				if(call.execute()){
					//Tout va bien
				}
				else{
					//Tout va mal
				}
			}
			else{//pas une clé en hexa
			}
		}

		else{
			//pas une clé valide
		}
	}

	public static void setFriendship(String rkey, String akey, String bkey) throws SQLException{
		String sql="{call SetFriendship(?,?,?)}";
		CallableStatement call = conn.prepareCall(sql);
		call.setString(1, KeyGeneratorUtil.generateKey(4));
		call.setString(2, akey);
		call.setString(3, bkey);

		if(call.execute()){
			//Tout va bien
		}
		else{
			//Tout va mal
		}
	}

	public static void DeleteFriendship(String akey, String bkey) throws SQLException{
		String sql="{call DeleteFriendship(?,?)}";
		CallableStatement call = conn.prepareCall(sql);
		call.setString(1, akey);
		call.setString(2, bkey);

		if(call.execute()){
			//Tout va bien
		}
		else{
			//Tout va mal
		}
	}

	public static void GetFriendship(String akey, String bkey) throws SQLException{
		String sql="{call GetFriendship(?,?)}";
		CallableStatement call = conn.prepareCall(sql);
		call.setString(1, akey);
		call.setString(2, bkey);

		if(call.execute()){
			//Tout va bien
		}
		else{
			//Tout va mal
		}
	}

	public static void SearchUser(String search_str, int offset_pos, int limit_l, boolean in_bool_mode) throws SQLException{
		String sql="{call SearchUser(?,?,?,?)}";
		CallableStatement call = conn.prepareCall(sql);
		call.setString(1, search_str);
		call.setInt(2, offset_pos);
		call.setInt(3, limit_l);
		call.setBoolean(4, in_bool_mode);
		if(call.execute()){
			//Tout va bien
		}
		else{
			//Tout va mal
		}
	}

	public static boolean keyAlreadyExists(String key) throws SQLException{
		String sql="Select 1 from users where `key`=0x"+key;
		ResultSet req = stmt.executeQuery(sql);
		return req.next();
	}

	public static boolean emailAlreadyExists(String email) throws SQLException{
		String sql="Select 1 from users where `email`= '" + email + "'";
		ResultSet req = stmt.executeQuery(sql);
		return req.next();
	}




} */
