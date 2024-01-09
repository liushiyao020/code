package banker_1;

import java.awt.event.*;

import java.util.*;

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
	public int[] request;
	private int safe_seq[];

	public BankersAlgorithm(int proNums, int sourceKinds, int source[]) { // 对数组进行初始化
		this.proNums = proNums;
		this.sourceKinds = sourceKinds;
		this.source = source;
		this.Max = new int[proNums][sourceKinds];
		this.Allocation = new int[proNums][sourceKinds];
		this.Need = new int[proNums][sourceKinds];
		this.request = new int[sourceKinds];
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

	public void setProNeed(Scanner inNeed) { // 设置初始已分配资源
		request = new int[sourceKinds];
		for (int j = 0; j < sourceKinds; j++) {
			request[j] = inNeed.nextInt();
		}
	}

	private void setNeed() { // 计算need矩阵
		for (int i = 0; i < proNums; i++) {
			for (int j = 0; j < sourceKinds; j++)
				Need[i][j] = Max[i][j] - Allocation[i][j];
		}
		for (int i = 0; i < sourceKinds; i++) { // 计算剩余资源矩阵
			int sum = 0;
			for (int j = 0; j < proNums; j++)
				sum += Allocation[j][i];
			Available[i] = source[i] - sum;
		}
		System.arraycopy(Available, 0, Work, 0, Available.length);
	}

	private void setNeed1() { // 计算need矩阵
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
	
	
	public boolean compare(int m[], int n[]) {
		for(int i=0; i<sourceKinds; i++) {
			if(m[i]<n[i]) {
				return false;
			}
		}
		return true;
	}
	
	public boolean safe(JTextArea out_result) {
		boolean finish[] = new boolean[proNums];
		int work[] = new int[sourceKinds];
		int num = 0;
		for(int i=0; i<sourceKinds; i++) {
			work[i] = source[i];
		}
		
		for(int i=0; i<proNums; i++) {
			if(num==proNums) {
				break;
			}
			for(int j=0; j<proNums; j++) {
				if(!finish[j]) {
					continue;
				}else {
					if(compare(work, Need[j])) {
						finish[j] = true;
						safe_seq[num] = j+1;
						num++;
						
						for(int k=0; k<sourceKinds; k++) {
							work[k] = work[k] + Allocation[j][k];
						}
					}
				}
			}
		}
		for(int i=0; i<proNums; i++) {
			if(!finish[i]) {
				return false;
			}
		}
		
		for(int i=0; i<proNums; i++) {
			out_result.append(safe_seq[i] + " ");
			out_result.append("\n");
		}
		return true;
	}
	
	public void resafe(int n, JTextArea out_result) {
		if(compare(source, request) && compare(Need[n-1], request)) {
			for(int i=0; i<sourceKinds; i++) {
				Allocation[n-1][i] = Allocation[n-1][i] + request[i];
				Need[n-1][i] = Need[n-1][i] - request[i];
				source[i] = source[i] - request[i];
			}
			if(safe(out_result)) {
				out_result.append("允许进程" + n + "申请资源");
			}else {
				out_result.append("不允许进程" + n + "申请资源");
				for(int i=0; i<sourceKinds; i++) {
					Allocation[n-1][i] = Allocation[n-1][i] - request[i];
					Need[n-1][i] = Need[n-1][i] + request[i];
					source[i] = source[i] + request[i];
				}
			}
		}else {
			out_result.append("申请资源量越界");
		}
	}

	public void initInput(Scanner inMax, Scanner inAllo, Scanner inNeed, JTextArea out_result, int type, int k) { // 将初始化以及寻找安全序列函数整合到一起
		setMax(inMax);
		setAllocation(inAllo);
		if (type == 0) {
			setNeed();
			findSafeSequence(0, out_result);
		} else if (type == 1) {
			setNeed1();
			findSafeSequence(0, out_result);
		} else if (type == 2) {
			setNeed1();
			setProNeed(inNeed);
			resafe(k, out_result);
		}
		
	}
}

class Pframe implements ActionListener, ItemListener { // 简单界面
	JFrame frame;
	JButton start;
	JTextArea in_souNum, in_souInit, in_proNum, in_Max, in_Allo, in_pro, in_need, out_result;
	JLabel l_souNum, l_souInit, l_proNum, l_Max, l_Allo, l_result, l_choose, l_pro, l_need;
	JScrollPane scroll;
	JButton work;
	JComboBox<String> c1;
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
		in_pro = new JTextArea(6, 20);
		in_need = new JTextArea(6, 20);
		out_result = new JTextArea(6, 20);
		l_souNum = new JLabel("请输入资源类别数：");
		l_souInit = new JLabel("请输入可用资源矩阵：");
		l_proNum = new JLabel("请输入进程数目：");
		l_Max = new JLabel("请输入最大需求矩阵：");
		l_Allo = new JLabel("请输入分配矩阵：");
		l_result = new JLabel("运行结果如下：");
		l_choose = new JLabel("请选择题目：");
		l_pro = new JLabel("输入请求分配的进程号");
		l_need = new JLabel("输入要申请的各类资源个数");
		work = new JButton("Examining");
		scroll = new JScrollPane(out_result);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		c1 = new JComboBox<String>();
		c1.addItem("第一题");
		c1.addItem("第二题");
		c1.addItem("为进程申请资源");

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

		frame.add(l_pro);
		l_pro.setBounds(30, 275, 180, 25);
		frame.add(in_pro);
		in_pro.setBounds(30, 300, 180, 25);

		frame.add(l_need);
		l_need.setBounds(240, 275, 180, 25);
		frame.add(in_need);
		in_need.setBounds(240, 300, 180, 25);

		frame.add(work);
		work.setBounds(240, 380, 100, 30);
		frame.add(l_choose);
		l_choose.setBounds(30, 355, 180, 25);
		frame.add(c1);
		c1.setBounds(30, 380, 180, 25);

		work.addActionListener(this);
		c1.addItemListener(this);
//		c1.addActionListener(this);
		frame.setVisible(true);
	}

	@Override

	public void actionPerformed(ActionEvent e) {
		int index = c1.getSelectedIndex();
		JButton btmp = (JButton)e.getSource();
		if(btmp==work) {
			if (index == 0) {
//				System.out.println(index);
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
				banker.initInput(inMax, inAllo, null, out_result, 0, 0); // 调用initInput函数经行输入输出
				inMax.close();
				inAllo.close();
				if (out_result.getText().isEmpty()) // 当没有安全序列输出，out_result中内容将为空
					out_result.append("没有安全序列！");

			} else if (index == 1) {
//				System.out.println(index);
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
				banker.initInput(inMax, inAllo, null, out_result, 1, 0); // 调用initInput函数经行输入输出
				inMax.close();
				inAllo.close();
				if (out_result.getText().isEmpty()) // 当没有安全序列输出，out_result中内容将为空
					out_result.append("没有安全序列！");
			} else if (index == 2) {
//				System.out.println(index);
				// 得出求矩阵Need，输出，输出安全序列
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
				Scanner inNeed = new Scanner(in_need.getText());
				int pro = Integer.parseInt(in_pro.getText());
				banker.initInput(inMax, inAllo, inNeed, out_result, 2, pro); // 调用initInput函数经行输入输出
				inMax.close();
				inAllo.close();
				inNeed.close();
//				if (out_result.getText().isEmpty()) // 当没有安全序列输出，out_result中内容将为空
//					out_result.append("没有安全序列，不能分配！");
			}
		}
		

	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub

	}

}

public class Banker_1 {
	public static void main(String[] args) {
		new Pframe("银行家算法");
	}
}
