void main()
{
	int a=5,b=4,c=3;
	if(a>b){
		b=a;
		if(c>b){
			b=c;
		}else{
			b=b;
		}
		
		while(b>a){
			a=a+1;
		}
	}
}

