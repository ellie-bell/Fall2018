package cs445.a5;

public class A5Client{
	public static void main (String[] args){
		//test constructor with no parameters
		try{
			TernaryTree<Integer> testnull = new TernaryTree<Integer>();
			int t = testnull.getRootData();
		}
		catch (EmptyTreeException e){
			System.out.println("test empty tree, first constructor works");
		}
		
		//test constructor with just root data
		TernaryTree<Integer> test_one =  new TernaryTree<Integer>(1);
		if(test_one.getRootData() == 1){
			System.out.println("second constructor works");
		}
		
		//test the clear method
		try{
			test_one.clear();
			if(test_one.getRootData() == null){
				System.out.println("Did not catch exception");
			}
		}
		catch(EmptyTreeException e){
			System.out.println("Clear works");
		}
		
		//test is empty method
		test_one.clear();
		if(test_one.isEmpty()){
			System.out.println("isEmpty is working");
		}
		
		TernaryNode<Integer> n_one = new TernaryNode<Integer>(2);
		TernaryNode<Integer> n_two = new TernaryNode<Integer>(3);
		TernaryNode<Integer> n_three = new TernaryNode<Integer>(4);
		
		TernaryTree<Integer> test_three = new TernaryTree<Integer>(1, new TernaryTree<Integer>(2)
				, new TernaryTree<Integer>(3), new TernaryTree<Integer>(4));
		if(!test_three.isEmpty()){
			System.out.println("full constructor works");
		}
	}
}