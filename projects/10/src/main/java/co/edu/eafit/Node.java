package co.edu.eafit;

import java.util.LinkedList;
/**
Clase Nodo que sirve para formar una estructura de datos tipo árbol N-ario 
*/
class Node {
	String value;
	LinkedList<Node> nodes;

	 public Node() {
	 	nodes = new LinkedList<Node>();
	 }

	 public Node(String s) {
	 	value = s;
	 	nodes = new LinkedList<Node>();
	 }

	 public void val(String s) {
	 	value = s;
	 }

	 public void add(Node n){
	 	if(n != null)
	 		nodes.add(n);
	 }

	 public void add(String s){
	 	nodes.add(new Node(s));
	 }

	 public LinkedList<String> getList() {
	 	LinkedList<String> l = new LinkedList<String>();
	 	if(value != null)
	 		l.add(value);
	 	if(nodes.size() > 0) {
	 		for(Node n: nodes) {
	 			l.addAll(n.getList());
	 		}
	 	}
	 	return l;
	 }
}