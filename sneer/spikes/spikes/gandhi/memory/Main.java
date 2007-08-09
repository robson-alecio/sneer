package spikes.gandhi.memory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import spikes.gandhi.memory.thirdparty.IObjectProfileNode;
import spikes.gandhi.memory.thirdparty.ObjectProfiler;

public class Main {
	
	// observations:
	// instance fields memory can be tracked
	// transient fields cant

	public Main(){
		 Thread thread = new Thread(){
			 List<String> growing_memory = new ArrayList<String>(); //works
			 @Override
			public void run(){
				 //List<String> growing_memory = new ArrayList<String>(); //does not work
				 while(true){
					 growing_memory.add((new Date()).toString());
					 try{sleep(1000);}catch(Exception e){}
				 }
			 }
		 };
		 
		 thread.start();

		 while(true){
			 IObjectProfileNode profile = ObjectProfiler.profile (thread);
			 System.out.println ("obj size = " + profile.size () + " bytes");
			 //System.out.println (profile.dump ());
			 System.out.println ();
			 try{Thread.sleep(10000);}catch(Exception e){}
		 }

		 
	}
	
	public static void main(String[] args){
		new Main();
	}
}
