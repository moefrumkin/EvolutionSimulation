import java.util.ArrayList;

public class PhylogenicTree {
	Organism organism;
	ArrayList<Organism> children;
	
	PhylogenicTree(Organism organism){
		this.organism = organism;
		
		children = new ArrayList<>();
	}
	
	void addChild(Organism child) {
		children.add(child);
	}
}
