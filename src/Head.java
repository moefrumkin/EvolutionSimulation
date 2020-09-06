import javalib.worldimages.*;
import javalib.impworld.*;
import java.awt.Color;

public class Head {
	static final int BASE_NUTRIENTS_REQUIRED = 10;
	static final int BASE_VISION = 1;
	static final int VISION_NUTRIENTS_MULTIPLIER = 2;
	
	Organism organism;
	Appendage appendage;
	//relative vision of organism from 0-9
	int vision;
	
	Head(Organism organism, String genome){
		this.organism = organism;
		
		build(genome);
	}
	
	//convenience constructor for testing
	Head(Appendage appendage, int vision){
		this.appendage = appendage;
		this.vision = vision;
	}
	
	//adds the appendages following the genome
	void build(String genome) {
		//set vision level
		this.vision = Genome.getValueFollowing(genome, 'V', 1);
		
		//get the character for the appendage
		char appendage = Genome.getLetterFollowing(genome, 'N', 'E');
		
		//split the genome string
		String[] splitGenome = genome.split(String.valueOf(appendage), 2);
		
		String appendageGenome;
		if(splitGenome.length == 1) {
			appendageGenome = "E";
		} else {
			appendageGenome = splitGenome[1];
		}
		
		switch(appendage) {
			case 'S':
				this.appendage = new Stomach(appendageGenome);
			break;
			case 'W':
				this.appendage = new Leg(appendageGenome);
			case 'E':
				this.appendage = new EmptyAppendage();
			break;
			default:
				this.appendage = new EmptyAppendage();
		}
	}
	
	void setAppendage(Appendage appendage) {
		this.appendage = appendage;
	}
	
	int getSpeed() {
		return appendage.getSpeed();
	}
	
	int getNutrients() {
		return appendage.getNutrients();
	}
	
	int getVision() {
		return vision * BASE_VISION;
	}
	
	int getLocalNutrientsRequired() {
		return BASE_NUTRIENTS_REQUIRED + vision * VISION_NUTRIENTS_MULTIPLIER;
	}
	
	int getNutrientsRequired() {
		return getLocalNutrientsRequired() + appendage.getNutrientsRequired();
	}
	
	int getNutrientCapacity() {
		return appendage.getNutrientCapacity();
	}
	
	//consumes the given food as needed by the organs and leaves extra in stomachs
	void feed(int nutrients) {
		nutrients -= getLocalNutrientsRequired();
		appendage.feed(nutrients);
	}
	
	void draw(WorldScene scene, Posn posn) {
		//draw children
		appendage.draw(scene, new Posn(posn.x, posn.y + 10));
		//draw itself
		scene.placeImageXY(new CircleImage(5, "SOLID", Color.YELLOW), posn.x, posn.y);
	}
}
