package com.healthy.body.controller;

import java.awt.Window.Type;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.healthy.body.service.BodyService;
import com.healthy.entity.HeartRecord;
import com.healthy.entity.User;
import com.healthy.util.StreamUtil.StreamUtil;

/**
 * Servlet implementation class FindHeartData
 */
@WebServlet("/FindHeartData")
public class FindHeartData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FindHeartData() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		BodyService service = new BodyService();
		StreamUtil util = new StreamUtil();
		InputStream is = request.getInputStream();
		String userTel = util.readInputStreamToString(is);
		List<HeartRecord> list = service.findHeartRecordByUserId(userTel);
		response.getWriter().append(new Gson().toJson(list));
	
	}

}
