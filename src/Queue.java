package src;

import java.util.ArrayList;

public class Queue<E> {
	ArrayList<E> element;
	
	public Queue() {
		element = new ArrayList<E>();
	}
	
	public void enQ(final E el) {
		element.add(el);
	}
	
	public final E deQ() {
		final E el = element.get(0);
		element.remove(0);
		return el;
	}
	
	public boolean isEmpty() {
		return (element.size() == 0);
	}
	
	public boolean notEmpty() {
		return (element.size() > 0);
	}
	
	public void clearQ() {
		element = null;
	}

}
