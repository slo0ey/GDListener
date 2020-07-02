package com.github.DenFade.gdlistener.utils;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
    public static final String ROOT_DIR = "\\/data\\/data\\/com.github.DenFade.gdlistener\\/";

    private FileUtils(){

    }

    public static String readFile(String path) throws IOException{
        FileReader fr = new FileReader(path);
        BufferedReader buf = new BufferedReader(fr);
        StringBuilder result = new StringBuilder(buf.readLine());
        String t;
        while((t=buf.readLine()) != null){
            result.append("\\n").append(t);
        }
        return result.toString();
    }

    public static void writeFile(String path, String text) throws IOException{
        FileWriter fw = new FileWriter(path);
        fw.write(text);
        fw.flush();
    }

    public static void appendFile(String path, String text) throws IOException{
        String file = readFile(path);
        file += text;
        writeFile(path, file);
    }
}
