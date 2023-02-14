package mosesweb.AccountHelper;

public class CustomerNotFoundException extends RuntimeException
{
    public CustomerNotFoundException(Integer id)
    {
        super("Could not find customer " + id);
    }
}
