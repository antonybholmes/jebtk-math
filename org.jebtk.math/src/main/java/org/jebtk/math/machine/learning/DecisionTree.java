package org.jebtk.math.machine.learning;

import org.jebtk.core.tree.TreeNode;

public class DecisionTree extends TreeNode<Decision> {
	
	private static final long serialVersionUID = 1L;

	public DecisionTree(String name) {
		super(name);
	}

	public DecisionTree(String name, Decision d) {
		super(name, d);
	}

	public String classify(double[] values) {
		return classify(this, values);
	}
	
	//public String classify(String[] values) {
	//	return classify(this, values);
	//}
	
	/**
	 * Classify some data based on a decision tree.
	 * 
	 * @param root
	 * @param values
	 * @return
	 */
	public static String classify(TreeNode<Decision> root, double[] values) {
		
		TreeNode<Decision> current = root;
		
		while (current.getValue() != null) {
			Decision decision = current.getValue();
			
			int attIdx = decision.getAttIdx();
			
			double v = values[attIdx];
			
			int child;
			
			if (v <= decision.getPivot()) {
				child = 0;
			} else {
				child = 1;
			}
			
			// Move along to the next decision node.
			current = current.getChild(child);
		}
		
		return current.getName();
	}
}
