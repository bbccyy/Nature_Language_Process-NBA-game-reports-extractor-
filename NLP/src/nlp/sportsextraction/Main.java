package nlp.sportsextraction;

import Jet.*; 
import Jet.Tipster.*; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector; 
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException; 


public class Main {
	// make sure you set these two path correctly before compiling and running this program.
	public static final String match_Teams_and_Scores = "/Users/uername/Documents/jet/props/nbaScores.jet";
	public static final String match_Player_and_Points = "/Users/username/Documents/jet/props/point.jet";
	
	public static final HashMap<String, String> NBA_TEAMS = new HashMap<>();
	public static final HashMap<String, String> NBA_CITYS = new HashMap<>();
	
	public static final String[] DIGITS = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
	public static final String[] TENS = {"twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
	public static final String[] TEENS = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
	public static final String[] ZERO = {"zero", "oh"};
	
	static{		
		NBA_TEAMS.put("Magic", "Orlando Magic");
		NBA_TEAMS.put("Wizards", "Washington Wizards");
		NBA_TEAMS.put("76ers", "Philadelphia Sixers");
		NBA_TEAMS.put("Raptors", "Toronto Raptors");
		NBA_TEAMS.put("Bulls", "Chicago Bulls");
		NBA_TEAMS.put("Cavaliers", "Cleveland Cavaliers");
		NBA_TEAMS.put("Pelicans", "New Orleans Pelicans");
		NBA_TEAMS.put("Warriors", "Golden State Warriors");
		NBA_TEAMS.put("Nuggets", "Denver Nuggets");
		NBA_TEAMS.put("Timberwolves", "Minnesota Timberwolves");
		NBA_TEAMS.put("Thunder", "Oklahoma City Thunder");
		NBA_TEAMS.put("Blazers", "Portland Trail Blazers");
		NBA_TEAMS.put("Jazz", "Utah Jazz");
		NBA_TEAMS.put("Pistons", "Detroit Pistons");
		NBA_TEAMS.put("Pacers", "Indiana Pacers");
		NBA_TEAMS.put("Bucks", "Milwaukee Bucks");
		NBA_TEAMS.put("Bobcats", "Charlotte Bobcats");
		NBA_TEAMS.put("Suns", "Phoenix Suns");
		NBA_TEAMS.put("Lakers", "Los Angeles Lakers");
		NBA_TEAMS.put("Kings", "Sacramento Kings");
		NBA_TEAMS.put("Rockets", "Houston Rockets");
		NBA_TEAMS.put("Grizzlies", "Memphis Grizzlies");
		NBA_TEAMS.put("Mavericks", "Dallas Mavericks");
		NBA_TEAMS.put("Clippers", "Los Angeles Clippers");
		NBA_TEAMS.put("Heats", "Miami Heats");
		NBA_TEAMS.put("Spurs", "San Antonio Spurs");
		NBA_TEAMS.put("Hawks", "Atlanta Hawks");
		NBA_TEAMS.put("Celtics", "Boston Celtics");
		NBA_TEAMS.put("Nets", "Brooklyn Nets");
		NBA_TEAMS.put("Knicks", "New York Knicks");
		NBA_TEAMS.put("Sixers", "Philadelphia Sixers");	
	}
	
	static{
		NBA_CITYS.put("Cleveland", "Cleveland Cavaliers");
		NBA_CITYS.put("Detroit", "Detroit Pistons");
		NBA_CITYS.put("Philadelphia", "Philadelphia Sixers");
		NBA_CITYS.put("Toronto", "Toronto Raptors");
		NBA_CITYS.put("Chicago", "Chicago Bulls");
		NBA_CITYS.put("Phoenix", "Phoenix Suns");
		NBA_CITYS.put("Los Angeles", "Los Angeles Clippers");
		NBA_CITYS.put("Sacramento", "Sacramento Kings");
		NBA_CITYS.put("Portland", "Portland Trail Blazers");
		NBA_CITYS.put("Utah", "Utah Jazz");
		NBA_CITYS.put("Houston", "Houston Rockets");
		NBA_CITYS.put("Memphis", "Memphis Grizzlies");
		NBA_CITYS.put("Dallas", "Dallas Mavericks");
		NBA_CITYS.put("San Antonio", "San Antonio Spurs");
		NBA_CITYS.put("New Orleans", "New Orleans Pelicans");
		NBA_CITYS.put("Golden State", "Golden State Warriors");
		NBA_CITYS.put("OKC", "Oklahoma City Thunder");
		NBA_CITYS.put("POR", "Portland Trail Blazers");
		NBA_CITYS.put("Indiana", "Indiana Pacers");
		NBA_CITYS.put("Milwaukee", "Milwaukee Bucks");
		NBA_CITYS.put("Charlotte", "Charlotte Bobcats");
		NBA_CITYS.put("Miami", "Miami Heats");
		NBA_CITYS.put("Orlando", "Orlando Magic");
		NBA_CITYS.put("Washington", "Washington Wizards");
		NBA_CITYS.put("Denver", "Denver Nuggets");
		NBA_CITYS.put("Minnesota", "Minnesota Timberwolves");
		NBA_CITYS.put("Oklahoma", "Oklahoma City Thunder");
		NBA_CITYS.put("Atlanta", "Atlanta Hawks");
		NBA_CITYS.put("Boston", "Boston Celtics");
		NBA_CITYS.put("Brooklyn", "Brooklyn Nets");
		NBA_CITYS.put("New York", "New York Knicks");
		NBA_CITYS.put("Orleans", "New Orleans Pelicans");
		NBA_CITYS.put("State", "Golden State Warriors");
		NBA_CITYS.put("York", "New York Knicks");
		NBA_CITYS.put("Auburn", "Detroit Pistons");
		NBA_CITYS.put("Minneapolis", "Minnesota Timberwolves");
		NBA_CITYS.put("Salt Lake", "Utah Jazz");
	}

	// you can pass an input file name as an argument.
	public static void main(String[] args) throws IOException {
		String inputfile = "input";	// or change the file name here.
		if(args.length==1){
			inputfile = args[0];
		}
		HashMap<String, String> result = null;
		result = get_Score_Teams_Player_4each_txt(inputfile);
		for (int i = 1; i <= result.size(); i++) {
			System.out.println("Extraction of Paragraph " + i);
			System.out.println(result.get("" + i));
			System.out.println();
		}
				
	 }
	
	public static HashMap<String, String> get_Score_Teams_Player_4each_txt(String filename) throws IOException{
		HashMap<String, HashMap<String, String>> allResults = new HashMap<>();
		HashMap<String, HashMap<String, Integer>> allResults_player = new HashMap<>();
		HashMap<String, String> allResults_city = new HashMap<>();
		HashMap<String, String> finalResults = new HashMap<>();
		ReadingFile rf = new ReadingFile();
		rf.reading(filename);
		HashMap<String, String> sr = null;
		HashMap<String, Integer> pl = null;
		HashMap<String, Integer> pl_temp = null;
		int counter = 0;
		JetTest.initializeFromConfig(match_Teams_and_Scores);
		for (ArrayList<String> txt : rf.doc) {
			counter++;
			for (String line : txt) {
				sr = checkSocre(line);
				if(!sr.isEmpty()){
					allResults.put(""+counter, sr);
					break;
				}
			}
			if(!allResults.containsKey(""+counter)){
				HashMap<String, String> temp = new HashMap<>();
				allResults.put(""+counter, temp);
			}
		}
		counter = 0;
		JetTest.initializeFromConfig(match_Player_and_Points);
		for (ArrayList<String> txt : rf.doc) {
			counter++;
			pl = new HashMap<>();
			for (String line : txt) {
				pl_temp = checkPlayers(line);
				for (Entry<String, Integer> e : pl_temp.entrySet()) {
					if(pl.containsKey(e.getKey())){
						continue;
					}
					pl.put(e.getKey(), e.getValue());
				}
			}
			allResults_player.put(""+counter, pl);
		}
		
		counter = 0;
		for (ArrayList<String> txt : rf.doc) {
			counter++;
			String first = txt.get(0);
			String second = "";
			String[] temp = first.split("[ ,]");
			if(temp[0].length() < 3){
				first = temp[1].toLowerCase().trim();
				second = temp[2].toLowerCase().trim();
			}else{
				first = temp[0].toLowerCase().trim();
				second = temp[1].toLowerCase().trim();
			}
			String city = first.substring(0, 1).toUpperCase()
					+ first.substring(1);
			String city2 = "";
			if(second.length()>=3){
				city2 = second.substring(0, 1).toUpperCase()
						+ second.substring(1);
			}
			city2 = city + " " + city2;
			if(NBA_CITYS.containsKey(city)){
				allResults_city.put(""+counter, NBA_CITYS.get(city));
			}else if(NBA_CITYS.containsKey(city2)){
				allResults_city.put(""+counter, NBA_CITYS.get(city2));
			}else{
				allResults_city.put(""+counter, "");
			}
		}
		
		String output = null;
		for (int i = 1; i <= rf.doc.size(); i++) {
			output = "HOME COURT: ";
			output += allResults_city.get(""+i).equals("")?"???":allResults_city.get(""+i);
			output += "\nWIN TEAM: ";
			if(allResults.get(""+i).isEmpty()){
				output += "???	LOSE TEAM: ???	SCORE: ???";
			}else{
				output += allResults.get(""+i).get("WinTeam") + "	";
				output += "LOSE TEAM: " + allResults.get(""+i).get("LoseTeam") + "	";
				output += "SCORE: " + allResults.get(""+i).get("Score") + "\n";
			}
			if(allResults_player.get(""+i).isEmpty()){
				output += "PLAYER WITH HIGHEST POINTS: ??? SCORED ??? POINTS";
			}else{
				Entry<String, Integer> maxEntry = null;				
				for(Entry<String, Integer> entry : allResults_player.get(""+i).entrySet()){
					if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
				    {
				        maxEntry = entry;
				    }
				}
				output += "PLAYER WITH HEIGHEST POINTS: ";
				output += maxEntry.getKey() + " ";
				output += "SCORED " + maxEntry.getValue() + " POINTS";
			}
			finalResults.put(""+i, output);			
		}
		
		
		return finalResults;
		
	}
	
	
	public static HashMap<String, Integer> checkPlayers(String txt) throws IOException{
		HashMap<String, Integer> result = new HashMap<>();
		if(txt.length()<10){
			return result;
		}
		txt = txt.replaceAll("([A-Z])\\*", "$1.");
		txt = "<TEXT> " + txt + " </TEXT>";
//		System.out.println(txt);
		String player = "";
		int points = 0;
		Document doc = new Document(txt); 
//	    System.out.println(txt);
	    Control.processDocument (doc, null, false, 1); 
	    Vector v = doc.annotationsOfType("constit"); 
	    if(v==null){
	    	System.out.println("Not founded!");
	    	return result;
	    }
	    for (int i=0; i<v.size(); i++) { 
	      Annotation a = (Annotation) v.get(i);
	      if(a.get("cat").equals("pointevent")){
	    	  player = getWord(a.get("player").toString(), txt).trim();
	    	  if(!checkedName(player)){
	    		  System.out.println("skip this: " + player);
	    		  continue;
	    	  }
	    	  String temp = getWord(a.get("points").toString(), txt);
	    	  points = checkNum(temp);
	    	  if(checkValidation(player, points) && !result.containsKey(player)){
	    		  result.put(player, points);
	    	  }	    	  
	       }
	    }		
		return result;
	}
	
	public static boolean checkedName(String name){
		String subname = name.substring(0, 1);
  	  	if(!subname.matches("[A-Z]")){
  	  		return false;
  	  	}
  	  	if(NBA_TEAMS.containsKey(name)){
  	  		return false;
  	  	}
  	  	if(NBA_CITYS.containsKey(name)){
	  		return false;
	  	}
  	  	String[] names = name.split(" ");
  	  	for (int i = 0; i < names.length; i++) {
			if(NBA_TEAMS.containsKey(names[i])){
				return false;
			}
			if(NBA_CITYS.containsKey(names[i])){
		  		return false;
		  	}
		}		
		return true;
	}
	
	public static Integer checkNum(String num){
		String[] num1 = num.split(" ");
		if(num1.length>1){
			return 10;
		}
		num = num1[0];
		if(num.matches("[a-zA-Z]+")){
			for (int i = 0; i < DIGITS.length; i++) {
				if(DIGITS[i].equals(num)){
					return i+1;
				}
			}
			for (int i = 0; i < TENS.length; i++) {
				if(TENS[i].equals(num)){
					return (i+2)*10;
				}
			}
			for (int i = 0; i < TEENS.length; i++) {
				if(TEENS[i].equals(num)){
					return 10+i;
				}
			}
		}else{
			return Integer.parseInt(num);
		}
		return 0;
	}
	
	
	public static boolean checkValidation( 
			String player, int scores){
		if(scores>59){
			return false;
		}
		if(NBA_TEAMS.containsKey(player)){
			return false;
		}
		if(NBA_CITYS.containsKey(player)){
			return false;
		}
		return true;
	}
	
	// check team-A win team-AA with score-SS
	public static HashMap<String, String> checkSocre(String txt) throws IOException{
		HashMap<String, String> result = new HashMap<>();
		if(txt.length()<10){
			return result;
		}
		// Rookie P*J*  ->  Rookie P.J.
		txt = txt.replaceAll("([A-Z])\\*", "$1.");
		txt = "<TEXT> " + txt + " </TEXT>";
		String winteam = "";
		String loseteam = "";
		String score = ""; 
	    Document doc = new Document(txt); 
//	    System.out.println(txt);
	    Control.processDocument (doc, null, false, 1); 
	    Vector v = doc.annotationsOfType("constit"); 
	    if(v==null){
	    	System.out.println("Not founded!");
	    	return result;
	    }
	    System.out.println("Founded!");
	    for (int i=0; i<v.size(); i++) { 
	      Annotation a = (Annotation) v.get(i);
	      if(a.get("cat").equals("sportevent")){
	    	  winteam = getWord(a.get("winteam").toString(), txt);
	   	   	  loseteam = getWord(a.get("loseteam").toString(), txt);
	      }
	    }
	    // find out the score if exists.
	    Pattern pattern = Pattern.compile("([0-9]{2,3}-[0-9]{2,3})");
		Matcher matcher = pattern.matcher(txt);
		if (matcher.find())
		{
		    System.out.println(matcher.group(1));
		    score = matcher.group(1);
		}
	    if(!winteam.isEmpty() && !loseteam.isEmpty() && !score.isEmpty()){
	    	result.put("WinTeam", winteam);
	    	result.put("LoseTeam", loseteam);
	    	result.put("Score", score);
	    }
	    
		return result;
	}
	

	
	// [ 78 - 163 ]
	public static String getWord(String scope, String txt){
		if(scope.isEmpty()){
			return null;
		}
		String st = "";
		String ed = "";
		Pattern pa = Pattern.compile("([0-9]*) - ([0-9]*)");
		Matcher ma = pa.matcher(scope);
		if(ma.find()){
			st = ma.group(1);
			ed = ma.group(2);
		}
		
		int start = Integer.parseInt(st.trim());
		int end = Integer.parseInt(ed.trim());
		
		return txt.substring(start, end).trim();
	}
	
}
