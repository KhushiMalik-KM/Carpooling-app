package dev;
import java.sql.Connection;
import java.sql.DriverManager;
public class DBConnectionTest {
	public static void main(String[] args) {
		try {
			Class.forName("com.postgresql.jdbc.Driver");
			System.out.println("Connected");
			Connection cn= DriverManager.getConnection("jdbc:postgresql://local host: 5432/rideapp","postgres","khushi");
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		
	}
}


