import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
	public static void main(String[] args) {
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader (System.in));
			String read = br.readLine();
			int n = Integer.parseInt(read)+1;
			read = br.readLine();
			String[] readA= read.split("\\ ");
			int[] a = new int[n];
	    	int[] b = new int[n];
			int i = 0;
			while(i<n) {
				a[i]=Integer.parseInt(readA[i]);
				i++;
			}
			read = br.readLine();
			String[] readB= read.split("\\ ");
			i = 0;
			while(i<n) {
				b[i]=Integer.parseInt(readB[i]);
				i++;
			}
			
			int[] result = karatsuba(a, b, n);
			
	        i =0;
	        while(i<result.length) {
	        	System.out.print(result[i] + " ");
	        	i++;
	        }

		}catch (IOException e){
			System.err.println("Incorrect Input: " + e.getMessage());
		}
	}
	
	public static int[] karatsuba(int[]a, int[]b, int n) {// Standard implementation of karatsuba

		int[] ah = new int[n/2];// high low and mid arrays
		int[] al = new int[n/2];
		int[] bh = new int[n/2];        
		int[] bl = new int[n/2];
		int[] ca = new int[n/2];
		int[] cb = new int[n/2];
		int[] fi = new int[2*n -1];//return array
		if(n <= 8) {//naive multiply when too low
			for(int i = 0; i < n; i++) {
	            for(int j = 0; j < n; j++) {
	                fi[i+j] = fi[i+j] + a[i] * b[j];
	            }
	        }
			return fi;
		}
		for(int i = 0; i < n/2; i++) {
			al[i] = a[i];//lows
			bl[i] = b[i];
		}
		for(int i = n/2; i < n; i++) {
			ah[i-n/2] = a[i];//highs
			bh[i-n/2] = b[i];
		}
		for(int i = 0; i < n/2; i++) {
			ca[i] = al[i] + ah[i];//lows and high
			cb[i] = bl[i] + bh[i];
		}
		int[] H = karatsuba(ah, bh, n/2);//recurse on highs, lows and c
		int[] F = karatsuba(ca, cb, n/2);
		int[] L = karatsuba(al, bl, n/2);
		for(int i = 0; i < n-1; i++) {
			fi[i] = L[i];
		}
		fi[n-1] = 0;
		for(int i = 0; i < n-1; i++) {
			fi[n+i] = H[i];
		}
		for(int i = 0; i < n-1; i++) {
			fi[n/2 + i] += H[i] - (L[i] + H[i]);
		}
		return fi;
	}

}
	

