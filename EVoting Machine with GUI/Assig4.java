/**
 * Alex Drizos
 * CS401
 * Main Class to execute voting e-machine
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


public class Assig4 {


    public static void main (String [] args) throws IOException
    {

        /* variables */

        String ballotsFileName = args[0]; // gets the file name from command line
        JFrame theWindow; //assign variable name for main window
        ArrayList <Ballot> ballotsAr = new ArrayList<>(); //creates array for jpanel ballot objects
        ArrayList <Integer> numVotes = new ArrayList<>();
        int numBallots=0;
        int counter = 0; //to count each file read in loop
        JButton login, submitVote;
        //voter variables
        ArrayList <Integer> voterIdList = new ArrayList<>();
        ArrayList <String> voterNames = new ArrayList<>();
        ArrayList <Boolean> voterVoted = new ArrayList<>();

        /*create window*/

        theWindow = new JFrame ("Voting E Machine");
        theWindow.setLayout(new FlowLayout());
        theWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        /* read in ballot info and create ballot objects*/

        numBallots = readInBallotFile(ballotsFileName, numBallots, ballotsAr);

        /*read in data from voter file*/

        try {
            // method that accesses the file
            readInVoterFile(voterIdList, voterNames, voterVoted);
        }
        catch (Exception e)
        {}

        /*add local components*/
        submitVote = new JButton("Submit Vote(s)");
        submitVote.setEnabled(false);
        login = new JButton("Login");

        /*actionListener class to handle login and vote submissions*/

        class MListener implements ActionListener
        {
            public void actionPerformed(ActionEvent ae)
            {
                //if submitVote button is clicked
                if (ae.getSource() == submitVote)
                {
                    //confirm submission
                    int selectedOption;
                    selectedOption = JOptionPane.showConfirmDialog(null,"Confirm vote?","Choose",JOptionPane.YES_NO_OPTION);

                    if (selectedOption == JOptionPane.NO_OPTION)
                        JOptionPane.showMessageDialog(null, "Please review selection");
                    else if (selectedOption == JOptionPane.YES_OPTION)
                    {
                        //thank you message
                        JOptionPane.showMessageDialog(null, "Thank you for your vote!");
                        //loop through each ballot and each candidate to update total votes
                        for (int bb =0; bb < ballotsAr.size(); bb++)
                        {
                            for (int s =0; s <ballotsAr.get(bb).getNumCandidates(); s++)
                            {
                                if (ballotsAr.get(bb).getVoteCount(s) >0)
                                    ballotsAr.get(bb).addTotalNumVotes(s);
                            }
                        }

                        try {
                            // method that accesses the file
                            writeVoterFile(voterIdList, voterNames, voterVoted);
                        }
                        catch (Exception e)
                        {}
                        try {
                            // method that accesses the file
                            writeBallotResultsFiles(ballotsAr);
                        }
                        catch (Exception e)
                        {}

                        //reset voter machine
                        login.setEnabled(true);
                        submitVote.setEnabled(false);
                        for (int gg = 0; gg <ballotsAr.size(); gg++)
                        {
                            ballotsAr.get(gg).disableCandidateButtons();
                        }

                    }

                } //end of if source is submit vote button

                //if login button is clicked
                if (ae.getSource() == login)
                {
                    String userEntryIdString;
                    int userEntryId;
                    boolean registered=false;

                    do {
                        userEntryIdString = JOptionPane.showInputDialog(null, "Enter your voter ID number: ");
                        try {
                            userEntryId = Integer.parseInt(userEntryIdString);
                            //check id number against registered citizens
                            for (int v =0; v < voterIdList.size(); v++)
                            {
                                if (userEntryId == voterIdList.get(v) && !voterVoted.get(v))
                                {
                                    registered = true;
                                    voterVoted.set(v,true);
                                    login.setEnabled(false);
                                    //print welcome message with name
                                    JOptionPane.showMessageDialog(null, voterNames.get(v) + ", please make your choices");
                                }
                                else if (userEntryId == voterIdList.get(v) && voterVoted.get(v))
                                    JOptionPane.showMessageDialog(null, "YOU HAVE ALREADY VOTED. If think this is an error precede.");
                            }   // end of loop to check id against id list

                            if (registered)
                            {
                                //enable buttons
                                for (int j = 0; j<ballotsAr.size(); j++)
                                {
                                    ballotsAr.get(j).enableCandidateButtons();
                                }
                                submitVote.setEnabled(true);
                            }

                            else if (!registered)
                                System.out.println("Please try again. ");
                        }
                        catch (Exception e)
                        {break;}

                    } while (!registered);
                } //end of if login button is clicked
            } //end of action performed method

        }// end of actionlistener

        /*create action listener object */
        ActionListener listener2 = new MListener();

        /*add Ballot panels*/
        addPanels(theWindow, ballotsAr);

        /*assign components to actionlistener*/
        submitVote.addActionListener(listener2);
        login.addActionListener(listener2);

        /* Add local buttons to window */
        theWindow.add(login);
        theWindow.add(submitVote);

        /*pack and set window to visible*/

        theWindow.pack();
        theWindow.setVisible(true);

    }   //end of main

    public static int readInBallotFile(String ballotsFile, int numBallots, ArrayList<Ballot> ballotsAr) throws IOException
    {
        int counter = 0;
        //objects - open and setup scanner for file
        File myfile = new File (ballotsFile);
        Scanner textScan = new Scanner(myfile); // reads in date from text file
        numBallots = Integer.parseInt(textScan.nextLine()); //read in first line (number of ballots) and parse to int
        while (textScan.hasNextLine()) //one ballot's data for each loop
        {

            //read in each ballot's info
            //take the first line as a string
            String [] tempStr = textScan.nextLine().split(":");
            //parse line into proper data values
            int tempId = Integer.parseInt(tempStr[0]); //take first value for id
            String tempCategory = tempStr[1]; // take second value as category type
            String [] tempCandidates = tempStr[2].split(","); // takes the arbitrary number of candidates and splits into separate string variables

            //create ballot object
            ballotsAr.add(new Ballot(tempId,tempCategory,tempCandidates, ballotsFile));
            counter++; //adds counter to while loop
        }   // end of loop to read in ballot text file contents
        textScan.close(); //closes ballots.txt file
        return numBallots;
    }   //end of read in ballot file method

    public static void addPanels(JFrame theWindow, ArrayList <Ballot> ballotsAr)
    {
        for (int i =0; i<ballotsAr.size();i++)
        {
            theWindow.add(ballotsAr.get(i));
        }
    }

    public static void readInVoterFile(ArrayList <Integer> _voterIdList, ArrayList <String> _voterNames, ArrayList <Boolean> _voterVoted) throws IOException
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

    //update voter file local variables including false to true for voted
    //write data to temp file
    //delete original file
    //change temp file name to previous file name

    //create ballot files
    //reset voterprogram

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

    public static void writeBallotResultsFiles(ArrayList <Ballot> _ballotsAR) throws IOException
    {
        for (int h = 0; h <_ballotsAR.size(); h++)
        {
            String tempStr = String.valueOf(_ballotsAR.get(h).getId());
            PrintWriter outputFile = new PrintWriter(tempStr);
            for (int r = 0; r <_ballotsAR.get(h).getNumCandidates(); r++)
            {
                outputFile.print(_ballotsAR.get(h).getCandidateString(r)+":");
                outputFile.println(_ballotsAR.get(h).getTotalNumVotes(r));
            } //end of loop for number of candidates for each ballot
            outputFile.close();
        } // end of loop for each ballot

    }   // end of write ballot results method

} // end of class
