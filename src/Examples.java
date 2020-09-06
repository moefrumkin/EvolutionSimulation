import java.util.HashMap;
import java.util.Map;

import javalib.worldimages.Posn;
import tester.Tester;

public class Examples {
	
	void testGetValueFollowing(Tester t) {
		t.checkExpect(Genome.getValueFollowing("", 'G', 5), 5);
		t.checkExpect(Genome.getValueFollowing("ABC", 'C', 2), 2);
		t.checkExpect(Genome.getValueFollowing("V9P4", 'V', 2), 9);
		t.checkExpect(Genome.getValueFollowing("V9P4", 'P', 0), 4);
	}
	
	void testGetLetterFollowing(Tester t) {
		t.checkExpect(Genome.getLetterFollowing("", 'A', 'E'), 'E');
		t.checkExpect(Genome.getLetterFollowing(" ", 'L', 'E'), 'E');
		t.checkExpect(Genome.getLetterFollowing("ABC", 'C', 'F'), 'F');
		t.checkExpect(Genome.getLetterFollowing("ABC", 'B', 'G'), 'C');
		t.checkExpect(Genome.getLetterFollowing("ABC", 'A', 'G'), 'B');
	}
	
	void testTruncateAtLast(Tester t) {
		t.checkExpect(Genome.truncateAtLast("ABCDEFG", 'H'), "ABCDEFG");
		t.checkExpect(Genome.truncateAtLast("ABCDEFG", 'E'), "FG");
		t.checkExpect(Genome.truncateAtLast("ABCDEFG", 'C', 'E'), "FG");
		t.checkExpect(Genome.truncateAtLast("ABCDEFG", 'A'), "BCDEFG");
		t.checkExpect(Genome.truncateAtLast("ABCDECFG", 'C'), "DECFG");
		t.checkExpect(Genome.truncateAtLast("ABCDECFG", 'C', 'H'), "DECFG");
	}
	
	void testGeneration(Tester t) {
		Organism Org1 = new Organism(new Head(new Stomach(""), 5), new Posn(50 , 50), 100);
		Org1.genome = "V5NS";
		Org1.head.organism = Org1;
		Organism Org2 = new Organism("V5NS", new Posn(50 , 50), 100);
		Organism Org3 = new Organism(new Head(new Stomach("NSLWRWNS"), 2), new Posn(50, 50), 80);
		Org3.genome = "V2NSNSLWRWNS";
		Org3.head.organism = Org3;
		Organism Org4 = new Organism("V2NSNSLWRWNS", new Posn(50, 50), 80);
		
		t.checkExpect(Org2, Org1);
		t.checkExpect(Org4, Org3);
	}
	
	void testOrganismMethods(Tester t) {
		Organism Org = new Organism("V2NSNSLWRWNS", new Posn(50, 50), 20);
		
		t.checkExpect(Org.getNutrientCapacity(), Stomach.STOMACH_CAPACITY * 3);
		t.checkExpect(Org.getSpeed(), Leg.LEG_SPEED * 2);
		t.checkExpect(Org.getVision(), 2);
		t.checkExpect(Org.getNutrientsRequired(), Stomach.STOMACH_NUTRIENTS_REQUIRED * 3 + Leg.LEG_NUTRIENTS_REQUIRED *2 + Head.BASE_NUTRIENTS_REQUIRED + Head.VISION_NUTRIENTS_MULTIPLIER * 2);
	}
	
	void testGetWithin(Tester t) {
		Map<Posn, Patch> ground = new HashMap<>();
		ground.put(new Posn(0,0), new Patch());
		ground.put(new Posn(1,0), new Patch());
		ground.put(new Posn(2,0), new Patch());
		ground.put(new Posn(3,0), new Patch());
		ground.put(new Posn(1,1), new Patch());
		ground.put(new Posn(2,2), new Patch());
		
		Map<Posn, Patch> radiusOne = new HashMap<>();
		radiusOne.put(new Posn(0,0), new Patch());
		
		Map<Posn, Patch> radiusTwo = new HashMap<>();
		radiusTwo.put(new Posn(0,0), new Patch());
		radiusTwo.put(new Posn(1, 0), new Patch());
		radiusTwo.put(new Posn(1, 1), new Patch());
		
		Map<Posn, Patch> radiusThree = new HashMap<>();
		radiusThree.put(new Posn(0, 0), new Patch());
		radiusThree.put(new Posn(1, 0), new Patch());
		radiusThree.put(new Posn(2, 0), new Patch());
		radiusThree.put(new Posn(1, 1), new Patch());
		radiusThree.put(new Posn(2, 2), new Patch());
		
		t.checkExpect(Simulation.getWithin(ground, 0, new Posn(0, 0)), new HashMap<Posn, Patch>());
		t.checkExpect(Simulation.getWithin(ground, 1, new Posn(0, 0)), radiusOne);
		t.checkExpect(Simulation.getWithin(ground, 2, new Posn(0, 0)), radiusTwo);
		t.checkExpect(Simulation.getWithin(ground, 3, new Posn(0, 0)), radiusThree);
	}
	
	void testPatchAt(Tester t) {
		Simulation testSimulation = new Simulation("", 40, 40);
		
		t.checkExpect(testSimulation.patchAt(new Posn(0, 0)).get().position, new Posn(0, 0));
		t.checkExpect(testSimulation.patchAt(new Posn(30, 0)).get().position, new Posn(1, 0));
		t.checkExpect(testSimulation.patchAt(new Posn(50, 50)).get().position, new Posn(2, 2));
		t.checkExpect(testSimulation.patchAt(new Posn(140, 210)).get().position, new Posn(7, 10));
		
	}
	
	void testMutations(Tester t) {
		String genome = "P9V5F9RLWLNSRLWLEE";
		System.out.println("Demonstrating Mutation");
		for(int i = 0; i < 100; i++) {
			System.out.println(genome);
			genome = Genome.mutate(genome);
		}
	}
}
