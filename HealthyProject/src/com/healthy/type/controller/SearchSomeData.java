package com.healthy.type.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.healthy.entity.KeepFit;
import com.healthy.type.service.TypeService;

/**
 * Servlet implementation class SearchOneTable
 */
@WebServlet("/SearchSomeData")
public class SearchSomeData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchSomeData() {
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
		is.close();
		JSONObject object=new JSONObject(str);
		String tableName=object.getString("tableName");
		String search=object.getString("search");
		
		List<KeepFit> lists=new ArrayList<>();
		String message=new String();
		if("cookbook".equals(tableName)){
			
		}else if("medicine_chest".equals(tableName)){
			
		}else{
			TypeService typeService=new TypeService();
			lists=typeService.displaySomeKeepFitService(tableName, search);
			if(lists.size()==0){
				message="not";
			}else{
				Gson gson=new Gson();
				message=gson.toJson(lists);
			}
		}
		response.getWriter().append(message);
	}

}
