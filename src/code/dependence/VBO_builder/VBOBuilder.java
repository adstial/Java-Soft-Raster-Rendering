package code.dependence.VBO_builder;

import code.mygL.VBO;

public interface VBOBuilder<Self extends VBOBuilder<Self, Param>, Param> {
    VBO build();
    Self setParam(Param p);

}
