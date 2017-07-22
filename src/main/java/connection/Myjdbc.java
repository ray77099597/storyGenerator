package connection;

import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Driver;
import com.mysql.jdbc.Statement;

import generator.Parameter;

public class Myjdbc {
	
	public static Connection connect(){
		Connection con = null;
		try {
			Driver d = new com.mysql.jdbc.Driver();
			Properties p = new Properties();
			p.put("user", Parameter.jdbcUser);
			p.put("password", Parameter.jdbcPasd);
			con = (Connection) d.connect("jdbc:mysql://"+Parameter.jdbcIp+":3306", p);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return con;
		
	}
	public static void disconnect(Connection con){
		if(con!=null)
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public static Statement createStatement(Connection con){
		Statement stm = null;
		try {
			stm = (Statement) con.createStatement();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return stm;
	}
	public static void closeStatement(Statement stm){
		if(stm!=null)
			try {
				stm.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	}
}
