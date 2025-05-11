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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.learningexpapp.DatabaseHelper;
import com.example.learningexpapp.HomeScreen;
import com.example.learningexpapp.models.User;
import com.google.android.material.button.MaterialButton;
import com.example.learningexpapp.R;

public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    EditText usernameView;
    EditText passwordView;
    TextView instructionView;
    MaterialButton loginButton;
    MaterialButton registerButton;
    CheckBox rememberMeCheck;
    private DatabaseHelper database;
    SharedPreferences sharedPref;
    String IS_LOGGED_IN;
    String CURRENT_USER;


    // TODO: Rename and change types of parameters


    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("MAIN_LOG", "In login fragment.");
        instructionView = this.getActivity().findViewById(R.id.login_instruction);
        loginButton = view.findViewById(R.id.login_button);
        registerButton = view.findViewById(R.id.register_button);
        usernameView = view.findViewById(R.id.username);
        passwordView = view.findViewById(R.id.password);
        rememberMeCheck = view.findViewById(R.id.remember_me);
        sharedPref = this.getActivity().getSharedPreferences("com.example.quiz", Context.MODE_PRIVATE);

        database = new DatabaseHelper(this.getActivity());

        instructionView.setText("Please login with your information");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MAIN_LOG", "clicked login");
                User currentUser;
                String username = "";
                String password = "";
                try {
                    username = usernameView.getText().toString();
                    password = passwordView.getText().toString();
                    currentUser = database.login(username,password);
                } catch (Exception e) {
                    currentUser = new User();
                    Log.i("MAIN_LOG", "Exception: "+e);
                }

                if (currentUser.getID() == -1) {
                    Toast.makeText(getActivity(), "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    if (rememberMeCheck.isChecked()) {
                        editor.putBoolean("IS_LOGGED_IN", true);
                    }
                    editor.putString("CURRENT_USERNAME", currentUser.getUsername());
                    editor.putString("CURRENT_PASSWORD", currentUser.getPassword());
                    editor.putString("CURRENT_USER", currentUser.getFullname());
                    editor.apply();
                    Toast.makeText(getActivity(), "Login successfully. Welcome "+username, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginFragment.this.getActivity(), HomeScreen.class);
                    startActivity(intent);
                }

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MAIN_LOG", "clicked register");
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                RegisterFragment registerFragment = new RegisterFragment();
                fragmentTransaction.replace(R.id.signin_fragment, registerFragment, null)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
