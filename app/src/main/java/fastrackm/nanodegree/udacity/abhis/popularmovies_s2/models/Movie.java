package fastrackm.nanodegree.udacity.abhis.popularmovies_s2.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhis on 6/13/2017.
 */
public class Movie implements Parcelable {
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

    protected Movie(Parcel in) {
        mDBID = in.readString();
        mtitle = in.readString();
        mPoster = in.readString();
        mBackDrop = in.readString();
        mPlot = in.readString();
        mUserRating = in.readDouble();
        mReleaseDate = in.readString();
        if (in.readByte() == 0x01) {
            mMovieTrailers = new ArrayList<String>();
            in.readList(mMovieTrailers, String.class.getClassLoader());
        } else {
            mMovieTrailers = null;
        }
        if (in.readByte() == 0x01) {
            mReviews = new ArrayList<Reviews>();
            in.readList(mReviews, Reviews.class.getClassLoader());
        } else {
            mReviews = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDBID);
        dest.writeString(mtitle);
        dest.writeString(mPoster);
        dest.writeString(mBackDrop);
        dest.writeString(mPlot);
        dest.writeDouble(mUserRating);
        dest.writeString(mReleaseDate);
        if (mMovieTrailers == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mMovieTrailers);
        }
        if (mReviews == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mReviews);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}