package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.Gson;
import java.sql.*;

@WebServlet("/Login")
public class Login extends HttpServlet {
	
	private static Connection conn = null;

	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = request.getParameter("username");
		String password = request.getParameter("password");
		
		System.out.println(user);
		System.out.println(password);
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		PrintWriter out = response.getWriter();

		connect("jdbc:mysql://localhost/GROCERY_SCHEMA?user=root&password=root");

		response.setStatus(200); 
		response.setContentType("application/json");

		// Set CORS headers
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Headers", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST, DELETE");

		try {
			ps = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
			ps.setString(1, user);
			ps.setString(2, password);
			rs = ps.executeQuery();
			if (rs.next()) {
				// Authentication success
				System.out.println("Login success for " + user);
				out.println(new Gson().toJson(user));
			} else {
				// Authentication fail
				System.out.println("Could not login");
				out.println("null");
			}
			
			rs.close();
			ps.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void connect(String url) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
