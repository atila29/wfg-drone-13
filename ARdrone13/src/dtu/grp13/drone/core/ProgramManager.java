package dtu.grp13.drone.core;

import dtu.grp13.drone.core.matproc.Processable;
import dtu.grp13.drone.core.matproc.procs.CubeProc;
import dtu.grp13.drone.core.matproc.procs.QrProc;
import dtu.grp13.drone.cube.Cube;
import dtu.grp13.drone.gui.PositionFrame;
import dtu.grp13.drone.util.WFGUtilities;
import dtu.grp13.drone.vector.Vector2;

public class ProgramManager {
	private ICommandThread ct;
	private Vector2 position;
	private Processable proc;
	private String cube;
	private PositionFrame xFrame;
	private int cubeCount = 0;
	private PositionSystem posSystem;
	private int routeNr = 0;
	private double orientation = -1;
	private boolean ready = true;
	
	// PURELY TEST
	private int testSec = 800;
	private int testSpeed = 100;
	
	public void setxFrame(PositionFrame xFrame) {
		this.xFrame = xFrame;
	}
	
	public void setProc(Processable proc){
		this.proc = proc;
	}
	
	public void setPosSystem(PositionSystem possys){
		this.posSystem = possys;
	}
	
	public void positionFound(Vector2 pos, double orientation){
		this.position = pos;
		this.orientation = orientation;
		xFrame.setDronePosition(this.position);
	}
	
	public void cubeFound(String color) {
		this.cube = color;
		cubeCount++;
		if (position == null) {
			return;
		}
		xFrame.drawCube(new Cube(color, position));
		WFGUtilities.LOGGER.info("farve: " + color + ", position: " + position);
	}
	
	public void takeOffDrone() throws InterruptedException{
		ct.takeOff();
		ct.stabilize();
	}
	
	public void emergencyDrone(){
		ct.emegency();
	}
	
	public void landDrone() throws InterruptedException{
		ct.land();
	}
	
	public void findPosition() {
		new Thread(() -> {
			position = null;
			this.orientation = -1;
			int count = 5;
			while(position == null && count > 0) {
				try {
					count--;
					ct.rotateClockwise(10);
					ct.hover(1000);
					ct.hover(1000);
					Thread.sleep(100);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void findPositionOne() {
		new Thread(() -> {
			position = null;
			this.orientation = -1;
			int count = 5;
			while(position == null && count > 0) {
				try {
					count--;
					ct.rotateClockwise(10);
					ct.hover(1000);
					ct.hover(1000);
					Thread.sleep(100);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void findPosition(Runnable after) {
		new Thread(() -> {
			position = null;
			this.orientation = -1;
			int count = 10;
			while(position == null) {
				try {
					if(!(count-- >= 0)){
						ct.stepForward();
						ct.hover(1000);
						ct.stepForward();
						ct.hover(1000);
					}
					ct.rotateClockwise(10);
					ct.hover(1000);
					ct.hover(1000);
					Thread.sleep(100);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			after.run();
		}).start();
	}
	public void findPosition(Runnable after, Runnable err) {
		new Thread(() -> {
			position = null;
			this.orientation = -1;
			int count = 10;
			while(position == null) {
				try {
					if(!(count >= 0)){
						err.run();
						return;
					}
					count--;
					ct.rotateClockwise(10);
					ct.hover(1000);
					ct.hover(1000);
					Thread.sleep(100);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			after.run();
		}).start();
	}
	
	public void changeCam(){
		try {
			ct.next();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private ProgramManager getThis(){
		return this;
	}
	
	public void initHeight() {
		try {
			ct.up(100, 500);
			Thread.sleep(550);
			ct.hover(1000);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void rotateToWall(int wall) {
		Runnable callBackFromFindPosition = new Runnable() {
			@Override
			public void run() {
				// her antages det at vi har vores vinkel,
				// derefter skal vi rotate med udgangspunkt i den vinkel.
				double dOrientation = Math.toDegrees(orientation);
				double degreesToWall = (wall * 90) + dOrientation;
				try {
						int time = (int)(((850+(7*wall))/90) * degreesToWall);
						ct.rotateCounterClockwise(100, time);
						ct.hover(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		findPosition(callBackFromFindPosition);
	}
	
	public void flyToPoint(Vector2 point) throws InterruptedException {
		
		Runnable err = new Runnable() {
			@Override
			public void run() {
				try {
					ct.stepForward();
					ct.hover(1000);
					flyToPoint(point);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		Runnable loop = new Runnable() {
			@Override
			public void run() {
				double a = position.getX() - point.getX();
				double b = position.getY() - point.getY();
				double distance = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
				Vector2 stedsvektor = point.subtract(position);
				double degree = Math.toDegrees(stedsvektor.getAngle(point));
				int rotTime = ((int)((830/90)*degree));
				WFGUtilities.LOGGER.info("drone: " + position + " point: " + point + ", orientation: " + orientation);
				WFGUtilities.LOGGER.info("dist: " + distance + " degree: " + degree);
				WFGUtilities.LOGGER.info("radianer?: " + stedsvektor.getAngle(point));
				int steps = (int) (distance / 300);
				try {
					ct.rotateClockwise(100, rotTime);
					ct.hover(1000);
					ct.stepForward();
					ct.hover(1000);
					for(int i = 0; i < steps; i++){
						ct.stepForward();
						ct.hover(1000);
					}
					ct.hover(1000);
					if(distance > 60){
						flyToPoint(point);
					} else {
						landDrone();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		};
		
		new Thread(() -> {
			findPosition(loop, err);
		}).start();
	}
	
	public void testCycleTwo(){
		new Thread(() -> {
			try {
				ct.up(10, 500);
				//ct.hover(2000);
				ct.stepForward();
				//ct.hover(2000);
				ct.stepBackward();
				//ct.hover(2000);
				ct.stepLeft();
				//ct.hover(2000);
				ct.stepRight();
				//ct.hover(2000);
				ct.rotateClockwise(20);
				ct.stepForward();
				//ct.hover(2000);
				ct.land();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
	}
	
	public void searchRoomOne() throws InterruptedException{
	routeNr = 0;
	

		Runnable callback = new Runnable() {
			@Override
			public void run() {
				try{
					ct.next();
					Thread.sleep(1000);
					ready = false;
					ct.next();
					Thread.sleep(1000);
					ct.hover(2000);
					Thread.sleep(1000);
					proc.changeProcess(new CubeProc(getThis()));
					Thread.sleep(1000);
					
					Vector2 point = new Vector2(800, 300*routeNr);
					
					if(routeNr>=3){
						routeNr = 0;
					}
					
					double a = position.getX() - point.getX();
					double b = position.getY() - point.getY();
					double distance = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
					Vector2 stedsvektor = point.subtract(position);
					double degree = Math.toDegrees(stedsvektor.getAngle(point));
					int rotTime = ((int)((830/90)*degree));
					WFGUtilities.LOGGER.info("drone: " + position + " point: " + point + ", orientation: " + orientation);
					WFGUtilities.LOGGER.info("dist: " + distance + " degree: " + degree);
					WFGUtilities.LOGGER.info("radianer?: " + stedsvektor.getAngle(point));
					ct.rotateClockwise(100, rotTime);
					ct.hover(1000);
					ct.stepForward();
					ct.hover(1000);
					routeNr++;
					ready = true;
				} catch(InterruptedException e){
					//todoihdofa
				}
			}
		};
		new Thread(() -> {
			try {
				ready = true;
				findPosition();
				Thread.sleep(1000);
				ct.next();
				Thread.sleep(1000);
				proc.changeProcess(new CubeProc(this));
				Thread.sleep(1000);
				ct.hover(2000);
				ct.stepForward();
				ct.hover(1000);
				//ct.next();
				while(true){
					if(ready){
						position = null;
						Thread.sleep(1000);
						proc.changeProcess(new QrProc(this, posSystem));
						findPosition(callback);
						ct.next();
						Thread.sleep(1000);
						ready = false;
					}
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
	}
	
	
	public void searchRoom() throws InterruptedException{
	routeNr = 0;
	

		Runnable callback = new Runnable() {
			@Override
			public void run() {
				try{
					ready = false;
					ct.next();
					ct.hover(2000);
					Thread.sleep(1000);
					proc.changeProcess(new CubeProc(getThis()));
					Thread.sleep(1000);
					
					Vector2 point = new Vector2(800, 300*routeNr);
					
					if(routeNr>=3){
						routeNr = 0;
					}
					
					double a = position.getX() - point.getX();
					double b = position.getY() - point.getY();
					double distance = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
					Vector2 stedsvektor = point.subtract(position);
					double degree = Math.toDegrees(stedsvektor.getAngle(point));
					int rotTime = ((int)((830/90)*degree));
					WFGUtilities.LOGGER.info("drone: " + position + " point: " + point + ", orientation: " + orientation);
					WFGUtilities.LOGGER.info("dist: " + distance + " degree: " + degree);
					WFGUtilities.LOGGER.info("radianer?: " + stedsvektor.getAngle(point));
					ct.rotateClockwise(100, rotTime);
					ct.hover(1000);
					ct.stepForward();
					ct.hover(1000);
					routeNr++;
					ready = true;
				} catch(InterruptedException e){
					//todoihdofa
				}
			}
		};
		new Thread(() -> {
			try {
				ready = true;
				findPosition();
				Thread.sleep(1000);
				ct.next();
				proc.changeProcess(new CubeProc(this));
				Thread.sleep(1000);
				ct.hover(2000);
				ct.stepForward();
				ct.hover(1000);
				long time = (long) (System.currentTimeMillis() + (1000*60*4));
				while(cubeCount < 40 && time > System.currentTimeMillis()){
					if(ready){
						position = null;
						ct.next();
						Thread.sleep(1000);
						proc.changeProcess(new QrProc(this, posSystem));
						Thread.sleep(1000);
						ct.next();
						Thread.sleep(1000);
						findPosition(callback);
						ready = false;
					}
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
	}
	
	
	public void firstCycle(){
		findPosition(new Runnable() {
			@Override
			public void run() {
				try {
					ct.next();
					Thread.sleep(1000);
					proc.changeProcess(new CubeProc(getThis()));
					new Thread(() -> {
						while(cube == null) {
							try {
								ct.hover(5000);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						try {
							ct.land();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}).start();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}
	
	public void setCmd(ICommandThread ct) {
		this.ct = ct;
	}
	
	// TEST
		public void testSpeedUp(){
			this.testSpeed = testSpeed +5;
			System.out.println("[xTEST]speed: " + testSpeed);
		}
		
		public void testSpeedDown(){
			this.testSpeed = testSpeed - 5;
			System.out.println("[xTEST]speed: " + testSpeed);
		}
		
		public void testTimeUp() {
			this.testSec = testSec + 5;
			System.out.println("[xTEST]time: " + testSec);
		}
		
		public void testTimeDown() {
			this.testSec = testSec - 5;
			System.out.println("[xTEST]time: " + testSec);
		}
		
		public void testStepF() {
			try {
				ct.forward(testSpeed, testSec);
				ct.hover(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void testStepB() {
			try {
				ct.backward(testSpeed, testSec);
				ct.hover(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void testStepL() {
			try {
				ct.left(testSpeed, testSec);
				ct.hover(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void testStepR() {
			try {
				ct.right(testSpeed, testSec);
				ct.hover(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void testRotL(){
			try {
				ct.rotateCounterClockwise(testSpeed, testSec);
				ct.hover(500);
			} catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		public void testRotR(){
			try {
				ct.rotateClockwise(testSpeed, testSec);
				ct.hover(500);
			} catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		
		// TEST END
}
