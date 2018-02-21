/**
 * Created by adrizosimac on 11/23/15.
 * EXTRA CREDIT FOR ASSIGNMENT 4
 * This programs allows users to add voters to the voters.txt doc or create one if it doesnt exit.
 */
//import section
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.*;
import javax.swing.*;


public class VoterRegistration {

    public static void main (String [] args) throws IOException
    {
        //variables
        ArrayList <Integer> voterIdList = new ArrayList<>();
        ArrayList <String> voterNames = new ArrayList<>();
        ArrayList <Boolean> voterVoted = new ArrayList<>();
        boolean loop=true;
        String addAnother;
        //check to see if voters.txt exists
        File file = new File ("voters.txt");
        if (!file.exists())
            System.out.println("The file does not exist, but will be created.");
        //if so read it in
        else
            readInVoterFile(voterIdList,voterNames,voterVoted);

        //get data from user

        do {
            String tempStr = JOptionPane.showInputDialog("Please enter birthdate in this format (no slashes): 00/00/00:");
            voterIdList.add(Integer.parseInt(tempStr));
            voterNames.add(JOptionPane.showInputDialog("Please enter first and last name:"));
            voterVoted.add(false);
            do {
                addAnother = JOptionPane.showInputDialog("Would you like to add another name? (y/n)");
            } while (!addAnother.equals("y") && !addAnother.equals("n"));
            if (addAnother.equals("y"))
                loop = true;
            else
                writeVoterFile(voterIdList,voterNames,voterVoted);
                System.exit(0);

        } while (loop);

        //write new file

    }

    public static void readInVoterFile(ArrayList<Integer> _voterIdList, ArrayList <String> _voterNames, ArrayList <Boolean> _voterVoted) throws IOException
    {
        int counter = 0;
        //objects - open and setup scanner for file
        File myfile = new File ("voters.txt");
        Scanner textScan = new Scanner(myfile); // reads in date from text file
        while (textScan.hasNextLine()) //one voter's data for each loop
        {
            //take the first line as a string
            String [] tempStr = textScan.nextLine().split(":");
            //parse line into proper data values
            _voterIdList.add(Integer.parseInt(tempStr[0]));  //take first value for voter id
            _voterNames.add(tempStr[1]); // take second value as voter name
            _voterVoted.add(Boolean.parseBoolean(tempStr[2]));
            counter++; //adds counter to while loop
        }   // end of loop to read in voter text file contents
        textScan.close(); //closes ballots.txt file
    } // end of voter read in method

    public static void writeVoterFile(ArrayList <Integer> voterIdList, ArrayList <String> voterNames, ArrayList <Boolean> voterVoted) throws IOException
    {
        PrintWriter outputFile = new PrintWriter("tempVotersFile.txt");
        for (int q = 0; q < voterIdList.size(); q++)
        {
            outputFile.print(voterIdList.get(q)+":");
            outputFile.print(voterNames.get(q)+":");
            String tempStrboolean = String.valueOf(voterVoted.get(q));
            if (q == voterIdList.size()-1) //avoid blank line at end of text file
                outputFile.print(tempStrboolean);
            else
                outputFile.println(tempStrboolean);
        }
        outputFile.close();

        //change temp file name to voters.txt
        File tempFile = new File ("tempVotersFile.txt");
        File originalFile = new File ("voters.txt");
        tempFile.renameTo(originalFile);
    }   //end of write voter method
}
