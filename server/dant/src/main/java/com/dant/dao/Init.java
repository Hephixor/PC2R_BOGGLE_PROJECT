package com.dant.dao;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;

import net.spy.memcached.MemcachedClient;


class Init {

	public static Connection getJDBC() {
		return JDBCInit.jdbc;
	}

	public static MemcachedClient getMemcache() {
		return MemcacheInit.mcc;
	}

	private static class JDBCInit {

		private final static Connection jdbc = init();

		private static Connection init() {
			try {
				Class.forName("org.mariadb.jdbc.Driver");
				return DriverManager.getConnection("jdbc:mariadb://address=(host=169.254.47.97)(port=3306)(type=master)/imap_contacts", "imap_contacts", "VVpVk>*ATqoe-(f|a99Wz</7s7anri/[");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		}
	}

	private static class MemcacheInit {

		private final static MemcachedClient mcc = init();

		private static MemcachedClient init() {
			System.out.println("init memcache 2");
			 try {
				return new MemcachedClient(new InetSocketAddress("169.254.47.97", 11211));
			} catch (IOException e) {
				e.printStackTrace();
			}
			 return null;
		}
	}


}
