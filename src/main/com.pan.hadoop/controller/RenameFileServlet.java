package com.pan.hadoop.controller;

import com.pan.hadoop.model.HdfsDAO;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.mapred.JobConf;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RenameFileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        try{
            String username = (String) session.getAttribute("username");
        }catch (Exception e){

        }

        String filePath = new String(request.getParameter("filePath").getBytes("iso8859-1"), "utf-8");
        String newfilePath = new String(request.getParameter("newFilePath").getBytes("iso8859-1"), "utf-8");
        JobConf conf = HdfsDAO.config();
        HdfsDAO hdfs = new HdfsDAO(conf);

        if (!newfilePath.equals("null")&&newfilePath!=null){
            hdfs.rename(filePath,newfilePath);
        }


        // System.out.println("===="+filePath+"====");
        // FileStatus[] list = hdfs.ls("/user/root/");
        String currentPath = (String) session.getAttribute("currentPath");
        FileStatus[] list = hdfs.ls(currentPath);
        request.setAttribute("list", list);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req,resp);
    }
}
