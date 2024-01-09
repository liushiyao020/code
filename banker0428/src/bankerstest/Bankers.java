package bankerstest;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.annotation.Resource;
import javax.swing.*;

class BankersAlgorithm {
	private int sourceKinds; // 系统资源种类数
	private int proNums; // 系统进程数目
	private int source[]; // 初始资源数量
	private int[][] Max; // 线程需要的最大资源数矩阵
	private int[][] Allocation; // 已分配给线程的资源矩阵
	private int[][] Need; // 需求矩阵
	private int[] Available; // 资源剩余矩阵
	private int[] Work; // 系统可提供给进程继续运行所需的各类资源数目
	private boolean finish[]; // 判断状态
	private ArrayList<String> result; // 输出结果

	public BankersAlgorithm(int proNums, int sourceKinds, int source[]) { // 对数组进行初始化
		this.proNums = proNums;
		this.sourceKinds = sourceKinds;
		this.source = source;
		this.Max = new int[proNums][sourceKinds];
		this.Allocation = new int[proNums][sourceKinds];
		this.Need = new int[proNums][sourceKinds];
		finish = new boolean[proNums];
		for (int i = 0; i < proNums; i++) {
			this.Max[i] = new int[sourceKinds];
			this.Allocation[i] = new int[sourceKinds];
			this.Need[i] = new int[sourceKinds];
			finish[i] = false;
		}

		Available = new int[sourceKinds];
		Work = new int[sourceKinds];
		result = new ArrayList<>();
	}

	public void setMax(Scanner inMax) { // 设置进程需要最大资源数
		for (int i = 0; i < proNums; i++)
			for (int j = 0; j < sourceKinds; j++) {
				Max[i][j] = inMax.nextInt();
			}
	}

	public void setAllocation(Scanner inAllo) { // 设置初始已分配资源
		for (int i = 0; i < proNums; i++)
			for (int j = 0; j < sourceKinds; j++) {
				Allocation[i][j] = inAllo.nextInt();
			}
	}

	private void setNeed() { // 计算need矩阵
		for (int i = 0; i < proNums; i++) {
			for (int j = 0; j < sourceKinds; j++)
				Need[i][j] = Max[i][j] - Allocation[i][j];
		}
		// 计算剩余资源矩阵
		for (int i = 0; i < source.length; i++) {
			Available[i] = source[i];
		}

		System.arraycopy(Available, 0, Work, 0, Available.length);
	}

	private void findSafeSequence(int k, JTextArea out_result) { // 找所有的安全序列算法，运用DFS
		if (k == proNums) { // 当遍历到深度为proNums，表示找到安全序列，进行输出
			for (int i = 0; i < result.size(); i++)
				out_result.append("P" + result.get(i));
			out_result.append("\n");
			return;
		}

		for (int i = 0; i < proNums; i++) {
			if (!finish[i]) {
				boolean task = true;
				for (int j = 0; j < sourceKinds; j++) {
					if (Need[i][j] > Work[j])
						task = false;
				}
				if (task) { // 满足要求，对数据修改
					for (int j = 0; j < sourceKinds; j++)
						Work[j] += Allocation[i][j];
					finish[i] = true;
					result.add(i + "");
					findSafeSequence(k + 1, out_result); // 递归进入下一层

					result.remove((result.size() - 1)); // 回退该层，并进行数据还原
					for (int j = 0; j < sourceKinds; j++)
						Work[j] -= Allocation[i][j];
					finish[i] = false;
				}
			}
		}
	}

	public void initInput(Scanner inMax, Scanner inAllo, JTextArea out_result) { // 将初始化以及寻找安全序列函数整合到一起
		setMax(inMax);
		setAllocation(inAllo);
		setNeed();
		findSafeSequence(0, out_result);
	}
}

class Pframe { // 简单界面
	JFrame frame;
	JButton start;
	JTextArea in_souNum, in_souInit, in_proNum, in_Max, in_Allo, out_result;
	JLabel l_souNum, l_souInit, l_proNum, l_Max, l_Allo, l_result;
	JScrollPane scroll;
	JButton work;
	JPanel panel;

	Pframe(String title) { // 简单界面及其布局
		frame = new JFrame(title);
		frame.setSize(720, 480);
		frame.setLayout(null);

		in_souNum = new JTextArea(5, 20);
		in_souInit = new JTextArea(6, 20);
		in_proNum = new JTextArea(6, 20);
		in_Max = new JTextArea(6, 20);
		in_Allo = new JTextArea(6, 20);
		out_result = new JTextArea(6, 20);
		l_souNum = new JLabel("请输入资源类别数：");
		l_souInit = new JLabel("请输入每类资源的数目：");
		l_proNum = new JLabel("请输入进程数目：");
		l_Max = new JLabel("请输入最大需求矩阵：");
		l_Allo = new JLabel("请输入分配矩阵：");
		l_result = new JLabel("运行结果如下：");
		work = new JButton("Examining");
		scroll = new JScrollPane(out_result);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		frame.add(l_souNum);
		l_souNum.setBounds(30, 25, 180, 25);
		frame.add(in_souNum);
		in_souNum.setBounds(30, 50, 180, 25);

		frame.add(l_souInit);
		l_souInit.setBounds(30, 75, 180, 25);
		frame.add(in_souInit);
		in_souInit.setBounds(30, 100, 180, 25);

		frame.add(l_proNum);
		l_proNum.setBounds(240, 75, 180, 25);
		frame.add(in_proNum);
		in_proNum.setBounds(240, 100, 180, 25);

		frame.add(l_Max);
		l_Max.setBounds(30, 125, 180, 25);
		frame.add(in_Max);
		in_Max.setBounds(30, 150, 180, 125);

		frame.add(l_Allo);
		l_Allo.setBounds(240, 125, 180, 25);
		frame.add(in_Allo);
		in_Allo.setBounds(240, 150, 180, 125);

		frame.add(l_result);
		l_result.setBounds(480, 25, 180, 25);
		frame.add(scroll);
		scroll.setBounds(480, 50, 180, 275);

		frame.add(work);
		work.setBounds(140, 300, 100, 30);

		work.addActionListener(new ButtonEvent());

		frame.setVisible(true);
	}

	class ButtonEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			out_result.setText(""); // 每次响应将out_result置空
			int souNum = Integer.parseInt(in_souNum.getText()); // 读取in_souNum, in_souInit上的内容
			int[] source = new int[souNum];
			Scanner in = new Scanner(in_souInit.getText());
			for (int i = 0; i < souNum; i++)
				source[i] = in.nextInt();
			in.close();
			int proNums = Integer.parseInt(in_proNum.getText()); // 读取in_proNum中的内容

			BankersAlgorithm banker = new BankersAlgorithm(proNums, souNum, source);

			Scanner inMax = new Scanner(in_Max.getText());
			Scanner inAllo = new Scanner(in_Allo.getText());
			banker.initInput(inMax, inAllo, out_result); // 调用initInput函数经行输入输出
			inMax.close();
			inAllo.close();
			if (out_result.getText().isEmpty()) // 当没有安全序列输出，out_result中内容将为空
				out_result.append("没有安全序列！");
		}
	}

}

public class Bankers {
	public static void main(String[] args) {
		new Pframe("银行家算法");
	}
}
