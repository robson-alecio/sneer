package snuggin.refactorings;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

public class VisitorBasedRefactoring extends ASTVisitor implements Refactoring {

	private RefactoringContext _context;

	@Override
	public void apply(RefactoringContext context) {
		_context = context;
		try {
			context.compilationUnit().accept(this);
		} finally {
			_context = null;
		}
	}
	
	protected CompilationUnit compilationUnit() {
		return _context.compilationUnit();
	}
	
	protected AST ast() {
		return _context.ast();
	}
	
	protected ASTRewrite rewrite() {
		return _context.rewrite();
	}

	protected void remove(ASTNode node) {
		rewrite().remove(node, null);
	}

	// TODO: move to a ASTBuilder class
	protected Expression newTypeLiteral(Type type) {
		final TypeLiteral literal = ast().newTypeLiteral();
		literal.setType(type);
		return literal;
	}

	protected MethodInvocation newMethodInvocation(String name, Expression arg) {
		final MethodInvocation value = ast().newMethodInvocation();
		value.setName(ast().newSimpleName(name));
		value.arguments().add(arg);
		return value;
	}

	protected <T extends ASTNode> T copy(final T node) {
		return (T) rewrite().createCopyTarget(node);
	}

	protected ImportDeclaration newStaticImport(final String qualifiedName) {
		final ImportDeclaration newImport = ast().newImportDeclaration();
		newImport.setStatic(true);
		newImport.setName(newName(qualifiedName));
		return newImport;
	}

	protected Name newName(final String qualifiedName) {
		return ast().newName(qualifiedName);
	}

}
