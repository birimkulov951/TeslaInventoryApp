package com.example.teslainventory.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tesla_table")
public class Tesla {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String mModel;
    private String mPrice;
    private String mAvailableQuantity;
    private String mDescription;
    private String mInventoryType;
    private String mExteriorPaint;

    private String mGlassRoof;
    private String mAutoPilot;
    private int mPriority;

    public Tesla(String mModel,
                 String mPrice,
                 String mAvailableQuantity,
                 String mDescription,
                 String mInventoryType,
                 String mExteriorPaint,
                 String mGlassRoof,
                 String mAutoPilot,
                 int mPriority) {
        this.mModel = mModel;
        this.mPrice = mPrice;
        this.mAvailableQuantity = mAvailableQuantity;
        this.mDescription = mDescription;
        this.mInventoryType = mInventoryType;
        this.mExteriorPaint = mExteriorPaint;
        this.mGlassRoof = mGlassRoof;
        this.mAutoPilot = mAutoPilot;
        this.mPriority = mPriority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return mModel;
    }

    public String getPrice() {
        return mPrice;
    }

    public String getAvailableQuantity() {
        return mAvailableQuantity;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getInventoryType() {
        return mInventoryType;
    }

    public String getExteriorPaint() {
        return mExteriorPaint;
    }

    public String getGlassRoof() {
        return mGlassRoof;
    }

    public String getAutoPilot() {
        return mAutoPilot;
    }

    public int getPriority() {
        return mPriority;
    }
}
