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
	public void freeze(int milsec) throws InterruptedException;
	public void up(int speed, int milsec) throws InterruptedException;
	public void down(int speed, int milsec) throws InterruptedException;
	public void emegency();
	public void rotateClockwise(int angle) throws InterruptedException;
	public void rotateCounterClockwise(int angle) throws InterruptedException;
	public void stepForward() throws InterruptedException;
	public void stepBackward() throws InterruptedException;
	public void stepLeft() throws InterruptedException;
	public void stepRight() throws InterruptedException;
	public void next() throws InterruptedException;
	public void stabilize() throws InterruptedException;
}
