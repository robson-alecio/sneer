package snuggin.refactorings;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class Refactorings {
	
	public static void apply(ICompilationUnit cu, Refactoring refactoring)
			throws CoreException {
		
		final RefactoringContext context = new RefactoringContext(parse(cu));
		refactoring.apply(context);

		cu.applyTextEdit(context.rewriteAST(), null);
		cu.save(null, true);
	}

	public static void apply(IProject project, Refactoring refactoring)
			throws Exception {
		apply(JavaCore.create(project), refactoring);
	}

	public static void apply(final IJavaProject javaProject, Refactoring refactoring) throws JavaModelException, Exception {
		for (IPackageFragmentRoot root : javaProject.getAllPackageFragmentRoots()) {
			if (root.getKind() != IPackageFragmentRoot.K_SOURCE)
				continue;
			forEachPackage(root, refactoring);
		}
	}
	
	private static CompilationUnit parse(ICompilationUnit cu) {
		final ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(cu);
		return (CompilationUnit) parser.createAST(null);
	}

	private static void forEachPackage(IParent root, Refactoring refactoring)
			throws Exception {
		for (IJavaElement child : root.getChildren()) {
			switch (child.getElementType()) {
			case IJavaElement.PACKAGE_FRAGMENT:
				forEachPackage((IParent) child, refactoring);
				break;
			case IJavaElement.COMPILATION_UNIT:
				apply((ICompilationUnit) child, refactoring);
				break;
			}
		}
	}
	
	public static void apply(IJavaElement element, Refactoring refactoring) throws Exception {
		switch (element.getElementType()) {
		case IJavaElement.COMPILATION_UNIT:
			apply(ICompilationUnit.class.cast(element), refactoring);
			break;
		case IJavaElement.JAVA_PROJECT:
			apply(IJavaProject.class.cast(element), refactoring);
			break;
		case IJavaElement.PACKAGE_FRAGMENT:
		case IJavaElement.PACKAGE_FRAGMENT_ROOT:
			forEachPackage(IParent.class.cast(element), refactoring);
			break;
		}
	}
}
