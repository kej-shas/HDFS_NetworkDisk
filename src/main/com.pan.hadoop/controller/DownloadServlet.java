package com.pan.hadoop.controller;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pan.hadoop.model.HdfsDAO;
import com.pan.hadoop.tools.Encoding;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.mapred.JobConf;

/**
 * Servlet implementation class DownloadServlet
 */
public class DownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		ServletContext context = getServletContext();
		String local = context.getInitParameter("file-download");
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		String filePath = new String(request.getParameter("filePath").getBytes(StandardCharsets.ISO_8859_1),"utf-8");
		System.out.println(Encoding.getEncoding(request.getParameter("filePath")));
		JobConf conf = HdfsDAO.config();
		HdfsDAO hdfs = new HdfsDAO(conf);
		local=local+filePath.replace(HdfsDAO.getHdfs(),"");
		hdfs.download(filePath, local);

		File file = new File(local);
		if(!file.exists()) {
			response.getWriter().print("您要下载的文件不存在！");
		}else{
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/octet-stream");
			//解决 以文件形式下载 而不会被浏览器打开    以及中文文件名需要编码
			response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(file.getName(), "utf-8"));
			ServletOutputStream os = response.getOutputStream();
			BufferedInputStream bis = null;

			bis = new BufferedInputStream(new FileInputStream(file));

			byte[] buf = new byte[1024 * 8];
			int len = -1;
			while ((len = bis.read(buf)) != -1) {
				os.write(buf, 0, len);
			}
			bis.close();
			os.close();
		}


		FileStatus[] list = hdfs.ls((String)session.getAttribute("currentPath"));
		request.setAttribute("list", list);
		request.getRequestDispatcher("index.jsp").forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

}
