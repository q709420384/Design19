package com.sf.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sf.dao.LoginDao;
import com.sf.db.Data_jdbcTemplate;
import com.sf.entity.ordertableEntity;
import com.sf.entity.userEntity;
import com.sf.tool.C3P0Util;

@Component
public class LoginDaoimpl implements LoginDao {

	@Autowired
	Data_jdbcTemplate jdbcTemplate;
	
	public int login(String userName,String passWord) {
		
		String  sql="select count(*) from user where userName = ? and passWord = ?";
			QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
			
			List<userEntity> userEntity = null;
			try {
				userEntity = qr.query(sql, new BeanListHandler<userEntity>(userEntity.class),userName,passWord);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			int num=userEntity.size();
			return num;
	}

	public int paypassword(String userName,String pass) {
			String sql = "select count(*) from user where userName=? and userPaypassword=?";
			QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
			
			List<userEntity> userEntity = null;
			try {
				userEntity = qr.query(sql, new BeanListHandler<userEntity>(userEntity.class),userName,pass);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			int num=userEntity.size();
			return num;
	}

	public int updateRMB(String userRMB, String userName) {
		String update="update user set userRMB=? where userName=?";
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		
		int num = 0;
		try {
			num = qr.update(update, userRMB,userName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}

	public int ordertable(ordertableEntity ordertable) {
		String sql = "insert into ordertable (OrderID,OrderuserName,OrderIgridsName,OrdergridsImg,OrderzongRMB,OrderStat,OrderTime,OrderAddr) values(?,?,?,?,?,?,?,?)";
		
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		
		int num = 0;
		try {
			num =  qr.update(sql, ordertable.orderID,ordertable.orderuserName,ordertable.orderIgridsName,ordertable.orderzongRMB,ordertable.orderStat,ordertable.orderTime,ordertable.orderAddr);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
		
		
		
	}

	
	//待修复
	public int Confirmorder(String orderID) {
		int b = 0;
		//根据ID查询出是否存在这个订单 是否为发货状态
		String sql = "select count(*) from ordertable where OrderID = ? and OrderStat = 2";//是否有这个商品 并且是代发货状态
		int num = jdbcTemplate.getJdbcTemplate().queryForObject(sql, new Object[]{orderID},Integer.class);
			if(num>0){
				//有此订单.. 代发货状态
				String sql2 = "update  ordertable set OrderStat=4 where OrderID=?";//更改为收货
						b= jdbcTemplate.getJdbcTemplate().update(sql2,new Object[]{orderID});
			}else{
				b=0;//查不到此订单
			}
		return b;
		
	}

	public int Cancellationoforder(String orderID) {
		String sql2 = "update  ordertable set OrderStat=3 where OrderID=?";//更改为待取消订单
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		int num = 0;
		try {
			num =  qr.update(sql2, orderID);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}
	public int delordertable(String orderID) {
		String sql2 = "update  ordertable set OrderStat=5 where OrderID=?";//更改为待取消订单
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		int num = 0;
		try {
			num =  qr.update(sql2, orderID);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}
	public int Delivergoods(String OrderStat,String orderID) {
		String sql2 = "update  ordertable set OrderStat=? where OrderID=?";//立即发货
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		int num = 0;
		try {
			num =  qr.update(sql2, orderID,orderID);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}

}
