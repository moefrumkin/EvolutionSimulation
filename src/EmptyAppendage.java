import javalib.impworld.WorldScene;
import javalib.worldimages.Posn;

public class EmptyAppendage extends Appendage{

	EmptyAppendage() {
		super();
	}

	int getLocalNutrientsRequired() {
		return 0;
	}

	int getLocalSpeed() {
		return 0;
	}

	int getLocalNutrients() {
		return 0;
	}
	
	int getLocalNutrientCapacity() {
		return 0;
	}
	
	@Override
	public int getSpeed() {
		return 0;
	}
	
	@Override
	public int getNutrientsRequired() {
		return 0;
	}
	
	@Override
	public int getNutrients() {
		return 0;
	}
	
	@Override
	public int getNutrientCapacity() {
		return 0;
	}
	
	//distributes the nutrients amoung the appendages sub appendages
	public void feed(int nutrients) {
		return;
	}

	@Override
	void draw(WorldScene scene, Posn posn) {
		return;
	}

}
