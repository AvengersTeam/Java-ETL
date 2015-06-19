package main.java.utils;
import java.util.ArrayList;

public class CorporateMemberInfo {

	private String full_name;
	private String id;
	private ArrayList<String> children;
	private String parent_id;
	private ArrayList<String> predecessors_id; 					// Id de la corporacion antecesora (subcampo wa)
	private ArrayList<String> sucessors_id;							// Id de la corporacion sucesora (subcampo wb)

	public CorporateMemberInfo(String id, String full_name) {
		this.id =id;
		this.full_name=full_name;
		children = new ArrayList<String>();
		predecessors_id = new ArrayList<>();
		sucessors_id = new ArrayList<>();
		parent_id = "";
	}

	public void setParentID(String id) {
		this.parent_id = id;
	}

	public String getFullName() {
		return this.full_name;
	}

	public ArrayList<String> getPrevious_id() {
		return predecessors_id;
	}

	public void setPrevious_id(ArrayList<String> previous_id) {
		this.predecessors_id = previous_id;
	}

	public ArrayList<String> getNext_id() {
		return sucessors_id;
	}

	public void setNext_id(ArrayList<String> next_id) {
		this.sucessors_id = next_id;
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

	public void addPredecessor(String predecessor_id) throws Exception {
		if (predecessors_id.contains(predecessor_id)) {
			throw new Exception("El predecesor " + predecessor_id + " de " + this.id + " ya existía");
		}
		predecessors_id.add(predecessor_id);
	}
	
	public void addSucessor(String sucessor_id) throws Exception {
		if (sucessors_id.contains(sucessor_id)) {
			throw new Exception("El sucesor " + sucessor_id + " de " + this.id + " ya existía");
		}
		sucessors_id.add(sucessor_id);
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
