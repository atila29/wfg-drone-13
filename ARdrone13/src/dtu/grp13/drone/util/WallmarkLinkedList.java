package dtu.grp13.drone.util;

import java.util.ArrayList;
import java.util.List;

import dtu.grp13.drone.vector.Vector2;

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
		return wallList.get(wallList.indexOf(new Wallmark(name, new Vector2(0, 0))));
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
