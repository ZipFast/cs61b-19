import edu.princeton.cs.algs4.BST;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    /* Represents one node in the linked list that stores the
    key-value pair in the dictionary
     */
    private class Node {
        K key;
        V val;
        Node left, right;
        Node parent;
        int size;

        Node(K k, V v) {
            key = k;
            val = v;
            size = 1;
        }

    }

    private Node root;
    /** remove all of the mapping from this map */
    public void clear() {
        root = null;
    }

    /* return true if this map contains a mapping for the
    specified key
     */
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        return get(key) != null;
    }
    /* Return the value to which the specified key is mapped or
    null if this map contains no mapping for the key
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        return get(root, key);
    }

    private V get(Node node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            return node.val;
        } else if (cmp < 0) {
            return get(node.left, key);
        } else {
            return get(node.right, key);
        }
    }

    /* returns the number of key_value mappings in this map */
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null) {
            return 0;
        }
        return size(node.left) + size(node.right) + 1;
    }

    /* Associates the specified value with the specified key in
    this map
     */
    public void put(K key, V value) {
        put(root, key, value);
    }

    private void put(Node node, K key, V value) {
        if (node == null) {
            root = new Node(key, value);
            return;
        }
        Node n = new Node(key, value);
        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            node.val = value;
        } else if (cmp < 0) {
            if (node.left == null) {
                node.left = n;
                n.parent = node;
                return;
            }
            node.size = size(node.left) + size(node.right) + 1;
            put(node.left, key, value);
        } else {
            if (node.right == null) {
                node.right = n;
                n.parent = node;
                return;
            }
            node.size = size(node.left) + size(node.right) + 1;
            put(node.right, key, value);
        }
    }

    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        Iterator<K> iter = iterator();
        while (iter.hasNext()) {
            K key = iter.next();
            set.add(key);
        }
        return set;
    }

    public V remove(K key) {
        Node node = find(root, key);
        return remove(node);
    }

    public V remove(K key, V value) {
        Node node = find(root, key, value);
        return remove(node);
    }

    private V remove(Node node) {
        /* case 1. the node have no child
         */
       if (node.left == null && node.right == null) {
            V res = node.val;
            if (node.parent == null) {
                root = null;
                return res;
            }
            if (node.parent.right == node) {
                node.parent.right = null;
            } else {
                node.parent.left = null;
            }
            node.parent = null;
            return res;
       }
       /* case 2. node have just one child */
       else if (node.left == null || node.right == null) {
            V res = node.val;
            Node child = (node.left == null ? node.right : node.left);
            if (node.parent == null) {
                root = child;
                child.parent = null;
                return res;
            }
            if (node.parent.left == node) {
                node.parent.left = child;
                child.parent = node.parent;
            } else if (node.parent.right == node) {
                node.parent.right = child;
                child.parent = node.parent;
            }
            return res;
       }
       /* node have two child */
       else {
           Node successor = node.right;
           while (successor.left != null) {
               successor = successor.left;
           }
           K tmp1 = node.key;
           node.key = successor.key;
           successor.key = tmp1;
           V tmp2 = node.val;
           node.val = successor.val;
           successor.val = tmp2;
           return remove(successor);
       }
    }

    private Node find(Node node, K key) {
        if (root == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            return node;
        } else if (cmp < 0) {
            return find(node.left, key);
        } else {
            return find(node.right, key);
        }
    }

    private Node find(Node node, K key, V val) {
        Node res = find(node, key);
        if (res.val == val) {
            return node;
        }
        return null;
    }
    public Iterator iterator() {
        return new BSTMapIter(root);
    }

    private class BSTMapIter implements Iterator<K> {
        private Stack<Node> stack = new Stack<>();
        public BSTMapIter(Node node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }

        @Override
        public boolean hasNext() {
            return  !stack.empty();
        }

        @Override
        public K next() {
            Node node = stack.pop();
            K key = node.key;
            if (node.right != null) {
                node = node.right;
                while (node != null) {
                    stack.push(node);
                    node = node.left;
                }
            }
            return key;
        }
    }

    public void printInOrder(Node node) {
        if (node != null) {
            printInOrder(node.left);
            System.out.println(node.key);
            printInOrder(node.right);
        }
    }

}
