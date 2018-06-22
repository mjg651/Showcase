import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Solution {

	public static void main(String[] args) {
		TwoThreeTree ttt = new TwoThreeTree();//instantiate the tree

		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			int n = Integer.parseInt(br.readLine());// num problems;
			int i = 0;
			
			while(i<n) {
				String read = br.readLine();//line
				String readA[] = read.split("\\ ");
				if(Integer.parseInt(readA[0])==1) {//case 1 insert
					twothree.insert(readA[1], Integer.parseInt(readA[2]), ttt);
					i++;
				}else if(Integer.parseInt(readA[0])==2) {//case 2 range update
					String start = readA[1];
					String end = readA[2];
					if (start.compareTo(end) > 0) {//swap if start greater than end
	            		String temp = end;
	            		end = start;
	            		start = temp;
	            	}
					updateNode(start,end,"",ttt.root.guide,Integer.parseInt(readA[3]),ttt.height,ttt.root);
					i++;
				}else if (Integer.parseInt(readA[0])==3) {//case 3 search
					Search(readA[1],ttt.root,ttt.height,0);
					i++;
				}
				
			}
		}catch(Exception e) {
			System.err.println("Incorrect Input: " + e.getMessage());
		}
		
	}

	static void updateNode(String start, String end, String min, String max, int v,int h, Node n) {//Lazy without pathfinding
		if (h==0){
			if ((n.guide.compareTo(end) <= 0) && (n.guide.compareTo(start) >= 0)){
				n.value += v;
			}
			return;
		}
	
		if(start.compareTo(min) <= 0 && end.compareTo(max) >= 0) {//(min,max] is an element of the range
				n.value += v;
		}else if ((start.compareTo(max) <= 0) && (end.compareTo(min) > 0)) {//(min,max] is not in the range
			InternalNode p = (InternalNode) n;
				if(p.child2 ==null) {
				updateNode(start,end,min,p.child0.guide,v,h-1,p.child0);
				updateNode(start,end,p.child0.guide,p.child1.guide,v,h-1,p.child1);
				}else if(p.child2 !=null) {
					updateNode(start,end,min,p.child0.guide,v,h-1,p.child0);
					updateNode(start,end,p.child0.guide,p.child1.guide,v,h-1,p.child1);
					updateNode(start,end,p.child1.guide,p.child2.guide,v,h-1,p.child2);
				}
		}
	}
	
	
	static void Search(String x,Node n,int h,int y) {//Search edited to keep track of range additions
		if(h>0) {
			InternalNode p = (InternalNode) n;
			if(x.compareTo(p.child0.guide)<=0) {
				Search(x,p.child0,h-1,y+p.value);
			}else if(x.compareTo(p.child1.guide)<=0||p.child2==null) {
				Search(x,p.child1,h-1,y+p.value);
			}else {
				Search(x,p.child2,h-1,y+p.value);
			}
		}else {
			if(x.compareTo(n.guide)==0) {
				System.out.println(n.value+y);
			}else {
				System.out.println("-1");
			}
		}
	}
}

class twothree {

	   static void insert(String key, int value, TwoThreeTree tree) {
	   // insert a key value pair into tree (overwrite existing value
	   // if key is already present)

	      int h = tree.height;

	      if (h == -1) {
	          Node newLeaf = new Node();//Removed Leaf node because every node has a value now
	          newLeaf.guide = key;
	          newLeaf.value = value;
	          tree.root = newLeaf; 
	          tree.height = 0;
	      }
	      else {
	         WorkSpace ws = doInsert(key, value, tree.root, h);

	         if (ws != null && ws.newNode != null) {
	         // create a new root

	            InternalNode newRoot = new InternalNode();
	            if (ws.offset == 0) {
	               newRoot.child0 = ws.newNode; 
	               newRoot.child1 = tree.root;
	            }
	            else {
	               newRoot.child0 = tree.root; 
	               newRoot.child1 = ws.newNode;
	            }
	            resetGuide(newRoot);
	            tree.root = newRoot;
	            tree.height = h+1;
	         }
	      }
	   }

	   static WorkSpace doInsert(String key, int value, Node p, int h) {
	   // auxiliary recursive routine for insert

	      if (h == 0) {
	         // we're at the leaf level, so compare and 
	         // either update value or insert new leaf

	         Node leaf = (Node) p; //downcast
	         int cmp = key.compareTo(leaf.guide);

	         if (cmp == 0) {
	            leaf.value = value; 
	            return null;
	         }

	         // create new leaf node and insert into tree
	         Node newLeaf = new Node();
	         newLeaf.guide = key; 
	         newLeaf.value = value;

	         int offset = (cmp < 0) ? 0 : 1;
	         // offset == 0 => newLeaf inserted as left sibling
	         // offset == 1 => newLeaf inserted as right sibling

	         WorkSpace ws = new WorkSpace();
	         ws.newNode = newLeaf;
	         ws.offset = offset;
	         ws.scratch = new Node[4];

	         return ws;
	      }
	      else {
	         InternalNode q = (InternalNode) p; // downcast
	         int pos;
	         WorkSpace ws;
	         if (q.child2 == null) {
	         q.child0.value += q.value;//Keeping track of value throughout the tree since every node has value
	         q.child1.value += q.value;
	         } else if (q.child2 != null){
	        	 q.child0.value += q.value;
		         q.child1.value += q.value;
	        	 q.child2.value += q.value; 
	         }
	         q.value = 0;
	         
	         
	         if (key.compareTo(q.child0.guide) <= 0) {
	            pos = 0; 
	            ws = doInsert(key, value, q.child0, h-1);
	         }
	         else if (key.compareTo(q.child1.guide) <= 0 || q.child2 == null) {
	            pos = 1;
	            ws = doInsert(key, value, q.child1, h-1);
	         }
	         else {
	            pos = 2; 
	            ws = doInsert(key, value, q.child2, h-1);
	         }

	         if (ws != null) {
	            if (ws.newNode != null) {
	               // make ws.newNode child # pos + ws.offset of q

	               int sz = copyOutChildren(q, ws.scratch);
	               insertNode(ws.scratch, ws.newNode, sz, pos + ws.offset);
	               if (sz == 2) {
	                  ws.newNode = null;
	                  ws.guideChanged = resetChildren(q, ws.scratch, 0, 3);
	               }
	               else {
	                  ws.newNode = new InternalNode();
	                  ws.offset = 1;
	                  resetChildren(q, ws.scratch, 0, 2);
	                  resetChildren((InternalNode) ws.newNode, ws.scratch, 2, 2);
	               }
	            }
	            else if (ws.guideChanged) {
	               ws.guideChanged = resetGuide(q);
	            }
	         }

	         return ws;
	      }
	   }
