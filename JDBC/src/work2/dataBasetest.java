package work2;

import javax.swing.*;

import javax.swing.event.ListSelectionListener;

import java.sql.*;

import javax.swing.table.*;

import java.awt.*;

import java.util.Vector;

import java.awt.event.*;

class DBOperation {
	public static Connection conn=null;
	public static Statement stmt=null;
	public static ResultSet rs = null;
	
	public static Connection ConnectDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://Localhost:3306/test";		//�������ݿ���������������
			conn = DriverManager.getConnection(url, "root", "admin");
			return conn;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ResultSet Query(String sql) {
			
		try {
			conn = ConnectDB();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			return rs;

		} catch(SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static void insert(String sql) {
		try {
			conn = ConnectDB();
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close() {
		try {
			if(rs != null) {
				rs.close();
			}
			if(stmt!=null) {
				stmt.close();
			}
			if(conn != null) {
				conn.close();
			}
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	
}

class EventHandler implements ActionListener{
	dataBasetest dbt;
	public EventHandler(dataBasetest dbt) {
		super();
		this.dbt = dbt;
	}
	public void actionPerformed(ActionEvent e) {
		String sql, pname, school, leader, number, cho = null;
		if(e.getSource()==dbt.b1) {
			//¼��
			insert();
		} else if(e.getSource()==dbt.b2) {
			query();
		} else {
			displayAll();
		}
	}
	

	public void insert() {
		String type = null, name, school, leader, number, sql;
		if(dbt.rb1.isSelected()) {
			type="����";
		}
		else if(dbt.rb2.isSelected()) {
			type="��ҵ";
		}
		else if(dbt.rb1.isSelected() && dbt.rb2.isSelected()) {
			JOptionPane.showMessageDialog(null, "����ͬʱѡ����!");
			return;
		}
		else {
			JOptionPane.showMessageDialog(null, "��ѡ�����ͣ�");
			return;
		}
		name = dbt.t1.getText();
		school = (String)dbt.box1.getSelectedItem();
		leader = dbt.t2.getText();
		number = dbt.t3.getText();
		if("".equals(name)||"".equals(school)||"".equals(leader)||"".equals(number)) {
			JOptionPane.showMessageDialog(null, "��������Ϣ��");
		}
		else {
			sql="insert into project(��Ŀ����, ��Ŀ����, ����ѧԺ, ��Ŀ�鳤, ��Ա����)"+
				"values('"+type+"', '"+name+"', '"+school+"', '"+leader+"', '"+number+"')";
			DBOperation.insert(sql);
			//dbt.tableModel.setValueAt(type, 0, 0);
		}
	}
	
	public void query() {
		String school = (String)dbt.box2.getSelectedItem(), sql;
		sql = "select * from project where ����ѧԺ='"+school+"'";
		ResultSet rs = DBOperation.Query(sql);
		try {
			Vector<String> vector = new Vector<String>();
			vector.add("��Ŀ����");
			vector.add("��Ŀ����");
			vector.add("����ѧԺ");
			vector.add("��Ŀ�鳤");
			vector.add("��Ա����");
			Vector data = new Vector();
			while(rs.next()) {
				String type = null, name, leader, number;
				type=rs.getString("��Ŀ����");
				name=rs.getString("��Ŀ����");
				leader = rs.getString("��Ŀ�鳤");
				number=rs.getString("��Ա����");
				Vector<String> row = new Vector<String>();
				row.add(type);
				row.add(name);
				row.add(school);
				row.add(leader);
				row.add(number);
				data.add(row);
			}
			dbt.tableModel.setDataVector(data, vector);
			clear();
			
			if(rs !=null) {
				rs.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void displayAll() {
		String sql = "select * from project";
		Vector<String> vector=new Vector<String>();
		vector.add("��Ŀ����");
		vector.add("��Ŀ����");
		vector.add("����ѧԺ");
		vector.add("��Ŀ�鳤");
		vector.add("��Ա����");
		Vector data = new Vector();
		try {
			ResultSet rs = DBOperation.Query(sql);
			while(rs.next()) {
				String type=rs.getString("��Ŀ����");
				String name=rs.getString("��Ŀ����");
				String school=rs.getString("����ѧԺ");
				String leader=rs.getString("��Ŀ�鳤");
				String number=rs.getString("��Ա����");
				Vector row = new Vector();
				row.add(type);
				row.add(name);
				row.add(school);
				row.add(leader);
				row.add(number);
			}
			dbt.tableModel.setDataVector(data, vector);
			clear();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clear() {
		dbt.t1.setText("");
		dbt.t2.setText("");
		dbt.t3.setText("");
	}
	
}


class dataBasetest {
	JFrame f;
	JLabel l1, l2, l3, l4, l5, l6;
	JRadioButton rb1, rb2;
	JButton b1, b2;
	@SuppressWarnings("rawtypes")
	JComboBox box1, box2;
	JTextField t1, t2, t3;
	JScrollPane pane;
	JPanel p1, p2, p3, p4, p5, p6, p7, p8, p9, p10;
	
	@SuppressWarnings("rawtypes")
	Vector vdata = new Vector();
	@SuppressWarnings("rawtypes")
	Vector vcolumn = new Vector();
	DefaultTableModel tableModel;
	JTable table;
	@SuppressWarnings("unchecked")
	dataBasetest(String name) {
		f = new JFrame(name);
		String[] schoolName= {"����ѧԺ", "�ؼ�ѧԺ", "�ܶ�ѧԺ"};
		box1 = new JComboBox(schoolName);
		box2 = new JComboBox(schoolName);
		rb1 = new JRadioButton("����");
		rb2 = new JRadioButton("��ҵ");
		l1 = new JLabel("��Ŀ���ͣ�");
//		l1.setHorizontalAlignment(JLabel.LEFT);
		l2 = new JLabel("��Ŀ���ƣ�");
//		l2.setHorizontalAlignment(JLabel.CENTER);
		l3 = new JLabel("����ѧԺ��");
//		l3.setHorizontalAlignment(JLabel.CENTER);
		l4 = new JLabel("��Ŀ�鳤��");
//		l4.setHorizontalAlignment(JLabel.CENTER);
		l5 = new JLabel("��Ա������");
//		l5.setHorizontalAlignment(JLabel.CENTER);
		l6 = new JLabel("ѧԺ���ƣ�");
//		l6.setHorizontalAlignment(JLabel.CENTER);
		b1 =new JButton("¼��");
		b2 = new JButton("��ѯ");
		t1 = new JTextField(20);
		t2 = new JTextField(20);
		t3 = new JTextField(20);
		
		p1 = new JPanel();
		p1.setLayout(new GridLayout(5, 1));
		p4 = new JPanel();
		p5 = new JPanel();
		p6 = new JPanel();
		p7 = new JPanel();
		p8 = new JPanel();
		p9 = new JPanel();
		p10 = new JPanel();
		p5.setLayout(new BorderLayout(2, 2));
		p6.add(l1);
		p6.add(rb1);
		p6.add(rb2);
		p7.add(l2);
		p7.add(t1);
		p8.add(l3);
		p8.add(box1);
		p9.add(l4);
		p9.add(t2);
		p10.add(l5);
		p10.add(t3);
		p1.add(p6);
		p1.add(p7);
		p1.add(p8);
		p1.add(p9);
		p1.add(p10);
		p5.add(BorderLayout.SOUTH, p4);
		p4.add(b1);
		p5.add(BorderLayout.CENTER, p1);
	
		
		vcolumn.add("��Ŀ����");
		vcolumn.add("��Ŀ����");
		vcolumn.add("����ѧԺ");
		vcolumn.add("��Ŀ�鳤");
		vcolumn.add("��Ա����");
		tableModel = new DefaultTableModel(vdata, vcolumn);
		table = new JTable(tableModel);
		pane = new JScrollPane(table);
			
		b1.addActionListener(new EventHandler(this));
		b2.addActionListener(new EventHandler(this));
		
		
		p2 = new JPanel();
		p2.setLayout(new BorderLayout(2, 2));
		f.setLayout(new GridLayout(1, 2));
		//�ѱ�����ӽ�ȥ
		p2.add(BorderLayout.CENTER, pane);
		p3 = new JPanel();
		p2.add(BorderLayout.SOUTH, p3);
		p3.setLayout(new FlowLayout());
		p3.add(l6);
		p3.add(box2);
		p3.add(b2);
		f.add(p5);
		f.add(p2);
		
		
		f.setSize(800, 600);
		f.setVisible(true);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new dataBasetest("��ѧ�����´�ҵ��Ŀ����ϵͳ");
	}

}
