
package de.aaronoe.popularmovies.Data.TvShow.FullShow;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShowFull implements Parcelable
{

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("created_by")
    @Expose
    private List<CreatedBy> createdBy = null;
    @SerializedName("episode_run_time")
    @Expose
    private List<Integer> episodeRunTime = null;
    @SerializedName("first_air_date")
    @Expose
    private String firstAirDate;
    @SerializedName("genres")
    @Expose
    private List<Genre> genres = null;
    @SerializedName("homepage")
    @Expose
    private String homepage;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("in_production")
    @Expose
    private Boolean inProduction;
    @SerializedName("languages")
    @Expose
    private List<String> languages = null;
    @SerializedName("last_air_date")
    @Expose
    private String lastAirDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("networks")
    @Expose
    private List<Network> networks = null;
    @SerializedName("number_of_episodes")
    @Expose
    private Integer numberOfEpisodes;
    @SerializedName("number_of_seasons")
    @Expose
    private Integer numberOfSeasons;
    @SerializedName("origin_country")
    @Expose
    private List<String> originCountry = null;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("original_name")
    @Expose
    private String originalName;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("production_companies")
    @Expose
    private List<ProductionCompany> productionCompanies = null;
    @SerializedName("seasons")
    @Expose
    private List<Season> seasons = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    public final static Parcelable.Creator<TvShowFull> CREATOR = new Creator<TvShowFull>() {


        @SuppressWarnings({
            "unchecked"
        })
        public TvShowFull createFromParcel(Parcel in) {
            TvShowFull instance = new TvShowFull();
            instance.backdropPath = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.createdBy, (de.aaronoe.popularmovies.Data.TvShow.FullShow.CreatedBy.class.getClassLoader()));
            in.readList(instance.episodeRunTime, (java.lang.Integer.class.getClassLoader()));
            instance.firstAirDate = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.genres, (de.aaronoe.popularmovies.Data.TvShow.FullShow.Genre.class.getClassLoader()));
            instance.homepage = ((String) in.readValue((String.class.getClassLoader())));
            instance.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.inProduction = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            in.readList(instance.languages, (java.lang.String.class.getClassLoader()));
            instance.lastAirDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.networks, (de.aaronoe.popularmovies.Data.TvShow.FullShow.Network.class.getClassLoader()));
            instance.numberOfEpisodes = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.numberOfSeasons = ((Integer) in.readValue((Integer.class.getClassLoader())));
            in.readList(instance.originCountry, (java.lang.String.class.getClassLoader()));
            instance.originalLanguage = ((String) in.readValue((String.class.getClassLoader())));
            instance.originalName = ((String) in.readValue((String.class.getClassLoader())));
            instance.overview = ((String) in.readValue((String.class.getClassLoader())));
            instance.popularity = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.posterPath = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.productionCompanies, (de.aaronoe.popularmovies.Data.TvShow.FullShow.ProductionCompany.class.getClassLoader()));
            in.readList(instance.seasons, (de.aaronoe.popularmovies.Data.TvShow.FullShow.Season.class.getClassLoader()));
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            instance.type = ((String) in.readValue((String.class.getClassLoader())));
            instance.voteAverage = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.voteCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
            return instance;
        }

        public TvShowFull[] newArray(int size) {
            return (new TvShowFull[size]);
        }

    }
    ;

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public List<CreatedBy> getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(List<CreatedBy> createdBy) {
        this.createdBy = createdBy;
    }

    public List<Integer> getEpisodeRunTime() {
        return episodeRunTime;
    }

    public void setEpisodeRunTime(List<Integer> episodeRunTime) {
        this.episodeRunTime = episodeRunTime;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getInProduction() {
        return inProduction;
    }

    public void setInProduction(Boolean inProduction) {
        this.inProduction = inProduction;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public String getLastAirDate() {
        return lastAirDate;
    }

    public void setLastAirDate(String lastAirDate) {
        this.lastAirDate = lastAirDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Network> getNetworks() {
        return networks;
    }

    public void setNetworks(List<Network> networks) {
        this.networks = networks;
    }

    public Integer getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public void setNumberOfEpisodes(Integer numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }

    public Integer getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(Integer numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public List<String> getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(List<String> originCountry) {
        this.originCountry = originCountry;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        dest.writeValue(backdropPath);
        dest.writeList(createdBy);
        dest.writeList(episodeRunTime);
        dest.writeValue(firstAirDate);
        dest.writeList(genres);
        dest.writeValue(homepage);
        dest.writeValue(id);
        dest.writeValue(inProduction);
        dest.writeList(languages);
        dest.writeValue(lastAirDate);
        dest.writeValue(name);
        dest.writeList(networks);
        dest.writeValue(numberOfEpisodes);
        dest.writeValue(numberOfSeasons);
        dest.writeList(originCountry);
        dest.writeValue(originalLanguage);
        dest.writeValue(originalName);
        dest.writeValue(overview);
        dest.writeValue(popularity);
        dest.writeValue(posterPath);
        dest.writeList(productionCompanies);
        dest.writeList(seasons);
        dest.writeValue(status);
        dest.writeValue(type);
        dest.writeValue(voteAverage);
        dest.writeValue(voteCount);
    }

    public int describeContents() {
        return  0;
    }

}
