package com.andreea.ewa;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by andreeagb on 1/11/2018.
 */

public class EditAccountActivity extends Activity  implements View.OnClickListener {

    private EditText birthDay;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat simpleDateFormat;
    private Spinner genderSpinner;
    private Button saveButton;

    private CircleImageView picture;
    private Uri imageUri;
    private StorageReference mStorage;
    private UserAccount editedAccount;

    private static final int PICK_IMAGE = 100;

    //User data.
    private EditText name, phone, weight, height;

    private final String[] gender = { "Male", "Female" };
    private boolean pictureModiefied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editaccount);

        birthDay = findViewById(R.id.eBirthday);
        picture = findViewById(R.id.profile_image);
        genderSpinner = (Spinner) findViewById(R.id.spinnerGender);
        saveButton = (Button)findViewById(R.id.buttonSave);

        name = findViewById(R.id.eName);
        phone = findViewById(R.id.ePhone);
        weight = findViewById(R.id.eWeight);
        height = findViewById(R.id.eHeight);

        mStorage = FirebaseStorage.getInstance().getReference();

        ArrayAdapter<String> spin_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, gender);
        // setting adapteers to spinners
        genderSpinner.setAdapter(spin_adapter);

        birthDay.setInputType(InputType.TYPE_NULL);
        birthDay.requestFocus();

        setDateTimeField();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    }

    private void setDateTimeField() {
        birthDay.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                birthDay.setText(simpleDateFormat.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void saveDataToFirebase(){

        //Adding values
        String user = AppState.get().getUserId();
        editedAccount = new UserAccount(name.getText().toString(), birthDay.getText().toString(), genderSpinner.getSelectedItem().toString(), Integer.parseInt(phone.getText().toString()), Integer.parseInt(weight.getText().toString()), Integer.parseInt(height.getText().toString()));
        final DatabaseReference newRef = AppState.get().getDatabaseReference().child("Patients").child(user);

        if(pictureModiefied) {

            StorageReference filepath = mStorage.child("Photos").child(imageUri.getLastPathSegment());

            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(EditAccountActivity.this, "Data saved.", Toast.LENGTH_SHORT).show();

                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    newRef.child("imageUri").setValue(downloadUri);
                }
            });


        }
        newRef.child("Data").setValue(editedAccount);
    }

    private boolean dataOK(){
        if(TextUtils.isEmpty(name.getText()))
            name.setError("Name required!");
        else if(TextUtils.isEmpty(phone.getText()))
            phone.setError("Phone required!");
        else if(TextUtils.isEmpty(birthDay.getText()))
            birthDay.setError("Please enter birthday");
        else if(TextUtils.isEmpty(height.getText()))
            height.setError("Please enter height");
        else if (TextUtils.isEmpty(weight.getText()))
            weight.setError("Please enter weight");
        else
            return true;
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                picture.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v == birthDay)
            datePickerDialog.show();
        else if (v == picture) {
            openGallery();
            pictureModiefied = true;
        }else if(v == saveButton) {
            if( dataOK() ) {
                saveDataToFirebase();
                finish();
            }
        }
    }

}
