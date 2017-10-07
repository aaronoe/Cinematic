package de.aaronoe.cinematic.database.RealmHelpers;

import android.os.Parcel;
import android.os.Parcelable;

import org.parceler.Parcels;
import org.parceler.TypeRangeParcelConverter;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RealmListParcelConverter
        implements TypeRangeParcelConverter<RealmList<? extends RealmObject>,
                RealmList<? extends RealmObject>> {
    private static final int NULL = -1;

    @Override
    public void toParcel(RealmList<? extends RealmObject> input, Parcel parcel) {
        parcel.writeInt(input == null ? NULL : input.size());
        if (input != null) {
            for (RealmObject item : input) {
                parcel.writeParcelable(Parcels.wrap(item), 0);
            }
        }
    }

    @Override
    public RealmList fromParcel(Parcel parcel) {
        int size = parcel.readInt();
        RealmList list = new RealmList();
        for (int i=0; i<size; i++) {
            Parcelable parcelable = parcel.readParcelable(getClass().getClassLoader());
            list.add((RealmObject) Parcels.unwrap(parcelable));
        }
        return list;
    }
}