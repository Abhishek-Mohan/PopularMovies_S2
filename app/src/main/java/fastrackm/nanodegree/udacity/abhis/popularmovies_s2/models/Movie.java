package fastrackm.nanodegree.udacity.abhis.popularmovies_s2.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhis on 6/13/2017.
 */

public class Movie implements Serializable
{
    private String mDBID;
    private String mtitle;
    private String mPoster;
    private String mBackDrop;
    private String mPlot;
    private double mUserRating;
    private String mReleaseDate;
    private ArrayList<String> mMovieTrailers;
    private ArrayList<Reviews> mReviews;

    public Movie()
    {

    }


    public Movie(String mDBID, String mtitle, String mPoster, String mBackDrop, String mPlot, double mUserRating, String mReleaseDate)
    {
        this.mDBID = mDBID;
        this.mtitle = mtitle;
        this.mPoster = mPoster;
        this.mBackDrop = mBackDrop;
        this.mPlot = mPlot;
        this.mUserRating = mUserRating;
        this.mReleaseDate = mReleaseDate;
    }

    public Movie(String mDBID, String mtitle, String mPoster, String mBackDrop, String mPlot, double mUserRating, String mReleaseDate, ArrayList<String> movietitles, ArrayList<Reviews> reviews) {
        this.mDBID = mDBID;
        this.mtitle = mtitle;
        this.mPoster = mPoster;
        this.mBackDrop = mBackDrop;
        this.mPlot = mPlot;
        this.mUserRating = mUserRating;
        this.mReleaseDate = mReleaseDate;
        this.mMovieTrailers = movietitles;
        this.mReviews = reviews;
    }

    public ArrayList<String> getmMovieTrailers() {
        return mMovieTrailers;
    }

    public void setmMovieTrailers(ArrayList<String> mMovieTrailers) {
        this.mMovieTrailers = mMovieTrailers;
    }

    public ArrayList<Reviews> getmReviews() {
        return mReviews;
    }

    public void setmReviews(ArrayList<Reviews> mReviews) {
        this.mReviews = mReviews;
    }


    public String getmDBID() {
        return mDBID;
    }

    public void setmDBID(String mDBID) {
        this.mDBID = mDBID;
    }

    public String getMtitle() {
        return mtitle;
    }

    public void setMtitle(String mtitle) {
        this.mtitle = mtitle;
    }

    public String getmPoster() {
        return mPoster;
    }

    public void setmPoster(String mPoster) {
        this.mPoster = mPoster;
    }

    public String getmBackDrop()
    {
        return mBackDrop;
    }

    public void setmBackDrop(String mBackDrop)
    {
        this.mBackDrop = mBackDrop;
    }

    public String getmPlot() {
        return mPlot;
    }

    public void setmPlot(String mPlot) {
        this.mPlot = mPlot;
    }

    public double getmUserRating() {
        return mUserRating;
    }

    public void setmUserRating(double mUserRating) {
        this.mUserRating = mUserRating;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }
}
