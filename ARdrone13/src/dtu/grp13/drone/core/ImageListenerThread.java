package dtu.grp13.drone.core;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.yadrone.base.video.ImageListener;
import fi.jumi.actors.*;
import fi.jumi.actors.eventizers.dynamic.DynamicEventizerProvider;
import fi.jumi.actors.listeners.CrashEarlyFailureHandler;
import fi.jumi.actors.listeners.NullMessageListener;

public class ImageListenerThread {
	
	private ImageListener listener;
	private ExecutorService actorsThreadPool;
	private Actors actors;
	private ActorThread thread;
	private ActorRef<ImageListener> imageActor;
	
	public ImageListenerThread(ImageListener listener) {
		this.listener = listener;
		this.actorsThreadPool = Executors.newCachedThreadPool();
		this.actors = new MultiThreadedActors(
	            actorsThreadPool,
	            new DynamicEventizerProvider(),
	            new CrashEarlyFailureHandler(),
	            new NullMessageListener()
	    );
		
		this.thread = actors.startActorThread();
		this.imageActor = thread.bindActor(ImageListener.class, listener);
	}
	
	public void analyzeImage(BufferedImage img) {
		imageActor.tell().imageUpdated(img);
	}
	
	public void stop() {
		thread.stop();
		actorsThreadPool.shutdown();
	}
}
