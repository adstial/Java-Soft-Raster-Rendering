package code.mygL;


import code.dependence.utils.Vector3D;

public class Light {
    public Vector3D position;
    public Vector3D updatedPosition;
    public float la;
    public Light(float x, float y, float z, float la) {
        this.position = new Vector3D(x, y, z);
        this.updatedPosition = new Vector3D();
        this.la = la;
    }
}
