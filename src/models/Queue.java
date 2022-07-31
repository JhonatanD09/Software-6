package models;

public class Queue<T> {

	private Node<T> firstNode;
	private Node<T> finishNode;

	public Queue() {
		this.finishNode = null;
		this.firstNode = null;
	}

	public void push(T data) {
		Node<T> temp = new Node<T>(data);
		if (firstNode == null) {
			firstNode = temp;
			finishNode= temp;
		}else {			
			finishNode.setNext(temp);
			finishNode = finishNode.getNext();
		}
	}

	public T pop() {
		Node<T> tempNode = firstNode;
		firstNode = firstNode.getNext();
		return tempNode.getData();
	}

	public int size(){
		int size = 0;
		Node<T> actualNode = firstNode;
		while(actualNode != null){
			size++;
			actualNode = actualNode.getNext();
		}
		return size;
	}

	public void delete(Node<T> data) {
		Node<T> actualNode = firstNode;
		if (data == actualNode) {
			firstNode = firstNode.getNext();
		}
		while(actualNode != null){
			if (actualNode.getNext() != null && actualNode.getNext()==data) {
				actualNode.setNext(actualNode.getNext().getNext());
			}else {
				actualNode = actualNode.getNext();
			}
		}
	}
	
	public boolean isEmpty() {
		return firstNode == null;
	}
	

	public Node<T> peek() {
		return firstNode;
	}
	
	public Node<T> getFirstNode() {
		return firstNode;
	}
}
