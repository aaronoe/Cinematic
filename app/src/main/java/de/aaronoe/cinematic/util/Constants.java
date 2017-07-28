package de.aaronoe.cinematic.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * Created by private on 7/23/17.
 */

public class Constants {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({BACKDROP_ENTER, POSTER_ENTER, NONE})
    public @interface TransitionMode {}
    public static final int BACKDROP_ENTER = 879;
    public static final int POSTER_ENTER = 300;
    public static final int NONE = 234;


}
