package dtu.grp13.drone.core;

import de.yadrone.base.IARDrone;

public class CommandScheduler implements ICommandThread {

private IARDrone drone;
	
	public CommandScheduler(IARDrone drone) {
		this.drone = drone;
		
	}

	@Override
	public void forward(int speed, int milsec) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void left(int speed, int milsec) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void right(int speed, int milsec) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void backward(int speed, int milsec) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void takeOff() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void land() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void waitFor(int milsec) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hover(int milsec) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void freeze(int milsec) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void up(int speed, int milsec) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void down(int speed, int milsec) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void emegency() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotateClockwise(int angle) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotateCounterClockwise(int angle) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepForward() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepBackward() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepLeft() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepRight() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void next() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stabilize() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}
	
}
