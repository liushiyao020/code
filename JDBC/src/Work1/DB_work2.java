package Work1;

import java.sql.*;

import java.util.*;

//1����MySQL���ݿ��н������ݿ⣬���ݱ�
//2����Java�����������ݿ⣬Ҫ��ɲ�ѯ������Ȳ�����
//3�����SQL����ƴ�ӡ�

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
			System.out.printf("1:����һ����¼\t2:ɾ��һ����¼\t3:�޸�һ����¼\t4:��\t5:���ֵܷ�������\t6:��ƽ������������\t0:�˳�\n");
			n=sc.nextInt();
			sc.nextLine();
			switch(n) {
				case 1: {
					//����һ����¼
					String number, name, dep, pla;
					int age, score_a, score_b;
					
					System.out.println("������ѧ�ţ�");
					number = sc.nextLine();
					System.out.println("������������");
					name = sc.nextLine();
					//sc.nextLine();
					System.out.println("������רҵ��");
					dep = sc.nextLine();
					//sc.nextLine();
					System.out.println("�����뼮�᣺");
					pla = sc.nextLine();
					//sc.nextLine();
					System.out.println("���������䣺");
					age=sc.nextInt();
					System.out.println("������a�γɼ���");
					score_a = sc.nextInt();
					System.out.println("������b�γɼ���");
					score_b = sc.nextInt();
					String sql = "insert student(number, name, department, ����, age, score_a, score_b) values('"+number+"', '"+name+"', '"+dep+"', '"+pla+"', "+age+", "+score_a+", "+score_b+")";
					System.out.println(sql);
					try {
						rs = stmt.execute(sql);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}break;
				
				case 2: {
//					ɾ��һ����¼
					System.out.println("������Ҫɾ���˵�ѧ��:");
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
//					�޸�һ����¼
					System.out.println("����Ҫ�޸�������˵�ѧ��:");
					String num = sc.nextLine();
					System.out.println("����������:");
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
					System.out.println("��������ɽ��������:");
					String sql = "select * from student where ���� like 'ɽ��%'";
					try {
						res = stmt.executeQuery(sql);
						while(res.next()) {
							System.out.printf("ѧ��:%s		����:%s		����:%s		����:%s		����:%d		a�ɼ�	:%d	b�ɼ�	:%d	�ܷ�:%d		ƽ����:%.2f	\n", res.getString("number"), 
									res.getString("name"), res.getString("department"), res.getString("����"), res.getInt("age5"
											+ ""), 
									res.getInt("score_a"), res.getInt("score_b"), res.getInt("�ܷ�"), res.getDouble("avg"));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}break;
				case 5: {
					//��������˵��ܷ�
					String sql = "update student set �ܷ� = (score_a + score_b)";
					//���ֵܷ�������
					String sql2 = "select * from student order by �ܷ� desc";
					try {
						rs = stmt.execute(sql);
						res = stmt.executeQuery(sql2);
						while(res.next()) {
							System.out.printf("ѧ��:%s		����:%s		����:%s		����:%s		����:%d		a�ɼ�	:%d	b�ɼ�	:%d	�ܷ�:%d		ƽ����:%.2f	\n", res.getString("number"), 
									res.getString("name"), res.getString("department"), res.getString("����"), res.getInt("age"), 
									res.getInt("score_a"), res.getInt("score_b"), res.getInt("�ܷ�"), res.getDouble("avg"));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}break;
				case 6: {
//					�������˵�avg�ּ����������������
					//��ƽ���ֵ�������
					String sql = "update student set avg = (score_a+score_b)/2";
					String sql2 = "select * from student order by avg asc";
					try {
						rs = stmt.execute(sql);
						res = stmt.executeQuery(sql2);
						while(res.next()) {
						System.out.printf("ѧ��:%s		����:%s		����:%s		����:%s		����:%d		a�ɼ�	:%d	b�ɼ�	:%d	�ܷ�:%d		ƽ����:%.2f	\n", res.getString("number"), 
								res.getString("name"), res.getString("department"), res.getString("����"), res.getInt("age"), 
								res.getInt("score_a"), res.getInt("score_b"), res.getInt("�ܷ�"), res.getDouble("avg"));
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
  