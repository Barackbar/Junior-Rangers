package com.au_team11.aljuniorrangers;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Jon on 2/14/2016.
 */
public class infoFragment extends Fragment{

    Button quizButton, submitButton, exitQuizButton;
    private String metflightCode = null;
    View currentImage;
    TextView importText, isCorrectText;
    RadioGroup quizLayout;
    RadioButton answer1, answer2, answer3, answer4;
    ImageView pictureImage, hitbox1, hitbox2, hitbox3;


    public infoFragment() {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("infoFragment", "super.onCreateView");
        super.onCreateView(inflater, container, savedInstanceState);
        Log.i("infoFragment", "onCreateView");
        View view = inflater.inflate(R.layout.info_view, container, false);


        importText = (TextView) view.findViewById(R.id.infoBox);
        isCorrectText = (TextView) view.findViewById(R.id.isCorrect);

        currentImage = (ImageView) view.findViewById(R.id.image);
        pictureImage = (ImageView) view.findViewById(R.id.image);
        if (pictureImage != null){
            pictureImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent ev) {
                    boolean handledHere = false;

                    final int action = ev.getAction();

                    final int evX = (int) ev.getX();
                    final int evY = (int) ev.getY();
                    Toast.makeText(getActivity(), "You Touched it!", Toast.LENGTH_LONG).show();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            int touchColor = getHotspotColor(R.id.image_areas, evX, evY, v);
                            int tolerance = 25;

                            if (closeMatch(Color.BLUE, touchColor, tolerance)) {
                                currentImage.setVisibility(View.GONE);
                                importText.setVisibility(View.VISIBLE);
                                importText.setText("This is a test.");
                                //set which text you want from asset
                            } else if (closeMatch(Color.GREEN, touchColor, tolerance)) {
                                currentImage.setVisibility(View.GONE);
                                importText.setVisibility(View.VISIBLE);
                                //set which text you want from asset
                            } else if (closeMatch(Color.RED, touchColor, tolerance)) {
                                currentImage.setVisibility(View.GONE);
                                importText.setVisibility(View.VISIBLE);
                                //set which text you want from asset
                            }

                            handledHere = true;
                            break;
                        default:
                            handledHere = false;
                    } // end switch
                    Toast.makeText(getActivity(), "We made it team" + handledHere, Toast.LENGTH_SHORT).show();
                    return handledHere;
                }
            });
        }


        pictureImage.setImageResource(R.drawable.deerimage);
        hitbox1 = (ImageView) view.findViewById(R.id.image_areas);
        hitbox1.setBackgroundColor(0xff0000ff);

        hitbox2 = (ImageView) view.findViewById(R.id.image_areas);
        hitbox3 = (ImageView) view.findViewById(R.id.image_areas);

        quizLayout = (RadioGroup) view.findViewById(R.id.fullquiz);
        answer1 = (RadioButton) view.findViewById(R.id.answerA);
        answer2 = (RadioButton) view.findViewById(R.id.answerB);
        answer3 = (RadioButton) view.findViewById(R.id.answerC);
        answer4 = (RadioButton) view.findViewById(R.id.answerD);

        quizButton = (Button) view.findViewById(R.id.quizB);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizButton.setVisibility(quizButton.GONE);
                pictureImage.setVisibility(pictureImage.GONE);
                quizLayout.setVisibility(quizLayout.VISIBLE);
                hitbox1.setVisibility(hitbox1.GONE);

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
                pictureImage.setVisibility(pictureImage.VISIBLE);
                quizLayout.setVisibility(quizLayout.GONE);
                hitbox1.setVisibility(hitbox1.VISIBLE);
            }
        });
        return view;
    }

    /**
     * Get the color from the hotspot image at point x-y.
     *
     */

    public int getHotspotColor(int hotspotId, int x, int y, View v) {
        ImageView img = (ImageView) v.findViewById(hotspotId);
        //img.setDrawingCacheEnabled(true);
        //Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
        //img.setDrawingCacheEnabled(false);
        return 1;
    }


    public boolean closeMatch(int color1, int color2, int tolerance) {
        if ((int) Math.abs(Color.red(color1) - Color.red(color2)) > tolerance) return false;
        if ((int) Math.abs(Color.green(color1) - Color.green(color2)) > tolerance) return false;
        if ((int) Math.abs(Color.blue(color1) - Color.blue(color2)) > tolerance) return false;
        return true;
    }


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

    public void sendCorrectData(){
        //send information back to main activity indicating correct completion
    }
}
