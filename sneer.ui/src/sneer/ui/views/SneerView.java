//Copyright (C) 2005 Klaus Wuestefeld and Rodrigo B. de Oliveira
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.

package sneer.ui.views;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
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
import sneer.ui.EclipseSneerUser;
import sneer.ui.SneerUIPlugin;
import sneer.ui.topten.TopTen;
import wheelexperiments.reactive.Receiver;
import wheelexperiments.reactive.Signal;
import wheelexperiments.reactive.SetSignal.SetValueChange;


public class SneerView extends ViewPart {

	private static final Image YELLOW_EXCLAMATION_MARK = scaleImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK));
	private static final Image DEFAULT_IMAGE = scaleImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT));
	private static final int AS_WIDE_AS_POSSIBLE = 50000;

	private TreeViewer _contactsViewer;
	private DrillDownAdapter _drillDownAdapter;
	private Action _addContactAction;
	private Action _sendMessageAction;
	private Action _removeContactAction;
	private Action _personalInfoAction;

	private Text _nicknameText;
	private Text _nameText;
	private Text _thoughtOfTheDayText;
	private Text _contactInfoText;
	
	private GuiContact _me;

	private Set<String> _onlineContacts = new HashSet<String>();
	private long _startupTime = 0;

	private boolean _isStopped = false;

	private final TopTen _topTen = new TopTen(sneer());
	private Combo _categories;
	

	class GuiContact {

		private final GuiContact _parent;

		private Image _image;
		
		final private String _nickname;
		final private LifeView _lifeView;
		private String _cachedName;
		private String _cachedThoughtOfTheDay;

		private Set<GuiContact> _contacts = new HashSet<GuiContact>();
		
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
			
			_lifeView.name().addTransientReceiver(new Receiver<String>() {
				public void receive(String newValue) {
					if (_isStopped) return;
					
					_cachedName = newValue;
					refreshMyTreeItem();
				}
			});

			_lifeView.thoughtOfTheDay().addTransientReceiver(new Receiver<String>() {
				public void receive(String newValue) {
					if (_isStopped) return;
					
					_cachedThoughtOfTheDay = newValue;
					refreshMyTreeItem();
				}
			});

			_lifeView.picture().addTransientReceiver(new Receiver<JpgImage>() {
				public void receive(JpgImage newValue) {
					if (_isStopped) return;
					
					_image = null;
					refreshMyTreeItem();
				}
			});

			_lifeView.nicknames().addTransientSetReceiver(new Receiver<SetValueChange<String>>() {
				public void receive(SetValueChange<String> nicknamesChanged) {
					if (_isStopped) return;

					for (String newNickname : nicknamesChanged.elementsAdded()) {
						_contacts.add(new GuiContact(newNickname, GuiContact.this));
					}

					for (String removedNickname : nicknamesChanged.elementsRemoved()) {
						for (GuiContact contact : _contacts) {
							if (contact.nickname().equals(removedNickname)) {
								_contacts.remove(contact);
								break;
							}
						}
					}
					
					refreshMyTreeItem();
				}
			});
		}

		private void refreshMyTreeItem() {
			UIJob job = new UIJob("Contact refresh") {
				public IStatus runInUIThread(IProgressMonitor monitor) {
					_contactsViewer.refresh(GuiContact.this, true);
					return Status.OK_STATUS;
				}						
			};
			job.setSystem(true);
			job.schedule();
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
			Date lastSighting = _lifeView.lastSightingDate();   //FIXME Use the name signal.currency() intead of lastSightingDate().
			if (lastSighting == null) return false;
			return System.currentTimeMillis() - lastSighting.getTime() < 1000 * 60 * 2;
		}
		
		public GuiContact[] contacts() {
			GuiContact[] result = new GuiContact[_contacts.size()];
			return _contacts.toArray(result);
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
			JpgImage jpg = _lifeView.picture().currentValue();
			if (jpg == null) return DEFAULT_IMAGE;
			ImageData data = loader.load(jpg.jpegFileContents())[0];
			return new Image(null, scaleImageData(data));
		}
		
		public String nicknamePath() {
			if (distance() <= 1) return nickname();
			return _parent.nicknamePath() + " > " + nickname(); 
		}

		public String name() {
			return _cachedName;
		}

		public String thoughtOfTheDay() {
			return _cachedThoughtOfTheDay;
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
		
		public boolean hasChildren(Object contact) {
			return ((GuiContact)contact).contacts().length != 0;
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
			String result = contact.nickname() + " (" + contact.name() + ")";
			
			String thought = contact.thoughtOfTheDay();
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

		Composite mainForm = new SashForm(parent, SWT.HORIZONTAL | SWT.SMOOTH);
		createContactsViewer(mainForm);
		
		Composite contactForm = new SashForm(mainForm, SWT.VERTICAL | SWT.SMOOTH); //TODO Ricardo Birmann, please make the divider bar between both forms visible.
		createProfileForm(contactForm);
		createTopTenForm(contactForm);

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
	}

	private void createTopTenForm(Composite parent) {
		Composite form = new Composite(parent, 0);
		form.setLayout(new RowLayout(SWT.VERTICAL));
		
		new Label(form, 0).setText("'TopTen' Categories:");
		_categories = new Combo(form, SWT.DROP_DOWN);
		_categories.setLayoutData(new RowData(100, SWT.DEFAULT));
		
		_categories.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.keyCode != 13) return;
				_topTen.newCategory(_categories.getText());
				refreshTopTenForm(_me);
			}
		});
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
			manager.add(_sendMessageAction);
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

		
		
		_sendMessageAction = new Action() {
			public void run() {
				GuiContact contact = selectedContact();
				if (contact == null) return;
				if (contact.distance() != 1) {
					user().acknowledge("For now, you can only send messages to your immediate contacts.");
					return;
				}
				user().getChatForContact(contact.nickname());
			}
		};
		_sendMessageAction.setText("Send Message");
		_sendMessageAction.setToolTipText("Send a pop-up message to this contact.");
		_sendMessageAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_TOOL_COPY));

		
		
		_removeContactAction = new Action() {
			public void run() {
				GuiContact contact = selectedContact();
				if (contact == null) return;
				if (contact.distance() != 1) {
					user().acknowledge("You can only remove your immediate contacts.");
					return;
				}
				sneer().removeContact(contact.nickname());
			}
		};
		_removeContactAction.setText("Remove Contact");  //FIXME Removing any contact causes NullPointerException.
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
	}

	private void hookDoubleClickAction() {
//		_contactsViewer.addDoubleClickListener(new IDoubleClickListener() {
//			public void doubleClick(DoubleClickEvent event) {
//				_xyzAction.run();
//			}
//		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		_contactsViewer.getControl().setFocus();
	}

	public void refresh() {
		UIJob job = new UIJob("Contact refresh") {
			public IStatus runInUIThread(IProgressMonitor monitor) {
				try {
					GuiContact contact = selectedContact();
					refreshContactForm(contact);
					refreshTopTenForm(contact);
					
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

	private void refreshContactForm(GuiContact contact) {
		if (_nicknameText.isDisposed()) return;
		
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
	}
	
	private void refreshTopTenForm(GuiContact contact) {
		if (_categories.isDisposed()) return;
		
		if (contact == null) {
			clearTopTenForm();
			return;
		}
		_categories.setItems(topTenCategories(contact.lifeView()));
	}

	@SuppressWarnings("unchecked")
	public String[] topTenCategories(LifeView lifeView) {
		List<String> list = _topTen.categoriesList(lifeView);
		if (list == null) return new String[]{"<TopTen not installed>"};

		String[] result = new String[list.size()];
		for (int i = 0; i < result.length; i++)
			result[i] = list.get(i);

		return result;
	}
	
	private void clearTopTenForm() {
		_categories.setItems(new String[0]);
	}

	private static Image scaleImage(Image image) {
		return new Image(null, scaleImageData(image.getImageData()));
	}

	static private ImageData scaleImageData(ImageData data) {
		return data.scaledTo(32, 32);
	}
	
	private String nullToEmptyString(Signal<String> signal) {
		return nullToEmptyString(signal.currentValue());
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
	}

	public void stop() {
		_isStopped = true;
	}

	private EclipseSneerUser user() {
		return SneerUIPlugin.sneerUser();
	}
}