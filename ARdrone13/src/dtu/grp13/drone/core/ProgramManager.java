package dtu.grp13.drone.core;

import dtu.grp13.drone.core.matproc.Processable;
import dtu.grp13.drone.core.matproc.procs.CubeProc;
import dtu.grp13.drone.vector.Vector2;

public class ProgramManager {
	private CommandThread ct;
	private Vector2 position;
	private Processable proc;
	private String cube;
	
	public void setProc(Processable proc){
		this.proc = proc;
	}
	
	public void positionFound(Vector2 pos){
		this.position = pos;
	}
	
	public void cubeFound(String color) {
		this.cube = color;
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
		try {
			ct.up(20, 200);
			ct.up(20, 200);
			ct.up(20, 200);
			ct.up(20, 200);
			ct.up(20, 200);
			ct.up(20, 200);
			new Thread(() -> {
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
				after.run();
			}).start();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	public void firstCycle(){
		findPosition(new Runnable() {
			@Override
			public void run() {
				try {
					ct.next();
					proc.changeProcess(new CubeProc(getThis()));
					ct.down(20, 800);
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
	
	public void setCmd(CommandThread ct) {
		this.ct = ct;
	}
}
