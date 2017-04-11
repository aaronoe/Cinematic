
package de.aaronoe.popularmovies.Data.TvShow.FullSeason;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Episode implements Parcelable
{

    @SerializedName("air_date")
    @Expose
    private String airDate;
    @SerializedName("crew")
    @Expose
    private List<Crew> crew = null;
    @SerializedName("episode_number")
    @Expose
    private Integer episodeNumber;
    @SerializedName("guest_stars")
    @Expose
    private List<GuestStar> guestStars = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("production_code")
    @Expose
    private String productionCode;
    @SerializedName("season_number")
    @Expose
    private Integer seasonNumber;
    @SerializedName("still_path")
    @Expose
    private String stillPath;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    public final static Parcelable.Creator<Episode> CREATOR = new Creator<Episode>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Episode createFromParcel(Parcel in) {
            Episode instance = new Episode();
            instance.airDate = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.crew, (de.aaronoe.popularmovies.Data.TvShow.FullSeason.Crew.class.getClassLoader()));
            instance.episodeNumber = ((Integer) in.readValue((Integer.class.getClassLoader())));
            in.readList(instance.guestStars, (de.aaronoe.popularmovies.Data.TvShow.FullSeason.GuestStar.class.getClassLoader()));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.overview = ((String) in.readValue((String.class.getClassLoader())));
            instance.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.productionCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.seasonNumber = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.stillPath = ((String) in.readValue((String.class.getClassLoader())));
            instance.voteAverage = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.voteCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
            return instance;
        }

        public Episode[] newArray(int size) {
            return (new Episode[size]);
        }

    }
    ;

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public List<Crew> getCrew() {
        return crew;
    }

    public void setCrew(List<Crew> crew) {
        this.crew = crew;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public List<GuestStar> getGuestStars() {
        return guestStars;
    }

    public void setGuestStars(List<GuestStar> guestStars) {
        this.guestStars = guestStars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductionCode() {
        return productionCode;
    }

    public void setProductionCode(String productionCode) {
        this.productionCode = productionCode;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public String getStillPath() {
        return stillPath;
    }

    public void setStillPath(String stillPath) {
        this.stillPath = stillPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(airDate);
        dest.writeList(crew);
        dest.writeValue(episodeNumber);
        dest.writeList(guestStars);
        dest.writeValue(name);
        dest.writeValue(overview);
        dest.writeValue(id);
        dest.writeValue(productionCode);
        dest.writeValue(seasonNumber);
        dest.writeValue(stillPath);
        dest.writeValue(voteAverage);
        dest.writeValue(voteCount);
    }

    public int describeContents() {
        return  0;
    }

}
