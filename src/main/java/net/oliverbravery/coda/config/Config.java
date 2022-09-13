package net.oliverbravery.coda.config;

import net.oliverbravery.coda.Coda;
import net.oliverbravery.coda.utilities.Utils;

import java.io.*;
import java.lang.reflect.Field;

public class Config {
    public static String configPath = "config/CodaConfig.config";

    public Config() {
        File f = new File(configPath);
        if(!f.exists()) {
            try {
                f.createNewFile();
            }
            catch(Exception e) {}
        }
        LoadConfigSettings();
    }

    public void LoadConfigSettings() {
        Coda.autoFish.autoFishEnabled = Boolean.parseBoolean(Config.GetValue("AutoFishEnabled", "true"));
        Coda.autoSaveTool.isEnabled  = Boolean.parseBoolean(Config.GetValue("AutoSaveToolEnabled", "true"));
        Coda.autoSwapTools.isEnabled = Boolean.parseBoolean(Config.GetValue("AutoSwapToolsEnabled", "true"));
        Coda.codaButtonEnabled = Boolean.parseBoolean(Config.GetValue("CodaButtonEnabled", "true"));
        Coda.shulkerBoxUnloadEnabled = Boolean.parseBoolean(Config.GetValue("ShulkerBoxUnloadEnabled", "true"));
        if(Utils.SWITCHEROO_INSTALLED) {
            Coda.autoSwapTools.isEnabled = false;
            Coda.autoSaveTool.isEnabled = false;
        }
    }

    public static String GetValue(String key, String def) {
        String val = "";
        try
        {
            File file=new File(configPath);
            FileReader fr= new FileReader(file);
            BufferedReader br=new BufferedReader(fr);
            StringBuffer sb=new StringBuffer();
            String line;
            while((line=br.readLine())!=null)
            {
                String[] kv = line.split(":");
                if(kv[0].equals(key)) {
                    val = kv[1];
                }
            }
            fr.close();
        }
        catch (Exception e) {}
        if(val == "")
        {
            SetValue(key, def);
            return def;
        }
        else{return val;}
    }

    public static void SetValue(String key, String value) {
        try
        {
            boolean didSetValue = false;
            File file=new File(configPath);
            FileReader fr=new FileReader(file);
            BufferedReader br=new BufferedReader(fr);
            StringBuffer sb=new StringBuffer();
            String line;
            while((line=br.readLine())!=null)
            {
                if(line.contains(key)) {
                    didSetValue = true;
                    sb.append(String.format("%s:%s",key,value));
                }
                else {
                    sb.append(line);
                }
                sb.append("\n");
            }
            fr.close();
            if(!didSetValue) {
                sb.append(String.format("%s:%s",key,value));
                sb.append("\n");
            }
            try {
                FileWriter myWriter = new FileWriter(configPath);
                myWriter.write(sb.toString());
                myWriter.close();
            }
            catch(Exception e) {}
        }
        catch(Exception e){}
    }
}
