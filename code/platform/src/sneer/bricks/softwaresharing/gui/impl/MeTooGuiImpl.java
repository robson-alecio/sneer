package sneer.bricks.softwaresharing.gui.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import sneer.bricks.skin.image.ImageFactory;
import sneer.bricks.skin.main.menu.MainMenu;
import sneer.bricks.skin.main.synth.Synth;
import sneer.bricks.skin.main.synth.scroll.SynthScrolls;
import sneer.bricks.skin.windowboundssetter.WindowBoundsSetter;
import sneer.bricks.snapps.diff.text.gui.TextDiffPanel;
import sneer.bricks.snapps.diff.text.gui.TextDiffPanels;
import sneer.bricks.snapps.system.log.gui.LogConsole;
import sneer.bricks.softwaresharing.BrickVersion;
import sneer.bricks.softwaresharing.FileVersion;
import sneer.bricks.softwaresharing.BrickVersion.Status;
import sneer.bricks.softwaresharing.gui.MeTooGui;

class MeTooGuiImpl extends JFrame implements MeTooGui{

	private static final JToggleButton _meTooButton = new JToggleButton(loadIcon("add.png"));
	private static final JToggleButton _rejectButton = new JToggleButton(loadIcon("rejectedVersion.png"));
	private final JTree _tree = new JTree();
	private final JList _files = new JList();
	private final TextDiffPanel _diffPanel = my(TextDiffPanels.class).newPanel();
	
	private final int _OFFSET_X;
	private final int _OFFSET_Y;
	private final int _HEIGHT;
	private final int _X;
	protected Object _lastSelectedNode;
	
	private static ImageIcon loadIcon(String fileName){
		return my(ImageFactory.class).getIcon(MeTooGuiImpl.class, fileName);
	}
	
	MeTooGuiImpl(){
		super("MeToo");
		
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
			_lastSelectedNode = event.getPath().getLastPathComponent();
			tryShowFiles();
			adjustToolbar();
		}});
	
		_files.addListSelectionListener(new ListSelectionListener(){ @Override public void valueChanged(ListSelectionEvent event) {
			tryCompare();
		}});
		
		_meTooButton.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			BrickVersion version = selectedBrickVersion();
			version.setStagedForExecution(!version.isStagedForExecution());
			_meTooButton.setSelected(version.isStagedForExecution());
			_tree.repaint();
		}});
		
		_rejectButton.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			BrickVersion version = selectedBrickVersion();
			version.setRejected(version.status()!=Status.REJECTED);
			_rejectButton.setSelected(version.status()==Status.REJECTED);
			_tree.repaint();
		}});
	}

	protected void adjustToolbar() {
		_meTooButton.setSelected(false);
		_meTooButton.setEnabled(false);
		_rejectButton.setSelected(false);
		_rejectButton.setEnabled(false);
		
		if(!(_lastSelectedNode instanceof BrickVersionTreeNode))
			return;
		
		BrickVersion version = selectedBrickVersion();
		
		if(version.status()==Status.CURRENT)
			return;
		
		_meTooButton.setEnabled(true);
		_meTooButton.setSelected(version.isStagedForExecution());

		_rejectButton.setEnabled(true);
		_rejectButton.setSelected(version.status()==Status.REJECTED);
	}

	private BrickVersion selectedBrickVersion() {
		return (BrickVersion) ((BrickVersionTreeNode) _lastSelectedNode).sourceObject();
	}

	private void tryShowFiles() {
		if(_tree.getSelectionCount()==0) 
			return;
		
		Object selected = _tree.getSelectionPath().getLastPathComponent();
		if(! (selected instanceof BrickVersionTreeNode)){
			_files.setModel(new DefaultListModel());
			return;
		}
		
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
		_tree.setRootVisible(false);
		_tree.setModel(new DefaultTreeModel(new RootTreeNode(FakeModel.bricks())));
		_tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		_tree.setCellRenderer(new MeTooTreeCellRenderer());
		_tree.setBorder(new EmptyBorder(5,5,5,5));
		_tree.setShowsRootHandles(true);
		
		_files.setBorder(new EmptyBorder(5,5,5,5));
		_files.setCellRenderer(new MeeTooListCellRenderer());

		_meTooButton.setEnabled(false);
		_rejectButton.setEnabled(false);
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		JToolBar toolbar = new JToolBar();
		contentPane.add(toolbar, BorderLayout.NORTH);
		toolbar.add(_meTooButton);
		toolbar.add(_rejectButton);
		
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