package my.test.testapp;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageProduct implements Parcelable {

    private String mUrl;

    public ImageProduct(String url) {
        mUrl = url;
    }

    protected ImageProduct(Parcel in) {
        mUrl = in.readString();
    }

    public static final Creator<ImageProduct> CREATOR = new Creator<ImageProduct>() {
        @Override
        public ImageProduct createFromParcel(Parcel in) {
            return new ImageProduct(in);
        }

        @Override
        public ImageProduct[] newArray(int size) {
            return new ImageProduct[size];
        }
    };

    public String getUrl() {
        return mUrl;
    }

    public void setmUrl(String url) {
        mUrl = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
