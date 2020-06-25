
public class TreeTraversal
{
    static class Node
    {
        int value;
        Node left;
        Node right;
    };

    static Node newNode(int value)
    {
        Node temp = new Node();
        temp.value = value;
        temp.left = temp.right = null;
        return temp;
    }
    static void traverseLeaves(Node root)
    {
        if (root == null)
            return;

        if (root.left == null && root.right == null)
        {
            System.out.print( root.value +" "+"-> ");
            return;
        }

        if (root.right != null)
            traverseLeaves(root.right);

        if (root.left != null)
            traverseLeaves(root.left);
    }
    public static void main(String[] args)
    {
        Node root = newNode(8);
        root.left = newNode(3);
        root.right = newNode(10);
        root.left.left = newNode(1);
        root.left.right = newNode(6);
        root.right.right = newNode(15);
        root.left.right.left = newNode(4);
        root.left.right.right = newNode(7);
        root.right.right.left = newNode(13);

        traverseLeaves(root);
    }
}
