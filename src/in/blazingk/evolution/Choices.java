package in.blazingk.evolution;

public enum Choices {
	A("A"), B("B"), notPrev("notPrev"), prev("prev"), notOppPrev("notOppPrev"), oppPrev("oppPrev"), trust("trust");
	
	public final String name;
	Choices(String s){
		name = s;
	}
}
