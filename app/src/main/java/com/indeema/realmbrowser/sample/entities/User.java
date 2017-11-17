package com.indeema.realmbrowser.sample.entities;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ivan Savenko on 17.11.16
 */

public class User implements Parcelable {

    private static int sID;

    public static int getNextID() {
        sID++;
        return sID;
    }

    private int mId;
    private String mName;
    private String mAddress;
    private Device mDevice;
    private List<Contact> mContacts;

    public User() {
        mId = getNextID();
        mContacts = new ArrayList<>();
    }

    public User(String name, String address, Device device, List<Contact> contacts) {
        mId = getNextID();
        mName = name;
        mAddress = address;
        mDevice = device;
        mContacts = contacts;
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

    public Device getDevice() {
        return mDevice;
    }

    public void setDevice(Device device) {
        mDevice = device;
    }

    public List<Contact> getContacts() {
        return mContacts;
    }

    public void setContacts(List<Contact> contacts) {
        mContacts = contacts;
    }

    @Override
    public String toString() {
        return "User{" +
                "mId='" + mId + '\'' +
                ", mName='" + mName + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mDevice=" + mDevice +
                ", mContacts=" + Arrays.toString(mContacts.toArray()) +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mName);
        dest.writeString(this.mAddress);
        dest.writeParcelable(this.mDevice, flags);
        dest.writeTypedList(this.mContacts);
    }

    protected User(Parcel in) {
        this.mId = in.readInt();
        this.mName = in.readString();
        this.mAddress = in.readString();
        this.mDevice = in.readParcelable(Device.class.getClassLoader());
        this.mContacts = in.createTypedArrayList(Contact.CREATOR);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
