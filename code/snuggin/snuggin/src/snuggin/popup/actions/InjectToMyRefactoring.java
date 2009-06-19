/**
 * 
 */
package snuggin.popup.actions;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import snuggin.refactorings.VisitorBasedRefactoring;

public final class InjectToMyRefactoring extends VisitorBasedRefactoring {

	private boolean _usesInject = false;
	
	@Override
	public boolean visit(CompilationUnit node) {
		_usesInject = false;
		return super.visit(node);
	}
	
	@Override
	public void endVisit(CompilationUnit node) {
		if (_usesInject) {
			imports().insertLast(newStaticImport("wheel.lang.Environments.my"), null);
		}
	}
	
	@Override
	public void endVisit(ImportDeclaration node) {
		if (node.getName().toString().equals("sneer.kernel.container.Inject")) {
			remove(node);
		}
	}
	
	@Override
	public void endVisit(MarkerAnnotation node) {
		// remove @Inject
		if (isInjectAnnotation(node))
			remove(node);
	}

	@Override
	public void endVisit(FieldDeclaration node) {
		// from: @Inject ... Service foo;
		// to: ... Service foo = my(Service.class);
		if (isInjected(node)) {
			rewriteInjectedField(node);
			_usesInject = true;
		}
	}

	private void rewriteInjectedField(FieldDeclaration node) {
		removeStaticModifier(node);
		initializeToMy(node);
	}

	private void initializeToMy(FieldDeclaration node) {
		for (Object f : node.fragments()) {
			final VariableDeclarationFragment declaration = (VariableDeclarationFragment) f;
			
			final MethodInvocation value = newMethodInvocation("my", newTypeLiteral(copy(node.getType())));
			
			rewrite().set(declaration, VariableDeclarationFragment.INITIALIZER_PROPERTY, value, null);
		}
	}

	private void removeStaticModifier(FieldDeclaration node) {
		for (Object m : node.modifiers()) {
			if (isStaticModifier(m)) {
				remove(ASTNode.class.cast(m));
				break;
			}
		}
	}

	private boolean isStaticModifier(Object m) {
		return m instanceof Modifier
			&& Modifier.class.cast(m).isStatic();
	}

	private boolean isInjected(FieldDeclaration node) {
		for (Object m : node.modifiers())
			if (m instanceof MarkerAnnotation)
				if (isInjectAnnotation((MarkerAnnotation)m))
					return true;
		return false;
	}

	private boolean isInjectAnnotation(final MarkerAnnotation annotation) {
		return annotation.getTypeName().toString().equalsIgnoreCase("Inject");
	}
	

	private ListRewrite imports() {
		return rewrite().getListRewrite(compilationUnit(), CompilationUnit.IMPORTS_PROPERTY);
	}
}
