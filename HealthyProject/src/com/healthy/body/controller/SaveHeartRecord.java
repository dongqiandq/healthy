package com.healthy.body.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.healthy.body.service.BodyService;
import com.healthy.entity.HeartRecord;
import com.healthy.entity.User;
import com.healthy.util.StreamUtil.StreamUtil;

/**
 * Servlet implementation class SaveHeartRecord
 */
@WebServlet("/SaveHeartRecord")
public class SaveHeartRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveHeartRecord() {
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
//		Object session = request.getSession().getAttribute("user");
		InputStream is = request.getInputStream();
//		if(session==null){
//			response.getWriter().append("nologin");
//		}else {
			Gson gson = new Gson();
			HeartRecord record = gson.fromJson(util.readInputStreamToString(is), HeartRecord.class);
			record.setTime(new Timestamp(new Date().getTime()));
			if(service.saveHeartRecord(record)) {
				response.getWriter().append("ok");
			}else {
				response.getWriter().append("no");
//			}

		}
		
	}

}




