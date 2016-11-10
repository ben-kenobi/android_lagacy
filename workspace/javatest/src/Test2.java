import java.io.File;
import java.util.Arrays;



public class Test2 {
	public static void main(String[] args) {
//		System.out.println(showEditDistanceSequence("awerwera", "arewarew"));
		System.out.println(new File("F:tddownload/The.Hobbit-The.Desolation.of.Smaug.avi").isDirectory());
	}
	
	public static int showEditDistanceSequence(String s1,String s2){
		int ed=calEditDistance1(s1, s2);
		if(ed<1) return ed;
		char c ;
		char[] chary2=s2.toCharArray();
		int count=0;
		for(int i=ed-1;i>=0;i--){
			c=s1.charAt(i);
			int index = lastIndexOfchar(c, chary2, ed-1-i);
			if(index!=-1){
				char temp1=chary2[index],
						temp2;
				for(int j=0;j<=index;j++){
					temp2=chary2[j];
					chary2[j]=temp1	;
					temp1=temp2;
				}
				System.out.println(Arrays.toString(chary2)+"   @"+(++count));
			}
		}
		
		
		return ed;
	}
	
	public static int lastIndexOfchar(char c,char[] chary,int endOffset){
		for(int i=chary.length-1;i>=endOffset;i--){
			if(chary[i]==c)
				return i;
		}
		return -1;
	}
	
	public static int calEditDistance1(String s1,String s2){
		int index,count=s1.length()-1;
		char c=s1.charAt(s1.length()-1);
		index=s2.lastIndexOf(c, s1.length()-1);
		for(int i=s1.length()-2;i>=0;i--){
			c=s1.charAt(i);
			int ind=s2.lastIndexOf(c,index-1);
			if(index==-1)
				return count;
			if(ind>index)
				return count;
			count--;
			index=ind;
		}
		return count;
	}
	
//	public static int calEditDistance1(String s1,String s2){
//		int[] indexOfs1=new int[s1.length()];
//		Arrays.fill(indexOfs1, -1);
//		char c2;
//		int index;
//		for(int i=0;i<s2.length();i++){
//			c2=s2.charAt(i);
//			index=s1.indexOf(c2, 0);
//			while(contains(indexOfs1, index)){
//				index=s1.indexOf(c2, index);
//			}
//			indexOfs1[i]=index;
//		}
//	}
	
	public static boolean contains(int[] ary,int tar){
		for(int i=0;i<ary.length;i++){
			int in=ary[i];
			if(in<0) return false;
			if(in==tar) return true;
		}
		return false;
	}
	
	public static int calEditDistance(String s1,String s2){
		int length1=s1.length();
		int length2=s2.length();
		int[][] fn=new int[length1+1][length2+1];
		for(int i=0;i<=length1;i++)
			fn[i][0]=i;
		for(int i=0;i<=length2;i++)
			fn[0][i]=i;
		char c1,c2;
		for(int i=0;i<length1;i++){
			c1=s1.charAt(i);
			for(int j=0;j<length2;j++){
				c2=s2.charAt(j);
				if(c1==c2){
					fn[i+1][j+1]=fn[i][j];
				}else{
					int replace=fn[i][j]+1;
					int insert=fn[i+1][j]+1;
					int delete=fn[i][j+1]+1;
					int min=replace>insert?insert:replace;
					min = min>delete?delete:min;
					fn[i+1][j+1]=min;
				}
			}
		}
		return fn[length1][length2];
	}
}
