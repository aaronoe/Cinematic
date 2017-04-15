
package de.aaronoe.cinematic.Data.TvShow.FullSeason;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GuestStar implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("credit_id")
    @Expose
    private String creditId;
    @SerializedName("character")
    @Expose
    private String character;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("profile_path")
    @Expose
    private String profilePath;
    public final static Parcelable.Creator<GuestStar> CREATOR = new Creator<GuestStar>() {


        @SuppressWarnings({
            "unchecked"
        })
        public GuestStar createFromParcel(Parcel in) {
            GuestStar instance = new GuestStar();
            instance.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.creditId = ((String) in.readValue((String.class.getClassLoader())));
            instance.character = ((String) in.readValue((String.class.getClassLoader())));
            instance.order = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.profilePath = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public GuestStar[] newArray(int size) {
            return (new GuestStar[size]);
        }

    }
    ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(creditId);
        dest.writeValue(character);
        dest.writeValue(order);
        dest.writeValue(profilePath);
    }

    public int describeContents() {
        return  0;
    }

}
