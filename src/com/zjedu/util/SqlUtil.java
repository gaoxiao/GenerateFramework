package com.zjedu.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import com.zjedu.dao.DBMeta;

public class SqlUtil {

	public static void main(String[] args) {
		List<DBMeta> metas = getMetadata("info");
		for (DBMeta dbMeta : metas) {
			System.out.println(dbMeta);
		}
	}

	public static List<DBMeta> getMetadata(String tableName) {
		// 驱动
		String driver = "com.mysql.jdbc.Driver";
		// 数据库连接
		String url = PropertyUtil.getProperty("jdbcUrl");
		// 用户名
		String user = PropertyUtil.getProperty("user");
		// 数据库密码
		String password = PropertyUtil.getProperty("password");
		// 加载驱动

		List<DBMeta> metas = new Vector<DBMeta>();
		try {
			Class.forName(driver);
			// 获取链接
			Connection connection;
			connection = DriverManager.getConnection(url, user, password);

			// 创建查询声明
			PreparedStatement preparedStatement = connection
					.prepareStatement("select * from " + tableName + " limit 1");
			// 获取结果
			ResultSet resultSet = preparedStatement.executeQuery();
			// 获取各个列的信息
			ResultSetMetaData metaData = resultSet.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				// 填充
				DBMeta dbMeta = new DBMeta();
				dbMeta.colName = metaData.getColumnName(i);
				String className = metaData.getColumnClassName(i);
				dbMeta.colType = className
						.substring(className.lastIndexOf(".") + 1);
				metas.add(dbMeta);
			}
			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return metas;
	}
}
