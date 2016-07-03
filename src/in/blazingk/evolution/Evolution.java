package in.blazingk.evolution;

import java.util.ArrayList;
import java.util.Random;


public class Evolution {
	public static Random rand;
	public static double evolutionDeviancy = .001;
	public static int battleLength = 10;
	public static int creatures = 500;
	public static int rounds = 100;
	public static double percentTaken = .7;
	public static int cooperatePoints = 3;
	public static int betrayPoints = 5;
	public static int confessPoints = 1;
	
	public static void main(String[] args) {
		rand = new Random(System.currentTimeMillis());
		ArrayList<Strategy> strts = new ArrayList<Strategy>();
		for (int c = 0; c < creatures; c++){
			strts.add(new Strategy(battleLength, c));
		}
		for (int r = 1; r < rounds+1; r++){
			System.out.println("\nStarting round: "+r);
			int[] scores = new int[creatures];
			for (int c = 0; c < creatures; c++){
				for (int b = 0; b < creatures; b++){
					if (b == c){
						continue;
					}
					scores[c] += contestStrategies(strts.get(c), strts.get(b))[0];
				}
			}
			ArrayList<Strategy> cln = new ArrayList<Strategy>();
			ArrayList<Integer> scrs = new ArrayList<Integer>();
			double total = 0d;
			for (int a = 0; a < creatures; a++){
				boolean flag = false;
				for (int b = 0; b < cln.size(); b++){
					if (scrs.get(b) < scores[a]){
						flag = true;
						cln.add(b, strts.get(a));
						scrs.add(b, scores[a]);
						break;
					}
				}
				if (!flag){
					cln.add(strts.get(a));
					scrs.add(scores[a]);
				}
				total += scores[a];
			}
			System.out.println("Highest Score: "+scrs.get(0)+" ("+cln.get(0).speciesNumber+")");
			double avg = total / creatures;
			System.out.println("Average Score: "+avg);
			System.out.println("Median Score: "+scrs.get((int) Math.floor(scrs.size()/2)));
			System.out.println("Lowest Score: "+scrs.get(scrs.size()-1)+" ("+cln.get(scrs.size()-1).speciesNumber+")");
			strts.clear();
			int[] spcs = new int[creatures];
			for (int i = 0; i < creatures; i++){
				spcs[cln.get(i).speciesNumber] += 1;
			}
			int highestNumber = 0;
			int highestIndex = 0;
			for (int i = 0; i < creatures; i++){
				if (spcs[i] > highestNumber){
					highestNumber = spcs[i];
					highestIndex = i;
				}
			}
			int ttl = 0;
			for (int i = 0; i < creatures; i++){
				ttl += spcs[i]!=0?1:0;
			}
			System.out.println("Most abundant species is "+highestIndex+" with "+highestNumber+" creatures ("+ttl+" species total)");
			if (cln.get(0).speciesNumber != highestIndex){
				System.out.print("Top creature made choices: [");
				for (int i = 0; i < cln.get(0).choiceList.size(); i++){
					System.out.print(cln.get(0).choiceList.get(i).name);
					if (i+1 != cln.get(0).choiceList.size()){
						System.out.print(", ");
					}
				}
				System.out.println("]");
				}
			for (int i = 0; i < creatures; i++){
				if (cln.get(i).speciesNumber == highestIndex){
					System.out.print("Top of species "+ highestIndex+" had choices: [");
					for (int y = 0; y < cln.get(i).choiceList.size(); y++){
						System.out.print(cln.get(i).choiceList.get(y).name);
						if (y+1 != cln.get(i).choiceList.size()){
							System.out.print(", ");
						}
					}
					System.out.println("] with "+cln.get(i).deviations+" deviations");
					break;
				}
			}
			
			for (int c = 0; c < creatures; c++){
				strts.add(new Strategy(cln.get((int)Math.floor(c * percentTaken))));
			}
		}
		
		
	}
	
	public static int[] contestStrategies(Strategy a, Strategy b){
		int sumA = 0;
		int sumB = 0;
		ArrayList<Boolean> choicesMadeByA = new ArrayList<Boolean>();
		ArrayList<Boolean> choicesMadeByB = new ArrayList<Boolean>(); 
		for (int i = 0; i < a.choiceList.size(); i++){
			boolean aChoice = false; // false is A, true is B
			switch (a.choiceList.get(i)){
				case A:
				break;
				case B:
				aChoice = true;
				break;
				case notOppPrev:
				if (i > 0){
					aChoice = !(choicesMadeByB.get(i - 1));
				}
				break;
				
				case notPrev:
				if (i > 0){
					aChoice = !(choicesMadeByA.get(i - 1));
				}
				break;
				
				case prev:
					if (i > 0){
						aChoice = (choicesMadeByA.get(i - 1));
					}
				break;
				
				case oppPrev:
					if (i > 0){
						aChoice = (choicesMadeByB.get(i - 1));
					}
				break;
				case trust:
					aChoice = choicesMadeByB.contains(true);
				break;
			}
			
			boolean bChoice = false; // false is A, true is B
			switch (b.choiceList.get(i)){
				case A:
				break;
				case B:
				bChoice = true;
				break;
				case notOppPrev:
				if (i > 0){
					bChoice = !(choicesMadeByA.get(i - 1));
				}
				break;
				
				
				case notPrev:
				if (i > 0){
					bChoice = !(choicesMadeByB.get(i - 1));
				}
				break;
				
				
				case prev:
					if (i > 0){
						bChoice = (choicesMadeByB.get(i - 1));
					}
				break;
				
				case oppPrev:
					if (i > 0){
						bChoice = (choicesMadeByA.get(i - 1));
					}
				break;
				case trust:
					bChoice = choicesMadeByA.contains(true);
					break;
			}
			choicesMadeByA.add(aChoice);
			choicesMadeByB.add(bChoice);
			
			if (aChoice && bChoice){
				sumA += confessPoints;
				sumB += confessPoints;
			}else if((!aChoice) && (!bChoice)){
				sumA += cooperatePoints;
				sumB += cooperatePoints;
			}else if ((!aChoice) && bChoice){
				sumB += betrayPoints;
			}else{
				sumA += betrayPoints;
			}
			
		}
		return new int[] {sumA, sumB};
	}
	

}
