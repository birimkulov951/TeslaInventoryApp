package com.example.teslainventory.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.util.LogTime;
import com.example.teslainventory.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class AddEditTeslaActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 4;
    public static final String EXTRA_ID = "com.example.teslainventory.EX_ID";
    public static final String EXTRA_MODEL = "com.example.teslainventory.EXTRA_TITLE";
    public static final String EXTRA_IMAGE = "com.example.teslainventory.EXTRA_IMAGE";
    public static final String EXTRA_DESCRIPTION = "com.example.teslainventory.EXTRA_DESCRIPTION";
    public static final String EXTRA_INVENTORY_TYPE = "com.example.teslainventory.EXTRA_INVENTORY_TYPE";
    public static final String EXTRA_EXTERIOR_PAINT = "com.example.teslainventory.EXTRA_EXTERIOR_PAINT";
    public static final String EXTRA_AVAILABLE_QUANTITY = "com.example.teslainventory.EXTRA_AVAILABLE_QUANTITY";
    public static final String EXTRA_PRICE = "com.example.teslainventory.EXTRA_PRICE";
    public static final String EXTRA_PRIORITY = "com.example.teslainventory.EXTRA_PRIORITY";

    // View Casting
    private ImageView mImageView;
    private ImageButton btPrevious, btNext, addPhoto;
    private TextSwitcher textSwitcher;
    private String[] models = {"Model S","Model 3", "Model X","Model Y","Roadster(2020)","Tesla Semi","Cybertruck","Roadster(2008)"};
    int countModels = models.length;
    int position = 0;
    private EditText editDescription, editInventoryType, editExteriorPaint, editAvailabilityQuantity, editPrice;
    private NumberPicker numberPickerPriority;


    private boolean teslaHasChanged = false;
    private Intent intent;
    private File finalFile;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mPetHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            teslaHasChanged = true;
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tesla);

        mImageView = findViewById(R.id.tesla_image);
        addPhoto = findViewById(R.id.add_photo);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                ActivityCompat.requestPermissions(AddEditTeslaActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);
                if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePhotoIntent,REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        textSwitcherMethod();
        numberPickerMethod();
        editDescription = findViewById(R.id.edit_text_description);
        editInventoryType = findViewById(R.id.edit_text_inventory_type);
        editExteriorPaint = findViewById(R.id.edit_text_exterior_paint);
        editAvailabilityQuantity = findViewById(R.id.edit_text_availability_quantity);
        editPrice = findViewById(R.id.edit_text_price);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        editDescription.setOnTouchListener(mTouchListener);
        editInventoryType.setOnTouchListener(mTouchListener);
        editExteriorPaint.setOnTouchListener(mTouchListener);
        editAvailabilityQuantity.setOnTouchListener(mTouchListener);
        editPrice.setOnTouchListener(mTouchListener);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Tesla");
            textSwitcher.setText(intent.getStringExtra(EXTRA_MODEL));
            mImageView.setImageBitmap(showImageByUriPath(intent.getStringExtra(EXTRA_IMAGE)));
            editDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            editInventoryType.setText(intent.getStringExtra(EXTRA_INVENTORY_TYPE));
            editExteriorPaint.setText(intent.getStringExtra(EXTRA_EXTERIOR_PAINT));
            editAvailabilityQuantity.setText(intent.getStringExtra(EXTRA_AVAILABLE_QUANTITY));
            editPrice.setText(intent.getStringExtra(EXTRA_PRICE));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        } else {
            setTitle("Add Tesla");
        }


    }


    /**
     * Camera takes picture and return picture file path
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            finalFile = new File(getRealPathFromURI(tempUri));

            System.out.println("Debug: " + tempUri + " - " + imageBitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_tesla_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_tesla:
                saveTeslaCar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveTeslaCar() {
        int id = intent.getIntExtra(EXTRA_ID, -1);
        TextView model = (TextView) textSwitcher.getCurrentView();
        String image;
        if (finalFile != null) {

            image = finalFile.toString();

        } else {

            image = null;

        }
        String description = editDescription.getText().toString();
        String inventoryType = editInventoryType.getText().toString();
        String exteriorPaint = editExteriorPaint.getText().toString();
        String availableQuantity = editAvailabilityQuantity.getText().toString();
        String price = editPrice.getText().toString();
        int priority = numberPickerPriority.getValue();

        if (description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a description", Toast.LENGTH_SHORT).show();
            return;
        } else if (inventoryType.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a inventory type", Toast.LENGTH_SHORT).show();
            return;
        }else if (exteriorPaint.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a exterior paint", Toast.LENGTH_SHORT).show();
            return;
        }else if (availableQuantity.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a available quantity", Toast.LENGTH_SHORT).show();
            return;
        }else if (price.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a price", Toast.LENGTH_SHORT).show();
            return;
        } else if (image.trim().isEmpty()) {
            Toast.makeText(this, "Please take a picture of the car", Toast.LENGTH_SHORT).show();
            return;
        }

        if (intent.hasExtra(EXTRA_ID)) {
            Intent intentSender = new Intent();
            intentSender.putExtra(EXTRA_ID, id);
            intentSender.putExtra(EXTRA_MODEL, model.getText());
            intentSender.putExtra(EXTRA_IMAGE,image);
            intentSender.putExtra(EXTRA_DESCRIPTION, description);
            intentSender.putExtra(EXTRA_INVENTORY_TYPE, inventoryType);
            intentSender.putExtra(EXTRA_EXTERIOR_PAINT, exteriorPaint);
            intentSender.putExtra(EXTRA_AVAILABLE_QUANTITY, availableQuantity);
            intentSender.putExtra(EXTRA_PRICE, price);
            intentSender.putExtra(EXTRA_PRIORITY, priority);
            setResult(RESULT_OK, intentSender);
            finish();
        } else {
            Intent data = new Intent();
            data.putExtra(EXTRA_MODEL, model.getText());
            data.putExtra(EXTRA_IMAGE,image);
            data.putExtra(EXTRA_DESCRIPTION, description);
            data.putExtra(EXTRA_INVENTORY_TYPE, inventoryType);
            data.putExtra(EXTRA_EXTERIOR_PAINT, exteriorPaint);
            data.putExtra(EXTRA_AVAILABLE_QUANTITY, availableQuantity);
            data.putExtra(EXTRA_PRICE, price);
            data.putExtra(EXTRA_PRIORITY, priority);
            if (id != -1) {
                data.putExtra(EXTRA_ID, id);
            }
            setResult(RESULT_OK, data);
            finish();
        }

    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!teslaHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }
    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Go back");
        builder.setMessage("Please save changes!");
        builder.setPositiveButton("DISCARD", discardButtonClickListener);
        builder.setNegativeButton("KEEP EDITING", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }

            }

        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void textSwitcherMethod() {
        btPrevious = findViewById(R.id.button_previous);
        btNext = findViewById(R.id.button_next);
        textSwitcher = findViewById(R.id.text_switcher_model);
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(AddEditTeslaActivity.this);
                textView.setText("Model S");
                textView.setTextSize(22);
                textView.setTextColor(Color.BLACK);
                textView.setGravity(Gravity.CENTER);
                return textView;
            }
        });
        btPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textSwitcher.showPrevious();
                --position;
                if (position < 0) {
                    position = models.length-1;
                }
                textSwitcher.setText(models[position]);
            }
        });
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position++;
                if (position == countModels){
                    position = 0;
                }
                textSwitcher.setText(models[position]);

            }
        });
    }
    private void numberPickerMethod() {
        numberPickerPriority = findViewById(R.id.number_picker_priority);
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(5);
    }

    private Bitmap showImageByUriPath(String uriPathStr) {
        File imgFile = new  File(uriPathStr);
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        return myBitmap;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}