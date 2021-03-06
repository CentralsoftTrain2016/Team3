package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import domain.valueo.MailEnvironmentEnum;
import util.PropertiesUtil;

public abstract class Dao
{
	 protected Connection con;

	 public Dao(Connection con) {
		super();
		this.con = con;
	}

	public static Connection getConnection() throws ClassNotFoundException, SQLException
	 {
		String user = "train2016";
		String pass = "train2016";

		//	サーバー環境かクライアントかを取得
		MailEnvironmentEnum  deploedEnv = PropertiesUtil.isServer();
		String servername = deploedEnv.getServerName();

		String sid = "xe";

		Class.forName("oracle.jdbc.driver.OracleDriver");

		Connection c = DriverManager.getConnection(
					"jdbc:oracle:thin:@" + servername + ":1521:"
							+ sid,user,pass);
		return c;
	 }

}
