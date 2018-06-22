import java.io.*;
public class Solution {
	public static char[][] table1;//keep track of mismatches; i is top mis, j is bottom, n is no mis
	public static int[][] table2;//keep track of scores
	
	public static void main(String[] args) {
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader (System.in));
			String read = br.readLine();//First set
			String line_1 = read;
			read = br.readLine();//second set;
			String line_2 = read;
			table1 = new char[line_1.length()+1][line_2.length()+1];
			table2 = new int[line_1.length()+1][line_2.length()+1];
			int answer = optimalScore(line_1,line_2);
			System.out.println(answer);
			String answer2=optimalAlign(line_1,line_2);
			System.out.println(answer2);
		}catch (IOException e){
			System.err.println("Incorrect Input: " + e.getMessage());
		}
	}
	
	public static int optimalScore(String line_1, String line_2){//Dynamic iterative programming imp of getting optimal score
		int score = 0;
		int one;
		int two;
		int three;
		for (int  i = 0; i <= line_1.length() ; i++){//terminate when bottom right reached
			for (int j = 0; j <= line_2.length(); j++){
				if (j==0 && i==0){
					table2[i][j] = 0;
				}else if(i==0){//pop outside perim of table
					table2[i][j] = table2[i][j-1] -1;
				}else if(j==0){
					table2[i][j] = table2[i-1][j]-1;
				}else{//inside table
					if(line_2.charAt(j-1) == line_1.charAt(i-1)){
						score = 2; //match
					}else{
						score = -1;//mismatch
					}
					 one = table2[i][j-1] - 1;
					 two = table2[i-1][j] - 1;
					 three = table2[i-1][j-1] + score;
					 int max = Math.max(Math.max(one,two), three);//check the max btwn the 3 to and initiate update rule
					if(max == one){
						table2[i][j] = one;
						table1[i][j] = 'i';
					}else if(max == two){
						table2[i][j] = two;
						table1[i][j] = 'j';
					}else{
						table2[i][j] = three;
						table1[i][j] = 'n';
					}
				}
			}
		}
		 score =table2[line_1.length()][line_2.length()];
		return score;
	}
	
	public static String optimalAlign(String line_1, String line_2 ) {//helper method to assist w/ print optimal alignment
		String final_0 = "";
		String final_1 = "";
		String answer="";
		int leng_1 = line_1.length();
		int leng_2 = line_2.length();
		while(leng_1 > 0 && leng_2 > 0){//iterate through both strings
			 if(leng_1 == 0){//2 is longer add spaces
				final_0 = '_' + final_0;
				final_1 = line_2.charAt(--leng_2) + final_1;
				}else if(leng_2 == 0){//is longer
					final_1 = '_' + final_1;
					final_0 = line_1.charAt(--leng_1) + final_0;
			}else{
				if(table1[leng_1][leng_2] == 'i'){//optimal is misaligned 
					final_0 = '_' + final_0;
					final_1 = line_2.charAt(--leng_2) +final_1;
				}else if(table1[leng_1][leng_2] == 'j'){//optimal is misaligned
					final_1 = '_' + final_1;
					final_0 = line_1.charAt(--leng_1) + final_0;
				}else if(table1[leng_1][leng_2] == 'n'){//no mismatch;no space needed
					final_0 = line_1.charAt(--leng_1) + final_0;
					final_1 = line_2.charAt(--leng_2) +final_1;
				}
			}
		}
		answer= final_0+ "\n"+final_1;
		return answer;
	}
}