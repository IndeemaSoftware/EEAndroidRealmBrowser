package com.indeema.realmbrowser.sample.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Ivan Savenko
 *         Date: November, 13, 2017
 *         Time: 13:47
 */

public class Contact implements Parcelable {

    public static final String PHONE = "PHONE";
    public static final String EMAIL = "EMAIL";

    private String mData;

    private @ContactType String mType;

    public Contact() {

    }

    public Contact(String data, @ContactType String type) {
        mData = data;
        mType = type;
    }

    public @ContactType String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }

    public String getType() {
        return mType;
    }

    public void setType(@ContactType String type) {
        mType = type;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "mData='" + mData + '\'' +
                ", mType='" + mType + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mData);
        dest.writeString(this.mType);
    }

    protected Contact(Parcel in) {
        this.mData = in.readString();
        this.mType = in.readString();
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
