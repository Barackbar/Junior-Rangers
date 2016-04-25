package com.au_team11.aljuniorrangers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProgressReportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProgressReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressReportFragment extends Fragment
{
    private static Context context;
    private OnFragmentInteractionListener mListener;
    private SharedPreferences preferences;

    public ProgressReportFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProgressReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgressReportFragment newInstance(String param1, String param2)
    {
        ProgressReportFragment fragment = new ProgressReportFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_progress_report, container, false);
        TextView animalText;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String parkJSON = loadJSONFromAsset(getArguments().getString(getResources().getString(R.string.AssetBundleKey)));
        ArrayList<String> activityNameList = new ArrayList<String>();
        ArrayList<String> activityKeyList = new ArrayList<String>();
        try
        {
            JSONArray parkJSONArray = new JSONObject(parkJSON).getJSONArray("activities");
            for (int i = 0; i < parkJSONArray.length(); i++)
            {
                activityNameList.add(parkJSONArray.getJSONObject(i).getString("name"));
                activityKeyList.add(parkJSONArray.getJSONObject(i).getString("filename"));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        String progressText = "";
        ArrayList<String> activityList = new ArrayList<String>();
        int completed = 0;
        int incomplete = 0;
        for (int i = 0; i < activityNameList.size(); i++)
        {
            //add whether the activity is completed or not
            Boolean isComplete = preferences.getBoolean(activityKeyList.get(i), false);
            progressText += activityNameList.get(i) + ":\t\t";
            if (isComplete)
            {
                progressText += "Complete!\n";
                completed++;
            } else
            {
                progressText += "X\n";
                incomplete++;
            }
        }

        animalText = (TextView) view.findViewById(R.id.animalPartsCompletion);
        animalText.append(progressText);

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.progress);
        ProgressBar mProgress = (ProgressBar) view.findViewById(R.id.progressbar);
        int percentCompleted = Math.round(100 * completed / (incomplete + completed));
        mProgress.setProgress(percentCompleted);
        mProgress.setMax(100);
        mProgress.setProgressDrawable(drawable);

        TextView progText = (TextView) view.findViewById(R.id.progresstext);
        progText.append(percentCompleted + "%");

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        context = activity.getApplicationContext();
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

}
