package function_utils;

public class Substitution {

    private Term from;
    private Term to;

    public Substitution(String from, String to){
        this.from = new Term(from);
        this.to = new Term(to);
    }

    Term getFrom(){
       return from;
    }

    Term getTo(){
        return to;
    }

}
