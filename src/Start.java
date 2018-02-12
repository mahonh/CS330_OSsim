import java.util.Scanner;

public class Start
{
    public static void main(String args[])
    {
        Scanner scany = new Scanner(System.in);
        String choice;
        boolean run = true;

        while(run)
        {
            System.out.println("Ready to start?");
            System.out.println("Y to Start");
            System.out.println("Anything else to quit");

            choice = scany.nextLine();

            if(choice.equals("y")|| choice.equals("Y"))
            {
                OS myOS = new OS();
                run = false;
            }

            else
                {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }

        }
    }
}
