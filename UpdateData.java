package com.tuition.createcontactform;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class UpdateData extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private String imageValue;
    private final int imageRequest = 1000;
    ImageView imageSet;
    String i1;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        getSupportActionBar().hide();

        EditText nameEditText = findViewById(R.id.nameEdit_text);
        EditText lastNameEditText = findViewById(R.id.lastNameEdit_text);
        EditText companyEditText = findViewById(R.id.companyEdit_text);
        TextView phoneEditText = findViewById(R.id.phoneEdit_Text);
        EditText emailEditText = findViewById(R.id.emailEdit_Text);
        Button saveButton = findViewById(R.id.save_button);
        ImageView skip = findViewById(R.id.close_image);
        TextView nameValidation = findViewById(R.id.name_validation);
        TextView mailValidation = findViewById(R.id.mail_validation);
        TextView phoneValidation = findViewById(R.id.phone_validation);
        imageSet = findViewById(R.id.edit_photo);
        initDatePicker();
        dateButton = findViewById(R.id.btPickDate);

        ArrayList<DataModelClass> dataList;
        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra("phoneNumber");
        DataStoreSqlite db = new DataStoreSqlite(this);
        dataList = db.FetchUniqueData(phoneNumber);

        if (dataList != null && !dataList.isEmpty()) {
            String phone=dataList.get(0).phoneNumber;
            phoneEditText.setText("+91"+phone);
            nameEditText.setText(dataList.get(0).firstName);
            lastNameEditText.setText(dataList.get(0).lastName);
            companyEditText.setText(dataList.get(0).companyName);
            dateButton.setText(dataList.get(0).calender);
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

        //image
        imageSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getContext());
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);
                bottomSheetDialog.show();
                Button addImage = bottomSheetDialog.findViewById(R.id.add_button);
                Button deleteImage = bottomSheetDialog.findViewById(R.id.delete_button);
                addImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /* Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, imageRequest);
                        */

                            if (UpdateData.this.checkAndRequestPermissions(UpdateData.this)) {
                                ImagePicker.with(UpdateData.this).crop().cropSquare().provider(ImageProvider.BOTH).maxResultSize(200, 200, true).createIntentFromDialog(new Function1<Intent, Unit>() {
                                    @Override
                                    public Unit invoke(Intent intent) {
                                        pickerLauncher.launch(intent);
                                        return Unit.INSTANCE;
                                    }
                                });
                        }
                        bottomSheetDialog.dismiss();
                    }
                });
                deleteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageValue = null;
                        bottomSheetDialog.dismiss();
                    }
                });
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowPersonInformation.class);
                intent.putExtra("phoneNumber",phoneNumber);
                startActivity(intent);
                finish();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String lName = lastNameEditText.getText().toString();
                String companyName = companyEditText.getText().toString();
                String emailId = emailEditText.getText().toString();
                String calender = dateButton.getText().toString();
                String regex = "^(.+)@(.+.).com";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(emailId);

                if (name.equalsIgnoreCase("") ) {
                    Toast.makeText(getApplicationContext(), "fill required fields", Toast.LENGTH_LONG).show();
                } else if (!emailId.equals("") && !matcher.matches()) {
                    mailValidation.setText("please write correct email format like name@gmail.com");

                } else {
                    DataStoreSqlite database = new DataStoreSqlite(getApplicationContext());
                    database.DataUpdate(name, lName, companyName, phoneNumber, emailId, calender, imageValue);
                    dialogShow();
                    finish();
                    Intent intent = new Intent(getApplicationContext(), ShowPersonInformation.class);
                    intent.putExtra("phoneNumber",phoneNumber);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
    public void dialogShow() {

        AlertDialog.Builder dialogBuild = new AlertDialog.Builder(this);
        LayoutInflater inflater= getLayoutInflater();
        View view= inflater.inflate(R.layout.dialog_confirm_layout,(ViewGroup) findViewById(R.id.constraintToast));
        dialogBuild.setView(view);
        AlertDialog alertDialog= dialogBuild.create();
        dialogBuild.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        },2000);
        alertDialog.dismiss();
    }


    public boolean checkAndRequestPermissions(final Activity context) {
        int WExtStorePermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA);
        String[] listPermissionsNeeded = new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (cameraPermission != PackageManager.PERMISSION_GRANTED || WExtStorePermission != PackageManager.PERMISSION_GRANTED) {
            requestCameraStoragePermission.launch(listPermissionsNeeded);
            return false;
        }
        return true;
    }

    ActivityResultLauncher<String[]> requestCameraStoragePermission = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
        if (isGranted.containsValue(false)) {
            Toast.makeText(UpdateData.this, "Camera and storage permission required, please grant permission", Toast.LENGTH_LONG).show();
        }
    });

   /* protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (resultCode == Activity.RESULT_OK && requestCode == imageRequest) {
                Uri targetUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), targetUri);
                    imageSet.setImageBitmap(bitmap);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] bytes = stream.toByteArray();
                    imageValue = Base64.encodeToString(bytes, Base64.DEFAULT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
*/
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
       if (resultCode == RESULT_OK && data != null) {
           Uri uri = data.getData();
           try {
               Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
               imageSet.setImageBitmap(bitmap);
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