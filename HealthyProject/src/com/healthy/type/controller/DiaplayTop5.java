package com.healthy.type.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.healthy.entity.CookBook;
import com.healthy.type.service.TypeService;

/**
 * Servlet implementation class DiaplayTop5
 */
@WebServlet("/DiaplayTop5")
public class DiaplayTop5 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DiaplayTop5() {
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
		
		TypeService typeService=new TypeService();
		List<CookBook> lists=typeService.displayTop5Service();
		if(null==lists){
			response.getWriter().append("not");
		}else{
			Gson gson=new Gson();
			String message=gson.toJson(lists);
			response.getWriter().append(message);
		}
	}

}
