package dtu.grp13.drone.core;

public interface ICommandThread {
	public void forward(int speed, int milsec) throws InterruptedException;
	public void left(int speed, int milsec) throws InterruptedException;
	public void right(int speed, int milsec) throws InterruptedException;
	public void backward(int speed, int milsec) throws InterruptedException;
	public void takeOff() throws InterruptedException;
	public void land() throws InterruptedException;
	public void waitFor(int milsec) throws InterruptedException;
	public void hover(int milsec) throws InterruptedException;

}
