iimport java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;



public class Solution {

	public static void main(String[] args) {	
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String s[] = br.readLine().split("\\ ");
			int n = Integer.parseInt(s[0]);
			int m = Integer.parseInt(s[1]);
			Graph Graph = new Graph(n);
			int i=0;
			while (i<m){
				String read = br.readLine();
				String readA[] = read.split("\\ ");
				int a = Integer.parseInt(readA[0]);
				int b = Integer.parseInt(readA[1]);
				Graph.insert(a,b);
				i++;
			}
			ArrayList<Integer> ts = LexTopSort(Graph);
			toString(ts,Graph);
		}
		catch(IOException e){
			System.err.println("Incorrect Input: " + e.getMessage());
		}

	}

	public static class vertex {//class for each vertex with value indegree and out degree
		ArrayList<vertex> neighbour = new ArrayList<vertex>();
		public int out;
		public int in;
		public int val;

		public vertex(int val){
			this.out = 0;
			this.in = 0;
			this.val = val;
		}
	}

	
	public static ArrayList<Integer> LexTopSort(Graph g){//imp of top sort using queue and arraylist
		int index = 0;
		ArrayList<Integer> orderedVertices = new ArrayList<Integer>();//array list easier to add 
		PriorityQueue<Integer> zeroVertices = new PriorityQueue<Integer>(g.size);//standard java queue;no need to imp whole heap
		inAdd(zeroVertices,g);

		while(zeroVertices.isEmpty()!=true){//queue not empty
			index = zeroVertices.poll();
			vertex v = g.vertices[index];
			orderedVertices.add(v.val);
			for(int i = 0; i < v.neighbour.size(); i++){//neighbour of next vertex
				if (v.neighbour.get(i) != null){
					v.neighbour.get(i).in -=1; // indegree reduced
					if (v.neighbour.get(i).in == 0){//add to queue
						zeroVertices.add(v.neighbour.get(i).val);
					}
				}
			}
		}
		return orderedVertices;
	}

	public static void toString(ArrayList<Integer> vertexL, Graph g) {//aux function to print contents
		if(vertexL.size()>g.size-1){//potion completed
			for (int n = 0; n < vertexL.size(); n++){
				System.out.print(vertexL.get(n) + " ");
			}
		}
		else{//print -1 if stages not met
			System.out.println(-1);
		}
	}

	public static void inAdd(PriorityQueue<Integer> indegreeZ, Graph g) {//adds w/ indgree 0 to queue
		for(int i = 0; i < g.size; i++){
			if(g.vertices[i+1].in == 0){
				indegreeZ.add(g.vertices[i+1].val);
			}
		}
	}
	
	public static class Graph {//class for graph; contains list of vertices and their respective completion status
		int size;
		vertex[] vertices;
		boolean[] vList;

		public Graph(int size){//no default constructor needed
			this.size = size;
			this.vertices = new vertex[size+1];
			this.vList = new boolean[size+1];
		}
		
		public void insert(int a, int b){//inserts a new vertex into the vertices and checks for the existence of vertices
			vertex va;
			vertex vb;
			if (!vList[a]){//checks if exists 
				va = new vertex(a);
				vertices[a] = va;
				vList[a] = true;
			}else{
				va = vertices[a];
			}
			if (!vList[b]){
				vb = new vertex(b);
				vertices[b] = vb;
				vList[b] = true;
			}else{
				vb = vertices[b];
			}
			if(!va.neighbour.contains(vb)){//adds if neighbor not vb
				va.neighbour.add(vb);
			}
			if(!vb.neighbour.contains(va)){//adds if neighbor not va
				vb.neighbour.add(va);
			}
			vb.in++;
			va.out++;
		}
	}

}