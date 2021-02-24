import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class ExtractClob {
	public static void main(String[] args)
	throws Exception {
		InputStream propsIn = new FileInputStream(args[0]);
		Properties props = new Properties();
		props.load(propsIn);
		propsIn.close();

		String jdbcUrl  = props.getProperty("jdbcUrl");
		String username = props.getProperty("username");
		String password = props.getProperty("password");

		Class.forName("oracle.jdbc.OracleDriver");
		Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(props.getProperty("sql"));

		while (rs.next()) {
			Clob clob = rs.getClob(1);
			StringBuilder str = new StringBuilder();
			for (int i = 1; i <= clob.length(); i += 1024) {
				str.append(clob.getSubString(i, 1024));
			}

			System.out.println(str);
			System.out.println("----");
		}

		rs.close();
		stmt.close();
	}
}