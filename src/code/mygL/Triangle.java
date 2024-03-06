package code.mygL;

import code.dependence.math.Vector3D;

public class Triangle {
    public Vector3D[] vertices;
    public Vector3D[] normals;
    public Vector3D[] clippedVertices;
    public float[] vertexLight;
    public float[] clippedLight;
    public int[] xRight, xLeft;
    public float[] zRight, zLeft;
    public float[] lLeft, lRight;
    public float[][] vertices2D;
    public float[] vertexDepth;
    public int verticesCount;
    public int scanUpperPosition, scanLowerPosition;
    public boolean isClippingRightOrLeft;
    public boolean clipped;




    public Triangle() {
        vertices = new Vector3D[] {
                new Vector3D(), new Vector3D(),
                new Vector3D(), new Vector3D()
        };
        normals = new Vector3D[] {
                new Vector3D(), new Vector3D(),
                new Vector3D(), new Vector3D()
        };
        vertexLight = new float[4];
        clippedLight = new float[4];
        clippedVertices = new Vector3D[] {
                new Vector3D(), new Vector3D(),
                new Vector3D(), new Vector3D()
        };
        vertices2D = new float[4][2];
        vertexDepth = new float[4];
        verticesCount = 3;
    }
}
