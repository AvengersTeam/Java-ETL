/**
 * 
 */
package main.java.utils;

import java.text.Normalizer;

/**
 * @author Carlo
 *
 */
public class Unidecoder {
	public Unidecoder(){
		/* Nada */
	}
	
	public String unidecode(String s) throws Exception{
		String aux = Normalizer.normalize(s, Normalizer.Form.NFKD);
		String regex = "[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+";
		aux = new String(aux.replaceAll(regex, "").getBytes("ascii"), "ascii");
		return aux.toLowerCase();
	}
}
