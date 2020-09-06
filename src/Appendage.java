import javalib.impworld.WorldScene;
import javalib.worldimages.*;


public abstract class Appendage {
	Appendage left;
	Appendage right;
	Appendage next;
	
	Appendage(String genome){
		
		build(genome);
	}
	
	//constructor for testing
	Appendage(Appendage left, Appendage right, Appendage next){
		this.left = left;
		this.right = right;
		this.next = next;
	}
	
	//constructor to create an empty appendage
	Appendage(){
		
	}
	
	void build(String genome) {
		//get the character for each appendage
		char left = Genome.getLetterFollowing(genome, 'L', 'E');
		char right = Genome.getLetterFollowing(genome, 'R', 'E');
		char next = Genome.getLetterFollowing(genome, 'N', 'E');
		
		this.left = getAppendage(left, "");
		this.right = getAppendage(right, "");
		
		//truncate the genome and send it to next
		this.next = getAppendage(next, Genome.truncateAtLast(genome, 'L', 'R', 'N'));
	}
	
	//gets the given Appendage depending on the character given
	Appendage getAppendage(char character, String genome) {
		switch(character) {
			case 'S':
				return new Stomach(genome);
			case 'W':
				return new Leg(genome);
			case 'E':
				return new EmptyAppendage();
			default:
				return new EmptyAppendage();
		}
	}
	
	//gets the appendage's nutrients locally required
	abstract int getLocalNutrientsRequired();
	
	//gets the speed of the local appendage
	abstract int getLocalSpeed();
	
	//gets the nutrients contained locally within the appendage
	abstract int getLocalNutrients();
	
	//draw the component
	abstract void draw(WorldScene scene, Posn posn);
	
	abstract int getLocalNutrientCapacity();
	
	//gets the appendage's contribution to movement
	int getSpeed() {
		return this.getLocalSpeed() + left.getSpeed() + right.getSpeed() + next.getSpeed();
	}
	
	//gets the appendage's nutrients requirement recursively
	int getNutrientsRequired() {
		return this.getLocalNutrientsRequired() + right.getNutrientsRequired() + left.getNutrientsRequired() + next.getNutrientsRequired();
	}
	
	//gets the appendage's current amount of nutrients stored
	int getNutrients() {
		return this.getLocalNutrients() + right.getNutrients() + left.getNutrients() + next.getNutrients();
	}
	
	int getNutrientCapacity() {
		return this.getLocalNutrientCapacity() + right.getNutrientCapacity() + left.getNutrientCapacity() + next.getNutrientCapacity();
	}
	
	//distributes the nutrients among the appendages sub appendages
	void feed(int nutrients) {
		int leftNeeded = left.getNutrientsRequired();
		int rightNeeded = right.getNutrientsRequired();
		int nextNeeded = next.getNutrientsRequired();
		
		//calculate remainder after what is needed is give
		int remainder = nutrients - leftNeeded - rightNeeded - nextNeeded;
		
		//feed the appendages what they need along with a third of the remainder
		left.feed(leftNeeded + remainder / 3);
		right.feed(rightNeeded + remainder / 3);
		next.feed(nextNeeded + remainder / 3);
	}
	
}
