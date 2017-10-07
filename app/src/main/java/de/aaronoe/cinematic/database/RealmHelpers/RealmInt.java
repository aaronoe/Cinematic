package de.aaronoe.cinematic.database.RealmHelpers;

import org.parceler.Parcel;

import io.realm.RealmIntRealmProxy;
import io.realm.RealmObject;

@Parcel(implementations = {RealmIntRealmProxy.class},
        value = Parcel.Serialization.FIELD,
        analyze = { RealmInt.class })
public class RealmInt extends RealmObject {
    public int value;

    public RealmInt(int val) {
        this.value = val;
    }

    public RealmInt() {}
}