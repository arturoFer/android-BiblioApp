package org.afgl.biblioapp.entities;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.afgl.biblioapp.domain.BookWrapper;

/**
 * Created by arturo on 09/06/2017.
 * Clase que almacena el titulo del capitulo y su uri
 */

public class Capitulo implements Parcelable {

    private String title;
    private String location;

    public Capitulo(String title, String location){
        this.title = title;
        this.location = location;
    }

    private Capitulo(Parcel in){
        title = in.readString();
        location = in.readString();
    }

    public String getTitle(){
        return this.title;
    }

    public Uri getUri(){
        return BookWrapper.resourceName2Url(location);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(location);
    }

    public static final Parcelable.Creator<Capitulo> CREATOR =
            new Parcelable.Creator<Capitulo>(){
                public  Capitulo createFromParcel(Parcel in){
                    return new Capitulo(in);
                }

                public Capitulo[] newArray(int size){
                    return new Capitulo[size];
                }
            };
}
