import java.awt.Color;
import javalib.impworld.*;
import javalib.worldimages.*;

public class Leg extends Appendage {
	static final int LEG_NUTRIENTS_REQUIRED = 15;
	static final int LEG_SPEED = 20;
	
	Leg(String genome){
		super(genome);
		
		this.left = new EmptyAppendage();
		this.right = new EmptyAppendage();
		this.next = new EmptyAppendage();
	}
	
	int getLocalNutrientsRequired() {
		return LEG_NUTRIENTS_REQUIRED;
	}

	int getLocalSpeed() {
		return LEG_SPEED;
	}

	int getLocalNutrients() {
		return 0;
	}
	
	int getLocalNutrientCapacity() {
		return 0;
	}

	void draw(WorldScene scene, Posn posn) {
		scene.placeImageXY(new EllipseImage(17, 7, "SOLID", Color.RED), posn.x, posn.y);
	}

}
