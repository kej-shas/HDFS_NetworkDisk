package com.pan.hadoop.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pan.hadoop.tools.Encoding;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.mapred.JobConf;

import com.pan.hadoop.model.HdfsDAO;

/**
 * Servlet implementation class DocumentServlet
 */
public class DocumentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		 String filePath = new String(request.getParameter("filePath").getBytes(StandardCharsets.ISO_8859_1),"utf-8");
		 System.out.println("codinh"+ Encoding.getEncoding(filePath));
		 HttpSession session = request.getSession();
		 JobConf conf = HdfsDAO.config();
	     HdfsDAO hdfs = new HdfsDAO(conf);
	     FileStatus[] documentList = hdfs.ls(filePath);
	     request.setAttribute("documentList",documentList);
	     session.setAttribute("currentPath", filePath);
		 request.getRequestDispatcher("document.jsp").forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
