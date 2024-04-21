package dependence.json.token;

import java.util.LinkedList;

public class TokenList extends LinkedList<Token>{

    public Token nextToken() {
        return this.size() > 0 ? this.removeFirst() : null;
    }
}
