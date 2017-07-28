package de.aaronoe.cinematic.ui.showdetail;

import java.util.List;

import de.aaronoe.cinematic.model.Crew.Credits;
import de.aaronoe.cinematic.model.TvShow.FullShow.TvShowFull;
import de.aaronoe.cinematic.model.TvShow.TvShow;

/**
 * Created by private on 7/23/17.
 */

public class ShowDetailContract {

    public interface View {
        void showInfo(TvShowFull showFull);
        void showCast(Credits credits);
        void showSimilar(List<TvShow> similarList);
        void showErrorInfo();
        void showErrorCast();
        void showErrorSimilar();
    }

    interface Presenter {
        void downloadInfo(int showId);
        void downloadCast(int showId);
        void downloadSimilar(int showId);
    }

}