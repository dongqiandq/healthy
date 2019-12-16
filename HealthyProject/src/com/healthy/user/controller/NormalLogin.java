package com.healthy.user.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.healthy.user.service.UserService;

/**
 * Servlet implementation class NormalLogin
 */
@WebServlet("/NormalLogin")
public class NormalLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public NormalLogin() {
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

		InputStream is=request.getInputStream();
		byte[] buffer=new byte[256];
		int len=is.read(buffer);
		String str=new String(buffer,0,len);
		JSONObject object=new JSONObject(str);
		String phone=object.getString("phone");
		String pwd=object.getString("pwd");
		
		UserService userService=new UserService();
		String isSuccess=userService.normalLoginService(phone, pwd);
		is.close();
		
		response.getWriter().append(isSuccess);
	}

}
