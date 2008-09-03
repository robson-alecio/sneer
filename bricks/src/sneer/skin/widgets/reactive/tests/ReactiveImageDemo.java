package sneer.skin.widgets.reactive.tests;

import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.skin.widgets.reactive.ImageWidget;
import sneer.skin.widgets.reactive.RFactory;
import wheel.graphics.Images;
import wheel.lang.Functor;
import wheel.reactive.Signal;
import wheel.reactive.impl.Adapter;
import wheel.reactive.impl.mocks.RandomBoolean;

public class ReactiveImageDemo {
	
	private static final Image ONLINE = getImage("sample.png");
	private static final Image OFFLINE = getImage("sampleOff.png");
	
	private static Image getImage(String fileName) {
		return Images.getImage(ReactiveImageDemo.class.getResource(fileName));
	}
	
	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer();

		RFactory rfactory = container.produce(RFactory.class);
		
		Signal<Boolean> isOnline = new RandomBoolean().output();
		Functor<Boolean, Image> functor = new Functor<Boolean, Image>(){
			@Override
			public Image evaluate(Boolean value) {
				return value?ONLINE:OFFLINE;
			}};
		
		Adapter<Boolean, Image> imgSource = new Adapter<Boolean, Image>(isOnline, functor);	
		
		ImageWidget img = rfactory.newImage(imgSource.output());
		createTestFrame(img, 10, 10, 300, 100);
	}

	private static void createTestFrame(final ImageWidget img, final int x, final int y, final int width, final int height) {

		SwingUtilities.invokeLater(
			new Runnable(){
				@Override
				public void run() {
					final JFrame frm = new JFrame(img.getClass().getSimpleName());
					frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frm.getContentPane().setLayout(new FlowLayout());
					frm.getContentPane().add(img.getComponent());
					frm.setBounds(x, y, width, height);
					frm.setVisible(true);
				}
			}
		);
	}
}