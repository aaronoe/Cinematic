
package de.aaronoe.popularmovies.Data.Crew;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Crew implements Parcelable
{

    @SerializedName("credit_id")
    @Expose
    private String creditId;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("job")
    @Expose
    private String job;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_path")
    @Expose
    private Object profilePath;
    public final static Parcelable.Creator<Crew> CREATOR = new Creator<Crew>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Crew createFromParcel(Parcel in) {
            Crew instance = new Crew();
            instance.creditId = ((String) in.readValue((String.class.getClassLoader())));
            instance.department = ((String) in.readValue((String.class.getClassLoader())));
            instance.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.job = ((String) in.readValue((String.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.profilePath = ((Object) in.readValue((Object.class.getClassLoader())));
            return instance;
        }

        public Crew[] newArray(int size) {
            return (new Crew[size]);
        }

    }
    ;

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(Object profilePath) {
        this.profilePath = profilePath;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(creditId);
        dest.writeValue(department);
        dest.writeValue(id);
        dest.writeValue(job);
        dest.writeValue(name);
        dest.writeValue(profilePath);
    }

    public int describeContents() {
        return  0;
    }

}
