import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javalib.impworld.WorldScene;
import javalib.worldimages.*;

//class to represent a patch of dirt
public class Patch {
	static final int CarryingCapacity = 256;
	static final float GrowthRate = 0.1f;
	static final int Width = 20; 
	static final int StartLevel = 50;
	static final int BaseLevel = 20;
	
	Posn position;
	int grass;
	Map<Posn, Patch> neighbors;

	Patch(){
		grass = StartLevel;
		neighbors = new HashMap<>();
	}
	
	Patch(Posn position){
		this();
		this.position = position;
	}
	
	//increased the amount of grass on the patch as well as in neighbor patches
	void grow() {
		//logistically calculate the amount of new grass
		int newGrass = (int)(GrowthRate * grass) * (CarryingCapacity - grass) / CarryingCapacity;
		grass = Math.max(grass + newGrass, BaseLevel);
	}
	
	//returns the amount of grass on the patch
	int getGrass() {
		return grass;
	}
	
	//increase the amount of grass by the given amount
	void addGrass(int amount) {
		grass = grass + amount;
	}
	
	//decrease the amount of grass by the given amount
	void subractGrass(int amount) {
		addGrass(-amount);
	}
	
	//connect the cell to its neighbors in the map
	void connectNeighbors(Posn posn, Map<Posn, Patch> patches) {
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				Posn neighborPosn = new Posn(posn.x + i, posn.y + j);
				Posn neighborDelta = new Posn(i, j);
				Optional.ofNullable(patches.get(neighborPosn)).ifPresent(patch -> neighbors.put(neighborDelta, patch));
			}
		}
	}
	
	void draw(WorldScene scene, Posn posn){
		scene.placeImageXY(new RectangleImage(Width, Width, "SOLID", Color.BLACK ), posn.x + Width/2, posn.y + Width/2);
		scene.placeImageXY(new RectangleImage(Width - 2, Width - 2, "SOLID", new Color(0, grass < 255 && grass >= 0 ? grass : 255, 0)), posn.x + Width/2 - 1, posn.y + Width/2 - 1);
	}
	
	@Override
	public String toString() {
		return "Patch: " + position;
	}
}
