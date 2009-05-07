package snapps.wind.gui.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.BoundedRangeModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import snapps.wind.Shout;
import snapps.wind.Wind;
import snapps.wind.gui.WindGui;
import sneer.commons.lang.ByRef;
import sneer.hardware.cpu.lang.Consumer;
import sneer.hardware.gui.guithread.GuiThread;
import sneer.pulp.logging.Logger;
import sneer.pulp.reactive.Signals;
import sneer.pulp.reactive.collections.CollectionChange;
import sneer.pulp.reactive.collections.impl.SimpleListReceiver;
import sneer.skin.main.dashboard.InstrumentPanel;
import sneer.skin.main.instrumentregistry.InstrumentRegistry;
import sneer.skin.main.synth.Synth;
import sneer.skin.main.synth.scroll.SynthScrolls;
import sneer.skin.sound.player.SoundPlayer;
import sneer.skin.widgets.reactive.NotificationPolicy;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;

class WindGuiImpl implements WindGui {
	
	{my(Synth.class).load(this.getClass());}

	private final Wind _wind = my(Wind.class);
	private final SoundPlayer _player = my(SoundPlayer.class);
	private final ReactiveWidgetFactory _rfactory = my(ReactiveWidgetFactory.class);

	private final JTextPane _shoutsList = new JTextPane();
	private final JScrollPane _scrollPane = my(SynthScrolls.class).create();
	private final TextWidget<JTextPane> _myShout;{
		final Object ref[] = new Object[1];
		my(GuiThread.class).invokeAndWait(new Runnable(){ @Override public void run() {//Fix Use GUI Nature
			ref[0] = _rfactory.newTextPane(my(Signals.class).newRegister("").output(),  _wind.megaphone(), NotificationPolicy.OnEnterPressed);
		}});
		_myShout = (TextWidget<JTextPane>) ref[0];
	}
	private final WindAutoscrollSupport _autoscrollSupport = new WindAutoscrollSupport();

	private Container _container;
	
	public WindGuiImpl() {
		my(InstrumentRegistry.class).registerInstrument(this);
	} 
	
	@Override
	public void init(InstrumentPanel window) {
		_container = window.contentPane();
		initGui();
		_autoscrollSupport.placeAtEnd();
		initShoutReceiver();
		new WindClipboardSupport();
	}

	private void initGui() {

		initListReceiversInOrder();
		_scrollPane.getViewport().add(_shoutsList);
		_shoutsList.setEditable(false);
		
		JScrollPane scrollShout = my(SynthScrolls.class).create();
		scrollShout.setOpaque(false);
		JPanel horizontalLimit = new JPanel(){
			@Override
			public Dimension getPreferredSize() {
				Dimension preferredSize = super.getPreferredSize();
				preferredSize.setSize(_container.getWidth()-30, preferredSize.getHeight());
				return preferredSize;
			}
		};
		horizontalLimit.setLayout(new BorderLayout());
		horizontalLimit.add(_myShout.getComponent());
		scrollShout.getViewport().add(horizontalLimit);	
		
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, _scrollPane, scrollShout);
		split.setBorder(new EmptyBorder(0,0,0,0));

		split.setOpaque(false);
		split.setDividerLocation((int) (defaultHeight()*0.68)); 
		split.setDividerSize(3);
		_container.setLayout(new BorderLayout());
		_container.add(split, BorderLayout.CENTER);

		_shoutsList.setBorder(new EmptyBorder(0,0,0,0));
		_myShout.getComponent().setBorder(new EmptyBorder(0,0,0,0));
	}

	private void initListReceiversInOrder() {
		_autoscrollSupport.initPreChangeReceiver();
		
		Consumer<CollectionChange<Shout>> _windConsumer = new Consumer<CollectionChange<Shout>>(){ @Override public void consume(CollectionChange<Shout> change) {
			for (Shout shout : change.elementsAdded()) 
				ShoutPainter.appendShout(shout, _shoutsList);

			if(!change.elementsRemoved().isEmpty())
				ShoutPainter.repaintAllShoults(_wind.shoutsHeard(), _shoutsList);
		}};
		
		my(Signals.class).receive(this, _windConsumer, _wind.shoutsHeard());
		_autoscrollSupport.initPosChangeReceiver();
	}

	private void initShoutReceiver() {
		my(Signals.class).receive(this, new Consumer<CollectionChange<Shout>>() { @Override public void consume(CollectionChange<Shout> ignored) {
			shoutAlert();
		}}, _wind.shoutsHeard());
	}
	
	private void shoutAlert() {
		Window window = SwingUtilities.windowForComponent(_container);
		
		if(window.isActive()) return;
		alertUser(window);
	}

	private void alertUser(Window window) {
		window.toFront();
		_player.play(this.getClass().getResource("alert.wav"));
	}


	@Override
	public int defaultHeight() {
		return 248;
	}
	
	private final class WindAutoscrollSupport{
		
		@SuppressWarnings("unused")
		private SimpleListReceiver<Shout> _preChangeReceiverAvoidGc;
		
		@SuppressWarnings("unused")
		private SimpleListReceiver<Shout> _posChangeReceiverAvoidGc;
		
		protected boolean _shouldAutoscroll = true;
		
		{ initKeyTypedListener();}
		private void initKeyTypedListener() {
			_myShout.getMainWidget().addKeyListener(new KeyAdapter(){@Override public void keyReleased(KeyEvent e) {
				if(_shouldAutoscroll) placeAtEnd();
			}});
		}

		private void initPreChangeReceiver() {
			_preChangeReceiverAvoidGc = new SimpleListReceiver<Shout>(_wind.shoutsHeard()){ 
				private void fire() {
					_shouldAutoscroll = isAtEnd();
				}
				@Override protected void elementAdded(Shout newElement) { fire(); }
				@Override protected void elementPresent(Shout element) { fire(); }
				@Override protected void elementRemoved(Shout element) { fire(); }};
		}
		
		private void initPosChangeReceiver() {
			_posChangeReceiverAvoidGc = new SimpleListReceiver<Shout>(_wind.shoutsHeard()){ 
				private void fire() {
					if(_shouldAutoscroll) placeAtEnd();
				}
				@Override protected void elementAdded(Shout newElement) {	fire();	}
				@Override protected void elementPresent(Shout element) {fire();	}
				@Override protected void elementRemoved(Shout element) {fire();}
			};
		}
		
		private boolean isAtEnd() {
			final ByRef<Boolean> result = ByRef.newInstance();
			my(GuiThread.class).invokeAndWait(new Runnable(){ @Override public void run() {
				result.value =  scrollModel().getValue() + scrollModel().getExtent() == scrollModel().getMaximum();
			}});
			return result.value;
		}		
		
		private void placeAtEnd() {
			my(GuiThread.class).invokeLater(new Runnable(){ @Override public void run() {
				scrollModel().setValue(scrollModel().getMaximum()-scrollModel().getExtent());
			}});
		}
		private BoundedRangeModel scrollModel() {
			return _scrollPane.getVerticalScrollBar().getModel();
		}
	}
	
	private final class WindClipboardSupport implements ClipboardOwner{
		
		private WindClipboardSupport(){
			addKeyStrokeListener();
		}

		private void addKeyStrokeListener() {
			int modifiers = getPortableSoModifiers();
			final KeyStroke ctrlc = KeyStroke.getKeyStroke(KeyEvent.VK_C, modifiers);
			_shoutsList.getInputMap().put(ctrlc,  "ctrlc");
			_shoutsList.getActionMap().put("ctrlc",  new AbstractAction(){@Override public void actionPerformed(ActionEvent e) {
				copySelectedShoutToClipboard();
			}});
		}
		
		@Override
		public void lostOwnership(Clipboard arg0, Transferable arg1) {
			my(Logger.class).log("Lost Clipboard Ownership.");
		}
		
		private int getPortableSoModifiers() {
			return Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		}
		
		private void copySelectedShoutToClipboard() {
			StringSelection fieldContent = new StringSelection(_shoutsList.getSelectedText());
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(fieldContent, this);	
		}
	}

	@Override
	public String title() {
		return "Wind";
	}
}