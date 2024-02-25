package code.dependence.serialization.my_json;

import code.dependence.serialization.top.Constructor;
import code.dependence.serialization.top.ElementSearch;

public class Json extends Constructor implements ElementSearch<Object, String> {
    public Json(Type type, String name) {
        super(type, name);
    }

    @Override
    public Object getElement(String s) {
        return null;
    }

    @Override
    public Object[] getElements(String s) {
        return new Object[0];
    }
    
}
