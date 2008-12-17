package snuggin.popup.actions;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import snuggin.refactorings.Refactorings;

public class InjectToMy implements IObjectActionDelegate {

	private IStructuredSelection _selection;

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		try {
			for (Object resource : _selection.toList()) {
				Refactorings.apply(JavaCore.create((IResource)resource), new InjectToMyRefactoring());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		_selection = (IStructuredSelection) selection;
	}
}
