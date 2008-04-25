package sneer.lego.utils.metaclass;


public class MetaClassException
    extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public MetaClassException(String message, Throwable t)
    {
        super(message, t);
    }

    public MetaClassException(String message)
    {
        super(message);
    }
}
