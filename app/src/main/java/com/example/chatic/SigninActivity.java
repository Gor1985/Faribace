package com.example.chatic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SigninActivity extends AppCompatActivity {
    private static  final String TAG ="SigninActivity ";
    private FirebaseAuth mAuth;
    private EditText emaileEditText;
    private  EditText passwordEditText;
    private EditText nameEditText;
    private EditText passwordDoubleEditText;
    Button loginButton;
    TextView toggleLogineTextView;
    private boolean loginModActive;
    FirebaseDatabase database;// создаем поле бд
    DatabaseReference users;// создаем


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        database = FirebaseDatabase.getInstance();// запись получает доступ ко всей бд
       users = database.getReference().child("users");// создаем узел для сообщений в консоль

        mAuth = FirebaseAuth.getInstance();// cвязываем активити с бд

        emaileEditText=findViewById(R.id.emaileEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        passwordDoubleEditText=findViewById(R.id.passwordDoubleEditText);
        nameEditText=findViewById(R.id.NameEditText);
        loginButton=findViewById(R.id.loginButton);
        toggleLogineTextView=findViewById(R.id.toggleLogineTextView);




        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSignUpUser(emaileEditText.getText().toString().trim(),passwordEditText.getText().toString().trim());
            }// тримом обрезаем пробелы в начале и конце. Передаем в него наши значения из едит текстов
        });
        if (mAuth.getCurrentUser()!=null){
           startActivity(new Intent(SigninActivity.this, UserListActivity.class));// если юзер
            // уже залогиненый(не равен нулю) то мы сразу идем на ман активити
        }
    }
    private void loginSignUpUser(String email,String password) {//вход по мылу и паролю
        if (loginModActive){// входим в чат
            if (passwordEditText.getText().toString().trim().length() < 6) {
                Toast.makeText(this, "Cлишком короткий пароль", Toast.LENGTH_SHORT).show();
            }else if (emaileEditText.getText().toString().trim().equals(
                    "")){
                Toast.makeText(this, "Введите ваш емайл", Toast.LENGTH_SHORT).show();

            }else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent=new Intent(SigninActivity.this, UserListActivity.class);
                                    intent.putExtra("userName",nameEditText.getText().toString().trim());
                                    startActivity(intent);// передаем имя
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(SigninActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    // updateUI(null);
                                }
                            }
                        });
            }

        }else {//создаем пользователя
            if (!passwordEditText.getText().toString().trim().equals(
                    passwordDoubleEditText.getText().toString().trim())) {
                Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            } else if (passwordEditText.getText().toString().trim().length() < 6) {
                Toast.makeText(this, "Cлишком короткий пароль", Toast.LENGTH_SHORT).show();
            }else if (emaileEditText.getText().toString().trim().equals(
                    "")){
                Toast.makeText(this, "Введите ваш емайл", Toast.LENGTH_SHORT).show();

        }else {
                mAuth.createUserWithEmailAndPassword(email, password)// слушаем фарибейсом мыло и пароль
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();//создаем юзера
                                    createUser(user);
                                    Intent intent=new Intent(SigninActivity.this, UserListActivity.class);
                                    intent.putExtra("userName",nameEditText.getText().toString().trim());
                                    startActivity(intent);// перходим на майн активити

                                    //   updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(SigninActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();


                                    //  updateUI(null);
                                }
                            }
                        });
            }
        }

    }

    private void createUser(FirebaseUser user) {
        User user1=new User();
        user1.setId(user.getUid());
        user1.setEmaile(user.getEmail());
        user1.setName(nameEditText.getText().toString().trim());
        users.push().setValue(user1);
    }

    public void toggleLoginMode(View view){

        if (loginModActive){
            loginModActive=false;
          loginButton.setText("Зарегистрироваться");
            toggleLogineTextView.setText("регистрация");
            passwordDoubleEditText.setVisibility(View.VISIBLE);
        }else{
            loginModActive=true;
            loginButton.setText("Авторизируйтесь");
            toggleLogineTextView.setText("Войти");
            passwordDoubleEditText.setVisibility(View.GONE);

        }

}
}
