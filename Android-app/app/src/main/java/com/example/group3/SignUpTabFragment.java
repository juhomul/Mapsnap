package com.example.group3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class SignUpTabFragment extends Fragment {

    EditText username, email, password, confirmPassword;
    Button signupButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.sign_up_tab_fragment, container, false);

        username = root.findViewById(R.id.username_signup);
        password = root.findViewById(R.id.password_signup);
        email = root.findViewById(R.id.email_signup);
        confirmPassword = root.findViewById(R.id.confirm_password_signup);
        signupButton = root.findViewById(R.id.signUpButton);

        return root;
    }
}
