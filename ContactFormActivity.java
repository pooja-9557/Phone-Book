package com.tuition.createcontactform;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;
import static com.tuition.createcontactform.R.color.blue;
import static com.tuition.createcontactform.R.color.red;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.tuition.createcontactform.MVVM.ContactViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ContactFormActivity extends AppCompatActivity {
    private ContactViewModel contactViewModel;

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    String imageValue;
    AutoCompleteTextView autoCompleteTextView1, autoCompleteTextView2;
    ImageView imageSet;
    private int resultCode;
    private Intent data;

    ActivityResultLauncher<Intent> pickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (resultCode == RESULT_OK && data != null) {
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap1 = BitmapFactory.decodeFile(uri.toString());
                Log.d("TAG", "onActivityResult: " + bitmap1);
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                ImagePicker.Companion.getError(data);
            }
        }
    });
    //boolean isUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);

        getSupportActionBar().hide();
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);

        EditText nameEditText = findViewById(R.id.nameEdit_text);
        EditText lastNameEditText = findViewById(R.id.lastNameEdit_text);
        EditText companyEditText = findViewById(R.id.companyNameEdit_text);
        EditText phoneEditText = findViewById(R.id.phoneNameEdit_text);
        EditText emailEditText = findViewById(R.id.emailEdit_Text);
        TextView mailValidation = findViewById(R.id.mail_validation);
        TextView phoneValidation = findViewById(R.id.phone_validation);

        TextView titleText = findViewById(R.id.text1);
        autoCompleteTextView1 = findViewById(R.id.auto_text_view);
        textSpinnerPhone();
        autoCompleteTextView2 = findViewById(R.id.auto_text_view2);
        textSpinnerEmail();
        ImageView skip = findViewById(R.id.close_image);

        imageSet = findViewById(R.id.edit_photo);

     /*   isUpdate = getIntent().hasExtra("phoneNumber");
        if (isUpdate) {
            titleText.setText("Update Contact");
            String phoneNumber = getIntent().getStringExtra("phoneNumber");
            DataStoreSqlite db = new DataStoreSqlite(this);
            ArrayList<DataModelClass> dataList = db.FetchUniqueData(phoneNumber);

            if (dataList != null && !dataList.isEmpty()) {
                phoneEditText.setText(dataList.get(0).phoneNumber);
                nameEditText.setText(dataList.get(0).firstName);
                lastNameEditText.setText(dataList.get(0).lastName);
                companyEditText.setText(dataList.get(0).companyName);
//                dateButton.setText(dataList.get(0).calender);
                emailEditText.setText(dataList.get(0).emailId);
                imageValue = dataList.get(0).image;
            }

            Bitmap bitmap;
            Log.d("", "" + imageValue);
            if (imageValue != null) {
                byte[] bytes = Base64.decode(imageValue, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageSet.setImageBitmap(bitmap);
            }
        }

*/
        imageSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContactFormActivity.this.checkAndRequestPermissions(ContactFormActivity.this)) {
                    ImagePicker.with(ContactFormActivity.this).crop().cropSquare().provider(ImageProvider.BOTH).maxResultSize(200, 200, true).createIntentFromDialog(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            pickerLauncher.launch(intent);
                            return Unit.INSTANCE;
                        }
                    });

                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactFormActivity.this, PhoneContactView.class);
                startActivity(intent);
                finish();
            }
        });
        //
        initDatePicker();
        dateButton = findViewById(R.id.btPickDate);
        dateButton.setText("D.O.B");

        Button saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String lName = lastNameEditText.getText().toString();
                String companyName;
//                String companyNameText = companyEditText.getText().toString();
                if (companyEditText != null) {
                    companyName = companyEditText.getText().toString();
                } else {
                    companyName = "";
                }
                String ActivityphoneNumber = phoneEditText.getText().toString();
                String emailId = emailEditText.getText().toString();
                String calender = dateButton.getText().toString();
                String regex = "^(.+)@(.+.).com";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(emailId);
                String phoneChecked = ActivityphoneNumber;


                if (!ActivityphoneNumber.equals("")) {
                    if (ActivityphoneNumber.charAt(0) == '+') {
                        if (ActivityphoneNumber.charAt(1) == '9' && ActivityphoneNumber.charAt(2) == '1') {
                            phoneChecked = ActivityphoneNumber.substring(3);
                        } else {
                            phoneEditText.setBackgroundColor(0xFFFF0000);
                            phoneValidation.setText("Wrong country code and phone Number");
                        }
                    } else if (ActivityphoneNumber.charAt(0) == '0') {
                        phoneChecked = ActivityphoneNumber.substring(1);
                    } else {
                        phoneChecked = ActivityphoneNumber;
                    }

                }

                if (name.equalsIgnoreCase("") || ActivityphoneNumber.equals("")) {
                    Toast.makeText(getApplicationContext(), "fill required fields", Toast.LENGTH_LONG).show();
                } else if (!emailId.equals("") && !matcher.matches()) {

                    mailValidation.setText("please write correct email format");

                } else if (!phoneChecked.matches("\\d{10}")) {
                    phoneValidation.setText("Please enter up to 10 to 12 digits");

                } else {
                    mailValidation.setText("");
                    phoneValidation.setText("");


                    DataStoreSqlite database = new DataStoreSqlite(getApplicationContext());
                    ArrayList<DataModelClass> listFetch = database.FetchUniqueData(ActivityphoneNumber);
                    if (listFetch.isEmpty()) {
                        database.addData(name, lName, companyName, phoneChecked, emailId, calender, imageValue);
                        dialogShow();
                        Intent intent = new Intent(getApplicationContext(), PhoneContactView.class);
                        startActivity(intent);
                        finish();

                    } else {
                        phoneValidation.setText("Already Registered");

                    }

                }
            }
        });
    }

    public void dialogShow() {

        AlertDialog.Builder dialogBuild = new AlertDialog.Builder(this);
        LayoutInflater inflater= getLayoutInflater();
        View view= inflater.inflate(R.layout.dialog_confirm_layout,(ViewGroup) findViewById(R.id.constraintToast));
        TextView text= view.findViewById(R.id.textViewToast);
        text.setText("Succesfully created");
        dialogBuild.setView(view);
        AlertDialog alertDialog= dialogBuild.create();
        dialogBuild.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();

            }
        },2000);

    }

    public boolean checkAndRequestPermissions(final Activity context) {
        int WExtStorePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        String[] listPermissionsNeeded = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (cameraPermission != PackageManager.PERMISSION_GRANTED || WExtStorePermission != PackageManager.PERMISSION_GRANTED) {
            requestCameraStoragePermission.launch(listPermissionsNeeded);
            return false;
        }
        return true;
    }

    ActivityResultLauncher<String[]> requestCameraStoragePermission = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
        if (isGranted.containsValue(false)) {
            Toast.makeText(ContactFormActivity.this, "Camera and storage permission required, please grant permission", Toast.LENGTH_LONG).show();
        }
        else
        {
            ImageService();
        }
    });

    private void ImageService() {
        ImagePicker.with(ContactFormActivity.this).crop().cropSquare().provider(ImageProvider.BOTH).maxResultSize(200, 200, true).createIntentFromDialog(new Function1<Intent, Unit>() {
            @Override
            public Unit invoke(Intent intent) {
                pickerLauncher.launch(intent);
                return Unit.INSTANCE;
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                imageSet.setImageBitmap(bitmap);
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[] bytes = stream.toByteArray();
//                Log.d("", "" + bytes.length);
//                while (bytes.length > 5000) {
//                    Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                    //Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*0.8), (int)(bitmap.getHeight()*0.8), true);
//
//                    ByteArrayOutputStream streamByte = new ByteArrayOutputStream();
//                    boolean resized = bitmap1.compress(Bitmap.CompressFormat.JPEG, 50, streamByte);
//                    //resized.compress(Bitmap.CompressFormat.PNG, 100, streamByte);
//                    if (resized) {
//                        bytes = streamByte.toByteArray();
//                        Log.d("", "" + bytes.length);
//                        break;
//                    }
//
//                }
                imageValue = convert(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            ImagePicker.Companion.getError(data);
        }
    }

    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    private void textSpinnerEmail() {
        String[] mail = {"Email", "Gmail"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_text_data, R.id.text, mail);
        autoCompleteTextView2.setAdapter(adapter);
    }

    private void textSpinnerPhone() {
        String[] number = {"android", "apple"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_text_data, R.id.text, number);
        autoCompleteTextView1.setAdapter(adapter);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + year + " " + day;
    }

    private String getMonthFormat(int month) {
        if (month == 1) return "JAN";
        if (month == 2) return "FEB";
        if (month == 3) return "MARCH";
        if (month == 4) return "APRIL";
        if (month == 5) return "MAY";
        if (month == 6) return "JUNE";
        if (month == 7) return "JULY";
        if (month == 8) return "AUG";
        if (month == 9) return "SEP";
        if (month == 10) return "OCT";
        if (month == 11) return "NOV";
        if (month == 12) return "DEC";

        return "JAN";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}