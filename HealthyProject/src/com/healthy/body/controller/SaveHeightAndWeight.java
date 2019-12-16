package com.healthy.body.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

import org.json.JSONObject;

import com.healthy.body.service.BodyService;
import com.healthy.entity.User;
import com.healthy.util.StreamUtil.StreamUtil;

/**
 * Servlet implementation class SaveHeightAndWeight
 */
@WebServlet("/SaveHeightAndWeight")
public class SaveHeightAndWeight extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveHeightAndWeight() {
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
		BodyService service = new BodyService();
		StreamUtil util = new StreamUtil();
		Object session = request.getSession().getAttribute("user");
		InputStream is = request.getInputStream();
		
		JSONObject str = new JSONObject(util.readInputStreamToString(is));
		double height = str.getDouble("height");
		double weight = str.getDouble("weight");
		String userTel = str.getString("userTel");
		double bim = Double.parseDouble(str.getString("bim"));
		service.fixBodyData(height, weight, bim, userTel);
		response.getWriter().append("ok");
		is.close();
	}
}




