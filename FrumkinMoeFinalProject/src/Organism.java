import java.awt.Color;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javalib.impworld.*;
import javalib.worldimages.*;

public class Organism {
	static final int BaseNutrientsGiven = 25;
	//Minimum number of ticks between children
	static final int ChildTickRefresh = 10;
	
	String genome;
	Posn position;
	boolean alive;
	int ticksSinceLastChild;
	int children;
	int age;
	
	Head head;
	
	Organism(String genome, Posn position, int nutrients){
		this.genome = genome;
		this.position = position;
		this.alive = true;
		ticksSinceLastChild = 0;
		children = 0;
		age = 0;
		
		hatch();
		
		//check that it has enough nutrients
		if(nutrients < getNutrientsRequired()) {
			die();
		} else {
			head.feed(nutrients);
		}
	}
	
	//convenience constructor for testing
	Organism(Head head, Posn position, int nutrients){
		this.head = head;
		this.position = position;
		this.alive = true;
		ticksSinceLastChild = 0;
		children = 0;
		age = 0;
	}
	
	//generate the body using the genome
	void hatch() {
		this.head = new Head(this, genome);
	}
	
	//calculates the organism's move speed
	int getSpeed() {
		return head.getSpeed();
	}
	
	//calculates the amount of nutrients that the organism has
	int getNutrients() {
		return head.getNutrients();
	}
	
	//calculates the organisms vision distance
	int getVision() {
		return head.getVision();
	}
	
	//calculate the amount of nutrients that the organism can contain
	int getNutrientCapacity() {
		return head.getNutrientCapacity();
	}
	
	//calculate the number of nutrients an organism needs
	int getNutrientsRequired() {
		return head.getNutrientsRequired();
	}
	
	//moves organism in given direction
	void move(Posn target) {
		int distance = (int) Math.sqrt(Math.pow((target.x - position.x), 2) + Math.pow((target.y - position.y), 2));
		int speed = getSpeed();
		if(distance < speed) {
			this.position = target;
		} else {
			double direction = Math.atan2(target.x, target.y);
			int newX = distance * (int) Math.cos(direction);
			int newY = distance * (int) Math.sin(direction);
			this.position = new Posn(newX, newY);
		}
	}
	
	boolean alive() {
		return alive;
	}
	
	void die() {
		alive = false;
	}
	
	//updates the organism each tick
	void update(Map<Posn, Patch> ground, Simulation simulation) {
		//increment ticks since last child counter and age counter
		age++;
		ticksSinceLastChild++;
		//get the patch with the most food
		Optional<Entry<Posn, Patch>> targetEntry = ground.entrySet().stream().max((entry1, entry2) -> entry1.getValue().getGrass() - entry2.getValue().getGrass());
		
		//if the patch exists set its center as the target
		if(targetEntry.isPresent()) {
			int targetX = Patch.Width * (targetEntry.get().getKey().x + 1/2);
			int targetY = Patch.Width * (targetEntry.get().getKey().y + 1/2);
			move(new Posn(targetX, targetY));
		}
		
		//get the current patch the organism is located at. This may not be the target, because of move limitations
		Optional<Patch> currentPatchOptional = simulation.patchAt(this.position);
		
		//feed the organism if the patch exists
		if(currentPatchOptional.isPresent()) {
			Patch currentPatch = currentPatchOptional.get();
			
			int nutrientsHad = getNutrients();
			int nutrientCapacity = getNutrientCapacity();
			int toEat = Math.min(nutrientCapacity - nutrientsHad, currentPatch.getGrass());			
			
			//remove eaten nutrients
			currentPatch.subractGrass(toEat);
			nutrientsHad += toEat;
			
			//check if enough nutrients were eaten
			if(nutrientsHad < getNutrientsRequired()) {
				die();
			} else {
				//check if there are enough nutrients for a child and if it is allowed
				int toChild = BaseNutrientsGiven * Genome.getValueFollowing(genome, 'F', 1);
				if(nutrientsHad > toChild + getNutrientsRequired() && ticksSinceLastChild > ChildTickRefresh) {
					String childGenome = Genome.mutate(genome);
					nutrientsHad -=  toChild;
					simulation.addOrganism(new Organism(childGenome, new Posn(position.x + 50, position.y + 50), toChild));
					//increment children counter
					children++;
					//reset ticks since last child counter
					ticksSinceLastChild = 0;
				}
				head.feed(nutrientsHad);
			}
			
		} else {
			//if patch cannot be found, die
			die();
		}
		
	}
	
	//draws organism
	void draw(WorldScene scene) {
		if(alive) {
			head.draw(scene, position);
		} else {
			scene.placeImageXY(new TextImage("X", 25,  Color.BLACK), position.x, position.y);
		}
	}
	
	@Override
	public String toString(){
		return "Position: (" + position.x + ", " + position.y + "), Genome: " + genome + ", Children: " + children + ", Age: " + age + ", Alive: " + alive;
	}
}
