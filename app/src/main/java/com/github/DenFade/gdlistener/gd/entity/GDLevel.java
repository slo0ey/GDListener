package com.github.DenFade.gdlistener.gd.entity;

public final class GDLevel extends GDEntity {

    private String name;
    private String creatorName;
    private int likes;
    private int downloads;
    private int featuredScore;
    private int stars;
    private int difficulty;
    private int demonDifficulty;
    private boolean isDemon;
    private boolean isAuto;
    private long originalId;
    private boolean isEpic;
    private int coins;
    private boolean hasVerifiedCoin;
    private int objectCount;
    private GDSong song;

    public GDLevel(Long id, String name, String creatorName, int likes, int downloads, int featuredScore, int stars, int difficulty, int demonDifficulty, boolean isDemon, boolean isAuto, long originalId, boolean isEpic, int coins, boolean hasVerifiedCoin, int objectCount, GDSong song) {
        super(id);
        this.name = name;
        this.creatorName = creatorName;
        this.likes = likes;
        this.downloads = downloads;
        this.featuredScore = featuredScore;
        this.stars = stars;
        this.difficulty = difficulty;
        this.demonDifficulty = demonDifficulty;
        this.isDemon = isDemon;
        this.isAuto = isAuto;
        this.originalId = originalId;
        this.isEpic = isEpic;
        this.coins = coins;
        this.hasVerifiedCoin = hasVerifiedCoin;
        this.objectCount = objectCount;
        this.song = song;
    }

    public static GDLevel empty(long id){
        return new GDLevel(
                id,
                "-",
                "Unknown",
                0,
                0,
                0,
                0,
                0,
                0,
                false,
                false,
                0,
                false,
                0,
                false,
                0,
                GDSong.empty((long) 0)
        );
    }

    public String getName(){
        return name;
    }

    public String getCreatorName(){
        return creatorName;
    }

    public int getLikes(){
        return likes;
    }

    public int getDownloads(){
        return downloads;
    }

    public int getFeaturedScore(){
        return featuredScore;
    }

    public int getStars(){
        return stars;
    }

    public int getDifficulty(){
        return difficulty;
    }

    public int getDemonDifficulty(){
        return demonDifficulty;
    }

    public boolean isDemon(){
        return isDemon;
    }

    public boolean isAuto(){
        return isAuto;
    }

    public long getOriginalId(){
        return originalId;
    }

    public boolean isEpic() {
        return isEpic;
    }

    public int getCoins() {
        return coins;
    }

    public boolean hasVerifiedCoin() {
        return hasVerifiedCoin;
    }

    public int getObjectCount() {
        return objectCount;
    }

    public GDSong getSong(){
        return song;
    }

    @Override
    public String toString() {
        return "GDLevel[id=" + id + ", likes=" + likes + ", downloads=" + downloads + ", featuredScore=" + featuredScore + ", stars=" + stars + ", difficulty=" + difficulty + ", demonDifficulty=" + demonDifficulty + ", isDemon=" + isDemon
                + ", isAuto=" + isAuto + ",originalId=" + originalId + ", isEpic=" + isEpic + ", coins=" + coins + ", hasVerifiedCoin=" + hasVerifiedCoin + ", objectCount=" + objectCount + ", song=" + song + "]";
    }
}
