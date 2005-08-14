//Copyright (C) 2005 Klaus Wuestefeld and Rodrigo B. de Oliveira
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.

package sneer.ui.views;

import java.util.Date;
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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
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


public class ContactsView extends ViewPart {
	private TreeViewer _treeViewer;
	private DrillDownAdapter _drillDownAdapter;
	private Action _addContactAction;
	private Action _personalInfoAction;
	private Action _doubleClickAction;

	static class Contact {
		
		private static final Image YELLOW_EXCLAMATION_MARK = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK);
		private static final Image DEFAULT_IMAGE = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);

		final private String _nickname;
		final private LifeView _lifeView;

		private Image _image;
		
		Contact(LifeView lifeView) {
			this("me", lifeView);
		}
		
		Contact(String nickname, Contact parent) {
			this(nickname, parent._lifeView.contact(nickname));
		}
		
		private Contact(String nickname, LifeView lifeView) {
			lifeView.toString();			
			_nickname = nickname;
			_lifeView = lifeView;
		}
		
		String nickname() {
			return _nickname;
		}
		
		LifeView lifeView() {
			return _lifeView;
		}

		public boolean isOnline() {
			Date lastSighting = _lifeView.lastSightingDate();
			if (lastSighting == null) return false;
			return System.currentTimeMillis() - lastSighting.getTime() < 1000 * 10;
		}
		
		public Contact[] contacts() {
			if (!isOnline()) return new Contact[0];
			
			Set<String> nicknames = _lifeView.nicknames();
			
			Contact[] contacts = new Contact[nicknames.size()];
			int i = 0;
			for (String nickname : nicknames) {
				contacts[i++] = new Contact(nickname, this);
			}
			return contacts;
		}
		
		@Override
		public int hashCode() {
			return _lifeView.hashCode();
		}
		
		@Override
		public boolean equals(Object other) {
			return other instanceof Contact
				? _lifeView.equals(((Contact)other)._lifeView)
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
	}
	
	class ContactsTreeContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		
		public void dispose() {
		}
		
		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				Contact me = new Contact(life());
				return me.contacts();
			}
			return getChildren(parent);
		}
		
		public Object getParent(Object child) {
			return null;
		}
		
		public Object[] getChildren(Object parent) {
			return ((Contact)parent).contacts();
		}
		
		public boolean hasChildren(Object parent) {
			return ((Contact)parent).isOnline();
		}
	}
	
	class ContactsTreeLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			Contact contact = (Contact)obj;
			return contact.isOnline()
				? onlineLabel(contact)
				: contact.nickname();
		}
		
		private String onlineLabel(Contact contact) {
			//nickname (Full Name) - Thought of the day
			String result = contact.nickname() + " (" + contact.lifeView().name() + ")";
			
			String thought = contact.lifeView().thoughtOfTheDay();
			if (thought == null) return result;
			
			return result + " - " + thought;
		}
		
		public Image getImage(Object obj) {
			return ((Contact)obj).image();
		}

	}
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public ContactsView() {
	}
	
	private static Sneer sneer() {
		return SneerUIPlugin.sneer();
	}
	
	private static Life life() {
		return sneer().life();
	}


	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		
		SneerUIPlugin.sneerUser().contactsView(this);
		_treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		_drillDownAdapter = new DrillDownAdapter(_treeViewer);
		_treeViewer.setContentProvider(new ContactsTreeContentProvider());
		_treeViewer.setLabelProvider(new ContactsTreeLabelProvider());
		_treeViewer.setSorter(new NameSorter());
		_treeViewer.setInput(getViewSite());
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				ContactsView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(_treeViewer.getControl());
		_treeViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, _treeViewer);
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
		manager.add(_addContactAction);
		manager.add(new Separator());
		manager.add(_personalInfoAction);
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
				ISelection selection = _treeViewer.getSelection();
				Contact contact = (Contact)((IStructuredSelection)selection).getFirstElement();
				if (contact == null) return; //The tree view node might have been closed.
				sneer().sendPublicMessage(); //TODO: Make this a private message to the chosen contact.
			}
		};
	}

	private void hookDoubleClickAction() {
		_treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				_doubleClickAction.run();
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		_treeViewer.getControl().setFocus();
	}

	public void refresh() {
		if (null == _treeViewer) return;
		UIJob job = new UIJob("Contact refresh") {
			public IStatus runInUIThread(IProgressMonitor monitor) {
				try {
					refreshTree();
					sneer().checkNewMessages();
				} catch (RuntimeException e) {
					e.printStackTrace(); // Eclipse does not show the stack trace.
				}
				return Status.OK_STATUS;
			}

		};
		job.setSystem(true);
		job.schedule();
	}

	protected void refreshTree() {
		Object[] elements = _treeViewer.getVisibleExpandedElements();
		if (elements.length == 0) {
			_treeViewer.refresh();
			return;
		}
		
		for (Object element : elements) {
			_treeViewer.refresh(element, true);
		}
		_treeViewer.setExpandedElements(elements);
	}
}