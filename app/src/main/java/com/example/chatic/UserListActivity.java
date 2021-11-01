package com.example.chatic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {
    private String userName;


    private FirebaseAuth mAuth;

    private DatabaseReference usersDataBaseReference;
    private ChildEventListener usersChildEventListener;
    private ArrayList<User> userArrayList;
    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;
    private RecyclerView.LayoutManager userLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        Intent intent=getIntent();
        if (intent!=null) {
            userName = intent.getStringExtra(userName);// инициализируем переменную из предыдущего активити
        }



        mAuth=FirebaseAuth.getInstance();
        userArrayList=new ArrayList<>();

        buildReyckerView();
        attachUserDatabaceReferenceUser();

        }


    private void attachUserDatabaceReferenceUser() {
        usersDataBaseReference= FirebaseDatabase.getInstance().getReference().child("users");
        if (usersChildEventListener==null){
            usersChildEventListener=new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    User user = snapshot.getValue(User.class);
                    if (!user.getId().equals(mAuth.getCurrentUser().getUid())) {// ЕСЛИ
                        // ПОЛЬЗВАТЕЛЬ НЕ СОВПАДАЕТ С ТЕКЩИМ ПОЛЬЗОВАТЕЛЕМ ТОГДА ДОБАВЛЯЕМ ЕГО В СПИСОК


                        user.setAvatarMockUpResource(R.drawable.ic_baseline_person_24);
                        userArrayList.add(user);
                        userAdapter.notifyDataSetChanged();
                    }
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
        }usersDataBaseReference.addChildEventListener(usersChildEventListener);
    }


    private void buildReyckerView() {


        userRecyclerView=findViewById(R.id.recyclerUserList);
        userRecyclerView.setHasFixedSize(true);
        userLayoutManager=new LinearLayoutManager(this);
        userAdapter=new UserAdapter(userArrayList);
        userRecyclerView.addItemDecoration(new DividerItemDecoration(
                userRecyclerView.getContext(), DividerItemDecoration.VERTICAL));// разделитель между
        // нашими юзерами
        userRecyclerView.setLayoutManager(userLayoutManager);
        userRecyclerView.setAdapter(userAdapter);
        userAdapter.setOnUserCkickListener(new UserAdapter.OnUserCkickListener() {
            @Override
            public void nUserCkick(int position) {
                goToChat(position);// отправляем позицию
// при кликанье по айтему бы будем извлекать из аррайлиста той же позиции айди юзера
            }
        });


    }

    private void goToChat(int position) {// принимаем позицию
        Intent intent=new Intent(UserListActivity.this,ChatActivity.class);
        intent.putExtra("recepientUserId",userArrayList.get(position).getId());// смотри ид по позиции
        intent.putExtra("userName",userName);// смотри ид по позиции

        startActivity(intent);
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
                startActivity(new Intent(UserListActivity.this,SigninActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}