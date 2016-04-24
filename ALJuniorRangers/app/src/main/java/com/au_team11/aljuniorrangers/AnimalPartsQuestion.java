package com.au_team11.aljuniorrangers;

import java.util.ArrayList;

/**
 * Created by JDSS on 4/22/16.
 */
public class AnimalPartsQuestion {

    String question;
    ArrayList<String> choices;
    int answer;

    public AnimalPartsQuestion(String newQuestion, ArrayList<String> newChoices, int newAnswer) {
        this.setQuestion(newQuestion);
        this.setChoices(newChoices);
        this.setAnswer(newAnswer);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}
