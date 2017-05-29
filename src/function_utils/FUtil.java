package function_utils;

import java.util.Vector;

class FUtil {

    static Vector<String> depthSplit(String string, char regex){
        Vector<String> split = new Vector<>();
        int last = -1, depth = 0;
        for(int i = 0; i < string.length(); i ++){
            if((string.charAt(i) == regex && depth == 0)){
                split.add(string.substring(last + 1, i));
                last = i;
            }
            else if(i == string.length() - 1){
                split.add(string.substring(last + 1, i + 1));
            }
            else if(string.charAt(i) == '('){
                depth ++;
            }
            else if(string.charAt(i) == ')'){
                depth --;
            }
        }
        return split;
    }

    static int depthSplitSize(String string, char regex){
        int size = 0, depth = 0;
        for(int i = 0; i < string.length(); i ++){
            if((string.charAt(i) == regex && depth == 0) || i == string.length() - 1){
                size ++;
            }
            else if(string.charAt(i) == '('){
                depth ++;
            }
            else if(string.charAt(i) == ')'){
                depth --;
            }
        }
        return size;
    }

    static String sanitize(String string){
        string = string.replace(" ", "");
        string = string.replace(")(", ")*(");
        StringBuilder result = new StringBuilder("");
        int depth = 0;
        for(int i = 0; i < string.length(); i ++){
            if(string.charAt(i) == '-' && depth == 0){
                result.append("+(-1)*");
            }
            else{
                result.append(string.charAt(i));
            }
            if(string.charAt(i) == '('){
                depth ++;
            }
            else if(string.charAt(i) == ')'){
                depth --;
            }
        }
        return result.toString();
    }

    static String powerString(int power){
        if(power == 1){
            return "";
        }
        if(power < 0){
            return "^(" + power + ")";
        }
        return "^" + power;
    }

    static AdvancedFunction getAdvancedFunction(String string){
        for(AdvancedFunction af : AdvancedFunction.values()){
            if(string.matches(af.name().toLowerCase() + "\\((.*)\\)")){
                return af;
            }
        }
        return AdvancedFunction.NONE;
    }

}
