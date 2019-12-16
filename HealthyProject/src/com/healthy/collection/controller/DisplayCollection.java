package com.healthy.collection.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.healthy.collection.service.CollectionService;
import com.healthy.entity.CookBook;
import com.healthy.entity.KeepFit;
import com.healthy.entity.MedicineChest;

/**
 * Servlet implementation class DisplayCollection
 */
@WebServlet("/DisplayCollection")
public class DisplayCollection extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DisplayCollection() {
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
//		System.out.println("1111111111111");
		InputStream is=request.getInputStream();
		byte[] buffer=new byte[256];
		int len=is.read(buffer);
		String str=new String(buffer,0,len);
		is.close();
		JSONObject object=new JSONObject(str);
		Integer userId=object.getInt("userId");
		String tableName=object.getString("tableName");
		
		CollectionService collectionService=new CollectionService();
		Gson gson=new Gson();
		if("six".equals(tableName)){
			List<KeepFit> lists=collectionService.selectSixCollService(userId);
			String message=gson.toJson(lists);
			response.getWriter().append(message);
		}else if("cookbook".equals(tableName)){
			List<CookBook> cookBooks=collectionService.selectCookBookCollService(userId);
			System.out.println(cookBooks.size());
			String message=gson.toJson(cookBooks);
			response.getWriter().append(message);
		}else if("medicine_chest".equals(tableName)){
			List<MedicineChest> medicineChests=collectionService.selectMedicineChestCollService(userId);
			String message=gson.toJson(medicineChests);
			System.out.println("药箱："+medicineChests.size());
			response.getWriter().append(message);
		}else{
			String message="没有数据";
			response.getWriter().append(message);
		}
	}

}
