
package de.aaronoe.cinematic.model.FullMovie;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpokenLanguage implements Parcelable
{

    @SerializedName("iso_639_1")
    @Expose
    private String iso6391;
    @SerializedName("name")
    @Expose
    private String name;
    public final static Parcelable.Creator<SpokenLanguage> CREATOR = new Creator<SpokenLanguage>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SpokenLanguage createFromParcel(Parcel in) {
            SpokenLanguage instance = new SpokenLanguage();
            instance.iso6391 = ((String) in.readValue((String.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public SpokenLanguage[] newArray(int size) {
            return (new SpokenLanguage[size]);
        }

    }
    ;

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(iso6391);
        dest.writeValue(name);
    }

    public int describeContents() {
        return  0;
    }

}
