package com.healthy.collection.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.healthy.collection.dao.CollectionDao;
import com.healthy.collection.service.CollectionService;
import com.healthy.entity.Collection;

/**
 * Servlet implementation class SixCollection
 */
@WebServlet("/SixCollection")
public class SixCollection extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SixCollection() {
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
		Integer operation=object.getInt("operation");
		Integer userId=object.getInt("userId");
		String tableName=object.getString("tableName");
		Integer tableId=object.getInt("tableId");
		CollectionService collectionService=new CollectionService();
		int row=collectionService.sixCollectionService(operation,userId, tableName, tableId);
		if(row==0){
			response.getWriter().append("no");
		}else{
			response.getWriter().append("ok");
		}
	}
}
