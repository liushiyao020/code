package bankerstest;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.annotation.Resource;
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

	public BankersAlgorithm(int proNums, int sourceKinds, int source[]) { // ��������г�ʼ��
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

	private void setNeed() { // ����need����
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

	public void initInput(Scanner inMax, Scanner inAllo, JTextArea out_result) { // ����ʼ���Լ�Ѱ�Ұ�ȫ���к������ϵ�һ��
		setMax(inMax);
		setAllocation(inAllo);
		setNeed();
		findSafeSequence(0, out_result);
	}
}

class Pframe { // �򵥽���
	JFrame frame;
	JButton start;
	JTextArea in_souNum, in_souInit, in_proNum, in_Max, in_Allo, out_result;
	JLabel l_souNum, l_souInit, l_proNum, l_Max, l_Allo, l_result;
	JScrollPane scroll;
	JButton work;
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
		out_result = new JTextArea(6, 20);
		l_souNum = new JLabel("��������Դ�������");
		l_souInit = new JLabel("������ÿ����Դ����Ŀ��");
		l_proNum = new JLabel("�����������Ŀ��");
		l_Max = new JLabel("����������������");
		l_Allo = new JLabel("������������");
		l_result = new JLabel("���н�����£�");
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
			banker.initInput(inMax, inAllo, out_result); // ����initInput���������������
			inMax.close();
			inAllo.close();
			if (out_result.getText().isEmpty()) // ��û�а�ȫ���������out_result�����ݽ�Ϊ��
				out_result.append("û�а�ȫ���У�");
		}
	}

}

public class Bankers {
	public static void main(String[] args) {
		new Pframe("���м��㷨");
	}
}
