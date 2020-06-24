import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.awt.Color;

import javalib.impworld.*;
import javalib.worldimages.CircleImage;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;

public class Simulation extends World{
	static final int InitialFood = 300;
	static final int PanelHeight = 150;
	
	int width;
	int height;
	Map<Posn, Patch> ground;
	List<Organism> organisms;
	List<Organism> toAdd;
	boolean paused;
	long tick;
	
	public static void main(String[] args) {
		//start a simulation
		Simulation test = new Simulation("V2P2F3NSRWLWNSRELENSLWRWNS", 80, 40);
		test.start();
	}
	
	Simulation(String genome, int width, int height) {
		this.width = width;
		this.height = height;
		
		//create the ground
		ground = new HashMap<>(width * height);
		
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				ground.put(new Posn(i, j), new Patch(new Posn(i, j)));
			}
		}
		//start paused
		paused = true;
		tick = 0;
		
		//connect the ground
		ground.forEach((posn, patch) -> patch.connectNeighbors(posn, ground));
		
		//initialize organism list
		organisms = new ArrayList<>();
		
		//initialize organism to Add list
		toAdd = new ArrayList<>();
		
		//add parent to list
		organisms.add(new Organism(genome, new Posn(210, 210), InitialFood));
	}
	
	void start() {
		bigBang(width * Patch.Width, height * Patch.Width + PanelHeight, 0.1);
	}

	public WorldScene makeScene() {
		//update if not paused
		if(!paused) {
			update();
		}
		//get the scene
		WorldScene scene = getEmptyScene();
		//draw grass
		ground.forEach((posn, patch) -> patch.draw(scene, new Posn(posn.x * Patch.Width, posn.y * Patch.Width)));
		//draw organisms
		organisms.forEach(organism -> organism.draw(scene));
		//draw 'eggs' for each of the new organisms
		toAdd.forEach(organism -> scene.placeImageXY(new CircleImage(5, "SOLID", Color.WHITE), organism.position.x, organism.position.y));
		
		//draw stats
		Color panelColor = paused ? Color.RED: Color.BLUE;
		scene.placeImageXY(new RectangleImage(width * Patch.Width, 200, "SOLID", panelColor), width * Patch.Width/2, height * Patch.Width + 100);
		scene.placeImageXY(new TextImage("Organisms: " + organismsAlive() + " New Organisms: " + toAdd.size(), 30, Color.BLACK), width * Patch.Width/2, height * Patch.Width + 50);
		scene.placeImageXY(new TextImage("Press P to toggle pause, U to update, S to print out statistics", 20, Color.BLACK), width * Patch.Width/2, height * Patch.Width + 80);
		if(paused) {
			scene.placeImageXY(new TextImage("(Paused)", 20, Color.BLACK), width * Patch.Width/2, height * Patch.Width + 100);
		}
		
		return scene;
	}
	
	//key handler
	public void onKeyEvent(String key) {
		switch(key) {
			case "p":
				paused = ! paused;
			break;
			case "u":
				if(paused) {
					update();
				}
			break;
			case "s":
				System.out.println("\n\nSimulation Statistics, tick: " + tick);
				System.out.println("\nOrganisms: " + organisms.size() + ", Living Organisms: " + organismsAlive() + ", New Organisms: " + toAdd.size());
				
				Optional<Organism> mostFit = organisms.stream().collect(Collectors.maxBy((org1, org2) -> (org1.children - org2.children)));
				if(mostFit.isPresent()) {
					System.out.println("\nMost Fit Organism: " + mostFit.get());
				}
				
				Optional<Organism> oldestLivingOrganism = organisms.stream().filter(Organism::alive).collect(Collectors.maxBy((org1, org2) -> (org1.age - org2.age)));
				if(oldestLivingOrganism.isPresent()) {
					System.out.println("\nOldest Living Organism: " + oldestLivingOrganism.get());
				}
				
				System.out.println("\nLiving Organisms:");
				//print all living organisms
				organisms.stream().filter(Organism::alive).forEach(organism -> System.out.println(organism));
			break;
		}
	}
	
	//returns the number of living organisms
	long organismsAlive() {
		return organisms.stream().filter(Organism::alive).collect(Collectors.counting());
	}
	
	//update the grass and the organisms
	void update() {
		//increment the tick counter
		tick++;
		//add all new organisms to the organism list and clear the list
		organisms.addAll(toAdd);
		toAdd.clear();
		//grow grass
		ground.forEach((posn, patch) -> patch.grow());
		//update organisms
		for(Organism organism: organisms) {
			if(organism.alive) {
				Map<Posn, Patch> view = getWithin(ground, organism.getVision(), new Posn(patchCoord(organism.position.x), patchCoord(organism.position.y)));
				organism.update(view, this);
			}
		}
	}
	
	//return a map of all patches within the given range
	public static Map<Posn, Patch> getWithin(Map<Posn, Patch> map, int range, Posn posn){
		return map.
		entrySet().
		stream().
		filter(entry -> (Math.pow(entry.getKey().x - posn.x,2) + Math.pow(entry.getKey().y - posn.y, 2)) < Math.pow(range, 2))
		.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
	
	//returns the patch on a given position
	public Optional<Patch> patchAt(Posn posn) {
		return Optional.ofNullable(ground.get(new Posn(patchCoord(posn.x), patchCoord(posn.y))));
	}
	
	//returns the coordinate scaled down to patch coordinate
	public int patchCoord(int organismCoord) {
		return organismCoord/Patch.Width;
	}
	
	//adds new organisms to a temporary array for prevent mutating an array in organisms.forEach()
	public void addOrganism(Organism organism) {
		toAdd.add(organism);
	}

}
