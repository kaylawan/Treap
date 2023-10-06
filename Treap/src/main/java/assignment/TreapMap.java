package assignment;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class TreapMap<K extends Comparable<K>, V> implements Treap<K,V>{
    private Node root;
    //default constructor
    public TreapMap() {
        root = null;
    }
    //constructor that sets a root
    public TreapMap(Node root){
        this.root = root;
    }
    @Override
    public V lookup(K k) {
        if(k==null || root ==null) return null;
        if(k instanceof K == false) return null;
        Node currentNode = root;
        //searches left/right side depending on if the current node is above/below the desired value
        while(!currentNode.k.equals(k)){
            if((k.compareTo(currentNode.k))>0){
                if(currentNode.right==null){
                    return null;
                }
                currentNode = currentNode.right;
            }
            else {
                if(currentNode.left==null){
                    return null;
                }
                currentNode = currentNode.left;
            }
        }
        //found the node
        return (V) currentNode.v;
    }
    @Override
    public void insert(K k, V v) {
        if (k==null || v==null) {
            return;
        }
        if(!(k instanceof K) || !(v instanceof V)) return;
        //if there is nothing yet, set the node to the root
        if(root == null){
            root = new Node(k, v);
            return;
        }
        insertHelper(root, k, v);
    }
    private void insertHelper(Node node, K k, V v) {
        //when the key is the key of the initial node
        //swap the original value of the key with input value
        if(node.k.equals(k)){
            node.v = (V)v;
            return;
        }
        //if the key is less than the correct node's key
        if(k.compareTo(node.k)<0){
            //if left node is null, place node there
            if(node.left == null) {
                node.setLeft(new Node(k, v));
                heapify(node.left);
                return;
            }
            //if direct left node is not available, checks again from the left node
            insertHelper(node.left, k, v);
            return;
        }
        if(node.right == null){
            //if the right node is available, insert node there
            node.setRight(new Node( k, v));
            heapify(node.right);
            return;
        }
        //if right node is not available, recurse again to place left/right
        insertHelper(node.right, k, v);
    }

    //Rotate left around a given node
    private void rotateLeft(Node node){
        Node root = node.parent;
        Node leftNode = node.left;
        //checks for open spot
        if(root.parent == null){
            this.root = node;
            node.parent = null;
        }
        else if(root.parent.left != null && root.parent.left.equals(root)){
            root.parent.setLeft(node);
        }
        else{
            root.parent.setRight(node);
        }
        node.setLeft(root);
        root.setRight(leftNode);
    }
    //rotate right around a given node
    private void rotateRight(Node node){
        Node root = node.parent;
        Node rightNode = node.right;
        if(root.parent == null){
            this.root = node;
            node.parent = null;
        }
        else if(root.parent.right != null &&
                root.parent.right.equals(root)){
            root.parent.setRight(node);
        }
        else {
            root.parent.setLeft(node);
        }
        node.setRight(root);
        root.setLeft(rightNode);
    }
    @Override
    public V remove(K k) {
        //input validation
        if(k==null || lookup(k)==null || findNode(root, k) == null) return null;
        if(!(k instanceof K)) return null;
        Node node = findNode(root, k);
        return removeHelper(node);
    }
    private V removeHelper(Node node){
        //Base Case
        //Node is a leaf
        if(node.left == null && node.right == null){
            if (node == root){
                root = null;
                return (V) node.v;
            }
            if (node.parent.left != null && node.parent.left.equals(node)){
                node.parent.left = null;
            }
            else {
                node.parent.right = null;
            }
            node.parent = null;
            return (V) node.v;
        }
        //Recurse if node isn't at the bottom yet
        if (node.left == null){
            rotateLeft(node.right);
        }
        else if (node.right == null) {
            rotateRight(node.left);
        }
        else if(node.left.priority < node.right.priority){
            rotateLeft(node.right);
        }
        else {
            rotateRight(node.left);
        }
        return removeHelper(node);
    }
    private Node findNode(Node node, K key){
        if(node == null) return null;
        if(node.k.equals(key)){
            return node;
        }
        if(key.compareTo(node.k)<0){
            return findNode(node.left, key);
        }
        else return findNode(node.right, key);
    }
    @Override
    public Treap<K, V>[] split(K key) {
        Treap [] treapList = new Treap[2];
        if(root==null){
            for(int i=0;i<treapList.length;i++){
                treapList[i] = new TreapMap<>();
            }
            return treapList;
        }
        Treap<K, V> left;
        Treap<K, V> right;

        V value = (V) lookup(key);
        if(value == null){
            //This Treap doesn't contain the given key
            insertionAtTop((K) key, root.v);
            left = new TreapMap<>(root.left);
            right = new TreapMap<>(root.right);
            if(root.left != null){
                root.left.parent = null;
            }
            if(root.right != null){
                root.right.parent = null;
            }
        } else {
            //This Treap contains the given key
            insertionAtTop((K) key, value);
            left = new TreapMap<>(root.left);
            if(root.left != null){
                root.left.parent = null;
            }
            root.left = null;
            right = new TreapMap<>(root);
        }
        treapList[0] = left;
        treapList[1] = right;
        return treapList;
    }

    //Insert given key value pair into the root of the tree
    public void insertionAtTop(K key, V value) {
        if (key == null || value == null){
            return;
        }
        if (root == null){
            root = new Node(key, value);
            return;
        }
        insertionAtTop(root, key, value);
    }

    //Recursive helper method for insertionAtTop
    private void insertionAtTop(Node head, K key, V value){
        //Base Case
        if(head.k.equals(key)){
            head.v = value;
            head.priority = Treap.MAX_PRIORITY;
            heapify(head);
            return;
        }

        if(key.compareTo(head.k) < 0){
            int priority = MAX_PRIORITY;
            //Base Case
            if(head.left == null) {
                head.setLeft(new Node(key, value, Treap.MAX_PRIORITY));
                head.setPriority(priority);
                heapify(head.left);
                return;
            }
            //Recursive Case
            insertionAtTop(head.left, key, value);
            return;
        }
        if(head.right == null){
            head.setRight(new Node(key, value, Treap.MAX_PRIORITY));
            head.setPriority(head.priority);
            heapify(head.right);
            return;
        }
        //recurse through again if not done
        insertionAtTop(head.right, key, value);
    }
    @Override
    public void join(Treap t) {
        //checks if Treap t is instance of TreapMap
        if(t instanceof TreapMap == false){
            return;
        }
        TreapMap<K, V> treap2 = (TreapMap<K, V>) t;
        if(isTreapEmpty(treap2.root)){
            return;
        }
        if(isTreapEmpty(root)){
            root = treap2.root;
            return;
        }
        //we can assume that all values in T2 are greater than T1
        Node newRoot = root;
        root = new Node(root.k, root.v);
        if(treap2.root.k.compareTo(root.k)>0){
            root.setLeft(newRoot);
            root.setRight(treap2.root);
        }
        else if (treap2.root.k.compareTo(root.k)<0){
            root.setLeft(treap2.root);
            root.setRight(newRoot);
        }
        removeHelper(root);
    }
    private boolean isTreapEmpty(Node root){
        if(root==null){
            return true;
        }
        return false;
    }

    @Override
    public void meld(Treap t) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("have not implemented meld() yet");
    }

    @Override
    public void difference(Treap t) throws UnsupportedOperationException {
        //KARMA
        throw new UnsupportedOperationException("have not implemented difference() yet");
    }
    @Override
    public String toString(){
        if(root==null) return "";
        //calls helper method then removes an extra tabs/new lines at the end
        return toStringHelper(root, 0).strip();
    }
    private String toStringHelper(Node node, int indents){
        String output = "";
        for(int i=0;i<indents;i++){
            output = output + "\t";
        }
        output = output + node.toString() + "\n";
        if(isLeftNodeNull(node)==true){
            output += toStringHelper(node.left, indents+1);
        }
        if(isRightNodeNull(node)==true){
            output += toStringHelper(node.right, indents+1);
        }
        return output;
    }
    public boolean isLeftNodeNull(Node node){
        if(node.left!=null) return true;
        else return false;
    }
    public boolean isRightNodeNull(Node node){
        if(node.right!=null) return true;
        else return false;
    }
    @Override
    public Iterator iterator() {
        class TreapIterator implements Iterator<K>{
            //if there no more nodes to be iterated over, return null
            private Node node;
            private TreapIterator(){
                if(root != null){
                    smallestNode(root);
                }
            }

            @Override
            public boolean hasNext() {
                return isEmpty(node);
            }
            public boolean isEmpty(Node node){
                if(node!=null) return true;
                else return false;
            }

            //assigns new smallest node
            private void smallestNode(Node head){
                if (head.left != null){
                    smallestNode(head.left);
                } else {
                    node = head;
                }
            }
            //determines if the node is a left child
            private boolean isLeftChild(Node child){
                if(child.parent == null || child.parent.left == null){
                    return false;
                }
                return child.parent.left.equals(child);
            }
            //Returns the next key
            @Override
            public K next() {
                if(!hasNext()){
                    throw new NoSuchElementException();
                }
                K key = node.k;
                if(node.right != null){
                    node = node.right;
                    smallestNode(node);
                } else if(isLeftChild(node)){
                    node = node.parent;
                } else if(!isLeftChild(node)){
                    node = node.parent;
                    while(node != null && !isLeftChild(node)){
                        node = node.parent;
                    }
                    if(node != null){
                        node = node.parent;
                    }
                }
                return key;
            }
        }
        return new TreapIterator();
    }

    @Override
    public double balanceFactor() throws UnsupportedOperationException {
        //KARMA
        throw new UnsupportedOperationException("have not implemented balanceFactor() yet ");
    }
    //restores heap property (fixes map)
    private void heapify(Node node){
        if(node.parent == null){
            return;
        }
        int parentPriority = node.parent.priority;
        int currentNodePriority = node.priority;
        // if Parent has a low priority than current node
        if(parentPriority <= currentNodePriority){
            if(node.parent.left != null &&
                    node.parent.left.equals(node)){
                rotateRight(node);
            } else {
                rotateLeft(node);
            }
            heapify(node);
        }
    }
    private class Node{
        private K k;
        private V v;
        int priority = (int) (Math.random() * Treap.MAX_PRIORITY);
        private Node left = null;
        private Node right = null;
        private Node parent = null;
        //constructor
        private Node(K key, V value){
            this.k = key;
            this.v = value;
        }
        private Node(K key, V value, int priority){
            this.k = key;
            this.v = value;
            this.priority = priority;
        }
        //Sets the left child of the Node
        private void setLeft(Node l){
            left = l;
            if(left != null){
                left.parent = this;
            }
        }
        //Sets the right child of the Node
        private void setRight(Node r){
            this.right = r;
            if(right != null){
                right.parent = this;
            }
        }
        private void setPriority(int priorityValue){
            this.priority = priorityValue;
        }
        //to use in interface toString() method.
        public String toString(){
            return "[" + priority + "] <" + k + ", " + v + ">";
        }
    }
}

