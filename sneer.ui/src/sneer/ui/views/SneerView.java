//Copyright (C) 2005 Klaus Wuestefeld and Rodrigo B. de Oliveira
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.

package sneer.ui.views;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.UIJob;

import sneer.Sneer;
import sneer.life.JpgImage;
import sneer.life.Life;
import sneer.life.LifeView;
import sneer.ui.SneerUIPlugin;
import wheelexperiments.views.SetView.Observer;


public class SneerView extends ViewPart {

	private static final Image YELLOW_EXCLAMATION_MARK = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK);
	private static final Image DEFAULT_IMAGE = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
	private static final int AS_WIDE_AS_POSSIBLE = 50000;

	private TreeViewer _contactsViewer;
	private DrillDownAdapter _drillDownAdapter;
	private Action _addContactAction;
	private Action _removeContactAction;
	private Action _personalInfoAction;
	private Action _doubleClickAction;

	private Text _nicknameText;
	private Text _nameText;
	private Text _thoughtOfTheDayText;
	private Text _contactInfoText;
	private Text _profileText;
	
	private GuiContact _me;

	private Set<String> _onlineContacts = new HashSet<String>();
	private long _startupTime = 0;

	private boolean _isStopped = false;
	

	class GuiContact {

		private final GuiContact _parent;

		private Image _image;
		
		final private String _nickname;
		final private LifeView _lifeView;

		private GuiContact[] _contacts;

		
		GuiContact(LifeView lifeView) {
			this("Me", lifeView, null);
		}
		
		GuiContact(String nickname, GuiContact parent) {
			this(nickname, parent._lifeView.contact(nickname), parent);
		}
		
		private GuiContact(String nickname, LifeView lifeView, GuiContact parent) {
			_nickname = nickname;
			_lifeView = lifeView;
			_parent = parent;
		}
		
		String nickname() {
			return _nickname;
		}
		
		LifeView lifeView() {
			return _lifeView;
		}

		public boolean isOnline() {
			boolean isOnline = calculateOnline();
			notifyOnline(isOnline);
			return isOnline;
		}

		private void notifyOnline(boolean isOnline) {
			if (distance() != 1) return;

			boolean wasOnline = _onlineContacts.contains(_nickname);
			if (!isOnline) {
				_onlineContacts.remove(_nickname);
				return;
			}
			_onlineContacts.add(_nickname);

			if (wasOnline) return;
			if (ignoringInitialConnections()) return;
			
			sneer().acknowledgeContactOnline(_nickname);
		}

		private int distance() {
			return _parent == null
				? 0
				: _parent.distance() + 1;
		}

		private boolean ignoringInitialConnections() {
			return System.currentTimeMillis() - startupTime() < 1000 * 30;
		}

		private boolean calculateOnline() {
			Date lastSighting = _lifeView.lastSightingDate();
			if (lastSighting == null) return false;
			return System.currentTimeMillis() - lastSighting.getTime() < 1000 * 60;
		}
		
		public GuiContact[] contacts() {
			if (_contacts == null) _contacts = refreshContacts();

			return _contacts;
		}

		private GuiContact[] refreshContacts() {
			if (!isOnline()) return new GuiContact[0];
			
			Set<String> nicknames = _lifeView.nicknames();
			
			GuiContact[] contacts = new GuiContact[nicknames.size()];
			int i = 0;
			for (String nickname : nicknames) {
				contacts[i++] = new GuiContact(nickname, this);
			}
			return contacts;
		}
		
		@Override
		public int hashCode() {
			return _lifeView.hashCode();
		}
		
		@Override
		public boolean equals(Object other) {
			return other instanceof GuiContact
				? _lifeView.equals(((GuiContact)other)._lifeView)
				: false;
		}

		public Image image() {
			return isOnline() 
				? onlineImage()
				: YELLOW_EXCLAMATION_MARK;
		}

		private Image onlineImage() {
			if (_image == null) _image = produceImage();
			return _image;
		}

		private Image produceImage() {
			ImageLoader loader = new ImageLoader();
			JpgImage jpg = _lifeView.picture();
			if (jpg == null) return DEFAULT_IMAGE;
			ImageData data = loader.load(jpg.jpegFileContents())[0];
			return new Image(null, data.scaledTo(32, 32));
		}

		public String nicknamePath() {
			if (distance() <= 1) return nickname();
			return _parent.nicknamePath() + " > " + nickname(); 
		}
	}

	private GuiContact selectedContact() {
		IStructuredSelection selection = (IStructuredSelection)_contactsViewer.getSelection();
		if (selection.size() != 1) return null;
		return (GuiContact)selection.getFirstElement();
	}
	
	private long startupTime() {
		if (_startupTime == 0) _startupTime = System.currentTimeMillis();
		return _startupTime;
	}

	class ContactsTreeContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		
		public void dispose() {}
		
		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				return me();
			}
			return getChildren(parent);
		}

		public Object getParent(Object child) {
			return ((GuiContact)child)._parent;
		}
		
		public Object[] getChildren(Object parent) {
			return ((GuiContact)parent).contacts();
		}
		
		public boolean hasChildren(Object parent) {
			return ((GuiContact)parent).isOnline();
		}
	}

	private Object[] me() {
		if (_me == null) _me = new GuiContact(life());
		
		return new Object[]{_me};
	}
	
	class ContactsTreeLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			GuiContact contact = (GuiContact)obj;
			return contact.isOnline()
				? onlineLabel(contact)
				: contact.nickname();
		}
		
		private String onlineLabel(GuiContact contact) {
			//nickname (Full Name) - Thought of the day
			String result = contact.nickname() + " (" + contact.lifeView().name() + ")";
			
			String thought = contact.lifeView().thoughtOfTheDay();
			if (thought == null) return result;
			
			return result + " - " + thought;
		}
		
		public Image getImage(Object obj) {
			return ((GuiContact)obj).image();
		}

	}
	
	class NameSorter extends ViewerSorter {
	}

	private static Sneer sneer() {
		return SneerUIPlugin.sneer();
	}
	
	private static Life life() {
		return sneer().life();
	}

	/**
	 * This is a callback that will allow Eclipse
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {

		Composite sashForm = new SashForm(parent, SWT.HORIZONTAL | SWT.SMOOTH);

		createContactsViewer(sashForm);
		createProfileForm(sashForm);

		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();

		SneerUIPlugin.sneerUser().contactsView(this);
	}

	private void createProfileForm(Composite parent) {
		Composite form = new Composite(parent, 0);
		form.setLayout(new RowLayout(SWT.VERTICAL));
		
		_nicknameText = createTextFieldWithLabel(form, "Nickname:");
		_nicknameText.setLayoutData(new RowData(AS_WIDE_AS_POSSIBLE, SWT.DEFAULT));
		
		_nameText = createTextFieldWithLabel(form, "Name:");
		_nameText.setLayoutData(new RowData(AS_WIDE_AS_POSSIBLE, SWT.DEFAULT));
		
		_thoughtOfTheDayText = createTextFieldWithLabel(form, "Thought of the Day:");
		_thoughtOfTheDayText.setLayoutData(new RowData(AS_WIDE_AS_POSSIBLE, SWT.DEFAULT));
		
		_contactInfoText =  createTextFieldWithLabel(form, "Contact Information:");
		_contactInfoText.setLayoutData(new RowData(AS_WIDE_AS_POSSIBLE, 60));

		_profileText =  createTextFieldWithLabel(form, "Profile:");
		_profileText.setLayoutData(new RowData(AS_WIDE_AS_POSSIBLE, 60));
	}

	private Text createTextFieldWithLabel(Composite parent, String label) {
		new Label(parent, 0).setText(label);
		Text textField = new Text(parent, SWT.MULTI | SWT.WRAP | SWT.BORDER);
		return textField;
	}

	private void createContactsViewer(Composite sashForm) {
		_contactsViewer = new TreeViewer(sashForm, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		_drillDownAdapter = new DrillDownAdapter(_contactsViewer);
		_contactsViewer.setContentProvider(new ContactsTreeContentProvider());
		_contactsViewer.setLabelProvider(new ContactsTreeLabelProvider());
		_contactsViewer.setSorter(new NameSorter());
		_contactsViewer.setInput(getViewSite());
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SneerView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(_contactsViewer.getControl());
		_contactsViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, _contactsViewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(_addContactAction);
		manager.add(new Separator());
		manager.add(_personalInfoAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		if (selectedContact() != null) {
			manager.add(_removeContactAction);
			manager.add(new Separator());
		}
		_drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(_addContactAction);
		manager.add(_personalInfoAction);
		manager.add(new Separator());
		_drillDownAdapter.addNavigationActions(manager);
	}
	
	private void makeActions() {
		_addContactAction = new Action() {
			public void run() {
				sneer().addContact();
			}
		};
		_addContactAction.setText("Add Contact...");
		_addContactAction.setToolTipText("Give a nickname to a sovereign contact.");
		_addContactAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT));

		_removeContactAction = new Action() {
			public void run() {
				GuiContact contact = selectedContact();
				if (contact == null) return;
				sneer().removeContact(contact.nickname());
			}
		};
		_removeContactAction.setText("Remove Contact");
		_removeContactAction.setToolTipText("Remove selected contact.");
		_removeContactAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		
		_personalInfoAction = new Action() {
			public void run() {
				sneer().editPersonalInfo();
			}
		};
		_personalInfoAction.setText("Personal Info...");
		_personalInfoAction.setToolTipText("Edit your sovereign info.");
		_personalInfoAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		_doubleClickAction = new Action() {
			public void run() {
				GuiContact contact = selectedContact();
				if (contact == null) return; //The tree view node might have been closed.
				sneer().sendPublicMessage(); //TODO: Make this a private message to the chosen contact.
			}

		};
	}

	private void hookDoubleClickAction() {
		_contactsViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				_doubleClickAction.run();
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		_contactsViewer.getControl().setFocus();
	}

	public void refresh() {
		if (null == _contactsViewer) return;
		UIJob job = new UIJob("Contact refresh") {
			public IStatus runInUIThread(IProgressMonitor monitor) {
				try {
					refreshContacts();
					refreshContactForm();
					sneer().checkNewMessages();
				} catch (RuntimeException e) {
					e.printStackTrace(); // Eclipse does not show the stack trace.
				}
				return Status.OK_STATUS;
			}

		};
		if (_isStopped) return;
		
		job.setSystem(true);
		job.schedule();
	}

	private void refreshContactForm() {
		if (_nicknameText.isDisposed()) return;
		
		GuiContact contact = selectedContact();
		if (contact == null) {
			clearContactForm();
			return;
		}
		_nicknameText.setText(contact.nicknamePath());

		LifeView lifeView = contact.lifeView();
		if (lifeView == null || lifeView.lastSightingDate() == null) {
			clearSightedContactFields();
			return;
		}
		_nameText.setText(nullToEmptyString(lifeView.name()));
		_thoughtOfTheDayText.setText(nullToEmptyString(lifeView.thoughtOfTheDay()));
		_contactInfoText.setText(nullToEmptyString(lifeView.contactInfo()));
		_profileText.setText(nullToEmptyString(lifeView.profile()));
	}

	private String nullToEmptyString(String string) {
		return string == null ? "" : string;
	}

	private void clearContactForm() {
		_nicknameText.setText("");
		clearSightedContactFields();
	}

	private void clearSightedContactFields() {
		_nameText.setText("");
		_thoughtOfTheDayText.setText("");
		_contactInfoText.setText("");
		_profileText.setText("");
	}

	private void refreshContacts() {
		if (_contactsViewer.getControl().isDisposed()) return;
		
		Object[] elements = _contactsViewer.getVisibleExpandedElements();
		if (elements.length == 0) {
			_contactsViewer.refresh();
			return;
		}
		
		for (Object element : elements) {
			_contactsViewer.refresh(element, true);
		}
		_contactsViewer.setExpandedElements(elements);
	}

	public void stop() {
		_isStopped = true;
	}
}