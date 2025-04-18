package com.example.itubev3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    EditText usernameView;
    EditText passwordView;
    EditText fullnameView;
    EditText confirmPasswordView;
    TextView instructionView;
    MaterialButton signupButton;
    private DatabaseHelper database;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public RegisterFragment() {
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
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i("MAIN_LOG", "In login fragment.");
        instructionView = this.getActivity().findViewById(R.id.login_instruction);
        signupButton = view.findViewById(R.id.signup_button);
        fullnameView = view.findViewById(R.id.register_fullname);
        confirmPasswordView = view.findViewById(R.id.register_confirm_password);
        usernameView = view.findViewById(R.id.register_username);
        passwordView = view.findViewById(R.id.register_password);

        database = new DatabaseHelper(this.getActivity());

        instructionView.setText("Please register your information.");

        signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String registerResult = "fail";
                String username = usernameView.getText().toString();
                String password = passwordView.getText().toString();
                String fullname = fullnameView.getText().toString();
                String confirmPassword = confirmPasswordView.getText().toString();

                if (password.equals(confirmPassword)) {

                    Log.i("MAIN_LOG", "Registering with username: " + username + " | password: " + password);
                    try {
                        registerResult = database.register(fullname, username, password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (registerResult.equals("success")) {
                        Toast.makeText(getActivity(), "Successfully registered the user.", Toast.LENGTH_SHORT).show();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        LoginFragment loginFragment = new LoginFragment();
                        fragmentTransaction.replace(R.id.signin_fragment, loginFragment, null)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        Toast.makeText(getActivity(), "Failed to register with the given username. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Confirm password does not match with your input password. Please try again.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}