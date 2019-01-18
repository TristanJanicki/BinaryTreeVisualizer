package comp2402a6;

import java.util.*;

public class GeometricTree extends BinaryTree<GeometricTreeNode> {

	public GeometricTree() {
		super (new GeometricTreeNode());
	}
	
	public void inorderDraw() {

		Stack<GeometricTreeNode> s = new Stack<>();

		GeometricTreeNode curr = r;

		r.position.y = 0;
		int count = 0;

		while(curr != nil || s.size()>0){
			while(curr != nil){
				s.push(curr);
				if(curr != r){
					curr.position.y = curr.parent.position.y + 1;
				}
				curr = curr.left;
			}
			curr = s.pop();
			curr.position.x = count;
			count ++;
			curr = curr.right;
		}


		/*
		int value = 0;
		GeometricTreeNode curr = firstNode();
		curr.position.x = value;
		curr.position.y = depth(curr);
		while(nextNode(curr) != null){
			curr = nextNode(curr);
			curr.position.x = ++ value;
			curr.position.y = depth(curr);
		}*/
	}


	
	protected void randomX(GeometricTreeNode u, Random r) {
		if (u == null) return;
		u.position.x = r.nextInt(60);
		randomX(u.left, r);
		randomX(u.right, r);
	}


	protected void assignLevels() {
		assignLevels(r, 0);

	}


	protected void assignLevels(GeometricTreeNode u, int i) {
		if (u == null) return;

		Queue<GeometricTreeNode> q = new LinkedList<>();

		if (u != nil) {
			q.add(u);
			u.position.y = i;

		}


		while (!q.isEmpty()) {
			GeometricTreeNode t = q.remove();
			if (t.left != nil) {
				q.add(t.left);
				t.left.position.y = t.position.y+1;
			}
			if (t.right != nil) {
				q.add(t.right);
				t.right.position.y = t.position.y+1;

			}

		}

	}

	/**
	 * Draw each node so that it's x-coordinate is as small
	 * as possible without intersecting any other node at the same level 
	 * the same as its parent's
	 */
	public void leftistDraw() {
		Queue<GeometricTreeNode> q = new LinkedList<GeometricTreeNode>();
		int height = 0, count = 0;


		r.position.y = 0;

		q.add(r);

		while(!q.isEmpty()){
			GeometricTreeNode u = q.remove();
			if(u!= r){
				u.position.y = u.parent.position.y + 1;
			}

			if(u.position.y != height){
				count = 0;
				height = u.position.y;
			}

			u.position.x = count;
			count ++;

			if(u.left != nil){
				q.add(u.left);
			}
			if(u.right != nil){
				q.add(u.right);
			}
		}


		/*HashMap<Integer, Set<GeometricTreeNode>> depthMap = new HashMap<>();

		LinkedHashSet<GeometricTreeNode> level0 = new LinkedHashSet<>();
		level0.add(r);
		depthMap.put(0, level0);

		if(r != nil) q.add(r);

		while(!q.isEmpty()){

			GeometricTreeNode u = q.remove();

			if(u.left != nil){
				q.add(u.left);
				int ld = depth(u.left);
				if(depthMap.containsKey(ld)){
					depthMap.get(ld).add(u.left);
				}else{
					Set<GeometricTreeNode> ll = new LinkedHashSet<>();
					ll.add(u.left);
					depthMap.put(ld, ll);
				}
			}
			if(u.right != nil){
				q.add(u.right);
				int rd = depth(u.right);
				if(depthMap.containsKey(rd)){
					depthMap.get(rd).add(u.right);
				}else{
					Set<GeometricTreeNode> ll = new LinkedHashSet<>();
					ll.add(u.right);
					depthMap.put(rd,ll);
				}

			}

		}

		depthMap.forEach((k,v)->{


			int posX = 0;

			for (GeometricTreeNode n : v){
				n.position.y = k;
				n.position.x = posX;
				posX ++;
			}


		});*/
	}

	public int nrSize2(GeometricTreeNode u ){
		GeometricTreeNode prev = nil, next;
		int n = 0;

		while (u != null){
			if(prev == u.parent){
				n++;
				if(u.left != nil) next = u.left;
				else if(u.right != nil) next = u.right;
				else next = u.parent;
			}else if (prev == u.left){
				if(u.right != nil) next = u.right;
				else next = u.parent;
			}else{
				next = u.parent;
			}
			prev = u;
			u = next;
		}
		return n;
	}


	GeometricTreeNode pickLarger(GeometricTreeNode n1, GeometricTreeNode n2){
		if(n1 == null && n2 != null){
			return n2;
		}else if(n1 != null && n2 == null){
			return n1;

		}

		if(n1 == null && n2 == null){
			return null;
		}

		if(nrSize2(n1) > nrSize2(n2)){
			return n1;
		}else{
			return n2;
		}

	}

	HashMap<GeometricTreeNode, Integer> calcDepths() {
		HashMap<GeometricTreeNode, Integer> m = new HashMap<>();
		Stack<GeometricTreeNode> S = new Stack<GeometricTreeNode>();
		ArrayList<GeometricTreeNode> l = new ArrayList<>();

		// Check for empty tree
		if (r == null)
			return m;
		S.push(r);
		GeometricTreeNode prev = null;
		// <editor-fold desc="First Iteration To Calc Depths">
		while (!S.isEmpty()) {
			GeometricTreeNode current = S.peek();

            /* go down the tree in search of a leaf an if so process it
            and pop stack otherwise move down */
			if (prev == null || prev.left == current || prev.right == current) {
				if (current.left != null) {
					S.push(current.left);
					//l.add(current.left);
				} else if (current.right != null) {
					S.push(current.right);
					//l.add(current.right);
				} else {
					S.pop();
					m.put(current, nrSize2(current));
					l.add(current);
				}

                /* go up the tree from left node, if the child is right
                   push it onto stack otherwise process parent and pop
                   stack */
			} else if (current.left == prev) {
				if (current.right != null)
					S.push(current.right);
				else {
					S.pop();
					m.put(current, nrSize2(current));
					l.add(current);
				}

                /* go up the tree from right node and after coming back
                 from right node process parent and pop stack */
			} else if (current.right == prev) {
				S.pop();
				m.put(current, nrSize2(current));
				l.add(current);
			}

			prev = current;
		}
		// </editor-fold>
		return m;
	}

	public void balancedDraw() {
		assignLevels();
		HashMap<GeometricTreeNode,Integer> nodeMap =new HashMap<GeometricTreeNode,Integer>();
		GeometricTreeNode u = r, prev = nil, next;
		GeometricTreeNode sTree1;//right
		GeometricTreeNode sTree2;//left

		int x = 0;
		int y = 0;

		while (u != nil) {
			if (prev == u.parent) {
				if (u.left != nil) {
					next = u.left;
				}
				else if (u.right != nil){
					next = u.right;
				}
				else{
					next = u.parent;
					nodeMap.put(u,1);
				}
			} else if (prev == u.left) {
				if (u.right != nil){
					next = u.right;
				}
				else {
					next = u.parent;
					nodeMap.put(u,nodeMap.get(u.left)+1);
				}
			} else {
				next = u.parent;
				if(u.left!=nil){
					nodeMap.put(u,nodeMap.get(u.right)+nodeMap.get(u.left)+1);
				}else{
					nodeMap.put(u,nodeMap.get(u.right)+1);
				}

			}
			prev = u;
			u = next;
		}

		u = r; //reset u to traverse a second time.
		prev = nil; //reset

		while (u != nil) {
			if(u.right==nil &&u.left==nil){
				sTree1 = nil;
				sTree2 = nil;
			}
			else if(u.right==nil){
				sTree1 = u.left;
				sTree2 = nil;
			}
			else if(u.left==nil){
				sTree1 = u.right;
				sTree2 = nil;
			}
			else if(nodeMap.get(u.left)>=nodeMap.get(u.right)){
				sTree1=u.left;
				sTree2=u.right;
			}
			else{
				sTree1 = u.right;
				sTree2 = u.left;
			}

			if (prev == u.parent) {
				u.position.y = y;
				u.position.x = x;

				if (sTree2 != nil) {
					next =sTree2;
					y++;
				}
				else if (sTree1 != nil){
					next = sTree1;
					x++;
				}
				else{
					next = u.parent;
				}
			} else if (prev == sTree2) {
				y--;
				if (sTree1 != nil){
					next = sTree1;
					x++;
				}
				else {
					next = u.parent;
				}
			} else {
				next = u.parent;
			}
			prev = u;
			u = next;
		}
	}

		//</editor-fold>





		/*HashMap<GeometricTreeNode, Integer> nodeSizeMapp = new HashMap<>();

		ArrayList<Object[]> nodeSizeArr = new ArrayList<>();

		int x = 0;
		int y = 0;

		GeometricTreeNode curr = firstNode();

		r.position.x = 0;
		r.position.y = 0; // might not be right. R might be supposed to end up somewhere else.

		do{

				nodeSizeMapp.put(curr, nrSize2(curr));
				nodeSizeArr.add(new Object[]{curr, nrSize2(curr)});

		}while((curr = nextNode(curr)) != null);


		curr = r;
		while(nextNode(curr) != nil){

			System.out.println("Compairing " + nodeSizeMapp.get(curr.left) + " to " + nodeSizeMapp.get(curr.right));

			if(curr.left != null && curr.right != null) {

				if (nodeSizeMapp.get(curr.left) < nodeSizeMapp.get(curr.right)) {
					curr.left.position.y = curr.position.y + 1;
					curr.right.position.x = curr.position.x + 1;
				} else {

					curr.right.position.x = curr.position.x + 1;
					curr.left.position.y = curr.position.y + 1;

				}
			}else{
				if(curr.left != null){
					curr.left.position.x = curr.position.x + 1;
				}

				if(curr.right != null){
					curr.right.position.x = curr.position.x + 1;
				}
			}
			curr = nextNode(curr);*/



	public static void main(String[] args) {
		GeometricTree t = new GeometricTree();
		galtonWatsonTree(t, 100);
		System.out.println(t);
		t.inorderDraw();
		System.out.println(t);
	}
	
}
