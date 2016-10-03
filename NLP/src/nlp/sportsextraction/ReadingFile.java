package nlp.sportsextraction;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadingFile {
	public ArrayList<ArrayList<String>> doc;
	public ArrayList<String> txt;
	private String preStr;
	
	public ReadingFile(){
		doc = new ArrayList<>();
		txt = new ArrayList<>();
		preStr = "";
	}
	
	@SuppressWarnings("unchecked")
	public void reading(String filename) throws IOException{
		Path filepath;
		filepath = Paths.get(filename);
		Scanner sc = new Scanner(filepath);
		String line = null;
		while (sc.hasNext()) {
			line = sc.nextLine();
			if(line.contains("<TEXT>")){
				continue;
			}
			if(line.contains("</TEXT>")){
				doc.add((ArrayList<String>) txt.clone());
				txt.clear();
				continue;
			}
			
			parseLine(line);
			
		}
		System.out.println(doc);
		System.out.println(doc.size());
	}
	
	
	// parse a line, store each sentence into array list
	public void parseLine(String line){
		line = line.trim();
		line = line.replaceAll("([A-Z])\\.", "$1*");
		String[] temp = null;
		String last3 = null;
		if(line.length() < 3){
			return;
		}else{
			last3 = line.substring(line.length()-3, line.length());
		}
		if(last3.contains(".") || last3.contains("?") || last3.contains("!")){
			if(last3.matches("[^a-zA-Z0-9]*")){ // fit
				temp = line.split("[.?!]");
//				System.out.println(Arrays.toString(temp));
				for (int i = 0; i < temp.length-1; i++) {
					if(i==0 && !this.preStr.isEmpty()){
						preStr += (" " + temp[i] + ".");
						txt.add(preStr);
						preStr = "";
						continue;
					}
					txt.add(temp[i] + ".");
				}
			}
			else if(last3.charAt(last3.length()-1)!='.'
					&& last3.charAt(last3.length()-1)!='?'
					&& last3.charAt(last3.length()-1)!='!'){  // need append	
				temp = line.split("[.?!]");
				for (int i = 0; i < temp.length-1; i++) {
					if(i==0 && !this.preStr.isEmpty()){
						preStr += (" " + temp[i] + ".");
						txt.add(preStr);
						preStr = "";
						continue;
					}
					txt.add(temp[i] + ".");
				}
				preStr = temp[temp.length-1];				
			}else{	// fit
				temp = line.split("[.?!]");
				if(temp.length==1){
					preStr += (" " + temp[0] + ".");
					txt.add(preStr);
					preStr = "";
				}else{
					for (int i = 0; i < temp.length; i++) {
						if(i==0 && !this.preStr.isEmpty()){
							preStr += (" " + temp[i] + ".");
							txt.add(preStr);
							preStr = "";
							continue;
						}
						txt.add(temp[i] + ".");
					}
				}	
			}
		}else{ // need append
			temp = line.split("[.?!]");
//			System.out.println(Arrays.toString(temp));
			if(temp.length==1){
				preStr += (" " + temp[0]);
			}else{
				for (int i = 0; i < temp.length-1; i++) {
					if(i==0 && !this.preStr.isEmpty()){
						preStr += (" " + temp[i] + ".");
						txt.add(preStr);
						preStr = "";
						continue;
					}
					txt.add(temp[i] + ".");
				}
				preStr = temp[temp.length-1];
			}
		}	
	}
	

}
