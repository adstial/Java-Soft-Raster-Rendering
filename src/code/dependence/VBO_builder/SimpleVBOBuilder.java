package code.dependence.VBO_builder;

import code.dependence.math.Vector3D;
import code.mygL.VBO;

import static code.mygL.VBO.TriangleFillStyle.*;

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
            case 2 -> createDefaultCube();
        }
        return vbo;
    }

    @Override
    public SimpleVBOBuilder setParam(Integer p) {
        style = p;
        return this;
    }


    private void createDefaultTriangle() {
        vbo.id = 1;
        vbo.tfs = SingleColor;
        vbo.hasLight = false;
        vbo.vertexes = new Vector3D[] {
                new Vector3D(0, 1, 2),
                new Vector3D(1, -1, 2),
                new Vector3D(-1, -1, 2)
        };
        vbo.vertexCount = 3;
        vbo.indexes = new int[] {2, 1, 0};
        vbo.triangleCount = 1;
        vbo.triangleColor = 0x123456;
        setDefault();
    }

    private void createDefaultCube() {
        vbo.id = 2;
        vbo.tfs = ColorList;
        vbo.hasLight = false;
        vbo.hasNormals = false;
        var l = 0.5f;
        vbo.vertexes = new Vector3D[] {
                new Vector3D( -l, -l, -l + 2.5f),
                new Vector3D(  l, -l, -l + 2.5f),
                new Vector3D(  l,  l, -l + 2.5f),
                new Vector3D( -l,  l, -l + 2.5f),
                new Vector3D( -l, -l,  l + 2.5f),
                new Vector3D(  l, -l,  l + 2.5f),
                new Vector3D(  l,  l,  l + 2.5f),
                new Vector3D( -l,  l,  l+ 2.5f),
        };
        vbo.vertexCount = vbo.vertexes.length;
        vbo.indexes = new int[] {
                0, 1, 2, 2, 3, 0,        // Front face
                1, 5, 6, 6, 2, 1,        // Right face
                5, 4, 7, 7, 6, 5,        // Back face
                4, 0, 3, 3, 7, 4,        // Left face
                3, 2, 6, 6, 7, 3,        // Top face
                4, 5, 1, 1, 0, 4
        };
        vbo.colors = new int[] {
                0xff0000, 0xff0000,
                0x00ff00, 0x00ff00,
                0x0000ff, 0x0000ff,
                0xffff00, 0xffff00,
                0xff00ff, 0xff00ff,
                0x00ffff, 0x00ffff
        };
        vbo.triangleCount = 12;
        setDefault();
    }

    private void setDefault() {
        vbo.scale = 1;
        vbo.localTranslation = new Vector3D();
        vbo.localRotation = new int[] {0, 0, 0};
        vbo.updateVertexes = new Vector3D[vbo.vertexCount];
        for (int i = 0; i < vbo.vertexCount; i++) {
            vbo.updateVertexes[i] = new Vector3D();
        }
    }

}
