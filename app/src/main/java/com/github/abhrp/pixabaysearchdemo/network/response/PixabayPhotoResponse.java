package com.github.abhrp.pixabaysearchdemo.network.response;

import com.github.abhrp.pixabaysearchdemo.model.PixabayPhoto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by abhrp on 4/30/17.
 */

public class PixabayPhotoResponse implements Serializable {
    public int totalHits;
    public int total;
    public List<PixabayPhoto> hits;
}
