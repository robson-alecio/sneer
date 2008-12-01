package sneer.skin.widgets.reactive.tests;

import java.awt.FlowLayout;

import javax.swing.JFrame;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import sneer.skin.widgets.reactive.WindowWidget;
import wheel.io.ui.GuiThread;
import wheel.reactive.Register;
import wheel.reactive.impl.Receiver;
import wheel.reactive.impl.RegisterImpl;

public class ReactiveWidgetsDemo {

	@SuppressWarnings("unused")
	private static Receiver<String> _receiver;

	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer();

		final ReactiveWidgetFactory rfactory = container.provide(ReactiveWidgetFactory.class);
		final Register<String> register = new RegisterImpl<String>("Jose das Coves");
		
		GuiThread.strictInvokeAndWait(new Runnable(){ @Override public void run() {
			TextWidget<?> textWidget;
			
			textWidget = rfactory.newTextField(register.output(), register.setter());
			createTestFrame(textWidget, 10, 10, 300, 100, "notifyOnlyWhenDoneEditing=false");

			textWidget = rfactory.newEditableLabel(register.output(), register.setter());
			createTestFrame(textWidget, 10, 120, 300, 100, "notifyOnlyWhenDoneEditing=false");

			textWidget = rfactory.newLabel(register.output());
			createTestFrame(textWidget, 10, 240, 300, 100, "notifyOnlyWhenDoneEditing=false");

			textWidget = rfactory.newTextPane(register.output(), register.setter());
			createTestFrame(textWidget, 10, 340, 300, 100, "notifyOnlyWhenDoneEditing=false");
			
			textWidget = rfactory.newTextField(register.output(), register.setter(), true);
			createTestFrame(textWidget, 350, 10, 300, 100, "notifyOnlyWhenDoneEditing=true");

			textWidget = rfactory.newEditableLabel(register.output(), register.setter(), true);
			createTestFrame(textWidget, 350, 120, 300, 100, "notifyOnlyWhenDoneEditing=true");

			textWidget = rfactory.newTextPane(register.output(), register.setter(), true);
			createTestFrame(textWidget, 350, 240, 300, 100, "notifyOnlyWhenDoneEditing=true");
			
			WindowWidget<JFrame> frame = rfactory.newFrame(register.output(), register.setter());
			frame.getMainWidget().setBounds(350, 340, 300, 100);
			frame.getMainWidget().setVisible(true);
		}});

		addConsoleLogger(register);
	}

	private static void addConsoleLogger(final Register<String> register) {
		_receiver = new Receiver<String>(register.output()){
			@Override
			public void consume(String valueObject) {
				System.out.println(valueObject);
			}
		};
	}

	private static void createTestFrame(final TextWidget<?> textWidget, final int x, final int y, final int width, final int height, final String title) {
		final JFrame frm = new JFrame();
		frm.setTitle(textWidget.getClass().getSimpleName() + " - " + title);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.getContentPane().setLayout(new FlowLayout());
		frm.getContentPane().add(textWidget.getComponent());
		frm.setVisible(true);
		frm.setBounds(x, y, width, height);
	}
}