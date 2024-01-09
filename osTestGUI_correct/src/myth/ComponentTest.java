package myth;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.*;

public class ComponentTest extends JFrame implements ActionListener {
	JFrame frame;
	JButton but1;
	JTextArea ta, tb;
	JTextField tf;
	JLabel label, label2;

	public ComponentTest() {
		frame = new JFrame("多道批处理作业调度模拟程序");
		frame.setBounds(100, 100, 600, 300);
		GridLayout gl = new GridLayout(4, 2, 5, 5); // 设置表格为3行两列排列，表格横向间距为5个像素，纵向间距为5个像素
		frame.setLayout(gl);
		label2 = new JLabel("输入进程的个数：");
		label = new JLabel("提交时间 \t" + "作业预计的运行时间（以分钟计）");
		frame.add(label2);
		frame.add(label);

		// 单行文本框

		tf = new JTextField(30); // 30列长度

		frame.add(tf);

		ta = new JTextArea(20, 10);
		frame.add(ta);

		// 多行文本框
		tb = new JTextArea(20, 10);
		frame.add(tb);
		

		// 按钮组件
		but1 = new JButton("提交");

		but1.addActionListener(this);

		Panel pn0 = new Panel();
		pn0.setLayout(new FlowLayout());
		pn0.add(but1);
		frame.add(pn0);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton temp = (JButton) e.getSource();
		if (temp == but1) {

			try {
				String s1 = ta.getText();
				int k = Integer.parseInt(tf.getText());
				String res = chuli(k, s1);
				tb.setText(res);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	public String chuli(int k, String s) {
		class work {
			int order;// 调度次序
			int num;// 作业号
			int tj;
			int ys;// 预计执行时间
			int start;// 开始上处理机运行的时刻
			float ratio;// 响应比
			int zhouzhuan;// 周转时间，从提交到运行完经过的时间
			boolean isSelected;
			int waittime;// 它自己从被提交到被选中的等待时间，初始值为0
			int weighttime;
		}

		ArrayList<work> works = new ArrayList<work>();
		ArrayList<work> finish = new ArrayList<work>();
		String[] sourceStr = s.split("\n");
		for (int i = 0; i < sourceStr.length; i++) {
			String[] numstr = sourceStr[i].split(" ");
			work w = new work();
			w.num = i;
			w.tj = Integer.parseInt(numstr[0]);
			w.ys = Integer.parseInt(numstr[1]);
			w.isSelected = false;
			w.waittime = 0;
			works.add(w);
		}

		// 遍历arraylist数组，每次按照响应比取最大的
		int len = works.size();

//				//所有作业按照开始时间从小到大排序

		for (int i = 1; i < works.size(); i++) {
			for (int j = i; j > 0; j--) {
				if (works.get(j).tj < works.get(j - 1).tj) {
					work tmp = works.get(j - 1);
					works.set(j - 1, works.get(j));
					works.set(j, tmp);
				} else
					break;
			}
		}
		int i;
		// 如果有提交时间相同的作业，就比较他们的运行时间，让运行时间短的排在前面
		for (int j = 1; j < works.size(); j++) {
			if (works.get(j - 1).tj == works.get(j).tj) {
				int tj = works.get(j - 1).tj;
				int p = j + 1;
				while (p < works.size() && works.get(p).tj == tj) {
					p++;
				}
				p--;// 这个p是最后一个相等的tj时间的元素的下标
				// 对从j到p，的元素进行对运行时间的排序，把小的放在前面，大的放在后面
				for (i = j; i <= p; i++) {
					for (int t = i; t > 0; t--) {
						if (works.get(t).ys < works.get(t - 1).ys) {
							work tmp = works.get(t - 1);
							works.set(t - 1, works.get(t));
							works.set(t, tmp);
						} else
							break;
					}
				}
			}
		}

		//
//		System.out.println("对运行时间的从小到大排序排完了");
//		for (int n = 0; n < works.size(); n++) {
//			System.out.println(works.get(n).tj + " " + works.get(n).ys);
//		}

		// 先运行第1个
		int m = 1;
		works.get(0).order = 1;
		works.get(0).start = works.get(0).tj;
		works.get(0).ratio = 1;
		works.get(0).zhouzhuan = works.get(0).ys;
		works.get(0).waittime = 0;
		works.get(0).isSelected = true;
		works.get(0).weighttime = works.get(0).zhouzhuan / works.get(0).ys;
		finish.add(works.get(0));// 第一个被调度的提交了
		int wait;
		int priortj = works.get(0).tj;

		int hour = priortj / 100;
		int min = priortj % 100;
		hour += (min + works.get(0).ys) / 60;
		min = (min + works.get(0).ys) % 60;
		int timenow = hour * 100 + min;
		// 计算的时候可以不按照24取模，输出的时候再统一取模
		len--;

		while (len > 0) {
			len--;
			// 上一个作业执行完的时候，先算这些的响应比，再取最大，
			for (int j = 1; j < works.size(); j++) {
				work temp = works.get(j);
				if (!temp.isSelected) {
					// 还没执行过的作业的响应比=1 + (等待时间)/需要的执行时间
					// 要计算一下每个进程的等待时间

					if (works.get(j).tj < timenow) {
						// 这个作业在上一个作业执行完之前已经被提交了，这个作业需要等
						int time0 = works.get(j).tj;
						int time0_hour = time0 / 100;
						int time0_min = time0 % 100;
						int dhour;
						if (time0_hour < hour) {
							dhour = hour - time0_hour;
							wait = dhour * 60 + min - time0_min;
						} else {
							wait = min - time0_min;
						}

						works.get(j).waittime = wait;
					} else {
						// 这是还不需要等
						works.get(j).waittime = 0;
					}

					works.get(j).ratio = 1 + ((float) works.get(j).waittime) / works.get(j).ys;

				}
			}

			// 取出最大的响应比，
			float max = -1;
			int maxNum = -1;
			for (int j = 1; j < works.size(); j++) {
				work temp = works.get(j);
				if (!temp.isSelected) {
					if (max < temp.ratio) {
						maxNum = j;
						max = temp.ratio;
					}
				}
			}

			// 取到了还未被执行的作业的最大响应比

			work temp = works.get(maxNum);
			int tjtime = temp.tj;
//					提交时间的小时
			hour = tjtime / 100;
//					提交时间的分钟
			min = tjtime % 100;
			// 计算开始上处理机的时间 start，是提交时间+它自己的等待时间
			hour += (temp.waittime + min) / 60;
			min = (temp.waittime + min) % 60;

			// 上处理机的时间
			works.get(maxNum).start = (hour % 24) * 100 + min;

			// 周转时间,是指作业从被提交给系统开始,到作业完成为止的这段时间间隔（分钟）
			// 周转时间=它自己的等待时间+它自己的执行时间
			works.get(maxNum).zhouzhuan = temp.waittime + temp.ys;

			// 带权周转时间=周转时间/运行时间
			works.get(maxNum).weighttime = works.get(maxNum).zhouzhuan / temp.ys;

			// 这个进程已选择
			works.get(maxNum).isSelected = true;

			// 调度次序
			m++;// order++
			works.get(maxNum).order = m;
			priortj = works.get(maxNum).tj;
			// 把这个进程提交的finish数组里
			finish.add(works.get(maxNum));

			// timenow是被选中的作业执行完时的时间
//					hour = timenow/100;
//					min = timenow%100;
			hour = works.get(maxNum).start / 100;
			min = works.get(maxNum).start % 100;
			hour += (min + temp.ys) / 60;
			min = (min + temp.ys) % 60;
			timenow = hour * 100 + min;
			// 这个timenow只是用来算时间差的，可能小时不需要取模24
		}

		// 计算平均周转时间和平均带权周转时间
		float avgzhouzhuan = 0, avgdaiquan = 0;
		for (int j = 0; j < finish.size(); j++) {
			avgzhouzhuan += finish.get(j).zhouzhuan;
			avgdaiquan += finish.get(j).weighttime;
		}
		k = finish.size();
		avgzhouzhuan /= k;
		avgdaiquan /= k;

		String res = "调度次序\t作业\t调度时间\t周转时间\t带权周转时间\n";

		for (int j = 0; j < finish.size(); j++) {
			work tmp = finish.get(j);
			if(tmp.start < 1000) {
				String str = "0" + String.valueOf(tmp.start);
				tmp.num++;
				res += tmp.order + " \t" + tmp.num + " \t" + str + " \t" + tmp.zhouzhuan + " \t" + tmp.weighttime
						+ "\n";
			}else {
				tmp.num++;
				res += tmp.order + " \t" + tmp.num + " \t" + tmp.start + " \t" + tmp.zhouzhuan + " \t" + tmp.weighttime
						+ "\n";
			}
			// 调度次序，作业号，调度时间，周转时间和带权周转时间

			

		}
		res += "平均周转时间\t平均带权周转时间\n";
		res += avgzhouzhuan + "\t" + avgdaiquan + "\n";
		return res;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ComponentTest();

	}

}