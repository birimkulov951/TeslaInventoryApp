package com.example.teslainventory.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teslainventory.mvp.MainContract;
import com.example.teslainventory.mvp.MainPresenter;
import com.example.teslainventory.R;
import com.example.teslainventory.TeslaAdapter;
import com.example.teslainventory.TeslaAdapter.OnItemClickListener;
import com.example.teslainventory.room.Tesla;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.View {


    private static final String TAG = "MainActivity";

    public static final int ADD_TESLA_REQUEST = 1;
    public static final int EDIT_TESLA_REQUEST = 2;

    private MainContract.Presenter presenter;
    private final TeslaAdapter adapter = new TeslaAdapter();
    private RecyclerView recyclerView;

    private FloatingActionButton buttonAddNote;
    private TextView mEmptyText,mEmptyText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmptyText = findViewById(R.id.empty_text);
        mEmptyText2 = findViewById(R.id.empty_text_2);
        buttonAddNote = findViewById(R.id.button_add_tesla_car);

        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditTeslaActivity.class);
                startActivityForResult(intent, ADD_TESLA_REQUEST);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);

        presenter = new MainPresenter(this, getApplication());

        presenter.getAllTeslaCars().observe(this, new Observer<List<Tesla>>() {
            @Override
            public void onChanged(List<Tesla> teslaCars) {

                adapter.submitList(teslaCars);
                if (presenter.getAllTeslaCarsSize() == 0) {
                    showEmptyStore();
                } else {
                    hideEmptyStore();
                }

            }
        });

        swipeToDelete();

        setOnItemClickListener();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TESLA_REQUEST && resultCode == RESULT_OK) {

            String model = data.getStringExtra(AddEditTeslaActivity.EXTRA_MODEL);
            String image = data.getStringExtra(AddEditTeslaActivity.EXTRA_IMAGE);
            String description = data.getStringExtra(AddEditTeslaActivity.EXTRA_DESCRIPTION);
            String inventoryType = data.getStringExtra(AddEditTeslaActivity.EXTRA_INVENTORY_TYPE);
            String exteriorPaint = data.getStringExtra(AddEditTeslaActivity.EXTRA_EXTERIOR_PAINT);
            String availabilityQuantity = data.getStringExtra(AddEditTeslaActivity.EXTRA_AVAILABLE_QUANTITY);
            String price = data.getStringExtra(AddEditTeslaActivity.EXTRA_PRICE);
            int priority = data.getIntExtra(AddEditTeslaActivity.EXTRA_PRIORITY, 1);

            Tesla tesla = new Tesla(model,
                    "Price: " + price,
                    "Available quantity: " + availabilityQuantity,
                    description,
                    "Inventory type: " + inventoryType,
                    "Exterior paint: " + exteriorPaint,image,
                    null,null,priority);
            presenter.insert(tesla);

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_TESLA_REQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(DetailsActivity.EXTRA_ID_2, -1);

            if (id == -1) {
                Toast.makeText(this, "Can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String model = data.getStringExtra(DetailsActivity.EXTRA_MODEL_2);
            String image = data.getStringExtra(DetailsActivity.EXTRA_IMAGE_2);
            String description = data.getStringExtra(DetailsActivity.EXTRA_DESCRIPTION_2);
            String inventoryType = data.getStringExtra(DetailsActivity.EXTRA_INVENTORY_TYPE_2);
            String exteriorPaint = data.getStringExtra(DetailsActivity.EXTRA_EXTERIOR_PAINT_2);
            String availabilityQuantity = data.getStringExtra(DetailsActivity.EXTRA_AVAILABLE_QUANTITY_2);
            String price = data.getStringExtra(DetailsActivity.EXTRA_PRICE_2);
            int priority = data.getIntExtra(DetailsActivity.EXTRA_PRIORITY_2, 1);

            Tesla tesla = new Tesla(model,
                    "Price: " + price,
                    "Available quantity: " + availabilityQuantity,
                    description,
                    "Inventory type: " + inventoryType,
                    "Exterior paint: " + exteriorPaint,image,
                    null,null,priority);

            tesla.setId(id);

            presenter.update(tesla);

            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:

                if (presenter.getAllTeslaCarsSize()>0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
                    builder.setTitle("Delete All");
                    builder.setMessage("You will lose all data!");
                    builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            presenter.deleteAllTesla();
                            Toast.makeText(getApplication(), "All Tesla cars deleted", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setPositiveButton("DISCARD",null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Toast.makeText(getApplication(), "Nothing to delete", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setOnItemClickListener() {

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Tesla tesla) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(DetailsActivity.EXTRA_ID_2, tesla.getId());
                intent.putExtra(DetailsActivity.EXTRA_MODEL_2, tesla.getModel());
                intent.putExtra(DetailsActivity.EXTRA_IMAGE_2, tesla.getTeslaImage());
                intent.putExtra(DetailsActivity.EXTRA_DESCRIPTION_2, tesla.getDescription());
                intent.putExtra(DetailsActivity.EXTRA_INVENTORY_TYPE_2, tesla.getInventoryType());
                intent.putExtra(DetailsActivity.EXTRA_EXTERIOR_PAINT_2, tesla.getExteriorPaint());
                intent.putExtra(DetailsActivity.EXTRA_AVAILABLE_QUANTITY_2, tesla.getAvailableQuantity());
                intent.putExtra(DetailsActivity.EXTRA_PRICE_2, tesla.getPrice());
                intent.putExtra(DetailsActivity.EXTRA_PRIORITY_2, tesla.getPriority());
                startActivityForResult(intent, EDIT_TESLA_REQUEST);
            }
        });

    }

    @Override
    public void swipeToDelete() {

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                presenter.delete(adapter.getTeslaAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            }

        }).attachToRecyclerView(recyclerView);

    }

    @Override
    public void showEmptyStore() {

        mEmptyText.setVisibility(View.VISIBLE);
        mEmptyText2.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideEmptyStore() {

        mEmptyText.setVisibility(View.INVISIBLE);
        mEmptyText2.setVisibility(View.INVISIBLE);

    }
}