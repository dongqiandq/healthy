package com.healthy.util.DBUtil;
	import java.util.List;
	import java.util.Map;
	import java.awt.geom.RectangularShape;
	import java.io.InputStream;
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.Properties;
	import java.sql.ResultSetMetaData;
	import java.lang.reflect.Field;

	public class DBUtil {
		private static Properties dProps = new Properties();
		//加载驱动
		static {
			try {
				InputStream is = DBUtil.class.getResourceAsStream("/dbinfo.properties");
				dProps.load(is);
				Class.forName(dProps.getProperty("db.driver"));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//得到连接
		public static Connection getConnection() {
			try {
				return DriverManager.getConnection(
						dProps.getProperty("db.connectUrl"),
						dProps.getProperty("db.user"), 
						dProps.getProperty("db.pwd"));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		//关闭连接
		public static void closeConnection(Connection con,PreparedStatement ps,ResultSet rs) {
			try {
				if(rs!=null)
					rs.close();
				if(ps!=null)
					ps.close();
				if(con!=null)
					con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		/**
		  *实现增删改功能的方法
		 * @param sql是要执行的SQL语句
		 * @param obj是可变参数列表,传入的是SQL语句中占位符所对应的实际值，前后顺序要一致
		 */
		public int fix(String sql,Object...obj) {
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			int fixNum = 0;
			int i;
			try {
				con = DBUtil.getConnection();
				ps = con.prepareStatement(sql);
				for(i=1;i<obj.length+1;i++) {//循环将SQL语句中的占位符设置成传入的值
					ps.setObject(i, obj[i-1]);
				}
				fixNum = ps.executeUpdate();
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				DBUtil.closeConnection(con, ps, rs);
			}
			return fixNum;
		}
		
		/**
		 * 
		     * @Title: getInsertId
		     * @Description: 得到插入数据的id值
		     * @param @param sql
		     * @param @param obj
		     * @param @return 参数
		     * @return int 返回类型
		     * @throws
		 */
		public int getInsertId(String sql,Object...obj) {
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			int id = 0;
			int i;
			try {
				con = DBUtil.getConnection();
				ps = con.prepareStatement(sql);
				for(i=1;i<obj.length+1;i++) {//循环将SQL语句中的占位符设置成传入的值
					ps.setObject(i, obj[i-1]);
				}
				ps.execute();
				ps = con.prepareStatement("select last_insert_id() from user");
				rs = ps.executeQuery();
				if(rs.next()) {
					id = rs.getInt(1);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				DBUtil.closeConnection(con, ps, rs);
			}
			return id;
		}
		

		
		
		/**
		 * 实现查询功能的方法
		 * @param sql是要执行的SQL语句
		 * @param obj是可变参数列表,传入的是SQL语句中占位符所对应的实际值，前后顺序要一致
		 * @return 返回查询到的符合条件的集合,集合的每一项是一个map，存储各列的值
		 */
		public List<Map<String, Object>> select(String sql,Object...obj) {
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			int i;
			try {
				con = DBUtil.getConnection();
				ps = con.prepareStatement(sql);
				for(i=1;i<obj.length;i++) {//循环将SQL语句中的占位符设置成传入的值
					ps.setObject(i, obj[i-1]);//此方法的源代码中会根据第二个参数的具体类型调用不同类型的对应方法
				}
				rs = ps.executeQuery();
				ResultSetMetaData rsm = rs.getMetaData();//得到resultset的一些信息
				while(rs.next()) {
				  Map<String,Object>  map = new HashMap<String, Object>();//每一行记录都是一个map，每个字段都作为这个map的一个键值对
				  for(int j=1;j<rsm.getColumnCount()+1;j++)
				  {//将各个字段作为键值对填入map中
					  String columName = rsm.getColumnName(j);
					  map.put(columName,rs.getString(columName));
				  }
				  list.add(map);
				}
				return list;
				
			}catch (Exception e) {
					e.printStackTrace();
					return null;
				}finally {
					DBUtil.closeConnection(con, ps, rs);
				}
		}
		
		
		
		//查询单个对象
		/**
		 * 本方法的对象的属性值要和表的字段值命名一致
		 * @param id 要查询的对象的Id值（主键）
		 * @param tableName 从那张表中查询对象
		 * @param className 单个对象所对应的类名是什么
		 * @return 这种类型的对象
		 */
		public Object selectOne(int id,String tableName,String className) {
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			ResultSetMetaData rsm = null;
			Class objClass = null;
			String[] colName = null;
			String[] fieldsName = null;
			int i,j,k;
			try {
				//得到数据库的连接和一些工具集合
				con = DBUtil.getConnection();
				ps = con.prepareStatement("select * from "+tableName+" where id=?");
				ps.setInt(1,id);
				rs = ps.executeQuery();
				rsm = rs.getMetaData();//resultset的相关信息
				
				int colCount = rsm.getColumnCount();//resultset的列数
				colName = new String[colCount];//resultset的列名数组
				
				objClass = Class.forName("com.emsys.entity."+className);//得到对应的类
				Object obj = objClass.newInstance();//实例化对应的类
				Field[] fields = objClass.getDeclaredFields();//得到该类的属性数组
				fieldsName = new String[fields.length];//该类类名的的数组
				
				for(i=0;i<colCount;i++) {//将列名，属性名填入对应的字符串数组
					colName[i] = rsm.getColumnName(i+1);
					fieldsName[i] = fields[i].getName();
				}
				while(rs.next()) {
					for(j=0;j<colName.length;j++) {
						for(k=0;k<fieldsName.length;k++) {
							fields[k].setAccessible(true);//设置对属性的其它访问权限也可以使用set赋值
							if(colName[j].equals(fieldsName[k]))//如果列名和属性名对应一致，将查到的数据赋给该属性
								fields[k].set(obj, rs.getObject(colName[j]));
						}
					}
				}
				return obj;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}finally {
				DBUtil.closeConnection(con, ps, rs);
			}
		}
		
		//单个数据的查询
		/**
		 * 
		 * @param id 要查询的对象的Id值（主键）
		 * @param tableName 从那张表中查询对象
		 * @param colName 具体要查询的哪个字段的对应字段名称
		 * @return 返回查询到的数据的对象
		 */
		public Object selectColumn(int id,String table,String colName) {
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			Object object = null;
			try {
				con = DBUtil.getConnection();
				ps = con.prepareStatement("select "+colName+" from "+table+" where id=?");
				ps.setInt(1, id);
				rs = ps.executeQuery();
				while(rs.next()) {
					object = rs.getObject(colName);
				}
				return object;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}finally {
				DBUtil.closeConnection(con, ps, rs);
			}
		}
		
		/**
		 * 查询所有对象某些字段的值
		 * @param table表名
		 * @param colName要查询的字段的列名
		 */
		public List<Map<String, Object>> selectCol(String table,String... colName){
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			Object object = null;
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			try {
				con = DBUtil.getConnection();
				String sql = "";
				for(String str:colName) {
					sql = sql+str+",";
				}
				sql = sql.substring(0, sql.length()-1);
				ps = con.prepareStatement("select "+sql+" from "+table);
				rs = ps.executeQuery();
				while(rs.next()) {
					Map<String, Object> map = new HashMap<>();
					for(String str2:colName) {
						map.put(str2, rs.getObject(str2));
					}
					list.add(map);
				}
				return list;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}finally {
				DBUtil.closeConnection(con, ps, rs);
			}
		}
		
		
		
		//分页查询
		/**
		 * 
		 * @param tableName 从那张表中查询对象
		 * @param className 单个对象所对应的类名是什么
		 * @param pageSize 每一页的大小
		 * @param pageNum 第几页
		 * @return 对应页码的对象集合
		 */
		public List<Object> findByPage(String tableName,String className,int pageSize,int pageNum){
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			List<Object> list = new ArrayList<Object>();
			try {
				con = DBUtil.getConnection();
				ps = con.prepareStatement("select * from "+tableName+" order by id ASC limit ?,?");
				ps.setInt(1,(pageNum-1)*pageSize);//从所有数据的第几个开始(起始下标为0)
				ps.setInt(2, pageSize);//取多少个
				rs = ps.executeQuery();
				while(rs.next()) {
					int id = (int)rs.getObject("id");
					Object object = selectOne(id, tableName, className);
					list.add(object);
				}
				return list;
				
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}finally {
				DBUtil.closeConnection(con, ps, rs);
			}
		}
		
		
		
	}


