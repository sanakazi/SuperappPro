package com.superapp.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Abhijeet-PC on 06-Feb-17.
 */

public class BeanAddMaterial implements Parcelable {

    BeanSelectCategory beanMaterial, beanSubMaterial, beanAllBrand, beanSize, beanColors, beanMeasurements;

    public BeanSelectCategory getBeanMaterial() {
        return beanMaterial;
    }

    public void setBeanMaterial(BeanSelectCategory beanMaterial) {
        this.beanMaterial = beanMaterial;
    }

    public BeanSelectCategory getBeanSubMaterial() {
        return beanSubMaterial;
    }

    public void setBeanSubMaterial(BeanSelectCategory beanSubMaterial) {
        this.beanSubMaterial = beanSubMaterial;
    }

    public BeanSelectCategory getBeanAllBrand() {
        return beanAllBrand;
    }

    public void setBeanAllBrand(BeanSelectCategory beanAllBrand) {
        this.beanAllBrand = beanAllBrand;
    }

    public BeanSelectCategory getBeanSize() {
        return beanSize;
    }

    public void setBeanSize(BeanSelectCategory beanSize) {
        this.beanSize = beanSize;
    }

    public BeanSelectCategory getBeanColors() {
        return beanColors;
    }

    public void setBeanColors(BeanSelectCategory beanColors) {
        this.beanColors = beanColors;
    }

    public BeanSelectCategory getBeanMeasurements() {
        return beanMeasurements;
    }

    public void setBeanMeasurements(BeanSelectCategory beanMeasurements) {
        this.beanMeasurements = beanMeasurements;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable((Parcelable) this.beanMaterial, flags);
        dest.writeParcelable((Parcelable) this.beanSubMaterial, flags);
        dest.writeParcelable((Parcelable) this.beanAllBrand, flags);
        dest.writeParcelable((Parcelable) this.beanSize, flags);
        dest.writeParcelable((Parcelable) this.beanColors, flags);
        dest.writeParcelable((Parcelable) this.beanMeasurements, flags);
    }

    public BeanAddMaterial() {
    }

    protected BeanAddMaterial(Parcel in) {
        this.beanMaterial = in.readParcelable(BeanSelectCategory.class.getClassLoader());
        this.beanSubMaterial = in.readParcelable(BeanSelectCategory.class.getClassLoader());
        this.beanAllBrand = in.readParcelable(BeanSelectCategory.class.getClassLoader());
        this.beanSize = in.readParcelable(BeanSelectCategory.class.getClassLoader());
        this.beanColors = in.readParcelable(BeanSelectCategory.class.getClassLoader());
        this.beanMeasurements = in.readParcelable(BeanSelectCategory.class.getClassLoader());
    }

    public static final Creator<BeanAddMaterial> CREATOR = new Creator<BeanAddMaterial>() {
        @Override
        public BeanAddMaterial createFromParcel(Parcel source) {
            return new BeanAddMaterial(source);
        }

        @Override
        public BeanAddMaterial[] newArray(int size) {
            return new BeanAddMaterial[size];
        }
    };
}
