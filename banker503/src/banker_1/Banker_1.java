package banker_1;

import java.awt.event.*;

import java.util.*;

import javax.swing.*;

class BankersAlgorithm {
	private int sourceKinds; // ϵͳ��Դ������
	private int proNums; // ϵͳ������Ŀ
	private int source[]; // ��ʼ��Դ����
	private int[][] Max; // �߳���Ҫ�������Դ������
	private int[][] Allocation; // �ѷ�����̵߳���Դ����
	private int[][] Need; // �������
	private int[] Available; // ��Դʣ�����
	private int[] Work; // ϵͳ���ṩ�����̼�����������ĸ�����Դ��Ŀ
	private boolean finish[]; // �ж�״̬
	private ArrayList<String> result; // ������
	public int[] request;
	private int safe_seq[];

	public BankersAlgorithm(int proNums, int sourceKinds, int source[]) { // ��������г�ʼ��
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

	public void setMax(Scanner inMax) { // ���ý�����Ҫ�����Դ��
		for (int i = 0; i < proNums; i++)
			for (int j = 0; j < sourceKinds; j++) {
				Max[i][j] = inMax.nextInt();
			}
	}

	public void setAllocation(Scanner inAllo) { // ���ó�ʼ�ѷ�����Դ
		for (int i = 0; i < proNums; i++)
			for (int j = 0; j < sourceKinds; j++) {
				Allocation[i][j] = inAllo.nextInt();
			}
	}

	public void setProNeed(Scanner inNeed) { // ���ó�ʼ�ѷ�����Դ
		request = new int[sourceKinds];
		for (int j = 0; j < sourceKinds; j++) {
			request[j] = inNeed.nextInt();
		}
	}

	private void setNeed() { // ����need����
		for (int i = 0; i < proNums; i++) {
			for (int j = 0; j < sourceKinds; j++)
				Need[i][j] = Max[i][j] - Allocation[i][j];
		}
		for (int i = 0; i < sourceKinds; i++) { // ����ʣ����Դ����
			int sum = 0;
			for (int j = 0; j < proNums; j++)
				sum += Allocation[j][i];
			Available[i] = source[i] - sum;
		}
		System.arraycopy(Available, 0, Work, 0, Available.length);
	}

	private void setNeed1() { // ����need����
		for (int i = 0; i < proNums; i++) {
			for (int j = 0; j < sourceKinds; j++)
				Need[i][j] = Max[i][j] - Allocation[i][j];
		}
		// ����ʣ����Դ����
		for (int i = 0; i < source.length; i++) {
			Available[i] = source[i];
		}

		System.arraycopy(Available, 0, Work, 0, Available.length);
	}

	private void findSafeSequence(int k, JTextArea out_result) { // �����еİ�ȫ�����㷨������DFS
		if (k == proNums) { // �����������ΪproNums����ʾ�ҵ���ȫ���У��������
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
				if (task) { // ����Ҫ�󣬶������޸�
					for (int j = 0; j < sourceKinds; j++)
						Work[j] += Allocation[i][j];
					finish[i] = true;
					result.add(i + "");
					findSafeSequence(k + 1, out_result); // �ݹ������һ��

					result.remove((result.size() - 1)); // ���˸ò㣬���������ݻ�ԭ
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
				out_result.append("�������" + n + "������Դ");
			}else {
				out_result.append("���������" + n + "������Դ");
				for(int i=0; i<sourceKinds; i++) {
					Allocation[n-1][i] = Allocation[n-1][i] - request[i];
					Need[n-1][i] = Need[n-1][i] + request[i];
					source[i] = source[i] + request[i];
				}
			}
		}else {
			out_result.append("������Դ��Խ��");
		}
	}

	public void initInput(Scanner inMax, Scanner inAllo, Scanner inNeed, JTextArea out_result, int type, int k) { // ����ʼ���Լ�Ѱ�Ұ�ȫ���к������ϵ�һ��
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

class Pframe implements ActionListener, ItemListener { // �򵥽���
	JFrame frame;
	JButton start;
	JTextArea in_souNum, in_souInit, in_proNum, in_Max, in_Allo, in_pro, in_need, out_result;
	JLabel l_souNum, l_souInit, l_proNum, l_Max, l_Allo, l_result, l_choose, l_pro, l_need;
	JScrollPane scroll;
	JButton work;
	JComboBox<String> c1;
	JPanel panel;

	Pframe(String title) { // �򵥽��漰�䲼��
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
		l_souNum = new JLabel("��������Դ�������");
		l_souInit = new JLabel("�����������Դ����");
		l_proNum = new JLabel("�����������Ŀ��");
		l_Max = new JLabel("����������������");
		l_Allo = new JLabel("������������");
		l_result = new JLabel("���н�����£�");
		l_choose = new JLabel("��ѡ����Ŀ��");
		l_pro = new JLabel("�����������Ľ��̺�");
		l_need = new JLabel("����Ҫ����ĸ�����Դ����");
		work = new JButton("Examining");
		scroll = new JScrollPane(out_result);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		c1 = new JComboBox<String>();
		c1.addItem("��һ��");
		c1.addItem("�ڶ���");
		c1.addItem("Ϊ����������Դ");

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
				out_result.setText(""); // ÿ����Ӧ��out_result�ÿ�
				int souNum = Integer.parseInt(in_souNum.getText()); // ��ȡin_souNum, in_souInit�ϵ�����
				int[] source = new int[souNum];
				Scanner in = new Scanner(in_souInit.getText());
				for (int i = 0; i < souNum; i++)
					source[i] = in.nextInt();
				in.close();
				int proNums = Integer.parseInt(in_proNum.getText()); // ��ȡin_proNum�е�����

				BankersAlgorithm banker = new BankersAlgorithm(proNums, souNum, source);

				Scanner inMax = new Scanner(in_Max.getText());
				Scanner inAllo = new Scanner(in_Allo.getText());
				banker.initInput(inMax, inAllo, null, out_result, 0, 0); // ����initInput���������������
				inMax.close();
				inAllo.close();
				if (out_result.getText().isEmpty()) // ��û�а�ȫ���������out_result�����ݽ�Ϊ��
					out_result.append("û�а�ȫ���У�");

			} else if (index == 1) {
//				System.out.println(index);
				out_result.setText(""); // ÿ����Ӧ��out_result�ÿ�
				int souNum = Integer.parseInt(in_souNum.getText()); // ��ȡin_souNum, in_souInit�ϵ�����
				int[] source = new int[souNum];
				Scanner in = new Scanner(in_souInit.getText());
				for (int i = 0; i < souNum; i++)
					source[i] = in.nextInt();
				in.close();
				int proNums = Integer.parseInt(in_proNum.getText()); // ��ȡin_proNum�е�����

				BankersAlgorithm banker = new BankersAlgorithm(proNums, souNum, source);

				Scanner inMax = new Scanner(in_Max.getText());
				Scanner inAllo = new Scanner(in_Allo.getText());
				banker.initInput(inMax, inAllo, null, out_result, 1, 0); // ����initInput���������������
				inMax.close();
				inAllo.close();
				if (out_result.getText().isEmpty()) // ��û�а�ȫ���������out_result�����ݽ�Ϊ��
					out_result.append("û�а�ȫ���У�");
			} else if (index == 2) {
//				System.out.println(index);
				// �ó������Need������������ȫ����
				out_result.setText(""); // ÿ����Ӧ��out_result�ÿ�
				int souNum = Integer.parseInt(in_souNum.getText()); // ��ȡin_souNum, in_souInit�ϵ�����
				int[] source = new int[souNum];
				Scanner in = new Scanner(in_souInit.getText());
				for (int i = 0; i < souNum; i++)
					source[i] = in.nextInt();
				in.close();
				int proNums = Integer.parseInt(in_proNum.getText()); // ��ȡin_proNum�е�����

				BankersAlgorithm banker = new BankersAlgorithm(proNums, souNum, source);

				Scanner inMax = new Scanner(in_Max.getText());
				Scanner inAllo = new Scanner(in_Allo.getText());
				Scanner inNeed = new Scanner(in_need.getText());
				int pro = Integer.parseInt(in_pro.getText());
				banker.initInput(inMax, inAllo, inNeed, out_result, 2, pro); // ����initInput���������������
				inMax.close();
				inAllo.close();
				inNeed.close();
//				if (out_result.getText().isEmpty()) // ��û�а�ȫ���������out_result�����ݽ�Ϊ��
//					out_result.append("û�а�ȫ���У����ܷ��䣡");
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
		new Pframe("���м��㷨");
	}
}
