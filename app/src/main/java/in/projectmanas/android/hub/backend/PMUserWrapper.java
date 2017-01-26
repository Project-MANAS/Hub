package in.projectmanas.android.hub.backend;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reubenjohn on 11/12/16.
 */

public class PMUserWrapper {

	public static final String firstName = "firstName";
	public static final String lastName = "lastName";
	public static final String division = "division";
	public static final String level = "level";

	public final ParseUser user;

	public PMUserWrapper(ParseUser user) {
		this.user = user;
	}

	public String getDivision() {
		return getString(division);
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

	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}

	public String getLevel() {
		return getString(level);
	}

	public static List<PMUserWrapper> fromList(List<ParseUser> objects) {
		ArrayList<PMUserWrapper> list = new ArrayList<>();
		for (ParseUser object : objects) {
			list.add(new PMUserWrapper(object));
		}
		return list;
	}
}
