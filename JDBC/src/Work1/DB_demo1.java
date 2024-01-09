package Work1;

import java.io.*;

import java.sql.*;

import java.util.*;

//2．用Java程序连接数据库，并完成以下功能：
//（1）插入一条学生信息，不包含平均分
//（2）计算所有学生的平均分，并存入数据库表
//（3）显示所有学生的成绩信息（按平均分从高到低）
//（4）删除一名学生的信息
//（5）交互方式随意，可通过键盘输入，也可程序中给定


public class DB_demo1 {
	static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static String DB_URL = "jdbc:mysql://Localhost:3306/testdatabase?serverTimezone=UTC";
	static String USER="root";
	static String PASSWARD="admin";
	public static void main(String[] args) 
			throws ClassNotFoundException, SQLException{
		// TODO Auto-generated method stub
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Connection con = DriverManager.getConnection(DB_URL, USER, PASSWARD);
		if (con != null) {
			System.out.println("连接成功" + con);
			
		}
		
		boolean state=true;
		Statement stmt = con.createStatement();
		int n;
		while(state) {
			System.out.println("1：插入一条学生信息	2:计算所有学生的平均分		"
					+ "3：显示所有学生的成绩信息（按平均分从高到低）		4：删除一名学生的信息		0:退出");
			Scanner sc = new Scanner(System.in);
			n= sc.nextInt();
			sc.nextLine();
			switch(n) {
				case 1: {
					String xName, xNumber;
					int xJava, xSjjg;
					
					System.out.println("请输入学生学号");
					xNumber = sc.nextLine();
					//sc.nextLine();
					System.out.println("请输入学生姓名");
					xName = sc.nextLine();
					System.out.println("请输入Java成绩");
					xJava=sc.nextInt();
					System.out.println("请输入数据结构成绩");
					xSjjg=sc.nextInt();
					
					String sql = "insert into score(number, name, JavaScore, SjjgScore) "
							+ "values(?, ?, ?, ?)";
					PreparedStatement ps = con.prepareStatement(sql);
					
					ps.setString(1, xNumber);
					ps.setString(2, xName);
					ps.setInt(3, xJava);
					ps.setInt(4, xSjjg);
					ps.executeUpdate();
					
					
				}break;
				
				case 2: {
					//（2）计算所有学生的平均分，并存入数据库表
					String sql = "update score set avg = (JavaScore+SjjgScore)/2";
					boolean rs = stmt.execute(sql);
					//stmt.close();
				}break;
				
				case 3: {
					//（3）显示所有学生的成绩信息（按平均分从高到低）
					String sql = "select * from score order by avg desc";
					ResultSet rs = stmt.executeQuery(sql);
					while (rs.next()) {
						System.out.printf("学号为%s\t 姓名为%s     java成绩为%d\t 数据结构成绩为%d\t 平均成绩为:%.2f\n", rs.getString("Number"), 
								rs.getString("Name"), rs.getInt("JavaScore"), rs.getInt("SjjgScore"), rs.getDouble("avg"));
					}
					//stmt.close();
					
				}break;
				
				case 4: {
					System.out.println("请输入要删除学生的学号:");
					String num = sc.nextLine();
					String sql = "delete from score where Number = '"+num+"'";
					boolean rs = stmt.execute(sql);
					//stmt.close();
				}break;
				
				case 0: {
					state=false;
					stmt.close();
					break;
				}
			
			}
			
		}
		
	}

}
