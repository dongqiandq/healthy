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
 * Servlet implementation class UpdateUserName
 */
@WebServlet("/UpdateUserName")
public class UpdateUserName extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateUserName() {
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
		
		InputStream is=request.getInputStream();
		byte[] buffer=new byte[256];
		int len=is.read(buffer);
		String str=new String(buffer,0,len);
		JSONObject object=new JSONObject(str);
		Integer id=object.getInt("id");
		String userName=object.getString("userName");
		
		UserService userService=new UserService();
		int row=userService.updateUserNameService(id, userName);
		if(row==0){
			response.getWriter().append("not");
		}else{
			response.getWriter().append("ok");
		}
		is.close();
	}

}
