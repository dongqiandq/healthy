package com.healthy.community.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.healthy.community.service.CommunityService;
import com.healthy.entity.Message;
import com.healthy.entity.User;
import com.healthy.user.service.UserService;
import com.healthy.util.StreamUtil.StreamUtil;

/**
 * Servlet implementation class AddQuestion
 */
@WebServlet("/AddQuestion")
public class AddQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddQuestion() {
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
		CommunityService service = new CommunityService();
		InputStream is = request.getInputStream();
		String gson = util.readInputStreamToString(is);
		User loginUser = new Gson().fromJson(gson, User.class);
		Message message = new Gson().fromJson(gson, Message.class);
		int result = service.addQuestion(message);
		if(result>0) {
			response.getWriter().append("ok");
		}else {
			response.getWriter().append("no");
		}
	
	
	}

}
