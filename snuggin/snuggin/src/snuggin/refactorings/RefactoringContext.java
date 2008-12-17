package snuggin.refactorings;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEdit;

public class RefactoringContext {

	private final CompilationUnit _root;
	private final ASTRewrite _rewrite;
	
	public RefactoringContext(CompilationUnit root) {
		_root = root;
		_rewrite = ASTRewrite.create(ast());
	}
	
	public ASTRewrite rewrite() {
		return _rewrite;
	}
	
	public CompilationUnit compilationUnit() {
		return _root;
	}
	
	public AST ast() {
		return _root.getAST();
	}

	public TextEdit rewriteAST() throws CoreException, IllegalArgumentException {
		return _rewrite.rewriteAST();
	}

}
