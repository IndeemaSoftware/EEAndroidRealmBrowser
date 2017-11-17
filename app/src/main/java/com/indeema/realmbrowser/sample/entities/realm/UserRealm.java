package com.indeema.realmbrowser.sample.entities.realm;

import com.indeema.realmbrowser.sample.entities.Contact;
import com.indeema.realmbrowser.sample.entities.User;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ivan Savenko on 16.11.16
 */

public class UserRealm extends RealmObject {

    @PrimaryKey
    private int mId;
    private String mName;
    private String mAddress;
    private RealmList<ContactRealm> mContacts;
    private DeviceRealm mDeviceRealm;

    public UserRealm() {
        mContacts = new RealmList<>();
    }

    public UserRealm(User user) {
        mId = user.getId();
        mName = user.getName();
        mAddress = user.getAddress();
        mDeviceRealm = new DeviceRealm(user.getDevice(), user.getId());
        mContacts = new RealmList<>();
        for (Contact contact : user.getContacts()) {
            mContacts.add(new ContactRealm(contact));
        }
    }

    public User convertToUser() {
        User user = new User();
        user.setId(mId);
        user.setName(mName);
        user.setAddress(mAddress);

        if (mDeviceRealm != null) {
            user.setDevice(mDeviceRealm.convertToDevice());
        }
        List<Contact> contacts = new ArrayList<>();
        if (mContacts != null) {
            for (ContactRealm contactRealm : mContacts) {
                contacts.add(contactRealm.convertToContact());
            }
        }
        user.setContacts(contacts);
        return user;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public DeviceRealm getDeviceRealm() {
        return mDeviceRealm;
    }

    public void setDeviceRealm(DeviceRealm deviceRealm) {
        mDeviceRealm = deviceRealm;
    }

    public RealmList<ContactRealm> getContacts() {
        return mContacts;
    }

    public void setContacts(RealmList<ContactRealm> contacts) {
        mContacts = contacts;
    }
}
