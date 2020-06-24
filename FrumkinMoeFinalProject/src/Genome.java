import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/*
Genome Helper Class

Genome  Key

P: mutation rate multiplier from 0-9. The higher it is the less likely mutations are
F: nutrients given to children multiplier
V: vision

S: stomach
W: leg

R: right appendage
L: left appendage
N: next appendage

E: Empty Data


with this encoding having a full tree is not doable
*/
public class Genome {
	static final char[] allowedCharacters = {'P', 'F', 'V', 'S', 'W', 'R', 'L', 'N', 'E'};
	static final int BaseMutationRate = 100;
	
	//randomly changes the genome taking into account the specified mutation rate
	//TODO: implement insertion and deletion mutations
	public static String mutate(String genome) {
		
		//set mutation rate to default to the base
		int mutationRate = BaseMutationRate * getValueFollowing(genome, 'P', 1);
		
		//initialize RNG
		Random RNG = new Random();
		
		//create an array to store the output
		String output = "";
		//mutate the genome
		for(int i = 0; i < genome.length(); i++) {
			char current = genome.charAt(i);

			//if current is a number
			if(Character.isDigit(current)) {
				//set the output to some offset of a random number
				int random = RNG.nextInt(mutationRate);
				if(random > 100) {
					output += current;
				} else {
					char newValue = (char) ((current - '0') + ((random/100)-5));
					//only change value if newValue is valid
					output += Character.isDigit(newValue)? newValue: current;
				}
			} 
			else {
				//randomly change current to another allowed character
				int random = RNG.nextInt(mutationRate);
				if(random + 1 > allowedCharacters.length) {
					output += current;
				} else {
					output += allowedCharacters[random];
				}
				//roll for insertion or deletion
				random = RNG.nextInt(mutationRate);
				if(random < allowedCharacters.length) {
					output += allowedCharacters[random];
				} else if(random > allowedCharacters.length && random < 2 * allowedCharacters.length) {
					output = output.substring(0, output.length() - 1);
				}
			}
		}
		return output;
	}
	
	//returns character following given character or default
	public static char getLetterFollowing(String text, char letter, char defaultLetter) {
		for(int i = 0; i < text.length(); i++) {
			//check if the current letter if the one being searched for
			if (text.charAt(i) == letter) {
				//check if next character exists
				if(i + 1 < text.length()) {
					//if next character exits return the character turned into a number
					//it is possible that the next character is not a number and such the return value will be outside of the expected range
					//this allows for further mutation
					return text.charAt(i + 1);
				}
			}
		}
		
		return defaultLetter;
	}
	
	//Convenience method returns number following a given letter or returns default;
	public static int getValueFollowing(String text, char letter, int defaultValue) {
		return getLetterFollowing(text, letter, (char)(defaultValue + '0')) -'0';
	}
	
	//returns the string from the last first occurrence of the characters
	public static String truncateAtLast(String text, Set<Character>characters) {
		//find the first occurrence of all of the characters
		Map<Character, Integer> firstOccurences = new HashMap<Character, Integer>();
		
		//loop through the string an find the first occurence of each character
		for(int i = 0; i < text.length(); i++) {
			char current = text.charAt(i);
			//if the current character is on the list
			if (characters.contains(current)) {
				//set the first occurence if it has not yet been set
				firstOccurences.putIfAbsent(current, i);
			}
		}
		
		//compute the last first occurence
		int lastFirstOccurence = firstOccurences.values().stream().collect(Collectors.maxBy((first, second) -> (first-second))).orElse(-1);
		
		return text.substring(lastFirstOccurence + 1);
	}
	
	//convenience method for truncateAtLast
	public static String truncateAtLast(String text, Character... characters) {
		return truncateAtLast(text, new HashSet<Character>(Arrays.asList(characters)));
	}
}
