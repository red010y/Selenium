package MysqlConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Vector;

//import com.ifeng.spider.util.Config;

public class MysqlDAOFactory {

	public static Vector<Connection> pool;
	private String mysql_driver_className;
	private String mysql_username;
	private String mysql_password;
	private String mysql_url;
	private int mysql_pool_size=5;
	private static MysqlDAOFactory instance = null;

	public static synchronized MysqlDAOFactory getInstance() {
		if (instance == null) {
			instance = new MysqlDAOFactory();
		}
		return instance;
	}
	
	public MysqlDAOFactory() {
		initConn();
	}

	private void initConn() {
		if (pool == null) {
			readConfig();
			pool = new Vector<Connection>(mysql_pool_size);
			addConnection();
		}
	}
	
	private void readConfig() {
//		this.mysql_driver_className = Config.mysql_driver_className;
//		this.mysql_username = Config.mysql_username;
//		this.mysql_password = Config.mysql_password;
//		this.mysql_url = Config.mysql_url;
//		this.mysql_pool_size = Config.mysql_pool_size;
		this.mysql_driver_className = "com.mysql.jdbc.Driver";
		this.mysql_username ="pmop";
		this.mysql_password ="ZStsZA2ysRQvL82t";
		this.mysql_url ="jdbc:mysql://local.db.cmpp.ifengidc.com:3309/pmopDB";
	}
	
	private void addConnection() {
		for (int i = 0; i < mysql_pool_size; i++) {
			Connection connection = null;
			try {
				Class.forName(this.mysql_driver_className);
				connection = DriverManager.getConnection(this.mysql_url, this.mysql_username, this.mysql_password);
			} catch (Exception e) {
				e.printStackTrace();
			}
			pool.add(connection);
		}
	}

	public synchronized Connection getConnection() {
		if (pool.size() > 0) {
			Connection connection = pool.get(0);
			pool.remove(connection);
			return connection;
		} else {
			return null;
		}
	}

	public synchronized void release(Connection connection) {
		pool.add(connection);
	}


}
