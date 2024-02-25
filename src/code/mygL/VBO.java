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

    public TriangleFillStyle triangleFillStyle;
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


    public enum TriangleFillStyle {
        SingleColor,
        ColorList
    }
}
