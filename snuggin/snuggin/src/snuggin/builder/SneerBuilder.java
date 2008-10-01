package snuggin.builder;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class SneerBuilder extends IncrementalProjectBuilder {
   
	public static final String BUILDER_ID = "snuggin.sneerBuilder";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int,
	 *      java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	void checkJavaFile(IProgressMonitor monitor, IResource resource) {
		if (!isJavaFile(resource)) return;
		
		final IFile file = (IFile) resource;
		deleteMarkers(file);
		
		final CompilationUnit ast = parse(monitor, file);
		ast.accept(new CheckingVisitor(file, ast));
	}

	private CompilationUnit parse(IProgressMonitor monitor, IFile file) {
		final ICompilationUnit element = (ICompilationUnit) JavaCore.create(file);
		final ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(element);
		parser.setResolveBindings(true);
		final CompilationUnit ast = (CompilationUnit) parser.createAST(monitor);
		return ast;
	}

	private boolean isJavaFile(IResource resource) {
		return resource instanceof IFile && resource.getName().endsWith(".java");
	}

	private void deleteMarkers(IFile file) {
		try {
			file.deleteMarkers(CheckingVisitor.MARKER_TYPE, false, IResource.DEPTH_ZERO);
		} catch (CoreException ce) {
		}
	}

	protected void fullBuild(final IProgressMonitor monitor)
			throws CoreException {
		try {
			getProject().accept(new IResourceVisitor() {
				public boolean visit(IResource resource) {
					checkJavaFile(monitor, resource);
					//return true to continue visiting children.
					return true;
				}
			});
		} catch (CoreException e) {
		}
	}

	protected void incrementalBuild(IResourceDelta delta,
			final IProgressMonitor monitor) throws CoreException {
		// the visitor does the work.
		delta.accept(new IResourceDeltaVisitor() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
			 */
			public boolean visit(IResourceDelta delta) throws CoreException {
				IResource resource = delta.getResource();
				switch (delta.getKind()) {
				case IResourceDelta.ADDED:
					// handle added resource
					checkJavaFile(monitor, resource);
					break;
				case IResourceDelta.REMOVED:
					// handle removed resource
					break;
				case IResourceDelta.CHANGED:
					// handle changed resource
					checkJavaFile(monitor, resource);
					break;
				}
				//return true to continue visiting children.
				return true;
			}
		});
	}
}
