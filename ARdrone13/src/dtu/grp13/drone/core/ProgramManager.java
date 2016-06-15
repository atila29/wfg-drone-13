package dtu.grp13.drone.core;

import dtu.grp13.drone.core.matproc.Processable;
import dtu.grp13.drone.core.matproc.procs.CubeProc;
import dtu.grp13.drone.vector.Vector2;

public class ProgramManager {
	private CommandThread ct;
	private Vector2 position;
	private Processable proc;
	
	public void setProc(Processable proc){
		this.proc = proc;
	}
	
	public void positionFound(Vector2 pos){
		this.position = pos;
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
	
	public Vector2 findPosition() {
		try {
			ct.up(20, 200);
			ct.up(20, 200);
			while(position == null) {
				ct.rotateClockwise(15);
				ct.hover(1000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return position;
	}
	
	public void firstCycle(){
		try {
			findPosition();
			ct.next();
			proc.changeProcess(new CubeProc());
			ct.hover(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setCmd(CommandThread ct) {
		this.ct = ct;
	}
}
