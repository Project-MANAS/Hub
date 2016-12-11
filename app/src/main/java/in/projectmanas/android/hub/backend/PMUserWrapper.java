package in.projectmanas.android.hub.backend;

import com.parse.ParseUser;

/**
 * Created by reubenjohn on 11/12/16.
 */

public class PMUserWrapper {

    private static final String firstName = "firstName";
    private static final String lastName = "lastName";
    private final ParseUser user;

    public PMUserWrapper(ParseUser user) {
        this.user = user;
    }

    public String getLastName() {
        return getString(lastName);
    }

    public void setLastName(String lName) {
        this.put(lastName, lName);
    }

    public String getFirstName() {
        return getString(firstName);
    }

    public void setFirstName(String fName) {
        put(firstName, fName);
    }

    public String getString(String key) {
        return user.getString(key);
    }

    public void put(String key, Object value) {
        user.put(key, value);
    }


    public ParseUser getUser() {
        return user;
    }
}
