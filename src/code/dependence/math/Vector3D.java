package code.dependence.math;


public final class Vector3D {
    public float x,y,z;
    public Vector3D() {
        x=0;y=0;z=0;
    }
    public Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3D(final Vector3D v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    public Vector3D set(final Vector3D v) {
        this.x=v.x;
        this.y=v.y;
        this.z=v.z;
        return this;
    }
    public Vector3D set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    public Vector3D add(final Vector3D v) {
        this.x+=v.x;
        this.y+=v.y;
        this.z+=v.z;
        return this;
    }
    public Vector3D add(float x, float y, float z) {
        this.x+=x;
        this.y+=y;
        this.z+=z;
        return this;
    }
    public Vector3D add(final Vector3D v, float scalar){
        x += v.x * scalar;
        y += v.y * scalar;
        z += v.z * scalar;
        return this;
    }
    public static Vector3D add(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.x+v2.x, v1.y+v2.y, v1.z+v2.z);
    }

    public Vector3D addX(float x) {
        this.x += x;
        return this;
    }

    public Vector3D addY(float y) {
        this.y += y;
        return this;
    }

    public Vector3D addZ(float z) {
        this.z += z;
        return this;
    }

    public Vector3D sub(final Vector3D v) {
        this.x-=v.x;
        this.y-=v.y;
        this.z-=v.z;
        return this;
    }

    public Vector3D sub(float x, float y, float z) {
        this.x-=x;
        this.y-=y;
        this.z-=z;
        return this;
    }

    public Vector3D sub(final Vector3D v, float scalar){
        x -= v.x * scalar;
        y -= v.y * scalar;
        z -= v.z * scalar;
        return this;
    }

    public static Vector3D sub(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.x-v2.x, v1.y-v2.y, v1.z-v2.z);
    }

    public Vector3D subX(float x) {
        this.x-=x;
        return this;
    }

    public Vector3D subY(float y) {
        this.y-=y;
        return this;
    }

    public Vector3D subZ(float z) {
        this.z-=z;
        return this;
    }

    public float dot(final Vector3D v2){
        return this.x*v2.x + this.y*v2.y + this.z*v2.z;
    }

    public float dot(float x, float y, float z){
        return this.x*x + this.y*y + this.z*z;
    }

    public Vector3D crossAs(final Vector3D v1, final Vector3D v2){
        x = v1.y*v2.z - v1.z*v2.y;
        y = v1.z*v2.x - v1.x*v2.z;
        z = v1.x*v2.y - v1.y*v2.x;
        return this;
    }

    public static Vector3D cross(final Vector3D v1, final Vector3D v2) {
        return new Vector3D(v1.y*v2.z - v1.z*v2.y, v1.z*v2.x - v1.x*v2.z, v1.x*v2.y - v1.y*v2.x);
    }

    public Vector3D cross(Vector3D v) {
        return new Vector3D(y*v.z - z*v.y, z*v.x - x*v.z, x*v.y - y*v.x);
    }

    public Vector3D unit() {
        float temp = QuickMath.invSqrt(x * x + y * y + z * z);
        x *= temp;y *= temp;z *= temp;
        return this;
    }

    public Vector3D scale(float scalar) {
        x *= scalar;
        y*=scalar;
        z*=scalar;
        return this;
    }

    public Vector3D rotate_X(int angle){
        float sin = QuickMath.getSin(angle);
        float cos = QuickMath.getCos(angle);
        float old_Y = y;
        float old_Z = z;
        y = cos*old_Y + sin*old_Z;
        z = -sin*old_Y + cos*old_Z;
        return this;
    }

    public Vector3D rotate_X(float sin, float cos) {
        float old_Y = y;
        float old_Z = z;
        y = cos*old_Y + sin*old_Z;
        z = -sin*old_Y + cos*old_Z;
        return this;
    }

    public Vector3D rotate_Y(int angle) {
        float sin = QuickMath.getSin(angle);
        float cos = QuickMath.getCos(angle);
        float old_X = x;
        float old_Z = z;
        x = cos*old_X + sin*old_Z;
        z = - sin*old_X + cos*old_Z;
        return this;
    }

    public Vector3D rotate_Y(float sin, float cos){
        float old_X = x;
        float old_Z = z;
        x = cos*old_X + sin*old_Z;
        z = -sin*old_X + cos*old_Z;
        return this;
    }

    public Vector3D rotate_Z(int angle){
        float sin = QuickMath.getSin(angle);
        float cos = QuickMath.getCos(angle);
        float old_X = x;
        float old_Y = y;
        x = cos*old_X + sin*old_Y;
        y = -sin*old_X + cos*old_Y;
        return this;
    }

    public Vector3D rotate_Z(float sin, float cos){
        float old_X = x;
        float old_Y = y;
        x = cos*old_X + sin*old_Y;
        y = -sin*old_X + cos*old_Y;
        return this;
    }

    public String toString() {
        return String.format("( x: %f, y: %f, z: %f)", x, y, z);
    }
}
