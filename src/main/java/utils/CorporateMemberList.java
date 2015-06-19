package main.java.utils;
import java.util.HashMap;

public class CorporateMemberList {

	private HashMap<String, CorporateMemberInfo> membersByID;
	private HashMap<String, CorporateMemberInfo> membersByFullname;

	public CorporateMemberList() {
		membersByID = new HashMap<String, CorporateMemberInfo>();						// membersByID sirve para accesar los objetos CorporateMemberIfo por ID
		membersByFullname = new HashMap<String, CorporateMemberInfo>();			// membersByID sirve para accesar los objetos CorporateMemberIfo por nombre
	}

	/** Crea un nuevo objeto CorporateMemberInfo a partir de la corporacion y lo agrega 
	 * a los hashMap membersByID y membersByName
	 * 
	 * @param ID
	 * @param full_name
	 * @throws Exception
	 */
	public void addMember(String ID, String full_name) throws Exception {
		String normalized_full_name = normalizeFullNameString(full_name);
		CorporateMemberInfo member = new CorporateMemberInfo(ID,
				normalized_full_name);
		membersByID.putIfAbsent(ID, member);
		membersByFullname.putIfAbsent(normalized_full_name, member);
	}
	/** Encuentra el CorporateMemberInfo y le asigna su referencia de antecesor o sucesor, dependiendo de
	 * si reference_string comienza con |wa o |wb
	 * 
	 * @param member_ID
	 * @param reference_string
	 * @throws Exception 
	 */
	public void setMemberReference(String member_ID, String reference_type, String reference_string) throws Exception {
		CorporateMemberInfo corp_element = membersByID.getOrDefault(member_ID, null);
		CorporateMemberInfo referenced_element = membersByFullname.getOrDefault(reference_string, null);
		if (referenced_element != null) {
			if(reference_type.equals("|wa")) {
				//System.out.println("REF_ELEMENT: |wa" + referenced_element.getFullName());
				corp_element.addPredecessor(referenced_element.getID());
				referenced_element.addSucessor(corp_element.getID());
			}
			else if(reference_type.equals("|wb")) {
				//System.out.println("REF_ELEMENT: |wb" + referenced_element.getFullName());
				corp_element.addSucessor(referenced_element.getID());
				referenced_element.addPredecessor(corp_element.getID());
			}
			else {
				throw new Exception("Error: typo de referencia inválido");
			}
		}
	}
	
	/** Recorre la lista de corporativos. Para cada corporativo busca a su padre y si lo encuentro lo linkea con su
	 * id y ademas se linkea a sí mismo como hijo del padre.
	 * 
	 * @throws Exception
	 */
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
	
	/** Retorna el objeto CorporateMemberInfo según ID
	 * 
	 * @param ID
	 * @return
	 */
	public CorporateMemberInfo getMember(String ID) {
		return membersByID.getOrDefault(ID, null);
	}
	
	/** Recibe el nombre completo de la corporacion, lo pasa a minúscula, saca los puntos al final de cada 
	 * una de sus partes, quita los tildes y caracteres no estandar. 
	 * Corta el nombre cuando aparece el primer pipe que no sea |a,|b,|p,|t ó |v. (en particular remueve los |d) 
	 * Retorna el nombre normalizado, conservando los pipes originales
	 * 
	 * @param corporation_original_name
	 * @return
	 * @throws Exception
	 */
	public static String normalizeFullNameString(String corporation_original_name) throws Exception {
		String[] nameTextArray = corporation_original_name.split("\\|");
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

	/** Recibe el nombre completo (con pipes) normalizado de una corporacion (resultante de aplicarle normalizeFullName). 
	 * Retorna todo lo que antecede al contenido del último pipe (excluyendo el último pipe)
	 * Ejemplo: extractParentFullName('|auchile|bfcfm|bdcc') = '|auchile|bfcfm'   
	 * 
	 * @param corporation_full_name
	 * @return
	 */
	private static String extractParentFullName(String corporation_full_name) {
		String [] corpNameArray = corporation_full_name.split("\\|");
		return corporation_full_name.substring(0, corporation_full_name.length() - corpNameArray[corpNameArray.length - 1].length() - 1);
	}
}
