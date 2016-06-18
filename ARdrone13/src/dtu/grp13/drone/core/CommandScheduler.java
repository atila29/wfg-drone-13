package dtu.grp13.drone.core;

import de.yadrone.base.IARDrone;
import de.yadrone.base.command.VideoChannel;

public class CommandScheduler implements ICommandThread {

private IARDrone drone;
	
	public CommandScheduler(IARDrone drone) {
		this.drone = drone;
		
	}

	@Override
	public void forward(int speed, int milsec) throws InterruptedException {
		drone.getCommandManager().schedule(2000, new Runnable(){
			public void run(){
				drone.getCommandManager().forward(speed);
				drone.getCommandManager().hover().doFor(100);
			}
		});
		
	}

	@Override
	public void left(int speed, int milsec) throws InterruptedException {
		drone.getCommandManager().schedule(2000, new Runnable(){
			public void run(){
				drone.getCommandManager().goLeft(speed);
				drone.getCommandManager().hover().doFor(100);
			}
		});
		
	}

	@Override
	public void right(int speed, int milsec) throws InterruptedException {
		drone.getCommandManager().schedule(2000, new Runnable(){
			public void run(){
				drone.getCommandManager().goRight(speed);
				drone.getCommandManager().hover().doFor(100);
			}
		});
		
	}

	@Override
	public void backward(int speed, int milsec) throws InterruptedException {
		drone.getCommandManager().schedule(2000, new Runnable(){
			public void run(){
				drone.getCommandManager().backward(speed);
				drone.getCommandManager().hover().doFor(100);
			}
		});
		
	}

	@Override
	public void takeOff() throws InterruptedException {
		drone.getCommandManager().schedule(2000, new Runnable(){
			public void run(){
				drone.getCommandManager().takeOff();
				drone.getCommandManager().flatTrim().doFor(1000);
			}
		});
		
	}

	@Override
	public void land() throws InterruptedException {
		drone.getCommandManager().schedule(2000, new Runnable(){
			public void run(){
				drone.getCommandManager().landing();
			}
		});
		
	}

	@Override
	public void waitFor(int milsec) throws InterruptedException {
		drone.getCommandManager().schedule(2000, new Runnable(){
			public void run(){
				drone.getCommandManager().waitFor(milsec);
			}
		});
		
	}

	@Override
	public void hover(int milsec) throws InterruptedException {
		drone.getCommandManager().schedule(2000, new Runnable(){
			public void run(){
				drone.getCommandManager().hover().doFor(milsec);
			}
		});
		
	}

	@Override
	public void freeze(int milsec) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void up(int speed, int milsec) throws InterruptedException {
		drone.getCommandManager().schedule(2000, new Runnable(){
			public void run(){
				drone.getCommandManager().up(speed).doFor(milsec);
				drone.getCommandManager().hover().doFor(100);
			}
		});
		
	}

	@Override
	public void down(int speed, int milsec) throws InterruptedException {
		drone.getCommandManager().schedule(2000, new Runnable(){
			public void run(){
				drone.getCommandManager().down(speed).doFor(milsec);
				drone.getCommandManager().hover().doFor(100);
			}
		});
		
	}

	@Override
	public void emegency() {
		this.drone.getCommandManager().emergency();
		
	}

	@Override
	public void rotateClockwise(int angle) throws InterruptedException {
		drone.getCommandManager().schedule(2000, new Runnable(){
			public void run(){
				drone.getCommandManager().spinRight(15).doFor(angle*100);
			}
		});
		
	}

	@Override
	public void rotateCounterClockwise(int angle) throws InterruptedException {
		drone.getCommandManager().schedule(2000, new Runnable(){
			public void run(){
				drone.getCommandManager().spinLeft(15).doFor(angle*100);
			}
		});
		
	}

	@Override
	public void stepForward() throws InterruptedException {
		this.forward(20, 500);
	}

	@Override
	public void stepBackward() throws InterruptedException {
		this.backward(20, 500);
	}

	@Override
	public void stepLeft() throws InterruptedException {
		this.left(20, 500);
	}

	@Override
	public void stepRight() throws InterruptedException {
		this.right(20, 500);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotateCounterClockwise(int speed, int time)
			throws InterruptedException {
		// TODO Auto-generated method stub
		
	}
	
}
