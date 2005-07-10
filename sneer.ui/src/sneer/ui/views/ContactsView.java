package sneer.ui.views;

import java.util.*;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.UIJob;

import sneer.*;
import sneer.life.*;
import sneer.ui.SneerUIPlugin;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class ContactsView extends ViewPart {
	private TreeViewer _treeViewer;
	private DrillDownAdapter drillDownAdapter;
	private Action _addContactAction;
	private Action _personalInfoAction;
	private Action doubleClickAction;

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	
	static class Contact {
		
		final private String _nickname;
		final private LifeView _lifeView;
		
		Contact(LifeView lifeView) {
			this("me", lifeView);
		}
		
		Contact(String nickname, Contact parent) {
			this(nickname, parent._lifeView.contact(nickname));
		}
		
		private Contact(String nickname, LifeView lifeView) {
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
			Date lastSighting = _lifeView.lastSighting();
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
			// nickname (Real Name) - thought of the day
			StringBuilder label = new StringBuilder(contact.nickname());
			label.append(" (");
			label.append(contact.lifeView().name());
			label.append(")");
			String thought = contact.lifeView().thoughtOfTheDay();
			if (null != thought) {
				label.append(" - ");
				label.append(thought);
			}
			return label.toString();
		}
		
		public Image getImage(Object obj) {
			String imageKey = ((Contact)obj).isOnline() 
				? ISharedImages.IMG_OBJ_ELEMENT
				: ISharedImages.IMG_OBJS_WARN_TSK;
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
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
		drillDownAdapter = new DrillDownAdapter(_treeViewer);
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
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(_addContactAction);
		manager.add(_personalInfoAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
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
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = _treeViewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		_treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			_treeViewer.getControl().getShell(),
			"Contacts View",
			message);
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
				_treeViewer.refresh();
				return Status.OK_STATUS;
			}
		};
		job.setSystem(true);
		job.schedule();
	}
}