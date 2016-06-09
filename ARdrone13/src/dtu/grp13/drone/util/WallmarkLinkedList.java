package dtu.grp13.drone.util;

import java.util.ArrayList;
import java.util.List;

public class WallmarkLinkedList {
	private Wallmark first;
	private Wallmark last;
	private List<Wallmark> wallList;
	
	public WallmarkLinkedList(){
		wallList = new ArrayList<Wallmark>();
	}
	
	public Wallmark get(String name) {
		if(name.equals(first.getName())){
			return first;
		}
		else if (name.equals(last.getName())){
			return last;
		}
		// bør fejlhåndteres
		return wallList.get(wallList.indexOf(name));
	}
	
	public Wallmark getLeft(String name){
		return this.get(name).getLeft();
	}
	
	public Wallmark getRight(String name) {
		return this.get(name).getRight();
	}
	
	public void add(Wallmark mark){
		if(first == null){
			first = mark;
			last = mark;
			wallList.add(mark);
		} 
		else {
			mark.setLeft(last);
			mark.setRight(first);
			last = mark;
		}
	}
	
}
