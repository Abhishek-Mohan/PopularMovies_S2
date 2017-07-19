package fastrackm.nanodegree.udacity.abhis.popularmovies_s2.models;

/**
 * Created by abhis on 7/18/2017.
 */

public class Reviews
{
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
}
