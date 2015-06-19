package main.java.utils;
import java.util.ArrayList;

public class CorporateMemberInfo {

	private String full_name;
	private String id;
	private ArrayList<String> children;
	private String parent_id;

	public CorporateMemberInfo(String id, String full_name) {
		this.id =id;
		this.full_name=full_name;
		children = new ArrayList<String>();
		parent_id = "";
	}

	public void setParentID(String id) {
		this.parent_id = id;
	}

	public String getFullName() {
		return this.full_name;
	}

	public String getID() {
		return this.id;
	}

	public ArrayList<String> getChildrenIDs() {
		return this.children;
	}

	public String getParentID() {
		return this.parent_id;
	}

	public void addChild(String id) throws Exception {
		// No debería haber repeticiones de hijo. Por ahora se hace un checkeo
		// lineal.
		if (children.contains(id)) {
			throw new Exception("El hijo " + id + " de " + this.id
					+ " ya existía");
		}
		children.add(id);
	}

}
