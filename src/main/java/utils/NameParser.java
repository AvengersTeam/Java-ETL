/**
 * 
 */
package main.java.utils;

import java.util.ArrayList;
import java.util.List;

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
		//System.out.println(Name[0]);
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
		System.out.println(N);
		return N;
	}
	public boolean hasDigit(String x){
		for(int i= 0; i<x.length();i++){
			if(Character.isDigit(x.charAt(i)))
				return true;
		}
		return false;
	}
	
	private String unPiper(String input){
		List<String> pipePosibilites = new ArrayList<>();
		pipePosibilites.add("|a");
		pipePosibilites.add("|d");
		String result = input;
		for(String posibility : pipePosibilites){
			result = result.replace(posibility, "");
		}
		return result;
	}
	
	private String soLongDates(String input){
		String[] words;
		words = input.split(",");
		String[] result = new String[words.length];
		for(int i = 0; i < words.length ; i++){
			if(!this.hasDigit(words[i])){
				result[i] = words[i];
			}
		}
		return this.join(result, ",");
	}
	
	public String labelNameParse(String input){
		return this.soLongDates(this.unPiper(input));
	}
	
	private String join(String[] input, String conjunction){
	   StringBuilder sb = new StringBuilder();
	   boolean first = true;
	   for (int i = 0; i < input.length; i++){
		  if(input[i] != null){
			  if (first)
			         first = false;
			      else
			         sb.append(conjunction);
			      sb.append(input[i]); 
		  }
	   }
	   return sb.toString();
	}
}
