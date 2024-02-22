package code.dependence.VBO_builder;

import code.dependence.logger.Logger;
import code.dependence.math.Vector3D;
import code.mygL.VBO;
import org.junit.Ignore;

public class SimpleVBOBuilder implements VBOBuilder<SimpleVBOBuilder, Integer> {
    public SimpleVBOBuilder() {
        vbo = new VBO();
    }
    private VBO vbo;
    private int style;

    @Override
    public VBO build() {
        vbo = new VBO();
        switch (style) {
            case 1 -> createDefaultTriangle();
        }
        return vbo;
    }

    @Override
    public SimpleVBOBuilder setParam(Integer p) {
        style = p;
        return this;
    }

    @Deprecated
    @Override
    public SimpleVBOBuilder setParam(Integer... p) {
        return null;
    }

    private void createDefaultTriangle() {
        vbo.id = 1;
        vbo.tfs = VBO.TriangleFillStyle.SingleColor;
        vbo.hasLight = false;
        vbo.vertexes = new Vector3D[] {
                new Vector3D(0, 1, 2),
                new Vector3D(1, -1, 2),
                new Vector3D(-1, -1, 2)
        };
        vbo.vertexCount = 3;
        vbo.indexes = new int[] {0, 1, 2};
        vbo.triangleCount = 1;
        vbo.triangleColor = 0x123456;
        setDefault();
    }

    private void setDefault() {
        vbo.scale = 1;
        vbo.localTranslation = new Vector3D();
        vbo.localRotationX = 0;
        vbo.localRotationY = 0;
        vbo.localRotationZ = 0;
        vbo.updateVertexes = new Vector3D[vbo.vertexCount];
        for (int i = 0; i < vbo.vertexCount; i++) {
            vbo.updateVertexes[i] = new Vector3D();
        }
    }

}
