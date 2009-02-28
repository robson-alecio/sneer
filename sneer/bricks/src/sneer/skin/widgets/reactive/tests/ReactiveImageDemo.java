package sneer.skin.widgets.reactive.tests;

import java.awt.FlowLayout;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import sneer.brickness.environments.Environments;
import sneer.kernel.container.Containers;
import sneer.skin.widgets.reactive.ImageWidget;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import wheel.io.Logger;
import wheel.io.ui.GuiThread;
import wheel.io.ui.TimeboxedEventQueue;
import wheel.lang.Functor;
import wheel.reactive.Signal;
import wheel.reactive.impl.Adapter;
import wheel.reactive.impl.mocks.RandomBoolean;

public class ReactiveImageDemo {
	
	private final Image ONLINE = getImage("sample.png");
	private final Image OFFLINE = getImage("sampleOff.png");
	
	private ReactiveImageDemo(){
		
		TimeboxedEventQueue.startQueueing(5000);
		
		GuiThread.strictInvokeAndWait(new Runnable(){@Override public void run() {
			ReactiveWidgetFactory rfactory = Environments.my(ReactiveWidgetFactory.class);
			
			Signal<Boolean> isOnline = new RandomBoolean().output();
			Functor<Boolean, Image> functor = new Functor<Boolean, Image>(){ @Override public Image evaluate(Boolean value) {
				return value?ONLINE:OFFLINE;
			}};
				
			Adapter<Boolean, Image> imgSource = new Adapter<Boolean, Image>(isOnline, functor);	
			ImageWidget img = rfactory.newImage(imgSource.output());
			
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
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}
	
	public static void main(String[] args) throws Exception {
		Logger.redirectTo(System.out);
		Environments.runWith(Containers.newContainer(), new Runnable(){ @Override public void run() {
			try {
				new ReactiveImageDemo();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}});
	}
}