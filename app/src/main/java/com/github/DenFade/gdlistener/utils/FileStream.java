package com.github.DenFade.gdlistener.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileStream {
    public static String ROOT_DIR = "";

    private FileStream(){

    }

    public static String read(String path) throws IOException{
        FileReader fr = new FileReader(path);
        BufferedReader buf = new BufferedReader(fr);
        StringBuilder result = new StringBuilder(buf.readLine());
        String t;
        while((t=buf.readLine()) != null){
            result.append("\\n").append(t);
        }
        return result.toString();
    }

    public static void write(String path, String text) throws IOException{
        FileWriter fw = new FileWriter(path);
        fw.write(text);
        fw.flush();
    }

    public static void remove(String path){
        File f = new File(path);
        if(f.exists()) f.delete();
    }

    public static void append(String path, String text) throws IOException{
        String file = read(path);
        file += text;
        write(path, file);
    }
}
