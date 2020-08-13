package com.example.teslainventory.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teslainventory.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static com.example.teslainventory.ui.AddEditTeslaActivity.EXTRA_AVAILABLE_QUANTITY;
import static com.example.teslainventory.ui.AddEditTeslaActivity.EXTRA_DESCRIPTION;
import static com.example.teslainventory.ui.AddEditTeslaActivity.EXTRA_EXTERIOR_PAINT;
import static com.example.teslainventory.ui.AddEditTeslaActivity.EXTRA_ID;
import static com.example.teslainventory.ui.AddEditTeslaActivity.EXTRA_IMAGE;
import static com.example.teslainventory.ui.AddEditTeslaActivity.EXTRA_INVENTORY_TYPE;
import static com.example.teslainventory.ui.AddEditTeslaActivity.EXTRA_MODEL;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_ID_2 = "com.example.teslainventory.EX_ID_2";
    public static final String EXTRA_MODEL_2 = "com.example.teslainventory.EXTRA_TITLE_2";
    public static final String EXTRA_IMAGE_2 = "com.example.teslainventory.EXTRA_IMAGE_2";
    public static final String EXTRA_DESCRIPTION_2 = "com.example.teslainventory.EXTRA_DESCRIPTION_2";
    public static final String EXTRA_INVENTORY_TYPE_2 = "com.example.teslainventory.EXTRA_INVENTORY_TYPE_2";
    public static final String EXTRA_EXTERIOR_PAINT_2 = "com.example.teslainventory.EXTRA_EXTERIOR_PAINT_2";
    public static final String EXTRA_AVAILABLE_QUANTITY_2 = "com.example.teslainventory.EXTRA_AVAILABLE_QUANTITY_2";
    public static final String EXTRA_PRICE_2 = "com.example.teslainventory.EXTRA_PRICE_2";
    public static final String EXTRA_PRIORITY_2 = "com.example.teslainventory.EXTRA_PRIORITY_2";

    private ImageView mImageView;
    private int mId;
    private TextView mModel,mDescription,mInventoryType,mExteriorPaint,mAvailableQuantity,mPrice,mPriority;

    private Intent intentReceiver,intentSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mImageView = findViewById(R.id.image_view);
        mModel = findViewById(R.id.model_2);
        mDescription = findViewById(R.id.description_2);
        mInventoryType = findViewById(R.id.inventory_type_2);
        mExteriorPaint = findViewById(R.id.exterior_paint_2);
        mAvailableQuantity = findViewById(R.id.available_quantity_2);
        mPrice = findViewById(R.id.price_2);
        mPriority = findViewById(R.id.priority_2);

        intentReceiver = getIntent();
        if (intentReceiver.hasExtra(EXTRA_ID_2)) {
            mId = intentReceiver.getIntExtra(EXTRA_ID_2,-1);
            File imgFile = new  File(intentReceiver.getStringExtra(EXTRA_IMAGE_2));
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            mImageView.setImageBitmap(myBitmap);
            mModel.setText(intentReceiver.getStringExtra(EXTRA_MODEL_2));
            mDescription.setText(intentReceiver.getStringExtra(EXTRA_DESCRIPTION_2));
            mInventoryType.setText(intentReceiver.getStringExtra(EXTRA_INVENTORY_TYPE_2));
            mExteriorPaint.setText(intentReceiver.getStringExtra(EXTRA_EXTERIOR_PAINT_2));
            mAvailableQuantity.setText(intentReceiver.getStringExtra(EXTRA_AVAILABLE_QUANTITY_2));
            mPrice.setText(intentReceiver.getStringExtra(EXTRA_PRICE_2));
            mPriority.setText(String.valueOf(intentReceiver.getIntExtra(EXTRA_PRIORITY_2, 1)));
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_tesla:
                editTeslaCar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editTeslaCar() {

        int id = mId;
        String model = mModel.getText().toString();
        String image = intentReceiver.getStringExtra(EXTRA_IMAGE_2);
        String description = intentReceiver.getStringExtra(EXTRA_DESCRIPTION_2);
        String inventoryType = intentReceiver.getStringExtra(EXTRA_INVENTORY_TYPE_2);
        String exteriorPaint = intentReceiver.getStringExtra(EXTRA_EXTERIOR_PAINT_2);
        String availableQuantity = intentReceiver.getStringExtra(EXTRA_AVAILABLE_QUANTITY_2);
        String price = intentReceiver.getStringExtra(EXTRA_PRICE_2);
        int priority = intentReceiver.getIntExtra(EXTRA_PRIORITY_2,1);

        Intent intentSender = new Intent(DetailsActivity.this, AddEditTeslaActivity.class);
        intentSender.putExtra(EXTRA_ID, id);
        intentSender.putExtra(EXTRA_MODEL, model);
        intentSender.putExtra(EXTRA_IMAGE, image);
        intentSender.putExtra(EXTRA_DESCRIPTION, description);
        intentSender.putExtra(EXTRA_INVENTORY_TYPE, inventoryType);
        intentSender.putExtra(EXTRA_EXTERIOR_PAINT, exteriorPaint);
        intentSender.putExtra(EXTRA_AVAILABLE_QUANTITY, availableQuantity);
        intentSender.putExtra(AddEditTeslaActivity.EXTRA_PRICE, price);
        intentSender.putExtra(AddEditTeslaActivity.EXTRA_PRIORITY, priority);
        startActivityForResult(intentSender, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3 && resultCode == RESULT_OK) {

            int id = data.getIntExtra(AddEditTeslaActivity.EXTRA_ID,-1);
            String model = data.getStringExtra(AddEditTeslaActivity.EXTRA_MODEL);
            String image = data.getStringExtra(AddEditTeslaActivity.EXTRA_IMAGE);
            String description = data.getStringExtra(AddEditTeslaActivity.EXTRA_DESCRIPTION);
            String inventoryType = data.getStringExtra(AddEditTeslaActivity.EXTRA_INVENTORY_TYPE);
            String exteriorPaint = data.getStringExtra(AddEditTeslaActivity.EXTRA_EXTERIOR_PAINT);
            String availableQuantity = data.getStringExtra(AddEditTeslaActivity.EXTRA_AVAILABLE_QUANTITY);
            String price = data.getStringExtra(AddEditTeslaActivity.EXTRA_PRICE);
            int priority = data.getIntExtra(AddEditTeslaActivity.EXTRA_PRIORITY,1);

            if (data.hasExtra(AddEditTeslaActivity.EXTRA_ID)) {
                mId = id;
                mModel.setText(model);
                mImageView.setImageBitmap(showImageByUriPath(image));
                mDescription.setText(description);
                mInventoryType.setText(inventoryType);
                mExteriorPaint.setText(exteriorPaint);
                mAvailableQuantity.setText(availableQuantity);
                mPrice.setText(price);
                mPriority.setText(String.valueOf(priority));
            }

            intentSender = new Intent();
            intentSender.putExtra(EXTRA_ID_2, id);
            intentSender.putExtra(EXTRA_MODEL_2, model);
            intentSender.putExtra(EXTRA_IMAGE_2, image);
            intentSender.putExtra(EXTRA_DESCRIPTION_2, description);
            intentSender.putExtra(EXTRA_INVENTORY_TYPE_2, inventoryType);
            intentSender.putExtra(EXTRA_EXTERIOR_PAINT_2, exteriorPaint);
            intentSender.putExtra(EXTRA_AVAILABLE_QUANTITY_2, availableQuantity);
            intentSender.putExtra(EXTRA_PRICE_2, price);
            intentSender.putExtra(EXTRA_PRIORITY_2, priority);
            setResult(RESULT_OK, intentSender);
            finish();

            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();

        }
    }



    private Bitmap showImageByUriPath(String uriPathStr) {
        File imgFile = new  File(uriPathStr);
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        return myBitmap;
    }


}