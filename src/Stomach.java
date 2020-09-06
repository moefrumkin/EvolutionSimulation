import java.awt.Color;

import javalib.impworld.WorldScene;
import javalib.worldimages.*;

public class Stomach extends Appendage {
	static final int STOMACH_NUTRIENTS_REQUIRED = 10;
	static final int STOMACH_CAPACITY = 75;
	
	int nutrients;

	public Stomach(String genome) {
		super(genome);
		
		nutrients = 0;
	}

	int getLocalNutrientsRequired() {
		return STOMACH_NUTRIENTS_REQUIRED;
	}

	int getLocalSpeed() {
		return 0;
	}

	int getLocalNutrients() {
		return nutrients;
	}
	
	int getLocalNutrientCapacity() {
		return STOMACH_CAPACITY;
	}
	
	@Override
	void feed(int nutrients) {
		int leftNeeded = left.getNutrientsRequired();
		int rightNeeded = right.getNutrientsRequired();
		int nextNeeded = next.getNutrientsRequired();
		
		//calculate remainder after what is needed is give
		int remainder = nutrients - leftNeeded - rightNeeded - nextNeeded;
		
		//store as much of the remainder as possible
		int toStore = Math.min(remainder, STOMACH_CAPACITY);
		
		remainder = remainder - toStore;
		this.nutrients = toStore;
		
		//feed the appendages what they need along with a third of the remainder
		left.feed(leftNeeded + remainder / 3);
		right.feed(rightNeeded + remainder / 3);
		next.feed(nextNeeded + remainder / 3);
	}

	void draw(WorldScene scene, Posn posn) {
		//draw appendages
		left.draw(scene, new Posn(posn.x - 5, posn.y));
		right.draw(scene, new Posn(posn.x +5, posn.y));
		next.draw(scene,  new Posn(posn.x, posn.y + 17));
		//draw self
		scene.placeImageXY(new EllipseImage(7, 17, "SOLID", Color.PINK), posn.x, posn.y);
	}
}
