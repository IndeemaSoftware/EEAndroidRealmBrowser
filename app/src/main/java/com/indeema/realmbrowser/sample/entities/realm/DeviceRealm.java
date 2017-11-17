package com.indeema.realmbrowser.sample.entities.realm;

import com.indeema.realmbrowser.sample.entities.Device;
import com.indeema.realmbrowser.sample.entities.OperationSystem;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ivan Savenko on 17.11.16
 */

public class DeviceRealm extends RealmObject {

    @PrimaryKey
    private int mUserId;
    private String mManufacturer;
    private String mModel;
    private RealmList<OperationSystemRealm> mSoftware;

    public DeviceRealm() {
        mSoftware = new RealmList<>();
    }

    public DeviceRealm(Device device, int userId) {
        mUserId = userId;
        mManufacturer = device.getManufacturer();
        mModel = device.getModel();
        mSoftware = new RealmList<>();
        for (OperationSystem software : device.getOperationSystem()) {
            mSoftware.add(new OperationSystemRealm(software));
        }
    }

    public Device convertToDevice() {
        Device device = new Device();
        device.setManufacturer(mManufacturer);
        device.setModel(mModel);

        List<OperationSystem> software = new ArrayList<>();
        if (mSoftware != null) {
            for (OperationSystemRealm softwareRealm : mSoftware) {
                software.add(softwareRealm.convertToSoftware());
            }
        }
        device.setSoftware(software);
        return device;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
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

    public RealmList<OperationSystemRealm> getSoftware() {
        return mSoftware;
    }

    public void setSoftware(RealmList<OperationSystemRealm> software) {
        mSoftware = software;
    }
}
