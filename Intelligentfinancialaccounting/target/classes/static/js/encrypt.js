function encrypt(date)
{
	var str;

	for(var i=0;i<date.length;i++)
		{
		if(i==0)
			{
			if(date.charAt(i)==0)
			{
			str="A";		
			}
		else if(date.charAt(i)==1)
			{
			str="B";		
			}
		else if(date.charAt(i)==2)
			{
			str="C";		
			}
		else if(date.charAt(i)==3)
			{	
			str="D";		
			}
		else if(date.charAt(i)==4)
			{
			str="E";		
			}
		else if(date.charAt(i)==5)
			{
			str="F";		
			}
		else if(date.charAt(i)==6)
			{
			str="G";		
			}
		else if(date.charAt(i)==7)
			{
			str="H";		
			}
		else if(date.charAt(i)==8)
			{
			str="I";		
			}
		else if(date.charAt(i)==9)
			{
			str="J";		
			}
			}
		else
			{
			if(date.charAt(i)==0)
				{
				str=str+"A";		
				}
			else if(date.charAt(i)==1)
				{
				str=str+"B";		
				}
			else if(date.charAt(i)==2)
				{
				str=str+"C";		
				}
			else if(date.charAt(i)==3)
				{	
				str=str+"D";		
				}
			else if(date.charAt(i)==4)
				{
				str=str+"E";		
				}
			else if(date.charAt(i)==5)
				{
				str=str+"F";		
				}
			else if(date.charAt(i)==6)
				{
				str=str+"G";		
				}
			else if(date.charAt(i)==7)
				{
				str=str+"H";		
				}
			else if(date.charAt(i)==8)
				{
				str=str+"I";		
				}
			else if(date.charAt(i)==9)
				{
				str=str+"J";		
				}
		}
		
		}

	return str;
}