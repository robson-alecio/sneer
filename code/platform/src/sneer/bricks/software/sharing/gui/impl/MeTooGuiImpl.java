package sneer.bricks.software.sharing.gui.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import sneer.bricks.skin.main.menu.MainMenu;
import sneer.bricks.skin.main.synth.Synth;
import sneer.bricks.skin.main.synth.scroll.SynthScrolls;
import sneer.bricks.skin.windowboundssetter.WindowBoundsSetter;
import sneer.bricks.snapps.diff.text.gui.TextDiffPanel;
import sneer.bricks.snapps.diff.text.gui.TextDiffPanels;
import sneer.bricks.snapps.system.log.gui.LogConsole;
import sneer.bricks.software.sharing.BrickVersion;
import sneer.bricks.software.sharing.FileVersion;
import sneer.bricks.software.sharing.gui.MeTooGui;

class MeTooGuiImpl extends JFrame implements MeTooGui{

	private final JTree _tree = new JTree();
	private final JList _files = new JList();
	private final TextDiffPanel _diffPanel = my(TextDiffPanels.class).newPanel();
	
	private final int _OFFSET_X;
	private final int _OFFSET_Y;
	private final int _HEIGHT;
	private final int _X;
	
	MeTooGuiImpl(){
		super("MeToo");
		
		_tree.setRootVisible(false);
		_tree.setModel(new DefaultTreeModel(FakeModel.root()));
		_tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		my(LogConsole.class);
		Synth _synth = my(Synth.class);
		_OFFSET_X = (Integer) _synth.getDefaultProperty("LodConsoleImpl.offsetX");
		_OFFSET_Y = (Integer) _synth.getDefaultProperty("LodConsoleImpl.offsetY");
		_HEIGHT = (Integer) _synth.getDefaultProperty("LodConsoleImpl.height");
		_X = (Integer) _synth.getDefaultProperty("LodConsoleImpl.x");		
		
		initGui(); 
		initListeners();
		initMeTooMenu();
	}

	private void initMeTooMenu() {
		final WindowBoundsSetter wbSetter = my(WindowBoundsSetter.class);
		wbSetter.runWhenBaseContainerIsReady(new Runnable(){ @Override public void run() {
			my(MainMenu.class).addAction("MeToo", new Runnable() { @Override public void run() {
				showMeToo(wbSetter);
			}});
		}});
	}
	
	private void initListeners() {
		_tree.addTreeSelectionListener(new TreeSelectionListener(){ @Override public void valueChanged(TreeSelectionEvent event) {
			tryShowFiles();	
		}});
	
		_files.addListSelectionListener(new ListSelectionListener(){ @Override public void valueChanged(ListSelectionEvent event) {
			tryCompare();
		}});
	}

	private void tryShowFiles() {
		if(_tree.getSelectionCount()==0) 
			return;
		
		Object selected = _tree.getSelectionPath().getLastPathComponent();
		if(! (selected instanceof BrickVersionTreeNode))
			return;
		
		BrickVersionTreeNode node = (BrickVersionTreeNode) selected;
		
		_files.setModel(new FileVersionListModel((BrickVersion) node.sourceObject()));
	}	
	
	private void tryCompare() {
		FileVersionWrapper selectedWrapper = (FileVersionWrapper)_files.getSelectedValue();
		if(selectedWrapper==null) {
			_diffPanel.compare("","");
			return;			
		}
		
		FileVersion selected = selectedWrapper.fileVersion();
		_diffPanel.compare(new String(selected.contents()), new String(selected.contentsInCurrentVersion()));
	}
	
	private void initGui() {
		Container contentPane = getContentPane();
		
		contentPane.setLayout(new BorderLayout());
		
		JScrollPane scrollTree = my(SynthScrolls.class).create();
		JScrollPane scrollFiles = my(SynthScrolls.class).create();
		JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollTree, scrollFiles);
		
		JScrollPane scrollDiff = my(SynthScrolls.class).create();
		JSplitPane horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, verticalSplit, scrollDiff);
		
		contentPane.add(horizontalSplit, BorderLayout.CENTER);
		
		verticalSplit.setDividerSize(2);
		horizontalSplit.setDividerSize(2);
		verticalSplit.setDividerLocation(300);
		horizontalSplit.setDividerLocation(300);
	
		scrollTree.getViewport().add(_tree);
		scrollDiff.getViewport().add(_diffPanel.component());
		scrollFiles.getViewport().add(_files);
	}
	
	private void showMeToo(final WindowBoundsSetter wbSetter) {
		Rectangle unused = wbSetter.unusedArea();
		setBounds(_X , _OFFSET_Y, unused.width-_OFFSET_X, unused.height -_HEIGHT-_OFFSET_Y*2);
		setFocusableWindowState(false);
		setVisible(true);
		setFocusableWindowState(true);
	}
}