package Work1;

import java.sql.*;

import java.util.*;

//1、在MySQL数据库中建立数据库，数据表。
//2、用Java程序连接数据库，要完成查询、插入等操作。
//3、完成SQL语句的拼接。

public class DB_work2 {
//	static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
//	static String DB_URL = "jdbc:mysql://Localhost:3306/test?serverTimezone=UTC";
//	static String USER="root";
//	static String PASSWORD="admin";
	
	public static void main(String[] args) {
		Connection con=null;
		Statement stmt=null;
		boolean rs;
		ResultSet res = null;
		// TODO Auto-generated method stub
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://Localhost:3306/test?serverTImezone=UTC", 
					"root", "admin");
			stmt = con.createStatement();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		int n;
		boolean state = true;
		Scanner sc =new Scanner(System.in);
		while(state) {
			System.out.printf("1:插入一条记录\t2:删除一条记录\t3:修改一条记录\t4:查\t5:按总分倒序排列\t6:按平均分正序排列\t0:退出\n");
			n=sc.nextInt();
			sc.nextLine();
			switch(n) {
				case 1: {
					//插入一条记录
					String number, name, dep, pla;
					int age, score_a, score_b;
					
					System.out.println("请输入学号：");
					number = sc.nextLine();
					System.out.println("请输入姓名：");
					name = sc.nextLine();
					//sc.nextLine();
					System.out.println("请输入专业：");
					dep = sc.nextLine();
					//sc.nextLine();
					System.out.println("请输入籍贯：");
					pla = sc.nextLine();
					//sc.nextLine();
					System.out.println("请输入年龄：");
					age=sc.nextInt();
					System.out.println("请输入a课成绩：");
					score_a = sc.nextInt();
					System.out.println("请输入b课成绩：");
					score_b = sc.nextInt();
					String sql = "insert student(number, name, department, 籍贯, age, score_a, score_b) values('"+number+"', '"+name+"', '"+dep+"', '"+pla+"', "+age+", "+score_a+", "+score_b+")";
					System.out.println(sql);
					try {
						rs = stmt.execute(sql);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}break;
				
				case 2: {
//					删除一条记录
					System.out.println("请输入要删除人的学号:");
					String num = sc.nextLine();
					String sql = "delete from student where number = '"+num+"'";
					try {
						rs = stmt.execute(sql);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}break;
				
				case 3: {
//					修改一条记录
					System.out.println("输入要修改年龄的人的学号:");
					String num = sc.nextLine();
					System.out.println("输入新年龄:");
					int newage = sc.nextInt();
					String sql = "update student set age = '"+newage+"' where number = '"+num+"'";
					System.out.println(sql);
					try {
						rs = stmt.execute(sql);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}break;
				
				case 4: {
					System.out.println("查找所有山东籍的人:");
					String sql = "select * from student where 籍贯 like '山东%'";
					try {
						res = stmt.executeQuery(sql);
						while(res.next()) {
							System.out.printf("学号:%s		姓名:%s		部门:%s		籍贯:%s		年龄:%d		a成绩	:%d	b成绩	:%d	总分:%d		平均分:%.2f	\n", res.getString("number"), 
									res.getString("name"), res.getString("department"), res.getString("籍贯"), res.getInt("age5"
											+ ""), 
									res.getInt("score_a"), res.getInt("score_b"), res.getInt("总分"), res.getDouble("avg"));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}break;
				case 5: {
					//求出所有人的总分
					String sql = "update student set 总分 = (score_a + score_b)";
					//按总分倒序排列
					String sql2 = "select * from student order by 总分 desc";
					try {
						rs = stmt.execute(sql);
						res = stmt.executeQuery(sql2);
						while(res.next()) {
							System.out.printf("学号:%s		姓名:%s		部门:%s		籍贯:%s		年龄:%d		a成绩	:%d	b成绩	:%d	总分:%d		平均分:%.2f	\n", res.getString("number"), 
									res.getString("name"), res.getString("department"), res.getString("籍贯"), res.getInt("age"), 
									res.getInt("score_a"), res.getInt("score_b"), res.getInt("总分"), res.getDouble("avg"));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}break;
				case 6: {
//					把所有人的avg分计算出来，倒叙排列
					//按平均分倒序排列
					String sql = "update student set avg = (score_a+score_b)/2";
					String sql2 = "select * from student order by avg asc";
					try {
						rs = stmt.execute(sql);
						res = stmt.executeQuery(sql2);
						while(res.next()) {
						System.out.printf("学号:%s		姓名:%s		部门:%s		籍贯:%s		年龄:%d		a成绩	:%d	b成绩	:%d	总分:%d		平均分:%.2f	\n", res.getString("number"), 
								res.getString("name"), res.getString("department"), res.getString("籍贯"), res.getInt("age"), 
								res.getInt("score_a"), res.getInt("score_b"), res.getInt("总分"), res.getDouble("avg"));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}break;
				case 0: {
					state = false;
					
					break;
				}
			}
		}
		
		
		
		
		
		


//		

		
		
		
		
		
	}

}
  