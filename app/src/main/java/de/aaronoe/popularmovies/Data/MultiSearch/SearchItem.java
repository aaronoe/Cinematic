
package de.aaronoe.popularmovies.Data.MultiSearch;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SearchItem implements Parcelable
{

    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("media_type")
    @Expose
    private String mediaType;
    @SerializedName("first_air_date")
    @Expose
    private String firstAirDate;
    @SerializedName("origin_country")
    @Expose
    private List<String> originCountry = new ArrayList<String>();
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = new ArrayList<Integer>();
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("original_name")
    @Expose
    private String originalName;
    @SerializedName("adult")
    @Expose
    private Boolean adult;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("video")
    @Expose
    private Boolean video;
    @SerializedName("profile_path")
    @Expose
    private String profilePath;
    @SerializedName("known_for")
    @Expose
    private List<KnownFor> knownFor = new ArrayList<KnownFor>();
    public final static Parcelable.Creator<SearchItem> CREATOR = new Creator<SearchItem>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SearchItem createFromParcel(Parcel in) {
            SearchItem instance = new SearchItem();
            instance.posterPath = ((String) in.readValue((String.class.getClassLoader())));
            instance.popularity = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.overview = ((String) in.readValue((String.class.getClassLoader())));
            instance.backdropPath = ((String) in.readValue((String.class.getClassLoader())));
            instance.voteAverage = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.mediaType = ((String) in.readValue((String.class.getClassLoader())));
            instance.firstAirDate = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.originCountry, (java.lang.String.class.getClassLoader()));
            in.readList(instance.genreIds, (java.lang.Integer.class.getClassLoader()));
            instance.originalLanguage = ((String) in.readValue((String.class.getClassLoader())));
            instance.voteCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.originalName = ((String) in.readValue((String.class.getClassLoader())));
            instance.adult = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.releaseDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.originalTitle = ((String) in.readValue((String.class.getClassLoader())));
            instance.title = ((String) in.readValue((String.class.getClassLoader())));
            instance.video = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.profilePath = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.knownFor, (de.aaronoe.popularmovies.Data.MultiSearch.KnownFor.class.getClassLoader()));
            return instance;
        }

        public SearchItem[] newArray(int size) {
            return (new SearchItem[size]);
        }

    }
    ;

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public List<String> getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(List<String> originCountry) {
        this.originCountry = originCountry;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public List<KnownFor> getKnownFor() {
        return knownFor;
    }

    public void setKnownFor(List<KnownFor> knownFor) {
        this.knownFor = knownFor;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(posterPath);
        dest.writeValue(popularity);
        dest.writeValue(id);
        dest.writeValue(overview);
        dest.writeValue(backdropPath);
        dest.writeValue(voteAverage);
        dest.writeValue(mediaType);
        dest.writeValue(firstAirDate);
        dest.writeList(originCountry);
        dest.writeList(genreIds);
        dest.writeValue(originalLanguage);
        dest.writeValue(voteCount);
        dest.writeValue(name);
        dest.writeValue(originalName);
        dest.writeValue(adult);
        dest.writeValue(releaseDate);
        dest.writeValue(originalTitle);
        dest.writeValue(title);
        dest.writeValue(video);
        dest.writeValue(profilePath);
        dest.writeList(knownFor);
    }

    public int describeContents() {
        return  0;
    }

}
