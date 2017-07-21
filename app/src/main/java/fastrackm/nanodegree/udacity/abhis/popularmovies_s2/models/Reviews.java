package fastrackm.nanodegree.udacity.abhis.popularmovies_s2.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abhis on 7/18/2017.
 */
public class Reviews implements Parcelable {
    private String mAuthor;
    private String mContent;

    public Reviews()
    {

    }

    public Reviews(String author, String content)
    {
        this.mAuthor = author;
        this.mContent = content;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    protected Reviews(Parcel in) {
        mAuthor = in.readString();
        mContent = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthor);
        dest.writeString(mContent);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Reviews> CREATOR = new Parcelable.Creator<Reviews>() {
        @Override
        public Reviews createFromParcel(Parcel in) {
            return new Reviews(in);
        }

        @Override
        public Reviews[] newArray(int size) {
            return new Reviews[size];
        }
    };
}