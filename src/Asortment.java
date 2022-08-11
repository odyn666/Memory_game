import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("SuspiciousIndentAfterControlStatement")
public class Asortment
    {


    private final List<String> questions;
    private final List<String> answers;

    private final boolean[] showQuestion;
    private final boolean[] showAnswer;

    private final String modeName;
    private int chances;
    private int checks;

    private final Offset[] offsets;

    public Asortment(String[] questions, String modeName, int chances)
        {

        this.questions = Arrays.asList(questions.clone());
        this.answers = Arrays.asList(questions.clone());
        Collections.shuffle(answers);

        this.modeName = modeName;
        this.chances = chances;
        this.checks = 0;

        showQuestion = new boolean[this.questions.size()];
        showAnswer = new boolean[this.answers.size()];

        offsets = new Offset[questions.length];

        for (int i = 0; i < questions.length; i++)
            {
            showQuestion[i] = false;
            showAnswer[i] = false;

            final int longest = Math.max(this.questions.get(i).length(), this.answers.get(i).length());
            final int smallest = Math.min(this.questions.get(i).length(), this.answers.get(i).length());

            offsets[i] = new Offset(longest, smallest);
            }
        }

    public boolean isShownQuestion(int position)
        {
        assert 0 <= position && position < showQuestion.length;

        return showQuestion[position];
        }

    public void setShow(int position, boolean question, boolean value)
        {
        if (question)
            {
            setShowQuestion(position, value);
            } else
            {
            setShowAnswer(position, value);
            }
        }

    public void setShowQuestion(int position, boolean value)
        {
        assert 0 <= position && position < showQuestion.length;

        showQuestion[position] = value;
        }

    public void setShowAnswer(int position, boolean value)
        {
        assert 0 <= position && position < showAnswer.length;

        showAnswer[position] = value;
        }

    public boolean answer(int questionPosition, int answerPosition)
        {
        assert 0 <= questionPosition && questionPosition < showQuestion.length;
        assert 0 <= answerPosition && answerPosition < showAnswer.length;

        if (questions.get(questionPosition).equals(answers.get(answerPosition)))
            {
            showQuestion[questionPosition] = true;
            showAnswer[answerPosition] = true;

            checks += 1;

            return true;
            } else
            {
            return false;
            }
        }

    public void decrementChances()
        {
        if (chances > 0)
            {
            chances -= 1;
            }
        }

    public int getChances()
        {
        return chances;
        }


    public boolean isFinished()
        {
        return chances <= 0 || checks >= questions.size();
        }

    public void printStatus()
        {
        System.out.println("---------------------");
        System.out.println("    Level: " + modeName);
        System.out.println("    Guess chances: " + chances);
        System.out.println();

//        System.out.println(questions);
//        System.out.println(answers);

        System.out.print(" ");
        for (int i = 0; i < offsets.length; i++)
            {
            final String label = "" + (i + 1);
            final int indent = getIndentationSize(i);

            System.out.print(" " + label);
            if (indent > label.length())
                {
                System.out.print(" ".repeat(indent - label.length()));
                }
            }
        System.out.println();

        System.out.print("A");
        for (int i = 0; i < offsets.length; i++)
            {
            String label;
            int indent = getIndentationSize(i);

            if (showQuestion[i])
                {
                label = questions.get(i);
                } else
                {
                label = "X";
                }

            System.out.print(" " + label);
            if (indent > label.length())
                {
                System.out.print(" ".repeat(indent - label.length()));
                }
            }
        System.out.println();

        System.out.print("B");
        for (int i = 0; i < offsets.length; i++)
            {
            String label;
            int indent = getIndentationSize(i);

            if (showAnswer[i])
                {
                label = answers.get(i);
                } else
                {
                label = "X";
                }

            System.out.print(" " + label);
            if (indent > label.length())
                {
                System.out.print(" ".repeat(indent - label.length()));
                }
            }
        System.out.println();

        System.out.println("---------------------");
        }

    private int getIndentationSize(int position)
        {
        assert 0 <= position && position < questions.size();
        assert 0 <= position && position < answers.size();

        if (showQuestion[position] || showAnswer[position])
            {
            return offsets[position].getLargest();
            } else
            {
            return 0;
            }
        }

    private int getIndentationSize(int position, boolean question)
        {
        assert 0 <= position && position < questions.size();
        assert 0 <= position && position < answers.size();

        if (showQuestion[position] || showAnswer[position])
            {
            Offset offset = offsets[position];

            if (question)
                {
                return showQuestion[position] ? offset.getDiff() : offset.getLargest();
                } else
                {
                return showAnswer[position] ? offset.getDiff() : offset.getLargest();
                }
            } else
            {
            return 0;
            }
        }

    static void scoreBoard(String name, int attempt, int gameTime) throws IOException
        {
        File scoreboard = new File("src/Scoreboard.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(scoreboard, true));
        BufferedReader reader = new BufferedReader(new FileReader(scoreboard));
        ArrayList<String> scores = new ArrayList<String>();

        String currentLine = reader.readLine();

        boolean exists = scoreboard.exists();
        if (exists)
            {                               //Kacper : 4 attempts won in 240 seconds
            if (currentLine == null)
                writer.append("High Scores");

            writer.newLine();
            //writer.append(name + " : " + attempt + " attempts" + "  won in " + gameTime + " seconds");
            writer.append(name).append(" : ").append(String.valueOf(attempt)).append(" attempts").append("  won in ").append(String.valueOf(gameTime)).append(" seconds");
            writer.newLine();
            writer.close();
            //sorting
            while (currentLine != null)
                {
                scores.add(currentLine);
                currentLine = reader.readLine();

                }

//                String[] scoresArr=scores.toArray(new String[0]);
//                for (int i = 0; i <scores.size() ; i++)
//                {
//                    System.out.println(scoresArr[i]);
//                }
            Collections.sort(scores);

            for (String lines : scores)
                {

                writer.write(lines);
                writer.newLine();
                }


            } else
            {
                scoreboard.createNewFile();
            }
        }


    }
