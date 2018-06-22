import java.io.*;
import java.util.*;

public class Solution {

	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			long n = Integer.parseInt(br.readLine());
			int i = 0;
			while(i<n) {
				String read = br.readLine();
				String[] reada = read.split("\\ ");
				String name = reada[0];
				long score = Integer.valueOf(reada[1]);
				add(name, score);
				i++;
			}

			long l = Long.parseLong(br.readLine());
			int j = 0;
			while(j<l) {
				String read2 = br.readLine();
				String[] read2a = read2.split("\\ ");
				String m = read2a[0];
				if(m.equals("1")) {
					String soldier = read2a[1];
					long score = Long.valueOf(read2a[2]);
					update(soldier, score);
					j++;
				}else if(m.equals("2")) {
					long cutoff = Long.valueOf(read2a[1]);
					cutOff(cutoff);
					j++;
				}
			}
		}catch(Exception e) {
			System.err.println("Incorrect Input: " + e.getMessage());
		}
	}

	private static Hashtable<String, SPARTAN> hash = new Hashtable<String,SPARTAN>();
	private static Heap heap = new Heap(hash);

	private static void add(String designation, long points) {//Adds soldier to heap/hash
		SPARTAN soldier = new SPARTAN(designation, points);
		hash.put(designation, soldier);
		heap.insert(soldier);
	}
	
	private static void update(String name, long eval) {//increase EVAL and change heap if needed
		SPARTAN soldier = hash.get(name);
		if (soldier == null) {
			return;
		}
		soldier.increaseEval(eval);
		heap.fixHeap(soldier);
	}

	private static void cutOff(long cutoff) {//cutsoff and prints the cutoff,
		int n = heap.cutOff(cutoff);
		System.out.println(n);
	}
}


class Heap {
	public ArrayList<SPARTAN> heap;
	private int size;

	private void deleteMin() {//deletes min and sets new root;min heap
		SPARTAN pos = heap.get(0);
		pos.setPos(-1);
		SPARTAN root = heap.get(size - 1);
		root.setPos(0);
		heap.set(0, root);
		bubbleDown(root);
		size--;
	}

	public int cutOff(long cutOff) {//Cut off if eval not met
		while (size > 0) {
			SPARTAN min = heap.get(0);
			if (min.getEval() < cutOff) deleteMin();
			else return size;
		}
		return 0;
	}

	public void insert(SPARTAN soldier) {//standard heap insert
		heap.add(size, soldier);
		soldier.setPos(size);
		bubbleUp(soldier);
		size++;
	}

	private void bubbleUp(SPARTAN soldier) {//bubble up, to maintain heap structure
		SPARTAN current = soldier;
		int b = soldier.getPos() + 1; 
		int parent = (b/ 2 - 1);
		while (parent >= 0) {
			SPARTAN par = heap.get(parent);
			if (par.getEval() > current.getEval()) {
				swap(current, par);
				parent = (((parent+ 1)/ 2)-1);
				continue;
			}else {
				break;
			}
		}
	}  

	private void swap(SPARTAN one, SPARTAN two) {//swap the position of two spartans,physically and numerically
		int temp1 = one.getPos();
		int temp2 = two.getPos();
		heap.set(temp1, two);
		heap.set(temp2, one);
		one.setPos(temp2);
		two.setPos(temp1);
	}

	private void bubbleDown(SPARTAN soldier) {// push down w/child swapping to maintain structure
		SPARTAN current = soldier;
		int pos = soldier.getPos() +1;
		int leftPos = (2 * pos) - 1; 
		int rightPos = (2 * pos) ;

		while (leftPos < size) {
			SPARTAN leftChild = heap.get(leftPos);
			SPARTAN rightChild = null;
			rightPos = leftPos + 1;
			if (rightPos < size) { 
				rightChild = heap.get(rightPos);
			}
			SPARTAN swapChild = null;
			if (rightChild != null) {//See if/which child is smaller then swap
				if (current.getEval() > rightChild.getEval()) {
					swapChild = rightChild;
				}
			} 
			if (current.getEval() > leftChild.getEval()) {
				if (swapChild == null) { 
					swapChild = leftChild;
				}else if (leftChild.getEval() < swapChild.getEval()) {
					swapChild = leftChild;
				}
			}
			if (swapChild == null) break;//no need to pushDown 
			swap(current, swapChild);
			leftPos = (2 * (current.getPos() + 1)) - 1;//update pos to continue loop
		}
	}
	
	public void fixHeap(SPARTAN soldier) {//maintains heap after update
		bubbleUp(soldier);
		bubbleDown(soldier);
	}
	
	Heap(Hashtable<String, SPARTAN> hash) {//Hashtable
		this.hash = hash;
		this.heap = new ArrayList<SPARTAN>();
		this.size = 0;
	}
	
	private Hashtable<String, SPARTAN> hash;

}

class SPARTAN {//Class for each Spartan soldier, along with getters and setters for name pos and eval
	private String name;
	private int pos;
	private long eval;

	SPARTAN(String name, long eval) {
		this.name = name;
		this.eval = eval;
	}

	public String getName() {
		return this.name;
	}

	public int getPos() {
		return this.pos;
	}

	public long getEval() {
		return this.eval;
	}

	public void setName(String name) {
		this.name=name;
	}
	
	public void setPos(int pos) {
		this.pos = pos;
	}

	public void setEval(long eval) {
		this.eval=eval;
	}

	public void increaseEval(long eval) {
		this.eval += eval;
	}
}
