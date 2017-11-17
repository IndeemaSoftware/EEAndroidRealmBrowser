package com.indeema.realmbrowser.sample.presenter;

import com.indeema.realmbrowser.sample.contract.MainContract;
import com.indeema.realmbrowser.sample.entities.User;
import com.indeema.realmbrowser.sample.managers.DbManager;

import java.util.List;

/**
 * Created by Ivan Savenko on 17.11.16
 */

public class MainPresenter implements MainContract.MainAction {

    MainContract.MainView mView;

    public MainPresenter(MainContract.MainView view) {
        mView = view;
    }

    @Override
    public void saveUser(User user) {
        DbManager.saveUser(user);
    }

    @Override
    public void getUsers() {
        List<User> users = DbManager.getAllUsers();
        mView.setUsers(users);
    }

    @Override
    public void savePrimitivesTestObjects(int count) {
        DbManager.savePrimitivesTestObjects(count);
    }

}