package com.au_team11.aljuniorrangers;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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

    View view;

    AnimalPartsView animalPartsView;

    String filename;

    Bitmap pic;

    //Strings for the information to show when each hitbox is clicked.
    ArrayList<String> informationArray, quizArray;

    //hitboxes, defined in percentage from left and top
    ArrayList<Rect> hitboxes;

    //quizButton starts the quiz
    //submitButton submits your answer in the quiz
    //exitQuizButton exits the quiz.
    //exitInfoButton exits the informational screen
    Button quizButton, submitButton, exitQuizButton, exitInfoButton;

    //importText is the textviewfor importing the information after hitbox clicks.
    //isCorrectText is the display after answering the quiz that informs the user of their results.
    TextView importText, isCorrectText;

    //Layout to hold the RadioButtons so that they can only be uniquely answered.
    RadioGroup quizLayout;

    TextView quizQuestion;

    //Quiz answer buttons
    RadioButton answer1, answer2, answer3, answer4;


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

        view = inflater.inflate(R.layout.animalparts_layout, container, false);

        animalPartsView = (AnimalPartsView) view.findViewById(R.id.animalPartsView);

        importText = (TextView) view.findViewById(R.id.infoBox);

        informationArray = new ArrayList<String>();
        quizArray = new ArrayList<String>();

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

                    Toast toast = Toast.makeText(
                            mCallback.getApplicationContext(),
                            "in hitbox " + Integer.toString(animalPartsView.isInBox((int) event.getX(), (int) event.getY(), hitboxes)),
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
                return true;
            }
        });

        filename = getArguments().getString(getResources().getString(R.string.AssetBundleKey));

        String jsonData = loadJSONFromAsset(filename);

        String picName = getPicName(jsonData);
        Log.i("AnimalParts", picName);

        int picID = context.getResources().getIdentifier(picName, "drawable", context.getPackageName());

        pic = BitmapFactory.decodeResource(mCallback.getApplicationContext().getResources(), picID);

        animalPartsView.setPic(pic);

        hitboxes = buildHitboxes(jsonData);

        /*
        hitboxes = new ArrayList<Rect>();
        hitboxes.add(new Rect(0,0,25,25));
        hitboxes.add(new Rect(25,25,50,50));
        hitboxes.add(new Rect(50,50,75,75));
        hitboxes.add(new Rect(75,75,100,100));
        */


        isCorrectText = (TextView) view.findViewById(R.id.isCorrect);

        quizLayout = (RadioGroup) view.findViewById(R.id.fullquiz);

        quizQuestion = (TextView) view.findViewById(R.id.quizQuestion);
        quizQuestion.setText(quizArray.get(0));

        answer1 = (RadioButton) view.findViewById(R.id.answerA);
        answer1.setText(quizArray.get(1));

        answer2 = (RadioButton) view.findViewById(R.id.answerB);
        answer2.setText(quizArray.get(2));

        answer3 = (RadioButton) view.findViewById(R.id.answerC);
        answer3.setText(quizArray.get(3));

        answer4 = (RadioButton) view.findViewById(R.id.answerD);
        answer4.setText(quizArray.get(4));

        quizButton = (Button) view.findViewById(R.id.quizB);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizButton.setVisibility(quizButton.GONE);
                animalPartsView.setVisibility(animalPartsView.GONE);
                quizLayout.setVisibility(quizLayout.VISIBLE);
                quizQuestion.setVisibility(quizQuestion.VISIBLE);

            }
        });
        submitButton = (Button) view.findViewById(R.id.submitAnswer);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gradeQuiz()) {
                    //Change Text of textview
                    isCorrectText.setText("Good Job!");
                    isCorrectText.setVisibility(isCorrectText.VISIBLE);
                    //sendCorrectData();
                }
                else {
                    //Change Text of textview
                    isCorrectText.setVisibility(isCorrectText.VISIBLE);
                }
            }
        });

        exitQuizButton = (Button) view.findViewById(R.id.exitButton);
        exitQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizButton.setVisibility(quizButton.VISIBLE);
                animalPartsView.setVisibility(animalPartsView.VISIBLE);
                quizLayout.setVisibility(quizLayout.GONE);
                quizQuestion.setVisibility(quizQuestion.GONE);
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
            }
        });

        return view;
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

    public ArrayList<Rect> buildHitboxes(String jsonData) {
        try {
            ArrayList<Rect> newHitboxes = new ArrayList<Rect>();
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
            }
            JSONArray jsonStringArray = jsonObject.getJSONArray("information");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject arrayObject = jsonStringArray.getJSONObject(i);
                informationArray.add(arrayObject.getString("info"));
            }
            JSONArray jsonQuestionArray = jsonObject.getJSONArray("quiz");
            for (int i = 0; i < jsonQuestionArray.length(); i++) {
                JSONObject arrayObject = jsonQuestionArray.getJSONObject(i);
                quizArray.add(arrayObject.getString("question"));
            }
            Log.i("quizarray", "" + quizArray.size());
            return newHitboxes;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //grades the quiz
    public boolean gradeQuiz() {
        int answerCorrect;
        int radioButtonSelected;
        answerCorrect = 1;
        radioButtonSelected = 0;
        if (answer1.isChecked()) {
            radioButtonSelected = 1;
        }
        if (answer2.isChecked()){
            radioButtonSelected = 2;
        }
        if (answer3.isChecked()){
            radioButtonSelected = 3;
        }
        if (answer4.isChecked()){
            radioButtonSelected = 4;
        }

        if(answerCorrect == radioButtonSelected){
            return true;
        }
        else{
            return false;
        }
    }
}
