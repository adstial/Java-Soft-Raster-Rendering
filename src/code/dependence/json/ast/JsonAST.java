package code.dependence.json.ast;

import code.dependence.json.token.Token;
import code.dependence.json.token.TokenType;

import java.util.LinkedList;

/*
 * Json 语法树
 *
 */
public class JsonAST {
    public Node root;
    private Node current;

    public JsonAST(final LinkedList<Token> tokens) {
        this.root = new Node(null, null, null);
        this.current = this.root;

        while (!tokens.isEmpty()) {

            var token = tokens.removeFirst();

            assert token.type != TokenType.EOF || this.root == this.current;



        }
    }

    public <T> T buildJson(final Class<T> clazz) {

        return null;
    }
}