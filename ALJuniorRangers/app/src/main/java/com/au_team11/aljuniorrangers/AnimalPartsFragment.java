package com.au_team11.aljuniorrangers;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by JDSS on 3/28/16.
 */
public class AnimalPartsFragment extends Fragment {

    Activity mCallback;
    Context context;

    SharedPreferences.Editor editor;

    View view;

    AnimalPartsView animalPartsView;

    String filename;

    Bitmap pic;

    //hitboxes, defined in percentage from left and top
    ArrayList<Rect> hitboxes;

    //quizButton starts the quiz
    //submitButton submits your answer in the quiz
    //exitQuizButton exits the quiz.
    //exitInfoButton exits the informational screen
    Button quizButton, submitButton, exitQuizButton, exitInfoButton;

    //importText is the textview for importing the information after hitbox clicks.
    //isCorrectText is the display after answering the quiz that informs the user of their results.
    TextView importText, isCorrectText, welcomeText;

    //Layout to hold the RadioButtons so that they can only be uniquely answered.
    RadioGroup quizLayout;

    TextView quizQuestion;

    //Quiz answer choice buttons
    RadioButton choice0, choice1, choice2, choice3;

    ArrayList<String> informationArray;

    int currentQuestion;
    ArrayList<AnimalPartsQuestion> questions;

    //number of correct answers by the user
    int correctAnswers;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (Activity) activity;
            context = activity.getApplicationContext();

        } catch (ClassCastException e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }

        //initialize shared preferences file for editing
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        view = inflater.inflate(R.layout.animalparts_layout, container, false);
        view.setBackgroundColor(0xFF606000);

        animalPartsView = (AnimalPartsView) view.findViewById(R.id.animalPartsView);

        importText = (TextView) view.findViewById(R.id.infoBox);

        welcomeText = (TextView) view.findViewById(R.id.welcomeText);

        animalPartsView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int infoArrayIndex = animalPartsView.isInBox((int) event.getX(), (int) event.getY(), hitboxes);

                if (infoArrayIndex > -1) {
                    importText.setText(informationArray.get(infoArrayIndex));
                    animalPartsView.setVisibility(View.GONE);
                    importText.setVisibility(View.VISIBLE);
                    quizButton.setVisibility(View.GONE);
                    exitInfoButton.setVisibility(View.VISIBLE);
                    welcomeText.setVisibility(View.GONE);

                    /*
                    Toast toast = Toast.makeText(
                            mCallback.getApplicationContext(),
                            "in hitbox " + Integer.toString(animalPartsView.isInBox((int) event.getX(), (int) event.getY(), hitboxes)),
                            Toast.LENGTH_SHORT);
                    toast.show();
                    */
                }
                return true;
            }
        });

        filename = getArguments().getString(getResources().getString(R.string.AssetBundleKey));

        String jsonData = loadJSONFromAsset(filename);

        String picName = getPicName(jsonData);

        int picID = context.getResources().getIdentifier(picName, "drawable", context.getPackageName());

        pic = BitmapFactory.decodeResource(mCallback.getApplicationContext().getResources(), picID);

        animalPartsView.setPic(pic);

        //also builds the info array
        hitboxes = buildHitboxes(jsonData);

        questions = buildQuestions(jsonData);

        currentQuestion = 0;

        isCorrectText = (TextView) view.findViewById(R.id.isCorrect);
        correctAnswers = 0;

        quizLayout = (RadioGroup) view.findViewById(R.id.fullquiz);

        //initialize the question text with the question in the first question
        quizQuestion = (TextView) view.findViewById(R.id.quizQuestion);

        //get the radio button views
        choice0 = (RadioButton) view.findViewById(R.id.answerA);
        choice1 = (RadioButton) view.findViewById(R.id.answerB);
        choice2 = (RadioButton) view.findViewById(R.id.answerC);
        choice3 = (RadioButton) view.findViewById(R.id.answerD);

        //initialize the question
        initializeQuestion();

        quizButton = (Button) view.findViewById(R.id.quizB);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizButton.setVisibility(View.GONE);
                animalPartsView.setVisibility(View.GONE);
                quizLayout.setVisibility(View.VISIBLE);
                quizQuestion.setVisibility(View.VISIBLE);
                welcomeText.setVisibility(View.INVISIBLE);
            }
        });
        submitButton = (Button) view.findViewById(R.id.submitAnswer);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //index of correct answer
                int answerIndex = questions.get(currentQuestion).getAnswer();
                //switch through choice cases based on correct answer value
                switch (answerIndex) {
                    case 0:
                        //if the correct choice is selected
                        if (choice0.isChecked()) {
                            isCorrectText.setText("Correct!");
                            isCorrectText.setVisibility(View.VISIBLE);
                            currentQuestion++;
                            correctAnswers++;
                            initializeQuestion();
                        }
                        //else an incorrect choice is selected
                        else {
                            isCorrectText.setText("Wrong.");
                            isCorrectText.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 1:
                        if (choice1.isChecked()) {
                            isCorrectText.setText("Correct!");
                            isCorrectText.setVisibility(View.VISIBLE);
                            currentQuestion++;
                            correctAnswers++;
                            initializeQuestion();
                        }
                        else {
                            isCorrectText.setText("Wrong.");
                            isCorrectText.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 2:
                        if (choice2.isChecked()) {
                            isCorrectText.setText("Correct!");
                            isCorrectText.setVisibility(View.VISIBLE);
                            currentQuestion++;
                            correctAnswers++;
                            initializeQuestion();
                        }
                        else {
                            isCorrectText.setText("Wrong.");
                            isCorrectText.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 3:
                        if (choice3.isChecked()) {
                            isCorrectText.setText("Correct!");
                            isCorrectText.setVisibility(View.VISIBLE);
                            currentQuestion++;
                            correctAnswers++;
                            initializeQuestion();
                        }
                        else {
                            isCorrectText.setText("Wrong.");
                            isCorrectText.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        break;
                }

                //if that was the last question, meaning they completed the quiz
                if (currentQuestion == questions.size()) {
                    //tell the progress report they completed the quiz
                    updateStatusReport();
                }
            }
        });

        exitQuizButton = (Button) view.findViewById(R.id.exitButton);
        exitQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizButton.setVisibility(View.VISIBLE);
                animalPartsView.setVisibility(View.VISIBLE);
                quizLayout.setVisibility(View.GONE);
                quizQuestion.setVisibility(View.GONE);
                welcomeText.setVisibility(View.VISIBLE);
            }
        });

        exitInfoButton = (Button) view.findViewById(R.id.infoExitButton);
        exitInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animalPartsView.setVisibility(View.VISIBLE);
                importText.setVisibility(View.GONE);
                quizButton.setVisibility(View.VISIBLE);
                exitInfoButton.setVisibility(View.GONE);
                welcomeText.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        pic.recycle();
    }

    //credit goes to GrlsHu on StackOverflow
    //returns a json string from the asset file
    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String getPicName(String jsonData) {
        try {
            return new JSONObject(jsonData).getString("image");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> buildInfoArray(String jsonData) {

        ArrayList<String> newInfoArray = new ArrayList<String>();

        try {
            //get the root json object
            JSONObject jsonObject = new JSONObject(jsonData);
            //get the information json array
            JSONArray infoArray = jsonObject.getJSONArray("information");

            //for all elements in the json info array
            for (int i = 0; i < infoArray.length(); i++) {
                //get the object at this index
                JSONObject arrayObject = infoArray.getJSONObject(i);
                //add that string to the new info array
                newInfoArray.add(arrayObject.getString("info"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return newInfoArray;
    }

    public ArrayList<Rect> buildHitboxes(String jsonData) {
        try {
            //build the hitboxes
            ArrayList<Rect> newHitboxes = new ArrayList<Rect>();
            //build the info array
            informationArray = new ArrayList<String>();

            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("hitboxes");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject arrayObject = jsonArray.getJSONObject(i);
                //add a new rect to the list
                newHitboxes.add(
                        new Rect(
                                arrayObject.getInt("left"),
                                arrayObject.getInt("top"),
                                arrayObject.getInt("right"),
                                arrayObject.getInt("bottom")));

                //add the new info to the info array
                informationArray.add(arrayObject.getString("info"));
            }

            return newHitboxes;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<AnimalPartsQuestion> buildQuestions(String jsonData) {

        //create a list of new questions
        ArrayList<AnimalPartsQuestion> newQuestions = new ArrayList<AnimalPartsQuestion>();

        try {
            //build root json object
            JSONObject jsonObject = new JSONObject(jsonData);
            //get the quiz json array from the json object
            JSONArray jsonArray = jsonObject.getJSONArray("quiz");

            //for each object in the quiz
            for (int i = 0; i < jsonArray.length(); i++) {
                //get the json object at this index
                JSONObject arrayObject = jsonArray.getJSONObject(i);

                //make temporary storage for the list of answer choices
                ArrayList<String> choices = new ArrayList<String>();

                //get the array of choices
                JSONArray jsonChoiceArray = arrayObject.getJSONArray("choices");
                //for each object in the array of choices
                for (int j = 0; j < jsonChoiceArray.length(); j++) {
                    //add a new string to the answer choice list
                    choices.add(
                            jsonChoiceArray
                                    .getJSONObject(j)
                                    .getString("choice"));
                }

                //add the new question to the arraylist
                newQuestions.add(
                        new AnimalPartsQuestion(
                                arrayObject.getString("question"),
                                choices,
                                arrayObject.getInt("answer")
                        ));
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return newQuestions;

    }

    public void initializeQuestion() {
        if (currentQuestion < questions.size()) {
            quizQuestion.setText(questions.get(currentQuestion).getQuestion());
            choice0.setText(questions.get(currentQuestion).getChoices().get(0));
            choice1.setText(questions.get(currentQuestion).getChoices().get(1));
            choice2.setText(questions.get(currentQuestion).getChoices().get(2));
            choice3.setText(questions.get(currentQuestion).getChoices().get(3));
        }
        else {
            //completed last question, set isCorrectText to say so
            isCorrectText.setText("Quiz Completed!");
            //make the submit button invisible
            submitButton.setVisibility(View.INVISIBLE);
            //do quiz completion recording here
        }
    }

    public void updateStatusReport() {
        //Dannial do your stuff here
        editor.putBoolean(filename, true);
        editor.commit();
    }
}
