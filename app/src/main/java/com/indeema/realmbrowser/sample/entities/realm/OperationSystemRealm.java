package com.indeema.realmbrowser.sample.entities.realm;

import com.indeema.realmbrowser.sample.entities.OperationSystem;

import io.realm.RealmObject;

/**
 * @author Ruslan Stosyk
 *         Date: November, 14, 2017
 *         Time: 11:57 AM
 */
public class OperationSystemRealm extends RealmObject {

    private String mName;
    private String mVersion;

    public OperationSystemRealm() {

    }

    public OperationSystemRealm(OperationSystem software) {
        mName = software.getName();
        mVersion = software.getVersion();
    }

    public OperationSystemRealm(String name, String versionOS) {
        mName = name;
        mVersion = versionOS;
    }

    public OperationSystem convertToSoftware() {
        OperationSystem software = new OperationSystem();
        software.setName(mName);
        software.setVersion(mVersion);
        return software;
    }

    public String getName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmVersion() {
        return mVersion;
    }

    public void setmVersion(String mVersion) {
        this.mVersion = mVersion;
    }
}
