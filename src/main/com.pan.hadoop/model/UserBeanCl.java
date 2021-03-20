package com.pan.hadoop.model;

import com.pan.hadoop.model.ConnDB;

import java.sql.*;

/**
 * 用户身份合法性验证模型
 * @author elon@elon33.com
 *
 */
public class UserBeanCl {
	private Statement sm = null;
	private Connection ct = null;
	private ResultSet rs = null;

	/**
	 * 检查登录用户是否合法
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            用户密码
	 * @return validate 用户登陆是否合法
	 */
	public boolean checkUser(String username, String password) {
		boolean val = false;
		try {

			// 获得连接
			ct = new ConnDB().getConn();
			// 创建statement
			sm = ct.createStatement();

			rs = sm.executeQuery("select * from user where username=\"" + username + "\"");

			if (rs.next()) {
				// 说明用户存在
				String pwd = rs.getString(4);
				if (password.equals(pwd)) {
					// 说明密码正确
					val = true;
				} else {
					val = false;
				}
			} else {
				val = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close();
		}

		return val;
	}
	public boolean register(UserBean user){
		ct = new ConnDB().getConn();

		String sql="INSERT  INTO  user(username,password,email) VALUES (?,?,?)";
		try {
			sm = ct.createStatement();
			rs = sm.executeQuery("select * from user where username=\"" + user.getUsername() + "\"");
			if (rs.next()) {
				return false;
			}
			PreparedStatement ps = ct.prepareStatement(sql);
			ps.setString(1,user.getUsername());
			ps.setString(2,user.getPassword());
			ps.setString(3,user.getEmail());
			int flag=ps.executeUpdate();
			if (flag==1){
				return true;
			}
		} catch (SQLException throwables) {
			return false;
		}

		return  false;
	}

	/**
	 * 连接关闭
	 */
	public void close() {
		try {

			if (sm != null) {
				sm.close();
				sm = null;
			}

			if (ct != null) {
				ct.close();
				ct = null;
			}

			if (rs != null) {
				rs.close();
				rs = null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
