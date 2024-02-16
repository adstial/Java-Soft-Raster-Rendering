package code.mygL;


import code.dependence.utils.Vector3D;

import java.util.ArrayList;

/*
* VBO (vertex Buffer Object)
*
*
*
* */
public class VBO {
    public long id;
    public int type;
    public TriangleColor tc;
    public int localRotationX, localRotationY, localRotationZ;
    public Vector3D localTranslation;
    public Vector3D[] vertexes;
    public Vector3D[] updateVertexes;
    public int[] indexes;
    public int triangleColor;
    public float scale;
    public int vertexCount;
    public int triangleCount;

    public boolean hasColors = false;
    public int [] colors;


    public boolean hasLight = false;
    public Vector3D[] normals;
    public ArrayList<Light> lightSource;
    public float kd;
    public float ks;
    public float[] vertexLightLevels;

    public enum TriangleColor {
        Single,
        ColorList
    }


    public static final int SingleColor = 0;
    public static final int Colorful = 1;
    public static final int ColorWithLight = 2;

}
