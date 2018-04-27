package util;

public class WildCardMatch {
    public  static  boolean match(String s, String p) {
        int is = 0;
        int ip = 0;

        int press = 0;
        int presp = 0;

        boolean backstrack = false;
        for( is = 0; is < s.length(); ){
            if( ip == p.length()){
                if(backstrack == false){
                    return false;
                }else if(p.charAt(p.length()-1) == '*'){
                    return true;
                }
                else {
                    ip = presp;
                    is = ++press;
                }
            }
            if(p.charAt(ip) == '?'){
                is++;
                ip++;
            }else if(p.charAt(ip) == '*'){
                presp = ++ip;
                press = is;
                backstrack = true;
            }else{
                if(p.charAt(ip) == s.charAt(is)){
                    is++;
                    ip++;
                }else if(backstrack){
                    ip = presp;
                    is = ++press;
                }else{
                    return false;
                }
            }
        }
        while(ip <= p.length() - 1 && p.charAt(ip) == '*' ){
            ip ++;
            if( ip == p.length()){
                break;
            }
        }
        return ip == p.length();
    }
}
