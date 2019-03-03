package crdma.genxcoders.com.disasterapp.models;

public class Helpline
{
    private String helper;

    private String contact;

    public String getHelper ()
    {
        return helper;
    }

    public void setHelper (String helper)
    {
        this.helper = helper;
    }

    public String getContact ()
    {
        return contact;
    }

    public void setContact (String contact)
    {
        this.contact = contact;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [helper = "+helper+", contact = "+contact+"]";
    }
}
			
			