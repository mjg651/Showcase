import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

public class Solution {
	static int INFIN = Integer.MAX_VALUE;//value for INFINITY
	public static void main(String[] args) {

		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String read = br.readLine();
			String[] readA= read.split("\\ ");
			int n = Integer.parseInt(readA[0]);//# cross roads
			Graph newGraph = new Graph((2*n)+1);
			int m = Integer.parseInt(readA[1]);
			int i = 0;
			while(i<m){
				String r = br.readLine();
				readA= r.split("\\ ");
				int a = Integer.parseInt(readA[0]);//roads
				int b = Integer.parseInt(readA[1]);
				int c = Integer.parseInt(readA[2]);//ent status
				if (a != b){
					newGraph.insert(a, b, c);
					int an = a + n;
					int bn = b + n;
					newGraph.insert(an, bn, c);
				}
				i++;
			}
			int iteration = minRoads(newGraph);
			System.out.println(iteration);
		}catch(IOException e){
			System.err.println("Incorrect Input: " + e.getMessage());
		}
	}
	
	static class Heap { //nearly identical imp to making the SPARTAN
		public ArrayList<vertex> heap;
		public int size;

		private vertex deleteMin(){//deletes min and sets new root;min heap; changed to return old min
			vertex pos = heap.get(0);
			heap.get(0).position = -1;
			vertex root = heap.get(size - 1);
			root.position=0;
			heap.set(0, root);
			size--;
			pushDown(root);
			return pos;
		}

		private void insert(vertex vert){//standard heap insert
			heap.add(size,vert);
			vert.position = size;
			size++;
			bubbleUp(vert);
		}

		private void bubbleUp(vertex vert){//bubble up, to maintain heap structure
			vertex current = vert;
			int b = vert.position + 1; 
			int parent = (b/ 2 - 1);
			while (parent > 1){
				vertex vertex = heap.get(parent);
				if(vertex.distance > current.distance){
					swap(current,vertex);
					parent = (((parent+1)/2)-1);
				}
				else{
					break;
				}
			}
		}

		private void swap(vertex one, vertex two){//swap the position of two vertices,physically and numerically
			int temp1 = one.position;
			int temp2 = two.position;
			heap.set(temp2, one);
			heap.set(temp1, two);
			one.position=temp2;
			two.position=temp1;
		}

		private void pushDown(vertex v){// push down w/child swapping to maintain structure
			vertex current = v;
			int leftPos = (2*(v.position+1))-1;
			int rightPos = leftPos+1;
			while(leftPos < size){
				vertex leftChild = heap.get(leftPos);
				vertex rightChild = null;
				rightPos = leftPos+1;
				if(rightPos< size){
					rightChild = heap.get(rightPos);
				}
				vertex swapChild = null;
				if(rightChild != null) { //See if/which child is smaller then swap
					if(current.distance > rightChild.distance){
						swapChild = rightChild;
					}
				}
				if(current.distance > leftChild.distance) { 
					if(swapChild==null){
						swapChild = leftChild;
					}else if(leftChild.distance < rightChild.distance) {
						swapChild = leftChild; 
					}
				}
				if(swapChild == null) break; //no need to pushDown
				swap(current,swapChild);
				leftPos = (2 * (current.position + 1)) - 1; //update pos to continue loop
			}
		}
		Heap() {
			this.heap = new ArrayList<vertex>();
			this.size = 0;
		}
	}

	public static class vertex{//class for each vertex w/ position distance and it's value
		ArrayList<vertex> successor = new ArrayList<vertex>();//list of vertices connected
		ArrayList<Integer> edges = new ArrayList<Integer>();//list of amount of edges
		public int distance;
		public int position;
		public int val;
		public vertex(int val){
			this.val = val;
			this.distance = INFIN;
		}
	}

	public static class Graph{//class for graph; contains list of vertices and their respective completion status; sim to Making Felix Felices
		 int size;
		 vertex[] vertices;
		 boolean[] vList;

		public Graph(int size){//no default constructor neeeded
			this.vertices = new vertex[size+1];
			this.vList = new boolean[size+1];
			this.size = size;
		}
		public void insert(int a, int b, int c){//inserts a new vertex into the vertices and it's state of comp
			vertex va = null;
			vertex vb = null;

			if (!vList[a]){//not done
				va = new vertex(a);
				vertices[a] = va;
				vList[a] = true;
			}else{//done
				va = vertices[a];
			}
			if (!vList[b]){//done
				vb = new vertex(b);
				vertices[b] = vb;
				vList[b] = true;
			}else{//not done
				vb = vertices[b];
			}
			va.successor.add(vb);
			va.edges.add(c-1);
		}
	}

	public static int djikikstra(vertex vertex){// Standard Dijikstra
		int paths = 0;
		vertex.distance = 0;
		Heap Q = new Heap();
		Q.insert(vertex);
		while(Q.size > 0){
			vertex u = Q.deleteMin();
			for (int v = 0; v < u.successor.size(); v++){//explore edge u->v
				vertex ua = u.successor.get(v);
				int weight = u.edges.get(v);
				if(u.distance + weight < ua.distance){
					if(ua.distance == INFIN)Q.insert(ua);
					ua.distance = u.distance + weight;
				}
				if(weight == 0){
					paths++;
				}
			}
		}
		return paths;
	}

	public static int minRoads(Graph graph){ //iterate through the graph to print minimum of parched roads
		for(int i = 1; i <= ((graph.size/2)-1); i++){
			vertex a = graph.vertices[i];
			int size = a.successor.size();
			for(int j = 0; j < size; j++){
				vertex b = a.successor.get(j);
				int weight = a.edges.get(j);
				if(weight == 0){
					graph.insert(a.val, (b.val+((graph.size-1)/2)), weight);//inserts if edgeweight = 0; there is a shortest path
				}
			}
		}
		int djk = djikikstra(graph.vertices[1]);// run to find shortest
		if(graph.vertices[((graph.size-1) / 2)*2].distance == INFIN || djk == 0){// no path
			return -1;
		}else {
		return graph.vertices[((graph.size-1)/2)*2].distance+1;//minimum parched roads
		}
	}
}
