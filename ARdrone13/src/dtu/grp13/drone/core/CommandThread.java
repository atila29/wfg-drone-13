package dtu.grp13.drone.core;

import de.yadrone.base.IARDrone;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.navdata.Altitude;
import de.yadrone.base.navdata.AltitudeListener;

/*
 * TEST:
 * Hvis denne ikke virker, så brug en concurrentBlockingScheduler. 
 * 
 */

public class CommandThread implements ICommandThread{	
	private IARDrone drone;
	private int speed = 20;
	private int milsec = 800;
	
	public CommandThread(IARDrone drone) {
		this.drone = drone;
		
	}

	@Override
	public void forward(int speed, int milsec) throws InterruptedException { 
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					drone.getCommandManager().forward(speed);
					Thread.currentThread().sleep(milsec);
					drone.getCommandManager().hover().doFor(100);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		t.start();
		t.join();
	}

	@Override
	public void left(int speed, int milsec) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					drone.getCommandManager().goLeft(speed);
					Thread.currentThread().sleep(milsec);
					drone.getCommandManager().hover().doFor(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		t.start();
		t.join();
	}

	@Override
	public void right(int speed, int milsec) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					drone.getCommandManager().goRight(speed);
					Thread.currentThread().sleep(milsec);
					drone.getCommandManager().hover().doFor(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		t.start();
		t.join();
	}

	@Override
	public void backward(int speed, int milsec) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					drone.getCommandManager().backward(speed);
					Thread.currentThread().sleep(milsec);
					drone.getCommandManager().hover().doFor(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
				drone.getCommandManager().flatTrim().doFor(1000);
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
		t.start();
		t.join();
	}

	@Override
	public void hover(int milsec) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					drone.getCommandManager().hover();
					Thread.currentThread().sleep(milsec);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		t.start();
		t.join();
	}

	@Override
	public void up(int speed, int milsec) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					drone.getCommandManager().up(speed);
					Thread.currentThread().sleep(milsec);
					drone.getCommandManager().hover().doFor(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		t.start();
		t.join();
		
	}

	@Override
	public void down(int speed, int milsec) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					drone.getCommandManager().down(speed);
					Thread.currentThread().sleep(milsec);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		t.start();
		t.join();
		
	}

	@Override
	public void freeze(int milsec) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					drone.getCommandManager().freeze();
					Thread.currentThread().sleep(milsec);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		t.start();
		t.join();
	}

	@Override
	public void emegency() {
		this.drone.getCommandManager().emergency();
	}

	// måske de resterende også burde indeholde en sleep, dette skal testes!
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
		this.forward(speed, milsec);
	}

	@Override
	public void stepBackward() throws InterruptedException {
		this.backward(speed, milsec);
	}

	@Override
	public void stepLeft() throws InterruptedException {
		this.left(speed, milsec);
	}

	@Override
	public void stepRight() throws InterruptedException {
		this.right(speed, milsec);
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

	@Override
	public void stop() throws InterruptedException {
		drone.stop();
	}

	@Override
	public void rotateClockwise(int speed, int time)
			throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					drone.getCommandManager().spinRight(speed);
					Thread.currentThread().sleep(time);
					drone.getCommandManager().hover().doFor(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		t.start();
		t.join();
	}

	@Override
	public void rotateCounterClockwise(int speed, int time)
			throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					drone.getCommandManager().spinLeft(speed);
					Thread.currentThread().sleep(time);
					drone.getCommandManager().hover().doFor(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		t.start();
		t.join();
	}

}
