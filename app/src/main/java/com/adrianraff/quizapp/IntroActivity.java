package com.adrianraff.quizapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.StringTokenizer;

import static com.adrianraff.quizapp.R.id.checkbox_answer_one_or_more_1;
import static com.adrianraff.quizapp.R.id.checkbox_answer_one_or_more_2;
import static com.adrianraff.quizapp.R.id.checkbox_answer_one_or_more_3;
import static com.adrianraff.quizapp.R.id.checkbox_answer_one_or_more_4;
import static com.adrianraff.quizapp.R.id.radioButton_answer_multi_1;
import static com.adrianraff.quizapp.R.id.radioButton_answer_multi_2;
import static com.adrianraff.quizapp.R.id.radioButton_answer_multi_3;
import static com.adrianraff.quizapp.R.id.radioButton_answer_multi_4;
import static com.adrianraff.quizapp.R.id.textView_free_question;
import static com.adrianraff.quizapp.R.id.textView_question_multi;
import static com.adrianraff.quizapp.R.id.textView_question_one_or_more;


public class IntroActivity extends AppCompatActivity {


    /**
     * Set up variables to be used app wide starting with user name, score, total right and total wrong answers
     */

    // For user name
    private EditText userName;

    // For email to send results

    private EditText emailAddress;
    public String emailTarget;


    // For layout changes. These designate the layout based on question type.
    private ViewGroup multi;
    private ViewGroup many;
    private ViewGroup free;
    private ViewGroup end;
    private ViewGroup intro;


    //Used to move through question array index
    public int questionIndex;

    public String questionType, theQuestion, answerA, answerB, answerC, answerD, theAnswer, theAnswer2, theAnswer3, theAnswer4, totalCorrectAnswers;


    public String[] getQuestions;

    //Used to record stored answers
    public String answerKeeperArray[][];

    // Checkbox answers
    public CheckBox answerCheck1;
    public CheckBox answerCheck2;
    public CheckBox answerCheck3;
    public CheckBox answerCheck4;

    //Radio group for answers
    RadioGroup radioAnswer;


    // Radio button for multi choice answers
    RadioButton answerRadio1;
    RadioButton answerRadio2;
    RadioButton answerRadio3;
    RadioButton answerRadio4;

    // used to grab total questions
    public int arrayLengthQuestions;

    // Used to get answer from user in free answer questions.
    EditText selectedFreeAnswer;

    // Total correct answer keeper
    public int totalScore = 0;

    // Used for email summary
    public String emailSummary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //TODO FIx layouts they are not keeping the formatting
        //Init all the layouts as viewgroups
        multi = findViewById(R.id.activity_multi_choice);
        many = findViewById(R.id.activity_one_or_more);
        free = findViewById(R.id.activity_free_text);
        end = findViewById(R.id.activity_end);
        intro = findViewById(R.id.activity_intro);

        //Set initial visibility of viewgroups
        intro.setVisibility(View.VISIBLE);
        multi.setVisibility(View.INVISIBLE);
        end.setVisibility(View.INVISIBLE);
        many.setVisibility(View.INVISIBLE);
        free.setVisibility(View.INVISIBLE);


        // used to get user name
        userName = findViewById(R.id.editText_user_name);

        // used to grab email address from user
        emailAddress = findViewById(R.id.editText_email_address);


        Resources res = getResources();
        radioAnswer = findViewById(R.id.radio_answers);

        // used to page through the questions
        questionIndex = 0;
        // used to retrieve the questions from the string array
        getQuestions = res.getStringArray(R.array.testQuestionArray);

        // determine the total amount of questions based on the array length
        arrayLengthQuestions = getQuestions.length;

        // array to store answers the user selected
        answerKeeperArray = new String[arrayLengthQuestions][4];


    }

    /**
     * The application starting point. Grab name and email address from user then select a question from the pool.
     */

    public void startApp(View view) {


        String enteredName = userName.getText().toString();
        emailTarget = emailAddress.getText().toString();
        String message = getString(R.string.startApp_thank_you) + enteredName + getString(R.string.startApp_get_ready);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        selectQuestion();


    }

    /**
     * Call this to select a question
     */

    public void selectQuestion() {

        // Hide the soft keyboard for better looks.
        hideKeyboard();


        // Grab a question from the string array
        StringTokenizer splitString = new StringTokenizer((getQuestions[questionIndex]), "|");


        // Parse the array into some string variables with the tokenizer
        questionType = splitString.nextToken();
        theQuestion = splitString.nextToken();
        answerA = splitString.nextToken();
        answerB = splitString.nextToken();
        answerC = splitString.nextToken();
        answerD = splitString.nextToken();
        theAnswer = splitString.nextToken();
        theAnswer2 = splitString.nextToken();
        theAnswer3 = splitString.nextToken();
        theAnswer4 = splitString.nextToken();
        totalCorrectAnswers = splitString.nextToken();


        // Check to see which type of question is being selected from the question string array and call the proper method to display it.
        switch (questionType) {
            case "MULTI":

                selectMulti(multi, theQuestion, answerA, answerB, answerC, answerD, theAnswer);
                break;

            case "MANY":

                selectMany(many, theQuestion, answerA, answerB, answerC, answerD, theAnswer, theAnswer2, theAnswer3, theAnswer4, totalCorrectAnswers);
                break;

            case "FREE":

                selectFree(free, theQuestion);
                break;

            default:
                // Error catching
                Toast.makeText(this, R.string.error_something_wrong, Toast.LENGTH_SHORT).show();
                Log.v("IntroActivity", "Some shit went south here.");
        }
    }

    /**
     * A multiple choice question with only one possible answer was selected.
     *
     * @param view      controls which layout is loaded
     * @param question  loads the question into the view
     * @param answerA   loads an answer choice into view
     * @param answerB   loads an answer choice into view
     * @param answerC   loads an answer choice into view
     * @param answerD   loads an answer choice into view
     * @param theAnswer loads an answer choice into view
     */

    public void selectMulti(View view, String question, String answerA, String answerB, String answerC, String answerD, String theAnswer) {

        hideKeyboard();

        // Set content view to multi layout

        intro.setVisibility(View.INVISIBLE);
        multi.setVisibility(View.VISIBLE);
        end.setVisibility(View.INVISIBLE);
        many.setVisibility(View.INVISIBLE);
        free.setVisibility(View.INVISIBLE);

        // Clear all the views from user selections
        clearSelections(view);

        // Set the values of the UI text to the string variable values. String theQuestionOne contains the question type.

        TextView questionTextView = findViewById(textView_question_multi);
        questionTextView.setText(question);


        answerRadio1 = findViewById(radioButton_answer_multi_1);
        answerRadio2 = findViewById(radioButton_answer_multi_2);
        answerRadio3 = findViewById(radioButton_answer_multi_3);
        answerRadio4 = findViewById(radioButton_answer_multi_4);

        answerRadio1.setText(answerA);
        answerRadio2.setText(answerB);
        answerRadio3.setText(answerC);
        answerRadio4.setText(answerD);


    }

    /**
     * A question with many answers is selected. Passed arguments are the questions and answers to set the text of the layout.
     *
     * @param view      controls the loaded view
     * @param question  loads the question into the view
     * @param answerA   loads an answer choice into view
     * @param answerB   loads an answer choice into view
     * @param answerC   loads an answer choice into view
     * @param answerD   loads an answer choice into view
     * @param theAnswer loads the answer for comparison
     */

    public void selectMany(View view, String question, String answerA, String answerB, String answerC, String answerD, String theAnswer, String theAnswer2, String theAnswer3, String theAnswer4, String totalCorrectAnswers) {


        hideKeyboard();

        // Set content view to many layout
        intro.setVisibility(View.GONE);
        end.setVisibility(View.GONE);
        many.setVisibility(View.VISIBLE);
        free.setVisibility(View.GONE);
        multi.setVisibility(View.GONE);
        clearSelections(view);

        // Set the values of the UI text to the string variable values. String theQuestionOne contains the question type.

        TextView questionTextView = findViewById(textView_question_one_or_more);

        questionTextView.setText(question);


        answerCheck1 = findViewById(checkbox_answer_one_or_more_1);
        answerCheck2 = findViewById(checkbox_answer_one_or_more_2);
        answerCheck3 = findViewById(checkbox_answer_one_or_more_3);
        answerCheck4 = findViewById(checkbox_answer_one_or_more_4);


        answerCheck1.setText(answerA);
        answerCheck2.setText(answerB);
        answerCheck3.setText(answerC);
        answerCheck4.setText(answerD);


    }

    /**
     * Select a free answer question.
     *
     * @param view     ~
     * @param question Is the question being asked.
     */

    public void selectFree(View view, String question) {

        hideKeyboard();


        // Set our layout views to have the proper one visible.

        intro.setVisibility(View.GONE);
        end.setVisibility(View.GONE);
        many.setVisibility(View.GONE);
        free.setVisibility(View.VISIBLE);
        multi.setVisibility(View.GONE);
        clearSelections(view);


        // Set the textview with the question to be asked.
        TextView questionTextView = findViewById(textView_free_question);
        questionTextView.setText(question);

    }

    /**
     * Answer checking area. Pass the question type to select
     */


    public void submitAnswer(View view) {
        hideKeyboard();
        // Look at the question type tag from the string array that stores questions and select the proper case
        switch (questionType) {

            // If a multiple choice question is selected start here.
            case "MULTI":

                // Check to see if any radio button is selected. If none then error message,
                RadioGroup rg = findViewById(R.id.radio_answers);
                if (rg.getCheckedRadioButtonId() == -1) {

                    Toast.makeText(this, R.string.error_please_select_answer, Toast.LENGTH_SHORT).show();
                    return;
                }

                String selectedMultiAnswer = ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString();
                Toast.makeText(this, "Your answer was " + selectedMultiAnswer, Toast.LENGTH_SHORT).show();

                //If the selected answer matches the stored correct answer from our array of questions, increase the total score counter
                // and record the answer in the answer keeper array
                if (selectedMultiAnswer.equals(theAnswer)) {
                    Toast.makeText(this, "The answer was correct. " + theAnswer, Toast.LENGTH_LONG).show();
                    answerKeeperArray[questionIndex][0] = selectedMultiAnswer;
                    totalScore = totalScore + 1;

                } else {
                    Toast.makeText(this, "WRONG! The answer was " + theAnswer, Toast.LENGTH_LONG).show();
                    //log the answer to the array even if it is wrong
                    answerKeeperArray[questionIndex][0] = selectedMultiAnswer;

                }
                break;

            // A question with more than one possible answer will start here.
            case "MANY":
                int manyCorrectAnswerKeeper = 0;
                if (answerCheck1.isChecked() && answerCheck1.getText().toString().equals(theAnswer)) {
                    // log answer into array
                    answerKeeperArray[questionIndex][0] = theAnswer;
                    Toast.makeText(this, R.string.answer_correct_a, Toast.LENGTH_SHORT).show();
                    manyCorrectAnswerKeeper++;
                }

                if (answerCheck1.isChecked()) {
                    // log answer into array
                    answerKeeperArray[questionIndex][0] = theAnswer;
                }


                if (answerCheck2.isChecked() && answerCheck2.getText().toString().equals(theAnswer2)) {
                    //log answer into array
                    answerKeeperArray[questionIndex][1] = theAnswer2;
                    Toast.makeText(this, R.string.answer_correct_b, Toast.LENGTH_SHORT).show();
                    manyCorrectAnswerKeeper++;
                }

                if (answerCheck2.isChecked()) {
                    // log answer into array
                    answerKeeperArray[questionIndex][1] = theAnswer2;

                }


                if (answerCheck3.isChecked() && answerCheck3.getText().toString().equals(theAnswer3)) {
                    //log answer into array
                    answerKeeperArray[questionIndex][2] = theAnswer3;
                    Toast.makeText(this, R.string.answer_correct_c, Toast.LENGTH_SHORT).show();
                    manyCorrectAnswerKeeper++;
                }

                if (answerCheck3.isChecked()) {
                    // log answer into array
                    answerKeeperArray[questionIndex][2] = theAnswer3;
                }


                if (answerCheck4.isChecked() && answerCheck4.getText().toString().equals(theAnswer4)) {
                    //log answer into array
                    answerKeeperArray[questionIndex][3] = theAnswer4;
                    Toast.makeText(this, R.string.answer_correct_d, Toast.LENGTH_SHORT).show();
                    manyCorrectAnswerKeeper++;
                }

                if (answerCheck4.isChecked()) {
                    // log answer into array
                    answerKeeperArray[questionIndex][3] = theAnswer4;
                }


                //check to see if the total correct answers have been met. Since the total is stored in a string
                // we must cast the total to an integer to compare.
                int castStringTotalCorrectAnswers = Integer.parseInt(totalCorrectAnswers);
                if (manyCorrectAnswerKeeper == castStringTotalCorrectAnswers) {
                    Toast.makeText(this, R.string.answers_correct, Toast.LENGTH_SHORT).show();
                    totalScore = totalScore + 1;
                } else {
                    Toast.makeText(this, R.string.answer_try_again, Toast.LENGTH_SHORT).show();
                }
                break;

            // A fill in the blank or free style question starts here.
            case "FREE":

                selectedFreeAnswer = findViewById(R.id.editText_free_answer_one);
                //Check to see if the edit text box matches the answer. Convert everything to lowercase for ease of comparison.
                if (selectedFreeAnswer.getText().toString().toLowerCase().equals(theAnswer.toLowerCase())) {
                    Toast.makeText(this, R.string.the_answer_was_correct, Toast.LENGTH_LONG).show();

                    answerKeeperArray[questionIndex][0] = selectedFreeAnswer.getText().toString();
                    totalScore = totalScore + 1;
                } else {
                    Toast.makeText(this, R.string.answer_wrong, Toast.LENGTH_LONG).show();
                }
                break;
            default:
                Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();

        }
        increment(view);

    }


    /**
     * Increase the questionIndex to select a question based on the index number from the array.xml file
     */
    public void increment(View view) {


        //Check to see if the total questions answered have met the total length of the string array storing questions. If so, end the quiz by calling the endQuiz() method.
        questionIndex = questionIndex + 1;

        if (questionIndex == arrayLengthQuestions) {
            endQuiz(end);
        } else

        {
            selectQuestion();
        }
    }


    /**
     * Decrease the question index to go to the previous questions.
     */
    public void decrement(View view) {


        if (questionIndex <= 0) {
            // In case the index falls below zero display an error message.
            Toast.makeText(this, R.string.error_no_prior_questions, Toast.LENGTH_SHORT).show();

        } else {
            // Move backwards through the question index.
            questionIndex = questionIndex - 1;
            selectQuestion();
        }
    }

    /**
     * End of quiz. Total the scores and grade the quiz results then give the user an option to email the results to a pre set mailbox.
     */

    //TODO format all this junk better!!! The output is sloppy AF!
    public void endQuiz(View view) {
        Toast.makeText(this, R.string.end_you_finished, Toast.LENGTH_SHORT).show();


        intro.setVisibility(View.GONE);

        end.setVisibility(View.VISIBLE);
        many.setVisibility(View.GONE);
        free.setVisibility(View.GONE);
        multi.setVisibility(View.GONE);

        StringTokenizer splitQuestions;


        //Build a string from the contents in the answer keeper array
        StringBuilder strBuilder = new StringBuilder();

        //TODO streamline this.....sloppy
        for (int i = 0; i < arrayLengthQuestions; i++) {
            splitQuestions = new StringTokenizer((getQuestions[i]), "|");

            // Parse the array into some string variables with the tokenizer
            questionType = splitQuestions.nextToken();
            theQuestion = splitQuestions.nextToken();
            answerA = splitQuestions.nextToken();
            answerB = splitQuestions.nextToken();
            answerC = splitQuestions.nextToken();
            answerD = splitQuestions.nextToken();
            theAnswer = splitQuestions.nextToken();
            theAnswer2 = splitQuestions.nextToken();
            theAnswer3 = splitQuestions.nextToken();
            theAnswer4 = splitQuestions.nextToken();
            totalCorrectAnswers = splitQuestions.nextToken();

            // Append the question to the final output string
            strBuilder.append("\n \n" + theQuestion + "\n");

            // Roll through the array and append the recorded answers
            for (int j = 0; j < 4; j++) {
                if (answerKeeperArray[i][j] != null) {
                    strBuilder.append(getString(R.string.end_your_answers_were) + answerKeeperArray[i][j] + "\n");
                }

            }


        }
        //Check the string array for null answers. use "#" as a null field identifier only append the string if there is an additional correct answer.
        //TODO this isnt working exactly as intended. Fix it.

        strBuilder.append(getString(R.string.end_correct_answers_were));
        if (!theAnswer.equals("#")) {
            strBuilder.append(theAnswer + " & ");
        } else if (!theAnswer2.equals("#")) {
            strBuilder.append(theAnswer2 + " & ");
        } else if (!theAnswer3.equals("#")) {
            strBuilder.append(theAnswer3 + " & ");

        } else if (!theAnswer4.equals("#")) {
            strBuilder.append(theAnswer4 + "\n \n");

        }

        //Append the answers here

// Cast the string builder to a string and us it in the final message string
        String reviewMessage = strBuilder.toString();


        TextView endQuizReview = findViewById(R.id.textView_end_totals);
        TextView endQuizScore = findViewById(R.id.textView_score);
        TextView endQuizGrade = findViewById(R.id.textView_grade);

        // Calculate percentage of score and grade it
        String grade;
        String messageScore;
        int percent = (totalScore * 100) / arrayLengthQuestions;
        // Check percent and grade A thru F
        if (percent < 0 || percent > 100) {
            grade = getString(R.string.error_out_of_bounds);
        } else if (percent <= 59) {
            grade = getString(R.string.end_grade_f);
        } else if (percent >= 60) {
            grade = getString(R.string.end_grade_d);
        } else if (percent >= 70) {
            grade = getString(R.string.end_grade_c);
        } else if (percent >= 80) {
            grade = getString(R.string.end_grade_b);
        } else if (percent >= 90) {
            grade = getString(R.string.end_grade_a);
        } else {
            grade = getString(R.string.error_something_wrong);
        }

        // Form a message and display the results
        messageScore = getString(R.string.end_congrats) + userName.getText().toString() + getString(R.string.exclamation) + getString(R.string.end_you_made_it_to_the_end) + "\n" + getString(R.string.end_total_questions_righ) + totalScore + getString(R.string.end_out_of) + arrayLengthQuestions + "\n" + getString(R.string.end_that_is) + percent + getString(R.string.percent) + "\n \n \n";
        endQuizScore.setText(messageScore);
        endQuizGrade.setText(grade);
        endQuizReview.setText(getString(R.string.grade_theAnswersYouSelectedWere) + "\n \n \n" + reviewMessage.toString());

        emailSummary = messageScore + getString(R.string.end_overall_grade) + grade + "\n" + getString(R.string.grade_theAnswersYouSelectedWere) + "\n" + reviewMessage.toString();
    }


    /**
     * Clear all selections from view. Call this before loading a question.
     */

    public void clearSelections(View view) {


        RadioGroup rg = findViewById(R.id.radio_answers);
        if (rg != null) {
            rg.clearCheck();
        }

        if (answerCheck1 != null) {
            answerCheck1.setChecked(false);
        }

        if (answerCheck2 != null) {
            answerCheck2.setChecked(false);
        }
        if (answerCheck3 != null) {
            answerCheck3.setChecked(false);
        }

        if (answerCheck4 != null) {
            answerCheck4.setChecked(false);
        }

        if (selectedFreeAnswer != null) {
            selectedFreeAnswer.setText("");
        }


        if (!userName.getText().toString().equals(null)) {
            userName.setText("");
        }
    }

    public void sendMail(View view) {

        // Send summary via e mail.

        String mailTo = "mailto:" + emailTarget;

        Intent sendMail = new Intent(Intent.ACTION_SEND);
        sendMail.setData(Uri.parse(mailTo));
        sendMail.setType("*/*");

        sendMail.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_testResults));
        sendMail.putExtra(Intent.EXTRA_TEXT, emailSummary);
        if (sendMail.resolveActivity(getPackageManager()) != null) {
            startActivity(sendMail);
        }
    }


  public void hideKeyboard(){

      //Hide softkey
      InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
      //Hide:
      imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
  }


}




