package dtu.grp13.drone.core;

import dtu.grp13.drone.core.matproc.Processable;
import dtu.grp13.drone.core.matproc.procs.CubeProc;
import dtu.grp13.drone.core.matproc.procs.QrProc;
import dtu.grp13.drone.cube.Cube;
import dtu.grp13.drone.gui.PositionFrame;
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
	}
	
	public void cubeFound(String color) {
		this.cube = color;
		cubeCount++;
		xFrame.drawCube(new Cube(color, position));
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
	
	public void findPosition(Runnable after) {
		new Thread(() -> {
			position = null;
			this.orientation = -1;
			try {
				ct.up(20, 500);
				Thread.sleep(550);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while(position == null) {
				try {
					ct.rotateClockwise(10);
					ct.hover(1000);
					ct.hover(1000);
					Thread.sleep(100);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			xFrame.setDronePosition(position);
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
	
	public void rotateToWall(int wall) {
		Runnable callBackFromFindPosition = new Runnable() {
			@Override
			public void run() {
				// her antages det at vi har vores vinkel,
				// derefter skal vi rotate med udgangspunkt i den vinkel.
				
			}
		};
		findPosition(callBackFromFindPosition);
	}
	
	public void flyToPoint(Vector2 point) throws InterruptedException {
		// TODO: lav denne fucking methode
		
		Vector2 flight = point.substract(position);
		int margin = 50;
		
		if(flight.getX() < 0) {
			rotateToWall(3);
		} else {
			rotateToWall(1);
		}
		
		while(!(position.getX() < point.getX() - margin && position.getX() > point.getX() + margin)){
			
		}
		
		
		
		
		if(flight.getY() < 0) {
			rotateToWall(0);
			ct.stepForward();
		} else {
			rotateToWall(2);
			ct.stepForward();
		}
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
	
	
	
	public void searchRoom() throws InterruptedException{
		routeNr = 0;
		cubeCount = 0;
		flyToPoint(new Vector2(50,50));
		Runnable callback = new Runnable() {
			@Override
			public void run() {
				try{
					if((routeNr%2) == 0)
						rotateToWall(1);
					else
						rotateToWall(3);
					ct.stepForward();
					ct.next();
					ct.hover(2000);
					Thread.sleep(1000);
					proc.changeProcess(new CubeProc(getThis()));
					Thread.sleep(1000);
					if(position.getX() > 926 - 100){
						if((routeNr%2) == 0){
							rotateToWall(1);
							ct.stepLeft();
							ct.hover(1000);
						}
						else{
							rotateToWall(3);
							ct.stepRight();
							ct.hover(1000);
						}
						routeNr++;
					}
			
				} catch(InterruptedException e){
					//todoihdofa
				}
			}
		};
		new Thread(() -> {
			try {
				ct.next();
				proc.changeProcess(new CubeProc(this));
				Thread.sleep(1000);
				ct.hover(2000);
				while(cubeCount < 40 /*add timer*/){
					ct.next();
					Thread.sleep(1000);
					proc.changeProcess(new QrProc(this, posSystem));
					findPosition(callback);
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
