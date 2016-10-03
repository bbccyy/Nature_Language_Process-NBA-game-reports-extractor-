import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;


public class Measure {

	public static void main(String[] args) {
		String filename = "Extraction Accuracy";
		if(args.length == 1){
			filename = args[0];
		}
		
		try {
			getResults(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void getResults(String filename) throws IOException{
		Path filepath;
		filepath = Paths.get(filename);
		Scanner sc = new Scanner(filepath);
		String line = null;
		int counter = 1;
		Integer[] home_court = new Integer[3];
		Integer[] scores = new Integer[3];
		Integer[] player = new Integer[3];
		Arrays.fill(home_court, 0);
		Arrays.fill(scores, 0);
		Arrays.fill(player, 0);
		double precision = 0.0;
		double recall = 0.0;
		while (sc.hasNext()) {
			line = sc.nextLine();
			if(line.contains("Extraction")){
				continue;
			}
			if(line.equals("")){ 
				continue;
			}
			if(counter == 1){
				counter++;
				String[] line1 = line.split(" ");
				if(line1[0].equals("1")){
					home_court[0] += 1;
				}else if(line1[0].equals("0")){
					home_court[1] += 1;
				}else if(line1[0].equals("2")){
					home_court[2] += 1;
				}
				continue;
			}
			if(counter == 2){
				counter++;
				String[] line2 = line.split(" ");
				for (int i = 0; i < line2.length; i++) {
					if(line2[i].equals("1")){
						scores[0] += 1;
					}else if(line2[i].equals("0")){
						scores[1] += 1;
					}else if(line2[i].equals("2")){
						scores[2] += 1;
					}
				}
				continue;
			}
			if(counter == 3){
				counter++;
				String[] line3 = line.split(" ");
				for (int i = 0; i < line3.length; i++) {
					if(line3[i].equals("1")){
						player[0] += 1;
					}else if(line3[i].equals("0")){
						player[1] += 1;
					}else if(line3[i].equals("2")){
						player[2] += 1;
					}
				}
				continue;
			}
			if(counter == 4){
				counter = 1;
				continue;
			}
		}
		precision = (double)home_court[0] / (double)(home_court[0] + home_court[1]);
		recall = (double)home_court[0] / (double)(home_court[0] + home_court[1] + home_court[2]);
		System.out.println("HOME COURT: ");
		System.out.println("Precision = " + precision);
		System.out.println("Recall = " + recall);
		System.out.println();
		precision = (double)scores[0] / (double)(scores[0] + scores[1]);
		recall = (double)scores[0] / (double)(scores[0] + scores[1] + scores[2]);
		System.out.println("WIN TEAM, LOSE TEAM and SCORES: ");
		System.out.println("Precision = " + precision);
		System.out.println("Recall = " + recall);
		System.out.println();
		precision = (double)player[0] / (double)(player[0] + player[1]);
		recall = (double)player[0] / (double)(player[0] + player[1] + player[2]);
		System.out.println("PLAYER WITH HEIGHEST POINTS: ");
		System.out.println("Precision = " + precision);
		System.out.println("Recall = " + recall);
		System.out.println();
		precision = (double)(player[0] + scores[0] + home_court[0]) / 
				(double)(player[0] + player[1] + home_court[0] + home_court[1] + scores[0] + scores[1]);
		recall = (double)(player[0] + scores[0] + home_court[0]) / 
				(double)(player[0] + player[1] + player[2] + 
						home_court[0] + home_court[1] + home_court[2] + 
						scores[0] + scores[1] + scores[2]);
		System.out.println("OVER ALL MEASURE: ");
		System.out.println("Precision = " + precision);
		System.out.println("Recall = " + recall);
		
		return;
	}

}
