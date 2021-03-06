package com.example.safetourbcn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PerfilUsuarioActivity extends AppCompatActivity {
    Session session = Session.getInstance();
    BackEndRequests ber = BackEndRequests.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        final TextView correoTextView = findViewById(R.id.correo);
        final TextView nombreTextView = findViewById(R.id.nombre);
        final TextView passwordTextView = findViewById(R.id.password);
        /* correo tiene que ser el correo del usuario actual*/

        String correo = session.getEmail();
        int N = correo.length();
        correoTextView.setText(correo.toCharArray(), 0, N   );
        /* usuario tiene que ser el nombre del usuario actual*/

        String usuario = session.getName();
        int M = usuario.length();
        nombreTextView.setText(usuario.toCharArray(), 0, M);

        String password = session.getPassword();
        int P = password.length();
        passwordTextView.setText(password.toCharArray(), 0, P);
    }
    public void editProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
}