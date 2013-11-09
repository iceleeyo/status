package download;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class PhoneInfo {
	static Logger logger = Logger.getLogger(PhoneInfo.class);
    public String model;
    public String osversion;
    public PhoneOS os;

    static String BaseRegex = "^((samsung-gt-|lg-)*[\\w\\d]+)[^/]*/([0-9a-z\\.]+)\\s(\\(([^\\)]+)\\))*";
    static String DetailRegex = ";\\s?|(Build)";
    static String IOSVersionRegex = "OS\\s([\\d_]+)\\s";
    static String AndroidVersionRegex = "Android([^\\d]*\\d+\\.\\d+(?:\\.\\d+)?)";
    static String UCWebRegex = "[\\-_]\\w+;\\s*([^/;)]+)";
    static String NokiaRegex = "(nokia[^-/]+)";
    static String NokiaSpecialRegex = "((?:NokiaC|NokiaX)\\d*\\-\\d+)";

    public PhoneInfo(String model, String osversion, PhoneOS os) {
        this.model = model;
        this.os = os;
        this.osversion = osversion;
    }

    public PhoneInfo(String osversion, PhoneOS os) {
        this.model = null;
        this.os = os;
        this.osversion = osversion;
    }

    public static PhoneInfo Parse(String userAgent) {
    	try{
	        Map<PhoneOS, String> os = GetOS(userAgent);
	        PhoneOS phoneos = PhoneOS.Unspecified;
	        Pattern p = Pattern.compile(BaseRegex, Pattern.CASE_INSENSITIVE);
	        Matcher m = p.matcher(userAgent);
	        if (!m.find()) {
	            Set<PhoneOS> osSet = os.keySet();
	            for (PhoneOS key : osSet) {
	                return new PhoneInfo(GetModelFromSpecialUseragent(userAgent), os.get(key), key);
	            }
	        }
	
	        Set<PhoneOS> osSet = os.keySet();
	        for (PhoneOS key : osSet) {
	            phoneos = key;
	        }
	        String model;
	        String header = m.group(1);
	        if (!"Mozilla".equalsIgnoreCase(header)) {
	            model = GetModelFromSpecialUseragent(userAgent);
	            return new PhoneInfo(model.isEmpty() ? header : model, os.get(phoneos), phoneos);
	        }
	
	        String detail = m.group(5);
	        if(detail == null)
	        {
	        	detail = "";
	        }
	        model = GetModelFromDetail(phoneos, detail);
	        if (model == null || model.isEmpty()) {
	            model = GetModelFromSpecialUseragent(userAgent);
	        }
	        return new PhoneInfo(model.isEmpty() ? header : model, os.get(phoneos), phoneos);
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.info("userAgent = " + userAgent);
    	}
    	return null;
    }

    public static String GetModelFromSpecialUseragent(String userAgent)
    {
        // Try Symbian first
        String model = GetModelFromSymbian(userAgent);

        // If failed, Try UCWeb
        if ((model == null || model.isEmpty()) && userAgent.toLowerCase().contains("ucweb"))
        {
            Pattern p = Pattern.compile(UCWebRegex);
            Matcher m = p.matcher(userAgent);
            if (m.find())
            {
                model = m.group(1);
            }
        }
        return model;
    }

    public static String GetModelFromDetail(PhoneOS os, String detail) {
        String model = "";
        String[] parts = detail.split(DetailRegex);
        switch (os) {
            case Android:
                if (parts.length > 4) {
//                    model = Regex.Replace(parts[4],"\\s[\\d.]*", "");
                    model = parts[4].replace("\\s[\\d.]*", "");
                }
                break;
            case BlackBerry:
                if (parts.length > 2) {
                    model = parts[2];
                }
                break;
            case J2ME:
            case Symbian:
                model = GetModelFromSymbian(detail);
                break;
            case IOS:
                if (parts.length > 0) {
                    model = parts[0];
                }
                break;
        }
        return model;
    }

    private static String GetModelFromSymbian(String detail) {
        //SymbianOS/9.3; Series60/3.2 NokiaC5-00/062.001; Profile/MIDP-2.1 Configuration/CLDC-1.1
        Pattern firstPattern = Pattern.compile(NokiaSpecialRegex, Pattern.CASE_INSENSITIVE);
        Matcher m = firstPattern.matcher(detail);
        if (m.find()) {
            return m.group(1);
        }

        Pattern secondPattern = Pattern.compile(NokiaRegex, Pattern.CASE_INSENSITIVE);
        m = secondPattern.matcher(detail);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }

    public static Map<PhoneOS, String> GetOS(String userAgent) {
        Map<PhoneOS, String> osMap = new HashMap<PhoneOS, String>();
        String version = "N/A";
        if (userAgent == null || userAgent.isEmpty()) {
            osMap.put(PhoneOS.Unspecified, version);
            return osMap;
        }
        if (userAgent.toLowerCase().contains("symbian")) {
            osMap.put(PhoneOS.Symbian, version);
            return osMap;
        }
        if (userAgent.toLowerCase().contains("blackberry")) {
            osMap.put(PhoneOS.BlackBerry, version);
            return osMap;
        }
        if (userAgent.toLowerCase().contains("iphone") || userAgent.toLowerCase().contains("ipad")) {
            Pattern p = Pattern.compile(IOSVersionRegex, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(userAgent);
            if (m.find()) {
                version = m.group(1).replace("_", ".");
            }
            osMap.put(PhoneOS.IOS, version);
            return osMap;
        }
        if (userAgent.toLowerCase().contains("android")) {
            Pattern p = Pattern.compile(AndroidVersionRegex, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(userAgent);
            if (m.find()) {
                version = m.group(1);
            }
            osMap.put(PhoneOS.Android, version);
            return osMap;
        }
        if (userAgent.toLowerCase().contains("midp-2")) {
            osMap.put(PhoneOS.J2ME, version);
            return osMap;
        }
        osMap.put(PhoneOS.Unspecified, version);
        return osMap;
    }

    public static void main(String[] args) {
        String test = "Mozilla/5.0 (SymbianOS/9.3; Series60/3.2 NokiaC5-00/062.001; Profile/MIDP-2.1 Configuration/CLDC-1.1 ) AppleWebKit/525 (KHTML, like Gecko) Version/3.0 BrowserNG/7.2.6.2 3gpp-gba";

        PhoneInfo info = Parse(test);
        if (!info.model.equals("iPhone")) {
            System.out.println(info.model + "!!!");
        }
    }
}
