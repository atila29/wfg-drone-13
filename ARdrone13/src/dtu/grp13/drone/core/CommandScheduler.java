package dtu.grp13.drone.core;

import de.yadrone.base.IARDrone;

public class CommandScheduler implements ICommandThread {

private IARDrone drone;
	
	public CommandScheduler(IARDrone drone) {
		this.drone = drone;
		
	}
	
	@Override
	public void forward(int speed, int milsec) throws InterruptedException {
		drone.getCommandManager().schedule(milsec, new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().forward(speed).doFor(milsec);
			}
		});
	}

	@Override
	public void left(int speed, int milsec) throws InterruptedException {
		drone.getCommandManager().schedule(milsec, new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().goLeft(speed).doFor(milsec);
			}
		});
	}

	@Override
	public void right(int speed, int milsec) throws InterruptedException {
		drone.getCommandManager().schedule(milsec, new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().goRight(speed).doFor(milsec);
			}
		});
	}

	@Override
	public void backward(int speed, int milsec) throws InterruptedException {
		drone.getCommandManager().schedule(milsec, new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().backward(speed).doFor(milsec);
			}
		});
	}

	@Override
	public void takeOff() throws InterruptedException {
		drone.getCommandManager().schedule(1000, new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().takeOff();
			}
		});
	}

	@Override
	public void land() throws InterruptedException {
		drone.getCommandManager().schedule(1000, new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().landing();
			}
		});
	}

	@Override
	public void waitFor(int milsec) throws InterruptedException {
		drone.getCommandManager().schedule(milsec, new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().waitFor(milsec);
			}
		});
	}

	@Override
	public void hover(int milsec) throws InterruptedException {
		drone.getCommandManager().schedule(milsec, new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().hover().doFor(milsec);
			}
		});
	}

	@Override
	public void freeze(int milsec) throws InterruptedException {
		drone.getCommandManager().schedule(milsec, new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().freeze().doFor(milsec);
			}
		});
	}

	@Override
	public void up(int speed, int milsec) throws InterruptedException {
		drone.getCommandManager().schedule(milsec, new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().up(speed).doFor(milsec);
			}
		});
	}

	@Override
	public void down(int speed, int milsec) throws InterruptedException {
		drone.getCommandManager().schedule(milsec, new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().down(speed).doFor(milsec);
			}
		});
	}

	@Override
	public void emegency() {
		drone.getCommandManager().schedule(1000, new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().emergency();
			}
		});
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
		this.forward(20, 500);
	}

	@Override
	public void stepBackward() throws InterruptedException {
		this.backward(20, 500);

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
		drone.getCommandManager().schedule(1000, new Runnable() {
			@Override
			public void run() {
				drone.getCommandManager().flatTrim().doFor(1000);
			}
		});
	}

}
