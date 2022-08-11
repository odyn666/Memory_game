import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import static java.lang.System.currentTimeMillis;

public class Main
    {
    private static class Position
        {
        private final int position;


        public Position(int position)
            {
            this.position = position;
            }

        public int getPosition()
            {
            return position;
            }


        }


    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


    public static void main(String[] args) throws IOException
        {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Whats your name ?");
        String name = scanner.nextLine();
        System.out.println("");
        System.out.print("Choose Game Mode:\n");
        System.out.print("1.Easy\n");
        System.out.print("2.Hard\n");

        int gameMode = scanner.nextInt();

        ArrayList<String> wordsList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("src/Words.txt"));
       // BufferedReader readerStat = new BufferedReader(new FileReader("src/ScoreBoard.txt"));

        try
            {
            do
                {
                String line = reader.readLine();
                if (line == null)
                    {
                    break;
                    }

                wordsList.add(line.trim());
                } while (true);
            } catch (Exception e)
            {
            throw new RuntimeException(e);
            }
        Random rand = new Random(currentTimeMillis());

        String[] words;
        int amount = gameMode == 1 ? 4 : 8;


        words = new String[amount];
        for (int i = 0; i < amount; i++)
            {
            words[i] = wordsList.get(rand.nextInt(wordsList.size()));
            }
        Asortment asortment = new Asortment(words, gameMode == 2 ? "hard" : "easy", gameMode == 1 ? 15 : 10);


        try
            {
            int attempt = 0;

            long starTime = currentTimeMillis() / 1000;
            while (!asortment.isFinished())
                {

                ++attempt;
                asortment.printStatus();

                final Position questionPosition = parsePosition("Set the position of question");

                if (asortment.isShownQuestion(questionPosition.getPosition()))
                    {
                    System.out.println("This word was being shown.");
                    asortment.decrementChances();
                    continue;
                    } else
                    {
                    asortment.setShowQuestion(questionPosition.getPosition(), true);
                    }

                asortment.printStatus();

                final Position answerPosition = parsePosition("Set the position of answer");
                asortment.setShowQuestion(questionPosition.getPosition(), false);

                final boolean status = asortment.answer(questionPosition.getPosition(), answerPosition.getPosition());
                if (!status)
                    {
                    asortment.decrementChances();
                    }

                long endTime = currentTimeMillis() / 1000;
                int gametime = (int) (endTime - starTime);
                System.out.println("Game took you :" + gametime + " seconds");
                if (asortment.isFinished())
                    {
                    System.out.println("Game finished in " + attempt + " attempts\n");
                    //          Scoreboard
                    Asortment.scoreBoard(name, attempt, gametime);

                    }
                }

            if (asortment.getChances() > 0)
                {
                System.out.println("Congratulations.");
                } else
                {
                System.out.println("No more chances.");
                }

            } catch (Exception e)
            {
            System.out.println("Unknown key input");

            }

        }

    private static Position parsePosition(String label) throws IOException
        {
        System.out.print(label + ": ");
        return parse(reader.readLine());
        }

    private static Position parse(String line) throws InvalidObjectException
        {
        line = line.trim();
        line.replaceAll(" +", "");

        final char row = Character.toLowerCase(line.charAt(0));
        final int col = Integer.parseInt(line.substring(1));

        if (col <= 0)
            {
            throw new InvalidObjectException("Wrong column no");
            } else if (row != 'a' && row != 'b')


            {
            throw new InvalidObjectException("Wrong row letter");
            }

        return new Position(col - 1);
        }
    }
