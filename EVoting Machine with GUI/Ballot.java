/**
 * Alex Drizos
 * CS401
 * Assignment4
 * This class establishes the ballot objects
 */
//import section
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class Ballot extends JPanel {

    //instance variables
    private int id;
    private JLabel categoryLabel;
    private String categoryS, ballotsFileName;
    private ArrayList <JButton> candidatesButton = new ArrayList<JButton>();
    private ArrayList<String> candidatesS = new ArrayList<String>();
    private int [] numVotes;
    private int [] totalNumVotes;  // total votes for all voters
    private boolean[] clicked;

    //constructor
    public Ballot(int _id,String _category,String [] _candidatesS, String _ballotsFileName)
    {
        //set variables
        id = _id;
        categoryS = _category;
        ballotsFileName = _ballotsFileName;
        for (int t= 0; t<_candidatesS.length; t++)
        {
            candidatesS.add(_candidatesS[t]);
        } //end of loop to copy array to arrayList
        categoryLabel = new JLabel(categoryS);
        numVotes = new int [candidatesS.size()];
        totalNumVotes = new int [candidatesS.size()];


        //align label
        categoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //make bold
        Font f = categoryLabel.getFont();
        categoryLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));

        // set layout for ballot panel
        setLayout(new GridLayout(candidatesS.size()+1,1));

        //add the category label to the panel
        add(categoryLabel);

        //create action listener object
        ActionListener listener = new BListener();

        //loop to create arbitrary number of candidate buttons
        clicked = new boolean[candidatesS.size()];
        for (int i = 0; i < candidatesS.size(); i++)
        {
            candidatesButton.add(new JButton(candidatesS.get(i)));
            clicked[i] = false;
            candidatesButton.get(i).addActionListener(listener);
            add(candidatesButton.get(i));
            candidatesButton.get(i).setEnabled(false);
        } // end of loop
    }   //end of ballot constructor

    //method to return click status
    public boolean getStatusClicked(int i)
    {
        return clicked[i];
    }

    //accessor methods
    public String getCategoryS() {return categoryS;}
    public String getCandidateString(int i) {return candidatesS.get(i);} //not right
    public int getNumCandidates() {return candidatesButton.size();}
    public int getId() {return id;}
    public int getVoteCount(int i) {return numVotes[i];}
    public int getTotalNumVotes (int i) {return totalNumVotes[i];}

    //mutator methods
    public void enableCandidateButtons()
    {
        for (int g = 0; g<candidatesS.size(); g++)
        {
            candidatesButton.get(g).setEnabled(true);
        }
    }
    public void disableCandidateButtons() //disable and reset buttons after one person votes
    {
        for (int g = 0; g<candidatesS.size(); g++)
        {
            candidatesButton.get(g).setEnabled(false);
            clicked[g] = false;
            candidatesButton.get(g).setForeground(Color.BLACK);
        }
    }

    public void addTotalNumVotes(int i) {totalNumVotes[i]++;}
    //listener to toggle the status of buttons when clicked
    private class BListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            int i = 0; //counter variable for arraylist calls
            for (JButton b: candidatesButton) //change to REGULAR FOR LOOP??
            {
                if (e.getSource() == candidatesButton.get(i))
                {
                    clicked[i] = !clicked[i];
                    if (clicked[i])
                    {
                        candidatesButton.get(i).setForeground(Color.RED);
                        numVotes[i]++;
                        //JOptionPane.showMessageDialog(null,numVotes[i]); debugging option to check vote count

                    }

                    else if (!clicked[i])
                    {
                        candidatesButton.get(i).setForeground(Color.BLACK);
                        numVotes[i]--;
                    }
                } // end of if get source is
                else
                {
                    candidatesButton.get(i).setForeground(Color.BLACK);
                    clicked[i] =false;
                    numVotes[i] = 0;
                }

                i++; //increment int counter
            } // end of cycle through all buttons
        } // end of actionperformed
    } //end of listener



} // end of ballot class
