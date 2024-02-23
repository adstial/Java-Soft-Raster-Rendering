package code.mygL;


import code.dependence.math.Vector3D;

import java.util.ArrayList;

/*
* VBO (vertex Buffer Object)
*
*
*
* */
public class VBO {
    public long id;

    public TriangleFillStyle tfs;
    public int localRotationX, localRotationY, localRotationZ;

    public static final int X = 0, Y = 1, Z = 2;
    public int[] localRotation;
    public Vector3D localTranslation;
    public Vector3D[] vertexes;
    public Vector3D[] updateVertexes;
    public int[] indexes;
    public int triangleColor;
    public float scale;
    public int vertexCount;
    public int triangleCount;
    public int [] colors;


    public boolean hasLight = false;
    public boolean hasNormals = false;
    public Vector3D[] normals;
    public ArrayList<Light> lightSource;
    public float kd;
    public float ks;
    public float[] vertexLightLevels;


    public static final int SingleColor = 0;
    public static final int Colorful = 1;
    public static final int ColorWithLight = 2;

    public enum TriangleFillStyle {
        SingleColor,
        ColorList
    }
}
