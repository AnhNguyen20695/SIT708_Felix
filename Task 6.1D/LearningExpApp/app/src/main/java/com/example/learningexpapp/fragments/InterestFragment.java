package com.example.learningexpapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.learningexpapp.DatabaseHelper;
import com.example.learningexpapp.R;
import com.example.learningexpapp.RecommendTopicService;
import com.example.learningexpapp.models.User;
import com.google.android.material.button.MaterialButton;
import com.example.learningexpapp.other.Utils;

import org.json.JSONException;
import org.json.JSONObject;


public class InterestFragment extends Fragment {
    private DatabaseHelper database;
    TextView instructionView;
    JSONObject initialRecords = new JSONObject();
    CheckBox topic1Box;
    CheckBox topic2Box;
    CheckBox topic3Box;
    CheckBox topic4Box;
    CheckBox topic5Box;
    CheckBox topic6Box;
    CheckBox topic7Box;
    CheckBox topic8Box;
    CheckBox topic9Box;
    CheckBox topic10Box;
    CheckBox topic11Box;
    CheckBox topic12Box;
    CheckBox topic13Box;
    CheckBox topic14Box;
    CheckBox topic15Box;
    CheckBox topic16Box;

    MaterialButton registerInterestButton;
//    MaterialButton registerInterestLaterButton;
    SharedPreferences sharedPref;

    public InterestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InterestFragment newInstance(String param1, String param2) {
        InterestFragment fragment = new InterestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_interests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i("MAIN_LOG","Registering interests.");
        database = new DatabaseHelper(this.getActivity());
        sharedPref = this.getActivity().getSharedPreferences("com.example.quiz", Context.MODE_PRIVATE);
        String current_username = sharedPref.getString("CURRENT_USERNAME", "");
        String current_userpassword = sharedPref.getString("CURRENT_USERPASSWORD", "");
        instructionView = this.getActivity().findViewById(R.id.login_instruction);
        instructionView.setText("You may select up to 10 topics:");

        registerInterestButton = view.findViewById(R.id.register_interest);
        registerInterestButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                database.registerInterests(current_username, initialRecords.toString());
                Toast.makeText(getActivity(), "Successfully registered the interests for user.", Toast.LENGTH_SHORT).show();
                Log.i("MAIN_LOG","Initial records has been inserted.");

                // Call the recommend topic API in the background
                User currentUser = database.login(current_username, current_userpassword);
                String recommendQuizParameters = Utils.generateRecommendedTopicURLParams(currentUser.getRecords());
                Intent intent = new Intent(getActivity(), RecommendTopicService.class);
                intent.putExtra("RECOMMENDED_QUIZ_PARAMETERS",recommendQuizParameters);
                intent.putExtra("CURRENT_USERNAME",current_username);
                getActivity().startService(intent);

                // Switch back to login screen
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.signin_fragment, LoginFragment.class, null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Topic options
        topic1Box = view.findViewById(R.id.interest_1);
        setTopicOnChange(topic1Box, getString(R.string.topic_1));
        topic2Box = view.findViewById(R.id.interest_2);
        setTopicOnChange(topic2Box, getString(R.string.topic_2));
        topic3Box = view.findViewById(R.id.interest_3);
        setTopicOnChange(topic3Box, getString(R.string.topic_3));
        topic4Box = view.findViewById(R.id.interest_4);
        setTopicOnChange(topic4Box, getString(R.string.topic_4));
        topic5Box = view.findViewById(R.id.interest_5);
        setTopicOnChange(topic5Box, getString(R.string.topic_5));
        topic6Box = view.findViewById(R.id.interest_6);
        setTopicOnChange(topic6Box, getString(R.string.topic_6));
        topic7Box = view.findViewById(R.id.interest_7);
        setTopicOnChange(topic7Box, getString(R.string.topic_7));
        topic8Box = view.findViewById(R.id.interest_8);
        setTopicOnChange(topic8Box, getString(R.string.topic_8));
        topic9Box = view.findViewById(R.id.interest_9);
        setTopicOnChange(topic9Box, getString(R.string.topic_9));
        topic10Box = view.findViewById(R.id.interest_10);
        setTopicOnChange(topic10Box, getString(R.string.topic_10));
        topic11Box = view.findViewById(R.id.interest_11);
        setTopicOnChange(topic11Box, getString(R.string.topic_11));
        topic12Box = view.findViewById(R.id.interest_12);
        setTopicOnChange(topic12Box, getString(R.string.topic_12));
        topic13Box = view.findViewById(R.id.interest_13);
        setTopicOnChange(topic13Box, getString(R.string.topic_13));
        topic14Box = view.findViewById(R.id.interest_14);
        setTopicOnChange(topic14Box, getString(R.string.topic_14));
        topic15Box = view.findViewById(R.id.interest_15);
        setTopicOnChange(topic15Box, getString(R.string.topic_15));
        topic16Box = view.findViewById(R.id.interest_16);
        setTopicOnChange(topic16Box, getString(R.string.topic_16));
    }

    public void setTopicOnChange(CheckBox topicBox, String boxString) {
        topicBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.i("MAIN_LOG","isChecked. Inserting "+boxString+" into initialRecords.");
                    try {
                        initialRecords.put(boxString, 0);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    Log.i("MAIN_LOG","Initial Records: "+initialRecords.toString());
                } else {
                    Log.i("MAIN_LOG","not Checked. Removing "+boxString+" into initialRecords.");
                    initialRecords.remove(boxString);
                    Log.i("MAIN_LOG","Initial Records: "+initialRecords.toString());
                }
            }
        });
    }
}
