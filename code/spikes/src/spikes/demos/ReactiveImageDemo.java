package spikes.demos;

import static sneer.foundation.environments.Environments.my;

import java.awt.FlowLayout;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.bricks.hardware.gui.timebox.TimeboxedEventQueue;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.skin.widgets.reactive.ImageWidget;
import sneer.bricks.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.foundation.brickness.Brickness;
import sneer.foundation.environments.Environments;
import sneer.foundation.lang.Functor;
import spikes.wheel.reactive.impl.mocks.RandomBoolean;

public class ReactiveImageDemo {
	
	private final Image ONLINE = getImage("sample.png");
	private final Image OFFLINE = getImage("sampleOff.png");
	
	private ReactiveImageDemo(){
		my(TimeboxedEventQueue.class).startQueueing(5000);
		
		my(GuiThread.class).invokeAndWait(new Runnable(){@Override public void run() {
			ReactiveWidgetFactory rfactory = Environments.my(ReactiveWidgetFactory.class);
			
			Signal<Boolean> isOnline = new RandomBoolean().output();
			Functor<Boolean, Image> functor = new Functor<Boolean, Image>(){ @Override public Image evaluate(Boolean value) {
				return value?ONLINE:OFFLINE;
			}};
				
			ImageWidget img = rfactory.newImage(my(Signals.class).adapt(isOnline, functor));
			
			JFrame frm = new JFrame(img.getClass().getSimpleName());
			frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frm.getContentPane().setLayout(new FlowLayout());
			frm.getContentPane().add(img.getComponent());
			frm.setBounds(10, 10, 300, 100);
			frm.setVisible(true);
		}});
	}
	
	private Image getImage(String fileName) {
		try {
			return ImageIO.read(ReactiveImageDemo.class.getResource(fileName));
		} catch (IOException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}
	
	public static void main(String[] args) throws Exception {
		Environments.runWith(Brickness.newBrickContainer(), new Runnable(){ @Override public void run() {
			try {
				new ReactiveImageDemo();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}});
	}
}