package com.indeema.realmbrowser.sample.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.indeema.realmbrowser.RealmBrowser;
import com.indeema.realmbrowser.entities.RbEntity;
import com.indeema.realmbrowser.entities.Theme;
import com.indeema.realmbrowser.sample.App;
import com.indeema.realmbrowser.sample.R;
import com.indeema.realmbrowser.sample.adapters.UsersListAdapter;
import com.indeema.realmbrowser.sample.contract.MainContract;
import com.indeema.realmbrowser.sample.entities.Contact;
import com.indeema.realmbrowser.sample.entities.Device;
import com.indeema.realmbrowser.sample.entities.OperationSystem;
import com.indeema.realmbrowser.sample.entities.User;
import com.indeema.realmbrowser.sample.entities.realm.UserRealm;
import com.indeema.realmbrowser.sample.presenter.MainPresenter;
import com.indeema.realmbrowser.sample.utils.DividerItemDecoration;
import com.indeema.realmbrowser.sample.utils.RealmMigrationUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by Ivan Savenko on 16.11.16
 */

public class MainActivity extends AppCompatActivity implements MainContract.MainView, CreateActivity.CreateUserListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FloatingActionButton mFAB;
    private RecyclerView mRecyclerView;
    private UsersListAdapter mAdapter;

    MainContract.MainAction mAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RbEntity rbEntity1 = new RbEntity(((App)getApplication()).mRealmConfiguration1);
        RbEntity rbEntity2 = new RbEntity(((App)getApplication()).mRealmConfiguration2);
        RbEntity rbEntity3 = new RbEntity(((App)getApplication()).mRealmConfiguration2, UserRealm.class);
//        RealmBrowser.initialize(this, Theme.DEFAULT, 10, rbEntity1, rbEntity2, rbEntity3);
        RealmBrowser.initialize(this, Theme.DRACULA, 10, rbEntity1, rbEntity2, rbEntity3);

        mAdapter = new UsersListAdapter(this, new ArrayList<User>(), new UsersListAdapter.OnItemClickListener() {
            @Override
            public void onClick(User user, int position) {
                Snackbar.make(mFAB, user.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }

            @Override
            public void onLongClick(User user, int position) {
                CreateActivity.start(MainActivity.this, user);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int paddingLeft = (int) getResources().getDimension(R.dimen.list_item_content_margin_left);
        DividerItemDecoration decorator = new DividerItemDecoration(ContextCompat.getDrawable(this, R.drawable.list_divider), paddingLeft);
        mRecyclerView.addItemDecoration(decorator);
        mRecyclerView.setAdapter(mAdapter);

        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mFAB.setOnClickListener(view -> CreateActivity.start(MainActivity.this));

        mAction = new MainPresenter(this);
        mAction.getUsers();
//        mAction.savePrimitivesTestObjects(100);
    }

    @Override
    public void setUsers(List<User> users) {
        if (users.size() > 0) {
            mAdapter.setData(users);
        } else {
            generateNewUsers();
        }
    }

    @Override
    public void onCreateUserError(String cause) {
        Snackbar.make(mFAB, "Error: " + cause, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    @Override
    public void onCreateUser(User user) {
        Log.d(TAG, "onCreateUser -> " + user);
        mAction.saveUser(user);
        mAction.getUsers();
    }

    private void generateNewUsers() {
        for (int i = 0; i < 5; i++) {
            List<Contact> contacts = new ArrayList<>();
            contacts.add(new Contact("+38067" + String.valueOf(7770000 + i), Contact.PHONE));
            contacts.add(new Contact("my" + i + "email@gmail.com", Contact.EMAIL));
            List<OperationSystem> software = new ArrayList<>();
            software.add(new OperationSystem("Oreo " + i, "version 26(" + i + ")"));
            Device device = new Device("SAMSUNG", "model_0" + i, software);
            User user = new User("user" + i, "address" + i, device, contacts);
            mAction.saveUser(user);
        }

        mAction.getUsers();
    }

}
