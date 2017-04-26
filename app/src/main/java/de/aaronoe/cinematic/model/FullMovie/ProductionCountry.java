
package de.aaronoe.cinematic.model.FullMovie;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductionCountry implements Parcelable
{

    @SerializedName("iso_3166_1")
    @Expose
    private String iso31661;
    @SerializedName("name")
    @Expose
    private String name;
    public final static Parcelable.Creator<ProductionCountry> CREATOR = new Creator<ProductionCountry>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ProductionCountry createFromParcel(Parcel in) {
            ProductionCountry instance = new ProductionCountry();
            instance.iso31661 = ((String) in.readValue((String.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public ProductionCountry[] newArray(int size) {
            return (new ProductionCountry[size]);
        }

    }
    ;

    public String getIso31661() {
        return iso31661;
    }

    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(iso31661);
        dest.writeValue(name);
    }

    public int describeContents() {
        return  0;
    }

}
