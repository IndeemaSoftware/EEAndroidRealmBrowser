package com.indeema.realmbrowser.sample.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Ruslan Stosyk
 *         Date: November, 14, 2017
 *         Time: 12:10 PM
 */
public class OperationSystem implements Parcelable {

    private String mName;
    private String mVersion;

    public OperationSystem() {

    }

    public OperationSystem(String name, String version) {
        mName = name;
        mVersion = version;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getVersion() {
        return mVersion;
    }

    public void setVersion(String mVersionOS) {
        this.mVersion = mVersionOS;
    }

    @Override
    public String toString() {
        return "OperationSystem{" +
                "mName='" + mName + '\'' +
                ", mVersion='" + mVersion + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mVersion);
    }

    protected OperationSystem(Parcel in) {
        this.mName = in.readString();
        this.mVersion = in.readString();
    }

    public static final Parcelable.Creator<OperationSystem> CREATOR = new Parcelable.Creator<OperationSystem>() {
        @Override
        public OperationSystem createFromParcel(Parcel source) {
            return new OperationSystem(source);
        }

        @Override
        public OperationSystem[] newArray(int size) {
            return new OperationSystem[size];
        }
    };
}
