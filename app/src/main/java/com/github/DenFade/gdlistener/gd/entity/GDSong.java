package com.github.DenFade.gdlistener.gd.entity;

import java.util.HashMap;
import java.util.Map;

public final class GDSong extends GDEntity{

    String title;
    String author;
    String size;
    String url;
    boolean isCustom;

    public static final Map<Integer, GDSong> AUDIO = new HashMap<Integer, GDSong>();

    static {
        AUDIO.put(0, new GDSong((long) 0, "Stereo Madness", "ForeverBound", "??MB", "", false));
        AUDIO.put(1, new GDSong((long) 1, "Back On Track", "DJVI", "??MB", "", false));
        AUDIO.put(2, new GDSong((long) 2, "Polargeist", "Step", "??MB", "", false));
        AUDIO.put(3, new GDSong((long) 3, "Dry Out", "DJVI", "??MB", "", false));
        AUDIO.put(4, new GDSong((long) 4, "Base After Base", "DJVI", "??MB", "", false));
        AUDIO.put(5, new GDSong((long) 5, "Cant Let Go", "DJVI", "??MB", "", false));
        AUDIO.put(6, new GDSong((long) 6, "Jumper", "Waterflame", "??MB", "", false));
        AUDIO.put(7, new GDSong((long) 7, "Time Machine", "Waterflame", "??MB", "", false));
        AUDIO.put(8, new GDSong((long) 8, "Cycles", "DJVI", "??MB", "", false));
        AUDIO.put(9, new GDSong((long) 9, "xStep", "DJVI", "??MB", "", false));
        AUDIO.put(10, new GDSong((long) 10, "Clutterfunk", "Waterflame", "??MB", "", false));
        AUDIO.put(11, new GDSong((long) 11, "Theory of Everything", "DJ-Nate", "??MB", "", false));
        AUDIO.put(12, new GDSong((long) 12, "Electroman Adventures", "Waterflame", "??MB", "", false));
        AUDIO.put(13, new GDSong((long) 13, "Clubstep", "DJ-Nate", "??MB", "", false));
        AUDIO.put(14, new GDSong((long) 14, "Electrodynamix", "DJ-Nate", "??MB", "", false));
        AUDIO.put(15, new GDSong((long) 15, "Hexagon Force", "Waterflame", "??MB", "", false));
        AUDIO.put(16, new GDSong((long) 16, "Blast Processing", "Waterflame", "??MB", "", false));
        AUDIO.put(17, new GDSong((long) 17, "Theory of Everything 2", "DJ-Nate", "??MB", "", false));
        AUDIO.put(18, new GDSong((long) 18, "Geometrical Dominator", "Waterflame", "??MB", "", false));
        AUDIO.put(19, new GDSong((long) 19, "Deadlocked", "F-777", "??MB", "", false));
        AUDIO.put(20, new GDSong((long) 20, "Fingerdash", "MDK", "??MB", "", false));
    }


    public GDSong(long id, String title, String author, String size, String url, boolean isCustom){
        super(id);
        this.title = title;
        this.author = author;
        this.size = size;
        this.url = url;
        this.isCustom = isCustom;
    }

    public static GDSong empty(Long id){
        return new GDSong(id, "-", "Unknown", "??MB", "", true);
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public boolean isCustom() {
        return isCustom;
    }

    @Override
    public String toString() {
        return "GDSong[id=" + id + ", title=" + title + ", author=" + author + ", size=" + size + ", url=" + url + ", isCustom=" + isCustom + "]";
    }
}
