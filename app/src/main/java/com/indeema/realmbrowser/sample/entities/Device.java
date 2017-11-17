package com.indeema.realmbrowser.sample.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ivan Savenko on 17.11.16
 */

public class Device implements Parcelable {

    private String mManufacturer;
    private String mModel;
    private List<OperationSystem> mSoftware;

    public Device() {

    }

    public Device(String manufacturer, String model, List<OperationSystem> software) {
        mManufacturer = manufacturer;
        mModel = model;
        mSoftware = software;
    }

    public String getManufacturer() {
        return mManufacturer;
    }

    public void setManufacturer(String manufacturer) {
        mManufacturer = manufacturer;
    }

    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        mModel = model;
    }

    public List<OperationSystem> getOperationSystem() {
        return mSoftware;
    }

    public void setSoftware(List<OperationSystem> software) {
        mSoftware = software;
    }

    @Override
    public String toString() {
        return "Device{" +
                "mManufacturer='" + mManufacturer + '\'' +
                ", mModel='" + mModel + '\'' +
                ", mSoftware=" + Arrays.toString(mSoftware.toArray()) +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mManufacturer);
        dest.writeString(this.mModel);
        dest.writeList(this.mSoftware);
    }

    protected Device(Parcel in) {
        this.mManufacturer = in.readString();
        this.mModel = in.readString();
        this.mSoftware = new ArrayList<OperationSystem>();
        in.readList(this.mSoftware, OperationSystem.class.getClassLoader());
    }

    public static final Parcelable.Creator<Device> CREATOR = new Parcelable.Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel source) {
            return new Device(source);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };
}
