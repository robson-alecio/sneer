/**
 * 
 */
package snuggin.builder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public final class CheckingVisitor extends ASTVisitor {
	
	static final String MARKER_TYPE = "snuggin.sneerProblem";
	
	private final IFile _file;
	private final CompilationUnit _ast;

	public CheckingVisitor(IFile file, CompilationUnit ast) {
		_file = file;
		_ast = ast;
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		if (node.isInterface())
			return false;
		
		final ITypeBinding type = node.resolveBinding();
		final ITypeBinding brickInterface = brickInterfaceFor(type);
		if (brickInterface == null)
			return false;
		
		if (!isDefaultBrickImplFor(type, brickInterface))
			return false;
		
		checkBrickTypeRules(node, brickInterface);
		return false;
	}

	private boolean isDefaultBrickImplFor(ITypeBinding type, ITypeBinding brickInterface) {
		final String defaultImplPackage = brickInterface.getPackage().getName() + ".impl";
		return defaultImplPackage.equals(type.getPackage().getName());
	}

	private ITypeBinding brickInterfaceFor(ITypeBinding type) {
		for (ITypeBinding itf : type.getInterfaces())
			if (isBrick(itf))
				return itf;
		return null;
	}

	private void checkBrickTypeRules(TypeDeclaration node, ITypeBinding brickInterface) {
		if (!node.getName().toString().endsWith("Impl"))
			addWarning(node, "Brick implementation should be named '" + brickInterface.getName() + "Impl'");
		if (Modifier.isPublic(node.getModifiers()))
			addWarning(node, "Brick implementation must not be public.");
	}

	private void addWarning(TypeDeclaration node, final String message) {
		addMarker(_file, message, _ast.getLineNumber(node.getStartPosition()), IMarker.SEVERITY_WARNING);
	}

	private boolean isBrick(final ITypeBinding type) {
		for (ITypeBinding itf : type.getInterfaces())
			if (itf.getName().endsWith("Brick")
				|| isBrick(itf))
				return true;
		return false;
	}
	
	void addMarker(IFile file, String message, int lineNumber,
			int severity) {
		try {
			IMarker marker = file.createMarker(MARKER_TYPE);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, severity);
			if (lineNumber == -1) {
				lineNumber = 1;
			}
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
		} catch (CoreException e) {
		}
	}
}