package com.calculate.test;
import java.util.*;
public class calculate {
	public static int gcd(int a,int b) {
		int gcd = 0,c;                       
		if(a > b) 
		{
			while(b != 0)
           {
				a = a % b;                   
				if(a < b)
				{
				c = a;
				a = b;
				b = c;
				}
				gcd = a;
			}
		}
		if(a == b)
			gcd = a;
		else 
		{
			c = a;a = b;b = c;
			while(b != 0)
	        {
				a = a % b;
				if(a < b){c = a;a = b;b = c;}
				gcd = a;
			}
		}
		return gcd;
	}
	
	

	

		public static int lcm(int a,int b) {
			// TODO Auto-generated method stub
//			Scanner sc=new Scanner(System.in);
//			int a=sc.nextInt();
//			int b=sc.nextInt();
			if(a<b){
				int temp=a;
		        a=b;//大值放在a的位置上
		        b=temp;//小值放在b的位置上
			}
			int multiple;
			for(multiple=a; multiple<a*b; multiple++) {
				if(multiple%a==0 && multiple%b==0) {
					break;
				}
			}
	      return multiple;
		}

		public int getMax(int[] a) {
			if(a==null) {
				throw new NullPointerException();
			}
			Arrays.sort(a);
			return a[a.length-1];
		}
		
		public int getMin(int[] a) {
			if(a==null) {
				throw new NullPointerException();
			}
			Arrays.sort(a);
			return a[0];
		}
		public String trans(char[] a) {
			String str = String.valueOf(a);
			return str;
		}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
