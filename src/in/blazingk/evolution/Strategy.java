package in.blazingk.evolution;


import java.util.ArrayList;

public class Strategy {
	int speciesNumber = 0;
	int deviations = 0;
	public ArrayList<Choices> choiceList = new ArrayList<Choices>();
	public Strategy(int length){
		for (int i = 0; i < length; i++){
			choiceList.add(Choices.values()[Evolution.rand.nextInt(Choices.values().length)]);
		}
	}
	public Strategy(int length, int species){
		this(length);
		speciesNumber = species;
	}
	
	public Strategy(Strategy parent){
		speciesNumber = parent.speciesNumber;
		deviations = parent.deviations;
		for (int i = 0; i < parent.choiceList.size(); i++){
			if ((((double)Evolution.rand.nextInt(10000))/10000) < Evolution.evolutionDeviancy){
				Choices c = Choices.values()[Evolution.rand.nextInt(Choices.values().length)];
				if (c != parent.choiceList.get(i)){
					deviations++;
				}
				choiceList.add(c);
			}else{
				choiceList.add(parent.choiceList.get(i));
			}
		}
	}

}
