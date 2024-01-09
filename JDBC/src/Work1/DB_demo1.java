package Work1;

import java.io.*;

import java.sql.*;

import java.util.*;

//2����Java�����������ݿ⣬��������¹��ܣ�
//��1������һ��ѧ����Ϣ��������ƽ����
//��2����������ѧ����ƽ���֣����������ݿ��
//��3����ʾ����ѧ���ĳɼ���Ϣ����ƽ���ִӸߵ��ͣ�
//��4��ɾ��һ��ѧ������Ϣ
//��5��������ʽ���⣬��ͨ���������룬Ҳ�ɳ����и���


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
			System.out.println("���ӳɹ�" + con);
			
		}
		
		boolean state=true;
		Statement stmt = con.createStatement();
		int n;
		while(state) {
			System.out.println("1������һ��ѧ����Ϣ	2:��������ѧ����ƽ����		"
					+ "3����ʾ����ѧ���ĳɼ���Ϣ����ƽ���ִӸߵ��ͣ�		4��ɾ��һ��ѧ������Ϣ		0:�˳�");
			Scanner sc = new Scanner(System.in);
			n= sc.nextInt();
			sc.nextLine();
			switch(n) {
				case 1: {
					String xName, xNumber;
					int xJava, xSjjg;
					
					System.out.println("������ѧ��ѧ��");
					xNumber = sc.nextLine();
					//sc.nextLine();
					System.out.println("������ѧ������");
					xName = sc.nextLine();
					System.out.println("������Java�ɼ�");
					xJava=sc.nextInt();
					System.out.println("���������ݽṹ�ɼ�");
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
					//��2����������ѧ����ƽ���֣����������ݿ��
					String sql = "update score set avg = (JavaScore+SjjgScore)/2";
					boolean rs = stmt.execute(sql);
					//stmt.close();
				}break;
				
				case 3: {
					//��3����ʾ����ѧ���ĳɼ���Ϣ����ƽ���ִӸߵ��ͣ�
					String sql = "select * from score order by avg desc";
					ResultSet rs = stmt.executeQuery(sql);
					while (rs.next()) {
						System.out.printf("ѧ��Ϊ%s\t ����Ϊ%s     java�ɼ�Ϊ%d\t ���ݽṹ�ɼ�Ϊ%d\t ƽ���ɼ�Ϊ:%.2f\n", rs.getString("Number"), 
								rs.getString("Name"), rs.getInt("JavaScore"), rs.getInt("SjjgScore"), rs.getDouble("avg"));
					}
					//stmt.close();
					
				}break;
				
				case 4: {
					System.out.println("������Ҫɾ��ѧ����ѧ��:");
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
