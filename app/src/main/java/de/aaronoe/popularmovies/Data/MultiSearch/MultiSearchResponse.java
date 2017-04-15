
package de.aaronoe.popularmovies.Data.MultiSearch;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MultiSearchResponse implements Parcelable
{

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("searchItems")
    @Expose
    private List<MultiSearchItem> searchItems = new ArrayList<MultiSearchItem>();
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    public final static Parcelable.Creator<MultiSearchResponse> CREATOR = new Creator<MultiSearchResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public MultiSearchResponse createFromParcel(Parcel in) {
            MultiSearchResponse instance = new MultiSearchResponse();
            instance.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
            in.readList(instance.searchItems, (MultiSearchItem.class.getClassLoader()));
            instance.totalResults = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.totalPages = ((Integer) in.readValue((Integer.class.getClassLoader())));
            return instance;
        }

        public MultiSearchResponse[] newArray(int size) {
            return (new MultiSearchResponse[size]);
        }

    }
    ;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<MultiSearchItem> getSearchItems() {
        return searchItems;
    }

    public void setSearchItems(List<MultiSearchItem> searchItems) {
        this.searchItems = searchItems;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(page);
        dest.writeList(searchItems);
        dest.writeValue(totalResults);
        dest.writeValue(totalPages);
    }

    public int describeContents() {
        return  0;
    }

}
