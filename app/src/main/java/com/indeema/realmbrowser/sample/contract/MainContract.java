package com.indeema.realmbrowser.sample.contract;

import com.indeema.realmbrowser.sample.entities.User;

import java.util.List;

/**
 * Created by Ivan Savenko on 17.11.16
 */

public class MainContract {

    public interface MainView {
        void setUsers(List<User> users);
    }

    public interface MainAction {
        void saveUser(User user);
        void getUsers();
        void savePrimitivesTestObjects(int count);
    }

}
