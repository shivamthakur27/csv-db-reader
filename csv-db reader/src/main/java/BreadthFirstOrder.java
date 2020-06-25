import java.util.LinkedList;
import java.util.Queue;

class Node
{
    int value;
    Node left, right;

    public Node(int data) {
        value = data;
        left = null;
        right = null;
    }
}

public class BreadthFirstOrder
{
    private static Node root;

    private static void breadthFirstOrderTraversal()
    {
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty())
        {
            Node tempNode = queue.poll();
            System.out.print(tempNode.value + " " + "-> ");

            if (tempNode.left != null) {
                queue.add(tempNode.left);
            }

            if (tempNode.right != null) {
                queue.add(tempNode.right);
            }
        }
    }
    public static void main(String[] args)
    {
        BreadthFirstOrder.root = new Node(1);
        BreadthFirstOrder.root.right = new Node(2);
        BreadthFirstOrder.root.right.right = new Node(5);
        BreadthFirstOrder.root.right.right.right = new Node(6);
        BreadthFirstOrder.root.right.right.left = new Node(3);
        BreadthFirstOrder.root.right.right.left.right = new Node(4);
        breadthFirstOrderTraversal();
    }
}
