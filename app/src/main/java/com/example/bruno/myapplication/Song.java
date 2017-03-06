package com.example.bruno.myapplication;

public class Song {
    public String pathSong;
    public String nameSong;

    public void addSong(String pathSong, String nameSong) {
        this.pathSong= pathSong;
        this.nameSong = nameSong;
    }

    public String getName(){
        return this.nameSong;
    }

    @Override
    public String toString(){
        return this.nameSong;
    }
}
