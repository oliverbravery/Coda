package net.oliverbravery.coda.config;
import net.oliverbravery.coda.utilities.Utils;
import java.io.*;

public class Config {
    public static String configPath = "config/CodaConfig.config";

    public static void Initialize() {
        File f = new File(configPath);
        if(!f.exists()) {
            try {
                f.createNewFile();
            }
            catch(Exception e) {}
        }
        LoadConfigSettings();
    }

    public static void LoadConfigSettings() {
        if(Utils.SWITCHEROO_INSTALLED) {
            Config.SetValue("AutoSwapToolsEnabled", "false");
            Config.SetValue("AutoSaveToolEnabled", "false");
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
