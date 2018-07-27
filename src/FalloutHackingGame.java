import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 7/26/2018
 */
public class FalloutHackingGame {

    private static final List<String> ALL_WORDS;

    static {
        ALL_WORDS = new ArrayList<>();
        try(BufferedReader read = new BufferedReader(new FileReader("enable1.txt"))) {
            String line;
            while ((line = read.readLine()) != null)
                ALL_WORDS.add(line.toUpperCase());
        } catch (Exception e) {}
    }

    private final List<String> words;
    private final int correctWord;

    private int numberOfGuesses;

    public FalloutHackingGame(int numberOfWords, int lengthOfWord) {
        // all words -> words with specified length (lengthOfWord)
        // -> shuffled list -> sublist to specified size (numberOfWords)
        words = shuffle(ALL_WORDS.stream().filter(w -> w.length() == lengthOfWord)
                .collect(Collectors.toList())).subList(0, numberOfWords); // neat
        correctWord = (int)(Math.random() * numberOfWords);
        numberOfGuesses = 4;
    }

    public boolean play(Scanner scan) {
        words.forEach(System.out::println); // print out all the words for the player to see

        while(numberOfGuesses > 0) {
            String input = "";
            while(!words.contains(input)) { // make sure that the input is actually one of the words
                System.out.printf("Guess (%d left)? ", numberOfGuesses);
                input = scan.nextLine().toUpperCase();
            }
            numberOfGuesses--;

            System.out.printf("%d/%d correct%n", getNumberCorrect(input), words.get(0).length());

            if(input.equals(words.get(correctWord))) {
                System.out.println("You win!");
                return true;
            }

        }

        System.out.printf("You lose... The word was %s. Better luck next time.%n", words.get(correctWord));
        return false;
    }

    public int getNumberCorrect(String guess) {
        // this is the easiest way
        int correct = 0;
        for(int i = 0; i < guess.length(); i++)
            if(words.get(correctWord).charAt(i) == guess.charAt(i))
                correct++;
        return correct;
    }

    private <E> List<E> shuffle(List<E> shuffle) {
        Collections.shuffle(shuffle);
        return shuffle;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        while(true) {
            System.out.print("Difficulty (1-5)? ");
            int difficulty = Integer.parseInt(scan.nextLine());

            int length = difficulty * 2 + 2;
            int number = difficulty * 2 + 3;

            new FalloutHackingGame(number, length).play(scan);
            System.out.print("Play again (y, n)? ");
            if(scan.nextLine().charAt(0) == 'n')
                return;
        }
    }
}
