package com.tuition.createcontactform;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ShowPersonInformation extends AppCompatActivity {
    String[] array;
    TextView phoneEditText, phoneEditText2, phoneEditText3, phoneEditText4;
    String phoneNumber;
    static String imageValue;
    private final int imageRequest = 1000;
    ImageView setPhoto;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<DataModelClass> dataList;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_contact_info);
        TextView nameEditText = findViewById(R.id.nameEdit_text);
        phoneEditText = findViewById(R.id.phoneEdit_Text);
        ImageView editable = findViewById(R.id.edit_imageview);
        TextView imageWord = findViewById(R.id.imageWord);
        ImageView sendMessage= findViewById(R.id.messageSend);
        ImageView phoneCall= findViewById(R.id.phoneCall);
        getSupportActionBar().hide();
        setPhoto = findViewById(R.id.edit_photo);
        ImageView edit3 = findViewById(R.id.edit_image);
        ImageView whatsApp= findViewById(R.id.whatsapp);
        ImageView emailSend= findViewById(R.id.mail);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + phoneNumber));
                startActivity(intent);
            }
        });

        whatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
            }
        });

        phoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    PhoneCallPermissionGrant();
                } else {
                    ActivityCompat.requestPermissions(ShowPersonInformation.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
            }
        });

        emailSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" +email));
                startActivity(intent);

            }
        });

        //add data in UI
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phoneNumber");

        DataStoreSqlite db = new DataStoreSqlite(this);
        dataList = db.FetchUniqueData(phone);
        if(!dataList.isEmpty())
        {
            phoneNumber = dataList.get(0).phoneNumber;
            phoneEditText.setText(phoneNumber);
            email= dataList.get(0).emailId;
        }

        if(email!= null && !email.equals(""))
        {
            ConstraintLayout constraintLayout= findViewById(R.id.constraintMail);
            constraintLayout.setVisibility(View.VISIBLE);
            TextView emailText=findViewById(R.id.email_textViewField);
            emailText.setText(email);
        }
        nameEditText.setText(dataList.get(0).firstName + " " + dataList.get(0).lastName);
        String urlImage = dataList.get(0).image;

        Bitmap bitmap;
        if (urlImage != null) {
            byte[] bytes = Base64.decode(urlImage, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            setPhoto.setImageBitmap(bitmap);
            setPhoto.setVisibility(View.VISIBLE);
        } else {
            String name = dataList.get(0).firstName;
            String lName = dataList.get(0).lastName;
            String val2;
            String val = String.valueOf(name.charAt(0));
            if (!lName.equals("")) {
                String val1 = String.valueOf(lName.charAt(0));
                val2 = val + val1;
            } else {
                val2 = val;
            }
            imageWord.setText(val2);
            imageWord.setVisibility(View.VISIBLE);
            if (val.equalsIgnoreCase("A")  || val.equalsIgnoreCase("G") ||
                    val.equalsIgnoreCase("J") || val.equalsIgnoreCase("M") || val.equalsIgnoreCase("P") ||
                    val.equalsIgnoreCase("U") || val.equalsIgnoreCase("Z")) {
                int color= ContextCompat.getColor(getApplicationContext(),R.color.purple_200);
                imageWord.setBackgroundTintList(ColorStateList.valueOf(color));
            }
            else if (val.equalsIgnoreCase("B") || val.equalsIgnoreCase("E") || val.equalsIgnoreCase("H") ||
                    val.equalsIgnoreCase("K") || val.equalsIgnoreCase("Q") ||
                    val.equalsIgnoreCase("v") || val.equalsIgnoreCase("y"))
            {
                int color= ContextCompat.getColor(getApplicationContext(),R.color.green);
                imageWord.setBackgroundTintList(ColorStateList.valueOf(color));
            }
            else if (val.equalsIgnoreCase("D")  || val.equalsIgnoreCase("L") || val.equalsIgnoreCase("O") || val.equalsIgnoreCase("R") ||
                    val.equalsIgnoreCase("T") || val.equalsIgnoreCase("S"))
            {
                int color= ContextCompat.getColor(getApplicationContext(),R.color.yellow);
                imageWord.setBackgroundTintList(ColorStateList.valueOf(color));
            }
            else if (val.equalsIgnoreCase("W")  || val.equalsIgnoreCase("I") || val.equalsIgnoreCase("C") || val.equalsIgnoreCase("N") ||
                    val.equalsIgnoreCase("F") || val.equalsIgnoreCase("X"))
            {
                int color= ContextCompat.getColor(getApplicationContext(),R.color.red);
                imageWord.setBackgroundTintList(ColorStateList.valueOf(color));
            }
            else
            {
                int color= ContextCompat.getColor(getApplicationContext(),R.color.white);
                imageWord.setBackgroundTintList(ColorStateList.valueOf(color));
            }
        }
        // Action for menu items in layout
        ImageView customButton = findViewById(R.id.custom_menu);
        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ShowPersonInformation.this, customButton);
                popupMenu.getMenuInflater().inflate(R.menu.image_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int value = item.getItemId();
                        switch (value) {
                            case R.id.Edit: {
                                Intent intent1 = new Intent(getApplicationContext(), UpdateData.class);
                                intent1.putExtra("phoneNumber", phoneNumber);
                                startActivity(intent1);
                                finish();
                                break;
                            }
                            case R.id.Back: {
                                Intent intent = new Intent(getApplicationContext(), PhoneContactView.class);
                                startActivity(intent);
                                finish();
                                break;
                            }
                            case R.id.Delete: {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ShowPersonInformation.this);
                                builder.setTitle("  Delete?").setMessage("Do you want to delete this contact?").setCancelable(true).setIcon(R.drawable.ic_baseline_delete_24).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getApplicationContext(), PhoneContactView.class);
                                        startActivity(intent);
                                        Toast.makeText(ShowPersonInformation.this, "Deleted", Toast.LENGTH_SHORT).show();
                                        DataStoreSqlite dataStoreSqlite = new DataStoreSqlite(getApplicationContext());
                                        dataStoreSqlite.DeleteData(phoneNumber);
                                        finish();
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // dialog.cancel();
                                        Toast.makeText(ShowPersonInformation.this, "back", Toast.LENGTH_LONG).show();
                                    }
                                }).show();
                                break;
                            }
                            case R.id.Share:
                            {
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, phoneNumber);
                                startActivity(Intent.createChooser(shareIntent, "Share via"));
                                break;
                            }

                            default: {

                            }
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }

        });
        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageView back = findViewById(R.id.back_image);
        ImageView edit = findViewById(R.id.edit_image);

        //back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhoneContactView.class);
                startActivity(intent);
                finish();
            }
        });

        //edit
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateData.class);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
                finish();
            }
        });
        edit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateData.class);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
                finish();
            }
        });

        editable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateData.class);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
                finish();
            }
        });
    }

   /* private void EmailRequestGrant() {
        Intent intent= new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" +email));
        if(intent.resolveActivity(getPackageManager())!=null) {
            startActivity(intent);
        }
        else {
            Toast.makeText(this,"Please permission grant for email",Toast.LENGTH_SHORT).show();
        }
    }
*/
    /*private void checkPermissionGrant() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},3);
        } else {
            // Permission already granted, send the SMS
            sendSMS();
        }
    }*/
    private void PhoneCallPermissionGrant() {

        Intent intent = new Intent(Intent.ACTION_VIEW);

        // Verify that there's an activity that can handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the activity to open WhatsApp
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber)));
        } else {
            // No activity can handle the intent, show an error message
            Toast.makeText(getApplicationContext(), "Denied Permission", Toast.LENGTH_SHORT).show();
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, send the SMS
               if(grantResults[0]==1)
                {
                    PhoneCallPermissionGrant();
                }


            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }

    }

    /*private void WhatsAppPermissionGrant() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber));

        // Verify that there's an activity that can handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the activity to open WhatsApp
            startActivity(intent);

             Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
        } else {
            // No activity can handle the intent, show an error message
            Toast.makeText(getApplicationContext(), "WhatsApp is not installed", Toast.LENGTH_SHORT).show();
        }
    }
*/
    private void sendSMS() {
        String message = "Hello, this is a test message.";

        // Create the intent with the action and data
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the activity to open the SMS app
            startActivity(intent);
        } else {
            // No activity can handle the intent, show an error message
            Toast.makeText(getApplicationContext(), "No SMS app found", Toast.LENGTH_SHORT).show();
        }


    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (resultCode == Activity.RESULT_OK && requestCode == imageRequest) {
                Uri targetUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), targetUri);
                    setPhoto.setImageBitmap(bitmap);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] bytes = stream.toByteArray();
                    imageValue = Base64.encodeToString(bytes, Base64.DEFAULT);
                    DataStoreSqlite database = new DataStoreSqlite(getApplicationContext());
                    database.DataUpdate(array[0], array[1], array[2], phoneNumber, array[4], array[5], imageValue);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

  /*  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item;
        item= list.get(position);
        if(position==0)
        {
                    }
        else if (position==1)
        {
            Intent intent= new Intent( getApplicationContext(),PhoneContactView.class);
            startActivity(intent);
            finish();
        }
        else if(position==2)
        {
            Intent intent= new Intent(getApplicationContext(),UpdateData.class);
            intent.putExtra("phone",array);
            startActivity(intent);
            finish();
        }
        else
        {
            AlertDialog.Builder builder= new AlertDialog.Builder(ShowPersonInformation.this);
            builder.setTitle("  Delete?")
                    .setMessage("Do you want to delete this contact?")
                    .setCancelable(true)
                    .setIcon(R.drawable.ic_baseline_delete_24)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent= new Intent(getApplicationContext(), PhoneContactView.class);
                            startActivity(intent);
                            Toast.makeText(ShowPersonInformation.this,"Deleted",Toast.LENGTH_SHORT).show();
                            DataStoreSqlite dataStoreSqlite= new DataStoreSqlite(getApplicationContext());
                            dataStoreSqlite.DeleteData(phoneNumber);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // dialog.cancel();
                            Toast.makeText(ShowPersonInformation.this,"back",Toast.LENGTH_LONG).show();
                        }
                    })
                    .show();        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/
}