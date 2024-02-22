package code.dependence.VBO_builder;

import code.mygL.VBO;

public class FunctionVBOBuilder implements VBOBuilder<FunctionVBOBuilder, String> {

    @Override
    public VBO build() {
        return null;
    }

    @Override
    public FunctionVBOBuilder setParam(String p) {
        return null;
    }

    @Override
    public final FunctionVBOBuilder setParam(String... p) {
        if (p == null) throw new AssertionError();
        return null;
    }
}
