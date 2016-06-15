package dtu.grp13.drone.core;

import de.yadrone.base.IARDrone;

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
				drone.getCommandManager().forward(speed).doFor(milsec);
			}});
		t.start();
		t.join();
	}

	@Override
	public void left(int speed, int milsec) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().goLeft(speed).doFor(milsec);
			}});
		t.start();
		t.join();
	}

	@Override
	public void right(int speed, int milsec) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().goRight(speed).doFor(milsec);
			}});
		t.start();
		t.join();
	}

	@Override
	public void backward(int speed, int milsec) throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().backward(speed).doFor(milsec);
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
	public void findPosition() throws InterruptedException {
		new Thread(() -> {
			try {
				this.up(15, 500);
				this.up(15, 500);
				this.up(15, 500);
				this.hover(5000);
				this.hover(5000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
		
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
}
