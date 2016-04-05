package com.au_team11.aljuniorrangers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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
        TextView animalText, trailText, wordText;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        int num = preferences.getInt("ANIMAL", 0);
        animalText = (TextView) view.findViewById(R.id.animalPartsCompletion);
        animalText.append(num + "% Completed");

        num = preferences.getInt("TRAIL", 0);
        animalText = (TextView) view.findViewById(R.id.trailWalkCompletion);
        animalText.append(num + "% Completed");

        num = preferences.getInt("WORD", 0);
        animalText = (TextView) view.findViewById(R.id.wordSearchCompletion);
        animalText.append(num + "% Completed");

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
}
