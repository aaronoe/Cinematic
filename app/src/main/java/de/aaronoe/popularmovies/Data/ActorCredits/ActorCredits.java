
package de.aaronoe.popularmovies.Data.ActorCredits;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActorCredits implements Parcelable
{

    @SerializedName("cast")
    @Expose
    private List<Cast> cast = null;
    @SerializedName("crew")
    @Expose
    private List<Crew> crew = null;
    @SerializedName("id")
    @Expose
    private Integer id;
    public final static Parcelable.Creator<ActorCredits> CREATOR = new Creator<ActorCredits>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ActorCredits createFromParcel(Parcel in) {
            ActorCredits instance = new ActorCredits();
            in.readList(instance.cast, (de.aaronoe.popularmovies.Data.ActorCredits.Cast.class.getClassLoader()));
            in.readList(instance.crew, (de.aaronoe.popularmovies.Data.ActorCredits.Crew.class.getClassLoader()));
            instance.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            return instance;
        }

        public ActorCredits[] newArray(int size) {
            return (new ActorCredits[size]);
        }

    }
    ;

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public List<Crew> getCrew() {
        return crew;
    }

    public void setCrew(List<Crew> crew) {
        this.crew = crew;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(cast);
        dest.writeList(crew);
        dest.writeValue(id);
    }

    public int describeContents() {
        return  0;
    }

}
