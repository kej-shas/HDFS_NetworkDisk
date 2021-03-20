package com.pan.hadoop.controller;

import com.pan.hadoop.model.HdfsDAO;
import com.pan.hadoop.model.UserBean;
import com.pan.hadoop.model.UserBeanCl;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.mapred.JobConf;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String passwords = request.getParameter("passwords");
        String email = request.getParameter("email");
        if (password.equals(passwords)){
            UserBean user=new UserBean();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            if(!new UserBeanCl().register(user)) {
                //用户不合法，调回界面，并提示错误信息
                response.setContentType("text/html; charset=utf-8");
                response.getWriter().println("<script type=\"text/javascript\">alert(\"用户存在\");window.history.go(-1);</script>");
                return;
            }
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("currentPath", HdfsDAO.getHdfs()+"/"+username);
            JobConf conf = HdfsDAO.config();
            HdfsDAO hdfs = new HdfsDAO(conf);
            hdfs.mkdirs("/"+username);
            FileStatus[] list = hdfs.ls("/"+username);
            request.setAttribute("list",list);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }else{
            //用户不合法，调回界面，并提示错误信息
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().println("<script type=\"text/javascript\">alert(\"密码输入不一致\");window.history.go(-1);</script>");

        }
    }
}
