package com.indeema.realmbrowser.sample.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.indeema.realmbrowser.sample.R;
import com.indeema.realmbrowser.sample.entities.Contact;
import com.indeema.realmbrowser.sample.entities.Device;
import com.indeema.realmbrowser.sample.entities.OperationSystem;
import com.indeema.realmbrowser.sample.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ruslan Stosyk
 *         Date: November, 14, 2017
 *         Time: 2:25 PM
 */
public class CreateActivity extends AppCompatActivity {

    private static final String ARGUMENT_BUNDLE = "ARGUMENT_BUNDLE";
    private static final String ARGUMENT_USER = "ARGUMENT_USER";

    private TextInputLayout mNameEtLayout;
    private TextInputLayout mAddressEtLayout;
    private TextInputLayout mTelephoneLayout;
    private TextInputLayout mEmailLayout;
    private TextInputLayout mManufacturerEtLayout;
    private TextInputLayout mModelEtLayout;
    private TextInputLayout mOperationNameLayout;
    private TextInputLayout mOperationVersionLayout;

    private EditText mNameEt;
    private EditText mAddressEt;
    private EditText mTelephoneEt;
    private EditText mEmailEt;
    private EditText mManufacturerEt;
    private EditText mModelEt;
    private EditText mOperationNameEt;
    private EditText mOperationVersionEt;

    private User mUser;

    public static CreateUserListener mListener;
    private String mTitle;
    private FloatingActionButton floatingActionButton;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private boolean isFABOpen;

    public static void start(@NonNull Activity activity) {
        mListener = (CreateUserListener) activity;
        Intent intent = new Intent(activity, CreateActivity.class);
        activity.startActivity(intent);
    }

    public static void start(Activity activity, User user) {
        mListener = (CreateUserListener) activity;
        Intent intent = new Intent(activity, CreateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_USER, user);
        intent.putExtra(ARGUMENT_BUNDLE, bundle);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        Bundle bundle = getIntent().getBundleExtra(ARGUMENT_BUNDLE);
        mTitle = "Create user";
        if (bundle != null) {
            mUser = bundle.getParcelable(ARGUMENT_USER);
            mTitle = "Update user";
        }
        setTitle(mTitle);
        Log.d("CheckListDialog", "onCreateDialog");
        init();
        if (mUser != null) {
            mNameEt.setText(mUser.getName());
            mAddressEt.setText(mUser.getAddress());
            mTelephoneEt.setText(mUser.getContacts().get(0).getData());
            mEmailEt.setText(mUser.getContacts().get(1).getData());
            mManufacturerEt.setText(mUser.getDevice().getManufacturer());
            mModelEt.setText(mUser.getDevice().getModel());
            mOperationNameEt.setText(mUser.getDevice().getOperationSystem().get(0).getName());
            mOperationVersionEt.setText(mUser.getDevice().getOperationSystem().get(0).getVersion());
        }

    }

    private void init() {
        mNameEt = (EditText) findViewById(R.id.name_et);
        mAddressEt = (EditText) findViewById(R.id.address_et);
        mTelephoneEt = (EditText) findViewById(R.id.telephone_et);
        mEmailEt = (EditText) findViewById(R.id.email_et);
        mManufacturerEt = (EditText) findViewById(R.id.manufacturer_et);
        mModelEt = (EditText) findViewById(R.id.model_et);
        mOperationNameEt = (EditText) findViewById(R.id.name_os_et);
        mOperationVersionEt = (EditText) findViewById(R.id.version_et);


        mNameEtLayout = (TextInputLayout) findViewById(R.id.name_layout);
        mAddressEtLayout = (TextInputLayout) findViewById(R.id.address_layout);
        mTelephoneLayout = (TextInputLayout) findViewById(R.id.telephone_layout);
        mEmailLayout = (TextInputLayout) findViewById(R.id.email_layout);
        mManufacturerEtLayout = (TextInputLayout) findViewById(R.id.manufacturer_layout);
        mModelEtLayout = (TextInputLayout) findViewById(R.id.model_layout);
        mOperationNameLayout = (TextInputLayout) findViewById(R.id.name_os_layout);
        mOperationVersionLayout = (TextInputLayout) findViewById(R.id.version_layout);


        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        floatingActionButton.setOnClickListener(view -> {
            if (!isFABOpen) {
                showFABMenu();
            } else {
                closeFABMenu();
            }
        });

        fab1.setOnClickListener(v -> {
            attemptToCreateUser();
        });

        fab2.setOnClickListener(v -> closeFABMenu());
    }

    private void showFABMenu() {
        isFABOpen = true;
        floatingActionButton.hide();
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
    }

    private void closeFABMenu() {
        isFABOpen = false;
        floatingActionButton.show();
        fab2.animate().translationY(0);
    }

    private boolean attemptToCreateUser() {
        mNameEtLayout.setError(null);
        mAddressEtLayout.setError(null);
        mTelephoneLayout.setError(null);
        mEmailLayout.setError(null);
        mManufacturerEtLayout.setError(null);
        mModelEtLayout.setError(null);
        mOperationNameLayout.setError(null);
        mOperationVersionLayout.setError(null);

        String name = mNameEt.getText().toString();
        String address = mAddressEt.getText().toString();
        String telephone = mTelephoneEt.getText().toString();
        String email = mEmailEt.getText().toString();
        String manufacturer = mManufacturerEt.getText().toString();
        String model = mModelEt.getText().toString();
        String operationName = mOperationNameEt.getText().toString();
        String operationVersion = mOperationVersionEt.getText().toString();


        boolean success = true;

        if (TextUtils.isEmpty(name)) {
            mNameEtLayout.setError(getString(R.string.error_field_required));
            success = false;
        }
        if (TextUtils.isEmpty(address)) {
            mAddressEtLayout.setError(getString(R.string.error_field_required));
            success = false;
        }
        if (TextUtils.isEmpty(telephone)) {
            mTelephoneLayout.setError(getString(R.string.error_field_required));
            success = false;
        }
        if (TextUtils.isEmpty(email)) {
            mEmailLayout.setError(getString(R.string.error_field_required));
            success = false;
        }
        if (TextUtils.isEmpty(manufacturer)) {
            mManufacturerEtLayout.setError(getString(R.string.error_field_required));
            success = false;
        }
        if (TextUtils.isEmpty(model)) {
            mModelEtLayout.setError(getString(R.string.error_field_required));
            success = false;
        }
        if (TextUtils.isEmpty(operationName)) {
            mOperationNameLayout.setError(getString(R.string.error_field_required));
            success = false;
        }
        if (TextUtils.isEmpty(operationVersion)) {
            mOperationVersionLayout.setError(getString(R.string.error_field_required));
            success = false;
        }
        if (mListener != null) {
            if (success) {
                if (mUser == null)
                    mUser = new User();
                mUser.setName(name);
                mUser.setAddress(address);
                List<Contact> contact = new ArrayList<>();
                contact.add(new Contact(telephone, Contact.PHONE));
                contact.add(new Contact(email, Contact.EMAIL));
                mUser.setContacts(contact);
                List<OperationSystem> software = new ArrayList<>();
                software.add(new OperationSystem(operationName, operationVersion));
                mUser.setDevice(new Device(manufacturer, model, software));

                mListener.onCreateUser(mUser);
                finish();
            }
        }
        return success;
    }

    @Override
    public void onBackPressed() {
        if (!isFABOpen) {
            super.onBackPressed();
        } else {
            closeFABMenu();
        }
    }

    public interface CreateUserListener {
        void onCreateUserError(String cause);

        void onCreateUser(User user);
    }
}
