package dtu.grp13.drone.test;

import de.yadrone.apps.controlcenter.CCFrame;
import de.yadrone.base.ARDrone;
import de.yadrone.base.command.VideoChannel;

public class TestConnection {
	
	private ARDrone drone=null;
	
	public TestConnection(){
		initialize();
	}

	
	private void initialize(){
		try
		{
			drone = new ARDrone();
			System.out.println("Connect");
			drone.getCommandManager().setVideoChannel(VideoChannel.VERT);
			drone.start();
			
			new CCFrame(drone);
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
			
			if (drone != null)
				drone.stop();
			System.exit(-1);
		}
	}
		
	public static void main(String args[]){
		new TestConnection();
	}

}
