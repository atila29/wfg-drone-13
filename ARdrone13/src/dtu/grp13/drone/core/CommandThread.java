package dtu.grp13.drone.core;

import de.yadrone.base.IARDrone;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.navdata.Altitude;
import de.yadrone.base.navdata.AltitudeListener;

public class CommandThread implements ICommandThread{	
	private IARDrone drone;
	
	public CommandThread(IARDrone drone) {
		this.drone = drone;
		
	}

	@Override
	public void forward(int speed, int milsec) throws InterruptedException { 
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().forward(speed).waitFor(milsec);
			}});
		t.start();
		t.join();
	}

	@Override
	public void left(int speed, int milsec) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().goLeft(speed).waitFor(milsec);
			}});
		t.start();
		t.join();
	}

	@Override
	public void right(int speed, int milsec) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().goRight(speed).waitFor(milsec);
			}});
		t.start();
		t.join();
	}

	@Override
	public void backward(int speed, int milsec) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().backward(speed).waitFor(milsec);
			}});
		t.start();
		t.join();
	}

	@Override
	public void takeOff() throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().takeOff();
			}});
		t.start();
		t.join();
	}

	@Override
	public void land() throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().landing();
			}});
		t.start();
		t.join();
	}

	@Override
	public void waitFor(int milsec) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().waitFor(milsec);
			}});
		Thread.sleep(milsec);
		t.start();
		t.join();
	}

	@Override
	public void hover(int milsec) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().hover().doFor(milsec);
			}});
		t.start();
		t.join();
	}

	@Override
	public void up(int speed, int milsec) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().up(speed).doFor(milsec);
			}});
		t.start();
		t.join();
		
	}

	@Override
	public void down(int speed, int milsec) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().up(speed).doFor(milsec);
			}});
		t.start();
		t.join();
		
	}

	@Override
	public void freeze(int milsec) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().freeze().doFor(milsec);
			}});
		t.start();
		t.join();
	}

	@Override
	public void emegency() {
		this.drone.getCommandManager().emergency();
	}

	@Override
	public void rotateClockwise(int angle) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().spinRight(15).doFor(angle*100);
			}});
		t.start();
		t.join();
	}

	@Override
	public void rotateCounterClockwise(int angle) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().spinLeft(15).doFor(angle*100);
			}});
		t.start();
		t.join();
	}

	@Override
	public void stepForward() throws InterruptedException {
		this.forward(10, 200);
	}

	@Override
	public void stepBackward() throws InterruptedException {
		this.backward(20, 200);
	}

	@Override
	public void stepLeft() throws InterruptedException {
		this.left(20, 200);
	}

	@Override
	public void stepRight() throws InterruptedException {
		this.right(20, 200);
	}

	@Override
	public void next() throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().setVideoChannel(VideoChannel.NEXT);
			}});
		t.start();
		t.join();
	}

	@Override
	public void stabilize() throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().flatTrim();
			}});
		t.start();
		t.join();
	}

}
