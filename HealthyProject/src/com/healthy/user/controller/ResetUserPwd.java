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
import com.healthy.util.StreamUtil.StreamUtil;

/**
 * Servlet implementation class ResetUserPwd
 */
@WebServlet("/ResetUserPwd")
public class ResetUserPwd extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResetUserPwd() {
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
		int i = service.resetUserPwd(json.getString("newPwd"), json.getString("phoneNumber"));
		if(i==1) {
			response.getWriter().append("ok");
		}else {
			response.getWriter().append("no");
		}
	
	
	}

}
