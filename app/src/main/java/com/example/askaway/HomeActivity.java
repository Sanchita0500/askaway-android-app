package com.example.askaway;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.askaway.Functionalities.AnswerActivity;
import com.example.askaway.Functionalities.AskActivity;
import com.example.askaway.Functionalities.DetailFeed;
import com.example.askaway.Functionalities.ProfileActivity;
import com.example.askaway.Functionalities.SearchAdapter;
import com.example.askaway.Objects.Feed;
import com.example.askaway.Objects.Prevalant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ActionBarDrawerToggle toggle;
    private RecyclerView feedView;
    private ProgressDialog mDialog;
    private LinearLayoutManager manager;
    private DatabaseReference mRef,userRef;
    private ArrayList<Feed> list;
    FirebaseRecyclerAdapter<Feed, FeedViewHolder> adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mRef = FirebaseDatabase.getInstance().getReference().child("Questions");

        mDialog = new ProgressDialog(HomeActivity.this);
        mDialog.setMessage("Please wait..");
        mDialog.setTitle("Loading");
        mDialog.setCanceledOnTouchOutside(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressWarnings("StatementWithEmptyBody")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent;

                switch (menuItem.getItemId()) {
                    case R.id.ask_que:
                        intent=new Intent(HomeActivity.this, AskActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.answer_que:
                        intent=new Intent(HomeActivity.this, AnswerActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.profile:
                        intent=new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.delete_account:
                        DeleteAccount();
                        break;

                }
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        View headerView=navigationView.getHeaderView(0);
        TextView userNameTextView=headerView.findViewById(R.id.user_profile_name);
        String display = "Hello " + Prevalant.currentOnlineUser.getName() + "!";
        userNameTextView.setText(display);

        feedView = (RecyclerView)findViewById(R.id.main_feed);
        manager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, true);
        //manager.setStackFromEnd(true);
        manager.setReverseLayout(true);
        feedView.setLayoutManager(manager);
        feedView.hasFixedSize();

    }

    private void DeleteAccount() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View DialogLayout = inflater.inflate(R.layout.delete_dialog, null);
        builder.setView(DialogLayout);

        Button delete = (Button) DialogLayout.findViewById(R.id.btn_del);
        Button cancel = (Button) DialogLayout.findViewById(R.id.btn_cancel_del);

        final android.app.AlertDialog del_dialog = builder.create();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef = FirebaseDatabase.getInstance().getReference().child("Users");
                String userID = Prevalant.currentOnlineUser.toString();
                Query delQuery = userRef.child(userID);

                delQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().removeValue();
                        Toast.makeText(HomeActivity.this, "Account deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(HomeActivity.this, "error : " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                del_dialog.dismiss();
            }
        });

        del_dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mRef != null){
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){

                        list = new ArrayList<>();

                        for(DataSnapshot ds: snapshot.getChildren()){
                            list.add(ds.getValue(Feed.class));
                        }

                        SearchAdapter searchAdapter = new SearchAdapter(list);
                        feedView.setAdapter(searchAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HomeActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View DialogLayout = inflater.inflate(R.layout.exit_dialog, null);
            builder.setView(DialogLayout);

            Button ok = (Button) DialogLayout.findViewById(R.id.btn_ok);
            Button cancel = (Button) DialogLayout.findViewById(R.id.btn_cancel);

            final android.app.AlertDialog exit_dialog = builder.create();

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishAffinity();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exit_dialog.dismiss();
                }
            });

            exit_dialog.show();
        }
    }

    //    @Override
//    protected void onStart() {
//        super.onStart();
//        mDialog.show();
//
//        FirebaseRecyclerOptions<Feed> options=new FirebaseRecyclerOptions.Builder<Feed>()
//                .setQuery(mRef,Feed.class)
//                .build();
//
//        adapter=new FirebaseRecyclerAdapter<Feed, FeedViewHolder>(options) {
//
//            String check_answer;
//
//            @Override
//            protected void onBindViewHolder(@NonNull FeedViewHolder holder, int position, @NonNull Feed model) {
//
//                check_answer=model.getAnswer();
//
//                holder.setmView(model.getQuestion(), model.getAnswer(), model.getBy());
//                holder.implementListener(HomeActivity.this, getRef(position).getKey());
//
//                if(check_answer.equals("")) {
//                    holder.itemView.setVisibility(View.GONE);
//                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
//                }
//                else
//                {
//                    holder.itemView.setVisibility(View.VISIBLE);
//                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
//                }
//                mDialog.dismiss();
//            }
//
//            @NonNull
//            @Override
//            public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_layout, parent, false);
//                return new FeedViewHolder(view);
//            }
//        };
//        feedView.setAdapter(adapter);
//        adapter.startListening();
//    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView QueView,AnsView,Ans_byView;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            QueView = mView.findViewById(R.id.feed_que);
            AnsView = mView.findViewById(R.id.feed_ans);
            Ans_byView = mView.findViewById(R.id.answer_by);
        }

        public void setmView(String que, String ans, String ans_by){
            QueView.setText(que);
            AnsView.setText(ans);
            Ans_byView.setText(ans_by);
        }

        public void implementListener(final Context context, final String id){
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailFeed.class);
                    intent.putExtra("id", id);
                    context.startActivity(intent);
                }
            });
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });
        return true;
    }

    private void search(String str) {

        ArrayList<Feed> myList = new ArrayList<>();

        for(Feed object : list){
            if(object.getQuestion().toLowerCase().contains(str.toLowerCase())){
                myList.add(object);
            }
        }

        SearchAdapter adapter = new SearchAdapter(myList);
        feedView.setAdapter(adapter);
    }


}