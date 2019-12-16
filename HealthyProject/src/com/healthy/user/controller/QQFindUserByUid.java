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

/**
 * Servlet implementation class QQFindUserByUid
 */
@WebServlet("/QQFindUserByUid")
public class QQFindUserByUid extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QQFindUserByUid() {
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
		StreamUtil util = new StreamUtil();
		UserService service = new UserService();
		InputStream is = request.getInputStream();
		String gson = util.readInputStreamToString(is);
		User loginUser = new Gson().fromJson(gson, User.class);
		String phoneNum = loginUser.getPhoneNumber();
		User user = service.findUserByTele(phoneNum);
		is.close();
		if(user!=null) {
			response.getWriter().append(user.getId()+"");
		}else {
			int id = service.registerQQuser(loginUser);
			response.getWriter().append(id+"");
		}
	}

}
