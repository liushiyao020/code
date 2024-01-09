package org.hcm.compile.myth;

import java.awt.*;
import java.io.File;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import org.hcm.compile.lexing.LexAnalyse;
import org.hcm.compile.parsing.Parser;
import org.hcm.compile.utils.FileInput;

//AddFrame继承自JFrame类, 并且实现了ActionListener 接口

public class JFrameExample4 extends JFrame implements ActionListener {

	// 组件
	JFrame frmMain;
	JPanel p1, p2, p1Left, p1Right, p2Left, p2Right;
	JButton b1, b2, b3, b4, jbClear1, jbClear2;
	JTabbedPane tp;
	JSplitPane sp1, sp2;
	
	
	JTextArea jtf1, jtf2, jtf3, jtf4;// 文本框,用于:显示数字
	
	

	public JFrameExample4() {// 构造方法
	
	// 初始化组件
	frmMain = new JFrame("四元式翻译");
	frmMain.setSize(800, 600);
    frmMain.setLocation(200, 200);
    frmMain.setLayout(null);
    
    p1 = new JPanel();
    p1Left = new JPanel();
    p1Right = new JPanel();
    
    p1Left.setBounds(50, 50, 300, 60);
    p1Right.setBounds(10, 150, 300, 60);
    p1.setLayout(new FlowLayout());
    p1Left.setLayout(new FlowLayout());
    b1 = new JButton("保存文件");
    b2 = new JButton("保存输出文件");
    jbClear1 = new JButton("清除");
    jtf1 = new JTextArea("请输入C程序：", 20, 20);// 文本框
    

	jtf2 = new JTextArea("得到的四元式：", 20, 20);// 文本框
	jtf2.setEditable(false);
	p1Left.add(b1);
    p1Left.add(b2);
    p1Left.add(jbClear1);
    p1Right.add(jtf1);
    p1Right.add(jtf2);
	p1.add(p1Left);
	p1.add(p1Right);
   
    sp1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, p1Left, p1Right);
    sp1.setDividerLocation(80);
    p1.add(sp1);
    
    
    p2 = new JPanel();
    p2.setBounds(10, 150, 300, 60);
    p2Left = new JPanel();
    p2Right = new JPanel();
    p2Left.setBounds(50, 50, 300, 60);
    p2Right.setBounds(10, 150, 300, 60);
    
    b3 = new JButton("打开文件");
    b4 = new JButton("保存输出文件");
//    b5 = new JButton("保存输出文件");
    jbClear2 = new JButton("清除");
    
    jtf3 = new JTextArea("C程序：", 20, 20);// 文本框
//	jtf3.setPreferredSize(new Dimension(300, 150));
	jtf3.setEditable(false);
	
	jtf4 = new JTextArea("得到的四元式：", 20, 20);// 文本框
//	jtf4.setPreferredSize(new Dimension(300, 150));
	jtf4.setEditable(false);
    
    p2Left.add(b3);
    p2Left.add(b4);
    p2Left.add(jbClear2);
    p2Right.add(jtf3);
    p2Right.add(jtf4);
    p2.add(p2Left);
    p2.add(p2Right);

    
    sp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, p2Left, p2Right);
    sp2.setDividerLocation(80);
    p2.add(sp2);
    
    tp = new JTabbedPane();
    tp.add(p1);
    tp.add(p2);
    
    tp.setTitleAt(0, "输入C程序");
    tp.setTitleAt(1, "导入C程序");
    
    frmMain.setContentPane(tp);
    frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frmMain.setVisible(true);

	
	
	b1.addActionListener(this);//因为本类已经实现了ActionListener接口,所以添加事件的接收器就是自己(this)
	b2.addActionListener(this);
	b3.addActionListener(this);
	b4.addActionListener(this);
	jbClear1.addActionListener(this);
	jbClear2.addActionListener(this);
	
	
	}

	/*
	
	*  实现ActionListener接口 就必须重写actionPerformed方法
	
	*/
	
	@Override
	
	public void actionPerformed(ActionEvent e) {// 当事件产生后,就会执行actionPerformed里的方法
	
	JButton jbTemp= (JButton) e.getSource();//得到事件源
	String fileName = null;
	String f1="C:\\Users\\lsy\\Desktop\\complie-pro\\output\\wordList.txt", f2="C:\\Users\\lsy\\Desktop\\complie-pro\\.\\output/LL1.txt", f3="C:\\Users\\lsy\\Desktop\\complie-pro\\.\\output/FourElement.txt";
	if(jbTemp==b1) {//如果是从b1产生的事件.那么执行加法操作
	
	String prog = jtf1.getText();//从文本框1得到字符串
	if(prog.equals("请输入C程序：") || prog.equals("")) {
		JOptionPane.showMessageDialog(null, "输入程序不能为空！");
	}
	LexAnalyse lex = new LexAnalyse();
	lex.lexAnalyse(prog);
	Parser parser = new Parser(new LexAnalyse(prog));
	parser.grammerAnalyse();
	try {
		System.out.println("输出信息到文件：" + lex.outputWordList() + "\n");
		System.out.println("输出信息到文件：" + parser.outputLL1() + "\n");
		System.out.println("输出信息到文件：" + parser.outputFourElem());
		fileName = parser.outputFourElem();
		String four = FileInput.readFile(fileName);
		jtf2.setText(four);//将四元式显示在文本框2上
		f1 = lex.outputWordList();
		f2 = parser.outputLL1();
		f3 = parser.outputFourElem();
	} catch(IOException ioe) {
		ioe.printStackTrace();
	}
	
	}else if(jbTemp == b2) {
		String prog = jtf1.getText();//从文本框1得到字符串
		LexAnalyse lex = new LexAnalyse();
		lex.lexAnalyse(prog);
		Parser parser = new Parser(new LexAnalyse(prog));
		parser.grammerAnalyse();
		try {
			fileName = parser.outputFourElem();
			f1 = lex.outputWordList();
			f2 = parser.outputLL1();
//			f3 = parser.outputFourElem();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "词法分析：输出信息到文件：" + f1 + "\n");
		JOptionPane.showMessageDialog(null, "语法分析：输出信息到文件：" + f2 + "\n");
		JOptionPane.showMessageDialog(null, "四元式生成：输出信息到文件：" + fileName);
	}else if(jbTemp==jbClear1) {//如果事件源是 jbClear,那么清除文本框的文字
	
		jtf1.setText("");
		
		jtf2.setText("");
	
	}else if(jbTemp == b3) {//打开文件
		
		fileName = fileChooser();
		try {
			String prog = FileInput.readFile(fileName);
			jtf3.setText(prog);
			LexAnalyse lex = new LexAnalyse();
			lex.lexAnalyse1(fileName);
			System.out.println("输出信息到文件：" + lex.outputWordList() + "\n");
			Parser parser = new Parser(new LexAnalyse(FileInput.readFile(fileName)));
			parser.grammerAnalyse();
			System.out.println("输出信息到文件：" + parser.outputLL1() + "\n");
			System.out.println("输出信息到文件：" + parser.outputFourElem());
			fileName = parser.outputFourElem();
			String four = FileInput.readFile(fileName);
			jtf4.setText(four);//将四元式显示在文本框2上
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
	}else if(jbTemp == b4){
		JOptionPane.showMessageDialog(null, "词法分析：输出信息到文件：" + f1 + "\n");
		JOptionPane.showMessageDialog(null, "语法分析：输出信息到文件：" + f2 + "\n");
		JOptionPane.showMessageDialog(null, "四元式生成：输出信息到文件：" + f3);
	} if(jbTemp == jbClear2) {
		
		jtf3.setText("");
		jtf4.setText("");
		
	}
	
	}

// main方法

	public static void main(String[] args) {
	
	new JFrameExample4();//创建一个AddFrame的实例
	
	}
	
	public static String fileChooser() {
		JFileChooser jfc=new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
        jfc.showDialog(new JLabel(), "选择");
        File file=jfc.getSelectedFile();
        String FileName = null;
        if(file.isDirectory()){
            System.out.println("文件夹:"+file.getAbsolutePath());
        }else if(file.isFile()){
            System.out.println("文件:"+file.getAbsolutePath());
            FileName = file.getAbsolutePath();
        }
        System.out.println(jfc.getSelectedFile().getName());  
        return FileName;
	}

}
