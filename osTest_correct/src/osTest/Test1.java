package osTest;
import java.util.*;



public class Test1 {

	public static void main(String[] args) {
		class work{
			int order;//���ȴ���
			int num;//��ҵ��
			int tj;
			int ys;//Ԥ��ִ��ʱ��
			int start;//��ʼ�ϴ��������е�ʱ��
			float ratio;//��Ӧ��
			int zhouzhuan;//��תʱ�䣬���ύ�������꾭����ʱ��
			boolean isSelected;
			int waittime;//���Լ��ӱ��ύ����ѡ�еĵȴ�ʱ�䣬��ʼֵΪ0
			int weighttime;
		}
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		List<work> works = new ArrayList<work>();
		List<work> finish = new ArrayList<work>();
		//������ҵ��
		String s = scan.nextLine();
		int k = Integer.parseInt(s);
		int i=1;

		while(k!=0) {
	        //1�������ַ���String���գ����ո�
	        String str = scan.nextLine();
	        //���˿ո�ȡ���ַ���
	        String[] numstr = str.split(" ");
	        work w = new work();
	        w.num = i;
	        w.tj = Integer.parseInt(numstr[0]);
	        w.ys = Integer.parseInt(numstr[1]); 
	        w.isSelected = false;
	        w.waittime=0;
	        works.add(w);
	        i++;
	        k--;
	        
		}
		

		//����arraylist���飬ÿ�ΰ�����Ӧ��ȡ����
		int len = works.size();
		
		
//		//������ҵ���տ�ʼʱ���С��������
	
		for(i=1; i<works.size(); i++) {
			for(int j=i; j>0; j--) {
				if(works.get(j).tj < works.get(j-1).tj) {
					work tmp = works.get(j-1);
					works.set(j-1, works.get(j));
					works.set(j, tmp);
				}else break;
			}
		}
		
		//������ύʱ����ͬ����ҵ���ͱȽ����ǵ�����ʱ�䣬������ʱ��̵�����ǰ��
		for(int j=1; j<works.size(); j++) {
			if(works.get(j-1).tj == works.get(j).tj) {
				int tj = works.get(j-1).tj;
				int p = j+1;
				while(p < works.size() && works.get(p).tj == tj) {
					p++;
				}
				p--;//���p�����һ����ȵ�tjʱ���Ԫ�ص��±�
				//�Դ�j��p����Ԫ�ؽ��ж�����ʱ������򣬰�С�ķ���ǰ�棬��ķ��ں���
				for(i=j; i<=p; i++) {
					for(int t=i; t>0; t--) {
						if(works.get(t).ys < works.get(t-1).ys) {
							work tmp = works.get(t-1);
							works.set(t-1, works.get(t));
							works.set(t, tmp);
						}else break;
					}
				}
			}
		}
		
		//
		System.out.println("������ʱ��Ĵ�С��������������");
		for(int n=0; n<works.size(); n++) {
			System.out.println(works.get(n).tj + " " + works.get(n).ys);
		}
		
		//�����е�1��
		int m=1;
		works.get(0).order=1;
		works.get(0).start = works.get(0).tj;
		works.get(0).ratio = 1;
		works.get(0).zhouzhuan = works.get(0).ys;
		works.get(0).waittime=0;
		works.get(0).isSelected=true;
		works.get(0).weighttime = works.get(0).zhouzhuan/works.get(0).ys;
		finish.add(works.get(0));//��һ�������ȵ��ύ��
		int wait;
		int priortj = works.get(0).tj;
		
		int hour = priortj/100;
		int min = priortj%100;
		hour += (min+works.get(0).ys)/60;
		min = (min+works.get(0).ys)%60;
		int timenow = hour*100+min;
		//�����ʱ����Բ�����24ȡģ�������ʱ����ͳһȡģ
		len--;
		
		while(len>0) {
			len--;
			//��һ����ҵִ�����ʱ��������Щ����Ӧ�ȣ���ȡ���
			for(int j=1; j<works.size(); j++) {
				work temp = works.get(j);
				if(!temp.isSelected) {
					//��ûִ�й�����ҵ����Ӧ��=1 + (�ȴ�ʱ��)/��Ҫ��ִ��ʱ��
					//Ҫ����һ��ÿ�����̵ĵȴ�ʱ��

					if(works.get(j).tj < timenow) {
						//�����ҵ����һ����ҵִ����֮ǰ�Ѿ����ύ�ˣ������ҵ��Ҫ��
						int time0 = works.get(j).tj;
						int time0_hour = time0/100;
						int time0_min = time0%100;
						int dhour;
						if(time0_hour<hour) {
							dhour = hour-time0_hour;
							wait = dhour*60+min-time0_min;
						}else {
							wait = min-time0_min;
						}
						
						works.get(j).waittime = wait;
					}else {
						//���ǻ�����Ҫ��
						works.get(j).waittime=0;
					}
					
					works.get(j).ratio = 1 + ((float)works.get(j).waittime)/works.get(j).ys;
					
				}	
			}
			
			//ȡ��������Ӧ�ȣ�
			float max = -1;
			int maxNum = -1;
			for(int j=1; j<works.size(); j++) {
				work temp = works.get(j);
				if(!temp.isSelected) {
					if(max<temp.ratio) {
						maxNum = j;
						max = temp.ratio;
					}
				}	
			}
			
			//ȡ���˻�δ��ִ�е���ҵ�������Ӧ��
			
			work temp = works.get(maxNum);
			int tjtime = temp.tj;
//			�ύʱ���Сʱ
			hour = tjtime/100;
//			�ύʱ��ķ���
			min = tjtime%100;
			//���㿪ʼ�ϴ�������ʱ�� start�����ύʱ��+���Լ��ĵȴ�ʱ��
			hour += (temp.waittime+min)/60;
			min = (temp.waittime+min)%60;
			
			//�ϴ�������ʱ��
			works.get(maxNum).start = (hour%24)*100+min;
			
			//��תʱ��,��ָ��ҵ�ӱ��ύ��ϵͳ��ʼ,����ҵ���Ϊֹ�����ʱ���������ӣ�
			//��תʱ��=���Լ��ĵȴ�ʱ��+���Լ���ִ��ʱ��
			works.get(maxNum).zhouzhuan = temp.waittime+temp.ys;
			
			//��Ȩ��תʱ��=��תʱ��/����ʱ��
			works.get(maxNum).weighttime = works.get(maxNum).zhouzhuan/temp.ys;
			
			
			//���������ѡ��
			works.get(maxNum).isSelected=true;
			
			//���ȴ���
			m++;//order++
			works.get(maxNum).order=m;
			priortj = works.get(maxNum).tj;
			//����������ύ��finish������ 
			finish.add(works.get(maxNum));
			
			//timenow�Ǳ�ѡ�е���ҵִ����ʱ��ʱ��
//			hour = timenow/100;
//			min = timenow%100;
			hour = works.get(maxNum).start/100;
			min = works.get(maxNum).start%100;
			hour += (min+temp.ys)/60;
			min = (min+temp.ys)%60;
			timenow = hour*100+min;
			//���timenowֻ��������ʱ���ģ�����Сʱ����Ҫȡģ24
		}
		
		 //����ƽ����תʱ���ƽ����Ȩ��תʱ��
		float avgzhouzhuan=0, avgdaiquan=0;
		for(int j=0; j<finish.size(); j++) {
			avgzhouzhuan += finish.get(j).zhouzhuan;
			avgdaiquan += finish.get(j).weighttime;
		}
		k = finish.size();
		avgzhouzhuan /= k;
		avgdaiquan /= k;
		
		
		
		//�������Ҫ�����е�ʱ�����Сʱת��24mod�� ��
		
		System.out.println("���ȴ���\t��ҵ��\t����ʱ��\t��תʱ��\t��Ȩ��תʱ��\t��Ӧ��");
		for(int j=0; j<finish.size(); j++) {
			work tmp = finish.get(j);
		//	���ȴ�����ҵ�ţ�����ʱ�䣬��תʱ��ʹ�Ȩ��תʱ��
			
			if(tmp.start < 1000) {
				String str = "0" + String.valueOf(tmp.start);
				System.out.println(tmp.order + " \t" + tmp.num + " \t" + str + " \t" + tmp.zhouzhuan + " \t" + tmp.weighttime + "\t" + tmp.ratio);
			}else {
				System.out.println(tmp.order + " \t" + tmp.num + " \t" + tmp.start + " \t" + tmp.zhouzhuan + " \t" + tmp.weighttime + "\t" + tmp.ratio);
			}
			
		
		}
		System.out.println("ƽ����תʱ�䣬ƽ����Ȩ��תʱ��");
		System.out.println(avgzhouzhuan + " " + avgdaiquan);
		scan.close();
	}

}

