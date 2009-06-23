package sneer.bricks.software.code.metaclass.asm.impl;

class ASMException extends RuntimeException {
	
    private static final long serialVersionUID = 1L;

    public ASMException(String message, Throwable t) {
        super(message, t);
    }

    public ASMException(String message) {
        super(message);
    }
}
