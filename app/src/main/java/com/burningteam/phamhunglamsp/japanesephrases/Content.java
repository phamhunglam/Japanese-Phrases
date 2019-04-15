package com.burningteam.phamhunglamsp.japanesephrases;

import android.net.Uri;

/**
 * Created by Hung Lam Pham on 1/28/2018.
 */

public class Content {
    private String target1;
    private  String target2;
    private String translate;
    private Uri audio;

    public Content(String target1, String target2, String translate, Uri audio) {
        this.target1 = target1;
        this.target2 = target2;
        this.translate = translate;
        this.audio = audio;
    }

    public String getTarget1() {
        return target1;
    }

    public void setTarget1(String target1) {
        this.target1 = target1;
    }

    public String getTarget2() {
        return target2;
    }

    public void setTarget2(String target2) {
        this.target2 = target2;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public Uri getAudio() {
        return audio;
    }

    public void setAudio(Uri audio) {
        this.audio = audio;
    }
}
