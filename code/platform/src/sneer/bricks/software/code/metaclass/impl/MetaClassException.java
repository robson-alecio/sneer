package sneer.bricks.software.code.metaclass.impl;

class MetaClassException extends RuntimeException {
	
    private static final long serialVersionUID = 1L;

    public MetaClassException(String message, Throwable t) {
        super(message, t);
    }

    public MetaClassException(String message) {
        super(message);
    }
}
