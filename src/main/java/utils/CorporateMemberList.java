package main.java.utils;
import java.util.HashMap;

public class CorporateMemberList {

	private HashMap<String, CorporateMemberInfo> membersByID;
	private HashMap<String, CorporateMemberInfo> membersByFullname;

	public CorporateMemberList() {
		membersByID = new HashMap<String, CorporateMemberInfo>();
		membersByFullname = new HashMap<String, CorporateMemberInfo>();
	}

	public void addMember(String ID, String full_name) throws Exception {
		String normalized_full_name = normalizeFullNameString(full_name);
		CorporateMemberInfo member = new CorporateMemberInfo(ID,
				normalized_full_name);
		membersByID.putIfAbsent(ID, member);
		membersByFullname.putIfAbsent(normalized_full_name, member);
	}

	public void linkMembers() throws Exception {
		for (CorporateMemberInfo member : membersByID.values()) {
			String parent_fullname = extractParentFullName(member.getFullName());
			if (!parent_fullname.equals("")) { // Si tiene padre según el nombre completo
				CorporateMemberInfo parent_element = membersByFullname
						.getOrDefault(parent_fullname, null);
				
				if (parent_element != null){
				// Si existe el padre en el map, se asignan las referencias correspondientes:
					member.setParentID(parent_element.getID()); // asignar padre
					parent_element.addChild(member.getID()); // Agregar este elemeto a la lista de hijos del padre.
					
				}
			  // Si no existe el padre en el map, avisar
				else {
					System.out.println("Advertencia: padre de corporativo no existe. Corp ID = '" + member.getID() + "', Nombre padre = '" + parent_fullname + "'");
				}
			}
		}
	}

	public CorporateMemberInfo getMember(String ID) {
		return membersByID.getOrDefault(ID, null);
	}

	private static String normalizeFullNameString(String originalName) throws Exception {
		String[] nameTextArray = originalName.split("\\|");
		/* corpName guarda corporation name */
		String corpName, newName = "";
		Unidecoder ud = new Unidecoder();
		
		for (int i = 0; i < nameTextArray.length; i++) {
			if (nameTextArray [i].equals("")) continue;
			String subFieldChar = nameTextArray[i].substring(0, 1);
			if (!subFieldChar.equals("a") && !subFieldChar.equals("b") && !subFieldChar.equals("p") && !subFieldChar.equals("t") && !subFieldChar.equals("v")) continue;
			String namePart = nameTextArray [i].substring(1);
			namePart = namePart.trim();
			if (namePart.equals("")) continue;
			if(namePart.substring(namePart.length() - 1).equals(".")) {
				namePart = namePart.substring(0, namePart.length() - 1);
			}
			namePart = namePart.trim();
		  newName += "|" + nameTextArray[i].substring(0, 1) + ud.unidecode(namePart);
		}
		return newName;
	}

	private static String extractParentFullName(String corporationFullName) {
		String [] corpNameArray = corporationFullName.split("\\|");
		return corporationFullName.substring(0, corporationFullName.length() - corpNameArray[corpNameArray.length - 1].length() - 1);
	}

}
