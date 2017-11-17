package com.indeema.realmbrowser.sample.entities.realm;

import com.indeema.realmbrowser.sample.entities.Contact;
import com.indeema.realmbrowser.sample.entities.ContactType;

import io.realm.RealmObject;

/**
 * @author Ivan Savenko
 *         Date: November, 13, 2017
 *         Time: 13:29
 */

public class ContactRealm extends RealmObject {

    private String mData;

    private String mType;

    public ContactRealm() {

    }

    public ContactRealm(Contact contact) {
        mData = contact.getData();
        mType = contact.getType();
    }

    public ContactRealm(String data, @ContactType String type) {
        mData = data;
        mType = type;
    }

    public Contact convertToContact() {
        Contact contact = new Contact();
        contact.setData(mData);
        contact.setType(mType);
        return contact;
    }

}
