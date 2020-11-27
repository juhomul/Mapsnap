package com.example.group3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class LoginTabFragment extends Fragment {

    EditText username, password;
    Button loginButton;
    CheckBox checkBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        username = root.findViewById(R.id.usernameEdit);
        password = root.findViewById(R.id.passwordEdit);
        loginButton = root.findViewById(R.id.loginButton);
        checkBox = root.findViewById(R.id.stay_logged_in_checkbox);

        return root;
    }
}
