// Casey Scott
// JAV1 - 1704
// MainActivity.java
package com.fullsail.caseyscott.mini_sudoku;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    //Variable for the game
    private int boardNumber = 1;
    private String[][] gameNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button checkButton = (Button) findViewById(R.id.checkBoardbutton);
        checkButton.setOnClickListener(mListener);

        gameNumber = firstGameboard();
        defaultNumsGame1(gameNumber);

    }


    //React to click events
    private final View.OnClickListener mListener = new View.OnClickListener(){
        Boolean b = true;
        @Override
        public void onClick(View v) {

            for (int x = 0; x < gameBoard().length; x++) {
                for (int y = 0; y < gameBoard().length; y++) {

                    if (gameBoard()[x][y].getText().toString().matches("")) {
                        presentErrorToast();
                        b = false;
                        return;
                    }
                }

            }
            if (b) {
                //Check for no problems = WIN
                if (checkForWin(gameNumber)) {
                    //Present the win dialog to the user
                    presentWinDialog();

                } else {
                    //Create a string to show the user that they have problem areas
                    StringBuilder sb = new StringBuilder();

                    for (String s : checkSections(gameNumber)) {

                        sb.append(s).append("\n");
                    }
                    for (String s : checkRows(gameNumber)) {

                        sb.append(s).append("\n");
                    }
                    for (String s : checkColumns(gameNumber)) {

                        sb.append(s).append("\n");
                    }

                    //Present the alert and tell the user where problems are
                    presentProblemsDialog(String.valueOf(sb));
                }
            }
        }
    };

    //Method to set the first board
    private String[][] firstGameboard() {

        return new String[][]{

                {"2", "1", "4", "3"},
                {"3", "4", "1", "2"},
                {"4", "3", "2", "1"},
                {"1", "2", "3", "4"}

        };
    }
    //Method to start the second board
    private String[][] secondGameboard(){

        return new String[][]{

                {"4","1","2","3"},
                {"2","3","1","4"},
                {"3","2","4","1"},
                {"1","4","3","2"}
        };
    }
    //Method to start the third board
    private String[][] thirdGameboard(){

        return new String[][]{

                {"1", "2", "4", "3"},
                {"4", "3", "1", "2"},
                {"3", "1", "2", "4"},
                {"2", "4", "3", "1"}
        };
    }

    //Method to reference the game board edit text views
    private EditText[][] gameBoard(){

        EditText row0column0 = (EditText) findViewById(R.id.row0column0);
        EditText row0column1 = (EditText) findViewById(R.id.row0column1);
        EditText row0column2 = (EditText) findViewById(R.id.row0column2);
        EditText row0column3 = (EditText) findViewById(R.id.row0column3);
        EditText row1column0 = (EditText) findViewById(R.id.row1column0);
        EditText row1column1 = (EditText) findViewById(R.id.row1column1);
        EditText row1column2 = (EditText) findViewById(R.id.row1column2);
        EditText row1column3 = (EditText) findViewById(R.id.row1column3);
        EditText row2column0 = (EditText) findViewById(R.id.row2column0);
        EditText row2column1 = (EditText) findViewById(R.id.row2column1);
        EditText row2column2 = (EditText) findViewById(R.id.row2column2);
        EditText row2column3 = (EditText) findViewById(R.id.row2column3);
        EditText row3column0 = (EditText) findViewById(R.id.row3column0);
        EditText row3column1 = (EditText) findViewById(R.id.row3column1);
        EditText row3column2 = (EditText) findViewById(R.id.row3column2);
        EditText row3column3 = (EditText) findViewById(R.id.row3column3);

        return new EditText[][]{

                {row0column0,row0column1,row0column2,row0column3},
                {row1column0,row1column1,row1column2,row1column3},
                {row2column0,row2column1,row2column2,row2column3},
                {row3column0,row3column1,row3column2,row3column3}

        };
    }
    //Method for checking the values of the sections
    private ArrayList<String> checkSections(String[][] answers){

        ArrayList<String> sections = new ArrayList<>();

        for(int x = 0; x < answers.length; x++){

            for(int y = 0; y < answers[x].length; y++) {
                if (String.valueOf(gameBoard()[x][y].getText()).matches(answers[x][y])) {
                    //Do Nothing
                } else {

                    if(x <= 1 && y <= 1){
                        String section1 = getString(R.string.Section1_Errors);
                        sections.add(section1);
                        break;
                    }
                    else if (x <= 1 && y >= 2){
                        String section2 = getString(R.string.Section2_Errors);
                        sections.add(section2);
                        break;
                    }
                    else if (x >= 1 && y >= 2){
                        String section3 = getString(R.string.Section3_Errors);
                        sections.add(section3);
                        break;
                    }
                    else if (x >= 2 && y >= 2){
                        String section4 = getString(R.string.Section4_Errors);
                        sections.add(section4);
                        break;
                    }
                }
            }
        }
        //Remove duplicate error messages for the sections
        for(int x=1; x<sections.size();x++){
            if(sections.toArray()[x-1].equals(sections.toArray()[x])){

                sections.remove(x);
            }
        }
        return sections;
    }
    //Method for checking the values of the rows
    private ArrayList<String> checkRows(String[][] answers){

        ArrayList<String> rows = new ArrayList<>();

        for(int x = 0; x < answers.length; x++){

            for(int y = 0; y < answers[x].length; y++) {
                if (String.valueOf(gameBoard()[x][y].getText()).matches(answers[x][y])) {
                    //Do Nothing
                } else {

                    if(x == 0){
                        rows.add(getString(R.string.Rows) + " " + (x+1) + " " + getString(R.string.Has_Errors));
                        break;
                    }
                    else if(x == 1){
                        rows.add(getString(R.string.Rows) + " " + (x+1) + " " + getString(R.string.Has_Errors));
                        break;
                    }
                    else if(x == 2){
                        rows.add(getString(R.string.Rows) + " " + (x+1) + " " + getString(R.string.Has_Errors));
                        break;
                    }
                    else if(x == 3){
                        rows.add(getString(R.string.Rows) + " " + (x+1) + " " + getString(R.string.Has_Errors));
                        break;
                    }
                }
            }
        }

        return rows;
    }
    //Method for checking the values of the columns
    private ArrayList<String> checkColumns(String[][] answers){

        ArrayList<String> column = new ArrayList<>();

        for(int y = 0; y < answers.length; y++){

            for(int x = 0; x < answers[y].length; x++) {
                if (String.valueOf(gameBoard()[x][y].getText()).matches(answers[x][y])) {
                    //Do Nothing
                } else {

                    if(y == 0){
                        column.add(getString(R.string.Column) + " " + (y+1) + " " + getString(R.string.Has_Errors));
                        break;
                    }
                    else if(y == 1){
                        column.add(getString(R.string.Column) + " " + (y+1) + " " + getString(R.string.Has_Errors));
                        break;
                    }
                    else if(y == 2){
                        column.add(getString(R.string.Column) + " " + (y+1) + " " + getString(R.string.Has_Errors));
                        break;
                    }
                    else if(y == 3){
                        column.add(getString(R.string.Column) + " " + (y+1) + " " + getString(R.string.Has_Errors));
                        break;
                    }
                }
            }
        }
        return column;
    }

    //Method for checking if the user has all the correct answers
    private boolean checkForWin(String[][] answers){

        for(int x = 0; x < (answers.length); x++){

            for(int y = 0; y< (gameBoard().length); y++){

                if (String.valueOf(gameBoard()[x][y].getText()).matches(answers[x][y])){

                }else{
                    return false;
                }
            }
        }
        return true;
    }
    //Method to present the user with a list of issues
    private  void presentProblemsDialog(String status){
        new AlertDialog.Builder(this)
                .setTitle(R.string.Problems_Title)
                .setMessage(
                        status)
                .setPositiveButton(R.string.OK,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int whichButton) {
                                dialog.dismiss();
                            }
                        })
                .create()
                .show();
    }
    //method th present the user with a win message
    private  void presentWinDialog(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.Winner)
                .setMessage(
                        getString(R.string.Winner_Message)+" "+changeBoards())
                .setPositiveButton(R.string.Yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int whichButton) {
                                updateGameBoards();
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(R.string.NO_Exit,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                dialog.dismiss();
                                System.exit(0);
                            }
                        })
                .create()
                .show();
    }
    //method th present the user with a win message
    private  void presentErrorToast(){

        Toast.makeText(MainActivity.this, R.string.Missing_Values_Message,
                Toast.LENGTH_SHORT).show();
    }

    //Change the board number for the next game and return an int
    private int changeBoards(){

        switch (boardNumber){
            case 1:
                boardNumber = 2;
                break;
            case 2:
                boardNumber = 3;
                break;
            case 3:
                boardNumber = 1;
                break;
        }
        return boardNumber;
    }
    //Method to update the game board to the next game board
    private void updateGameBoards(){

        for (int x = 0; x < gameBoard().length; x++){

            for (int y = 0; y < gameBoard().length; y++){

                gameBoard()[x][y].setText(null);
                gameBoard()[x][y].setEnabled(true);
            }
        }

        //Change the game board values to the next board
        switch (boardNumber){
            case 1:
                gameNumber = firstGameboard();
                defaultNumsGame1(firstGameboard());

                break;
            case 2:
                gameNumber = secondGameboard();
                defaultNumsGame2(secondGameboard());

                break;
            case 3:
                gameNumber = thirdGameboard();
                defaultNumsGame3(thirdGameboard());

                break;
        }

    }

    //Method to set the default values and disable the editText fields for game 1
    private void defaultNumsGame1(String[][] game){

        gameBoard()[0][3].setText(game[0][3]);
        gameBoard()[1][1].setText(game[1][1]);
        gameBoard()[2][2].setText(game[2][2]);
        gameBoard()[3][0].setText(game[3][0]);
        gameBoard()[0][3].setEnabled(false);
        gameBoard()[1][1].setEnabled(false);
        gameBoard()[2][2].setEnabled(false);
        gameBoard()[3][0].setEnabled(false);
    }
    //Method to set the default values and disable the editText fields for game 2
    private void defaultNumsGame2(String[][] game){

        gameBoard()[0][3].setText(game[0][3]);
        gameBoard()[1][1].setText(game[1][1]);
        gameBoard()[2][2].setText(game[2][2]);
        gameBoard()[3][0].setText(game[3][0]);
        gameBoard()[3][3].setText(game[3][3]);
        gameBoard()[0][3].setEnabled(false);
        gameBoard()[1][1].setEnabled(false);
        gameBoard()[2][2].setEnabled(false);
        gameBoard()[3][0].setEnabled(false);
        gameBoard()[3][3].setEnabled(false);
    }
    //Method to set the default values and disable the editText fields for game 3
    private void defaultNumsGame3(String[][] game){

        gameBoard()[0][0].setText(game[0][0]);
        gameBoard()[0][3].setText(game[0][3]);
        gameBoard()[1][1].setText(game[1][1]);
        gameBoard()[2][2].setText(game[2][2]);
        gameBoard()[3][0].setText(game[3][0]);
        gameBoard()[0][0].setEnabled(false);
        gameBoard()[0][3].setEnabled(false);
        gameBoard()[1][1].setEnabled(false);
        gameBoard()[2][2].setEnabled(false);
        gameBoard()[3][0].setEnabled(false);
    }
}
