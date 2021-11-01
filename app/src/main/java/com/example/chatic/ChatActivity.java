package com.example.chatic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private ListView messageListView;
    private ArrayAdaterss adapter;
    private ProgressBar progressBar;
    private ImageButton sendImageButton;
    private Button setMesageButton;
    private EditText editText;
    private String username;
    private String recepientUserId;
    private static final int RC=1234;

    FirebaseDatabase database;// создаем поле бд
    DatabaseReference main;// создаем поле для хранения сообщений
    ChildEventListener childEventListenerMessage;// со
    DatabaseReference mainUser;// создаем поле для хранения сообщений
    ChildEventListener childEventListenerMessageUser;// создаем поле для слушателя нашего корневого узла в сообщении
    FirebaseStorage storage;// создаем переменную для доступа к хранилищу фалов в фарибейс
    StorageReference childImagesStorageReference;// создаем поле для хранения картинок
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent=getIntent();
        recepientUserId=intent.getStringExtra("recepientUserId");

        if (intent!=null){
            username=intent.getStringExtra("userName");
        }else {
            username = "DeafolteUser";

        }

      storage=FirebaseStorage.getInstance();// соединяем пеерменную с базой
      childImagesStorageReference=storage.getReference().child("chat_imigies");// создаем узел для хранения данных

        mAuth=FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();// запись получает доступ ко всей бд
        main = database.getReference().child("messages");// создаем узел для сообщений в консоль
         mainUser=database.getReference().child("users");





        progressBar = findViewById(R.id.progressBar);
        sendImageButton = findViewById(R.id.fotoSendButton);
        setMesageButton = findViewById(R.id.sendMesageButton);
        editText = findViewById(R.id.mesageEditText);

        messageListView = findViewById(R.id.MesageListView);
        List<AwesomeMessage> mesagies = new ArrayList<>();
        adapter = new ArrayAdaterss(this, R.layout.list_view, mesagies);
        messageListView.setAdapter(adapter);

        progressBar.setVisibility(ProgressBar.INVISIBLE);
        editText.addTextChangedListener(new TextWatcher() {// устанавливаем слушатель для нашего эдит текста
            // нам надо прослушать состояние кнопки при отправке
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().trim().length() > 0) {// если количкество символов в строке больше нуля то включаем кнопку
                    setMesageButton.setEnabled(true);
                } else {
                    setMesageButton.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText.setFilters(new InputFilter[]{// ограничиваем длинну сообщений
                new InputFilter.LengthFilter(500)
        });

        setMesageButton.setOnClickListener((v) -> {
            AwesomeMessage message = new AwesomeMessage();// создаем обьект нашего класса авесоме
            message.setText(editText.getText().toString());// передаем туда данные
            message.setName(username);// передаем дефолтное имя
            message.setImageUrl(null);// передаем ничего так как для картинок у нас другая кнопка
            message.setSender(mAuth.getCurrentUser().getUid());// отправляем текущего юзера(отправитель)
            message.setRecepient(recepientUserId);

            main.push().setValue(message);// расшариваем наш обьект в консоль
            // вручную не назначаем узел узел назначает пуш что бы не было конфликтов когда юзеры пишут одновременно в чат
            editText.setText("");// очищаем едит текст при отправке
        });

        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          Intent intent=new Intent(Intent.ACTION_GET_CONTENT);// СОЗДАЕМ ИНТЕНТ ДЛЯ ПОЛУЧЕНИЯ КОНТЕНТА
                intent.setType("image/*");// указываем тип данных которые будем получать
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);// тут мы указываем откда будем брать данные(в данный момент с телефона)
                startActivityForResult(Intent.createChooser(intent, "Выберите изображение"),RC);
                // появляется активити из которой можно выбирать изображения
            }
        });
        childEventListenerMessageUser=new ChildEventListener() {// создаем слушатель для прослушивания айди юзера
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
             User user=snapshot.getValue(User.class);// вытаскиваем данные из юзера
                if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()));
                    // если айди юзера интентично текущему пользователю
                username=user.getName();
                // тогда мы переменной юзернейм присваиваем текущее имя

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };mainUser.addChildEventListener(childEventListenerMessageUser);

        childEventListenerMessage=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                AwesomeMessage mesage = snapshot.getValue(AwesomeMessage.class);// читаем все данные из класса
                if (mesage.getSender().equals(mAuth.getCurrentUser().getUid())
                        && mesage.getRecepient().equals(recepientUserId)) {
                    mesage.setMane(true);// если мы отправляем
                    adapter.add(mesage);// загружаем в адаптер
                    // если отправитель идентичен отправителю из бд и пролучатель тогда загружаем в адаптер
                    // и получаем сообщение от пользователя
                }else if (mesage.getRecepient().equals(mAuth.getCurrentUser().getUid())
                        && mesage.getSender().equals(recepientUserId))
                    mesage.setMane(false);// если мы принимаем

                    adapter.add(mesage);// загружаем в адаптер
                }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        main.addChildEventListener(childEventListenerMessage);// прикрпялем наш сулшатель к сообщениям из бд

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sign_out1:
                FirebaseAuth.getInstance().signOut();// выход из приложения
                startActivity(new Intent(ChatActivity.this,SigninActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {// принимаем данные из телефона со старт активити результ
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC && resultCode==RESULT_OK){
            // если рекуест код равен нашей ностанте и ресульт код =окей
            Uri selectImageUri=data.getData();// тут мы получаем путь к папке
            // потом надо имплементировать стораже
          final StorageReference imageReference=childImagesStorageReference.child(selectImageUri.getLastPathSegment());
           // создаем новую переменную узда данных и запиписываем в нее данные из телефона по ссылке из ури
            // получаем данные из папки последнего сегмента ури
            UploadTask uploadTask=imageReference.putFile(selectImageUri);

            uploadTask = imageReference.putFile(selectImageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return imageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();// получаем ури для помещение в бд
                        AwesomeMessage message=new AwesomeMessage();
                        message.setImageUrl(downloadUri.toString());
                        message.setName(username);// подлписываем  картинку
                        message.setSender(mAuth.getCurrentUser().getUid());// отправляем текущего юзера(отправитель)
                        message.setRecepient(recepientUserId);// добавляем в бд юсеров
                        main.push().setValue(message);//распушиваем сообщение в бд
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }
}