package casper.theamericancreed;

/**
 * Created by casper on 7/20/17.
 */

public class User {
    public String email;
    public String firstName;
    public String lastName;
    public String phone;
    public String alias;
    public String bio;
    public String profilePic;

    //Add multiple constructors with different paramaters
    public User (String email, String firstName, String lastName, String alias, String profilePic)
    {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.profilePic = profilePic;
    }
}
