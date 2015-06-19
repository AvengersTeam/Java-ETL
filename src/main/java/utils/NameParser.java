/**
 * 
 */
package main.java.utils;

import java.text.Normalizer;

/**
 * @author Avengers
 *
 */
public class NameParser {
	
	public String ParserName(String input){
		String[] Name;
		String N="";
		//separar por comas		
		Name=input.trim().split(",");
		//ver si tiene una fecha asociada
		if(hasDigit(Name[Name.length-1])){
			for(int i= 0; i<Name.length-2;i++){
				N=N+Name[i].trim()+"";
			}
			N+=Name[Name.length-2];
		}
		else{
			for(int i= 0; i<Name.length-1;i++){
				N=N+Name[i].trim()+"";
			}
			N+=Name[Name.length-1];
		}
		return N;
	}
	public boolean hasDigit(String x){
		for(int i= 0; i<x.length();i++){
			if(Character.isDigit(x.charAt(i)))
				return true;
		}
		return false;
	}
	public String stripAccents(String s) 
	{
	    s = Normalizer.normalize(s, Normalizer.Form.NFD);
	    s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	    return s;
	}
	
}
