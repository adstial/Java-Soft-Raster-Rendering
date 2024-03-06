package code.mygL;

import code.dependence.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EntityManager {
    public static Logger log = Logger.getGlobal();
    public static int lastIndex;
    public static int listSize;
    public static ArrayList<VBO> vboList = new ArrayList<>(1000);
    public static Map<Long, Integer> map = new HashMap<>();

    public static void addVBO(final VBO vbo) {
        if (vbo == null) {
            log.warming(EntityManager.class, "fail to add a null value");
            return;
        }
        var id = vbo.id;
        map.put(id, vboList.size());
        var res = vboList.add(vbo);
        if (res) log.info(EntityManager.class, "vboList add VBO: " + id);
        else log.warming(EntityManager.class, "fail to add vbo: " + id);
    }

    public static void loadVBOs() {

    }
}
