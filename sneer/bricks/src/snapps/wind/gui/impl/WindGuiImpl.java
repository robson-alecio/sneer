package snapps.wind.gui.impl;

import static wheel.lang.Environments.my;

import java.awt.BorderLayout;
import java.awt.Color;
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
import sneer.skin.dashboard.InstrumentWindow;
import sneer.skin.snappmanager.InstrumentManager;
import sneer.skin.sound.player.SoundPlayer;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.io.Logger;
import wheel.io.ui.GuiThread;
import wheel.lang.ByRef;
import wheel.lang.Consumer;
import wheel.reactive.impl.Constant;
import wheel.reactive.impl.Receiver;
import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.ListValueChange.Visitor;
import wheel.reactive.lists.impl.SimpleListReceiver;

class WindGuiImpl implements WindGui {
	
	private Wind _wind;
	private SoundPlayer _player;
	private final ReactiveWidgetFactory _rfactory;

	private final JTextPane _shoutsList = new JTextPane();
	private TextWidget<JTextPane> _myShout;

	private Container _container;

	private Receiver<ListValueChange<Shout>> _shoutReceiverToAvoidGc;

	private JScrollPane _scrollPane;

	private WindAutoscrollSupport _autoscrollSupportToAvoidGc;
	private Consumer<ListValueChange<Shout>> _windConsumerToAvoidGc;
	
	public WindGuiImpl() {
		_wind = my(Wind.class);
		_rfactory = my(ReactiveWidgetFactory.class);
		_player = my(SoundPlayer.class);
		my(InstrumentManager.class).registerInstrument(this);
	} 
	
	@Override
	public void init(InstrumentWindow window) {
		Container container = window.contentPane();
		_container = container;
		iniGui();
		_autoscrollSupportToAvoidGc.placeAtEnd();
		initShoutReceiver();
		new WindClipboardSupport();
	}

	private void iniGui() {
		_myShout = _rfactory.newTextPane(new Constant<String>(""), _wind.megaphone(), true);

		initScrollPane();
		initListReceiversInOrder();
		
		_container.setBackground(Color.WHITE);
		_scrollPane.getViewport().add(_shoutsList);
		
		JScrollPane scrollShout = new JScrollPane();
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
		scrollShout.setBorder(new EmptyBorder(5,5,5,5));
	}

	private void initListReceiversInOrder() {
		_autoscrollSupportToAvoidGc.initPreChangeReceiver();
		
		_windConsumerToAvoidGc = new Consumer<ListValueChange<Shout>>(){ @Override public void consume(ListValueChange<Shout> value) { value.accept(new Visitor<Shout>(){
				@Override public void elementAdded(int index, Shout shout) { ShoutPainter.appendShout(shout, _shoutsList); }
				@Override public void elementRemoved(int index, Shout element) { ShoutPainter.repaintAllShoults(_wind.shoutsHeard(), _shoutsList); }
				@Override public void elementMoved(int oldIndex, int newIndex, Shout element) { ShoutPainter.repaintAllShoults(_wind.shoutsHeard(), _shoutsList); }
				@Override public void elementReplaced(int index, Shout oldElement, Shout newElement) { ShoutPainter.repaintAllShoults(_wind.shoutsHeard(), _shoutsList); 	}
				@Override public void elementInserted(int index, Shout shout) {
					if(_wind.shoutsHeard().currentSize()<index+1) {
						ShoutPainter.repaintAllShoults(_wind.shoutsHeard(), _shoutsList);
						return;
					}
					ShoutPainter.appendShout(shout, _shoutsList);
				}
		});}};
		_wind.shoutsHeard().addListReceiver(_windConsumerToAvoidGc);
		
		_autoscrollSupportToAvoidGc.initPosChangeReceiver();
	}

	
	private void initScrollPane() {
		_scrollPane = new JScrollPane();
		_scrollPane.setBorder(new EmptyBorder(0,5,2,2));
		_scrollPane.setOpaque(false);
		_autoscrollSupportToAvoidGc = new WindAutoscrollSupport();
	}

	private void initShoutReceiver() {
		_shoutReceiverToAvoidGc = new Receiver<ListValueChange<Shout>>(){ @Override public void consume(ListValueChange<Shout> value) {
			shoutAlert();
		}};
		_wind.shoutsHeard().addListReceiver(_shoutReceiverToAvoidGc);
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
				@Override protected void elementRemoved(Shout element) { fire(); }
				
				@Override
				public void elementInserted(int index, Shout value) {
					throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
				}};
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
			GuiThread.invokeAndWait(new Runnable(){ @Override public void run() {
				result.value =  scrollModel().getValue() + scrollModel().getExtent() == scrollModel().getMaximum();
			}});
			return result.value;
		}		
		
		private void placeAtEnd() {
			GuiThread.invokeLater(new Runnable(){ @Override public void run() {
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
			Logger.log("Lost Clipboard Ownership.");
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
		return null;
	}
}