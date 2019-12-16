package com.healthy.user.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.healthy.entity.User;
import com.healthy.user.service.UserService;
import com.healthy.util.StreamUtil.StreamUtil;
import com.mysql.jdbc.StreamingNotifiable;

/**
 * Servlet implementation class RegisterUser
 */
@WebServlet("/RegisterUser")
public class RegisterUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		UserService service = new UserService();
		StreamUtil util = new StreamUtil();
		InputStream is = request.getInputStream();
		String string = util.readInputStreamToString(is);
		JSONObject json = new JSONObject(string);
		User user = service.registerUser(json.getString("pwd"), json.getString("tel"));
		if(user!=null) {
			response.getWriter().append(new Gson().toJson(user));
		}else {
			
		}
		response.getWriter().append("no");
	
	}

}
