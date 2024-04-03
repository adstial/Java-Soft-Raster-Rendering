package code.dependence.math;


public final class Vec3 {
    public float x,y,z;
    public Vec3() {
        x=0;y=0;z=0;
    }
    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vec3(final Vec3 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    public Vec3 set(final Vec3 v) {
        this.x=v.x;
        this.y=v.y;
        this.z=v.z;
        return this;
    }
    public Vec3 set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    public Vec3 add(final Vec3 v) {
        this.x+=v.x;
        this.y+=v.y;
        this.z+=v.z;
        return this;
    }
    public Vec3 add(float x, float y, float z) {
        this.x+=x;
        this.y+=y;
        this.z+=z;
        return this;
    }
    public Vec3 add(final Vec3 v, float scalar){
        x += v.x * scalar;
        y += v.y * scalar;
        z += v.z * scalar;
        return this;
    }
    public static Vec3 add(Vec3 v1, Vec3 v2) {
        return new Vec3(v1.x+v2.x, v1.y+v2.y, v1.z+v2.z);
    }

    public Vec3 addX(float x) {
        this.x += x;
        return this;
    }

    public Vec3 addY(float y) {
        this.y += y;
        return this;
    }

    public Vec3 addZ(float z) {
        this.z += z;
        return this;
    }

    public Vec3 sub(final Vec3 v) {
        this.x-=v.x;
        this.y-=v.y;
        this.z-=v.z;
        return this;
    }

    public Vec3 sub(float x, float y, float z) {
        this.x-=x;
        this.y-=y;
        this.z-=z;
        return this;
    }

    public Vec3 sub(final Vec3 v, float scalar){
        x -= v.x * scalar;
        y -= v.y * scalar;
        z -= v.z * scalar;
        return this;
    }

    public static Vec3 sub(Vec3 v1, Vec3 v2) {
        return new Vec3(v1.x-v2.x, v1.y-v2.y, v1.z-v2.z);
    }

    public Vec3 subX(float x) {
        this.x-=x;
        return this;
    }

    public Vec3 subY(float y) {
        this.y-=y;
        return this;
    }

    public Vec3 subZ(float z) {
        this.z-=z;
        return this;
    }

    public float dot(final Vec3 v2){
        return this.x*v2.x + this.y*v2.y + this.z*v2.z;
    }

    public float dot(float x, float y, float z){
        return this.x*x + this.y*y + this.z*z;
    }

    public Vec3 crossAs(final Vec3 v1, final Vec3 v2){
        x = v1.y*v2.z - v1.z*v2.y;
        y = v1.z*v2.x - v1.x*v2.z;
        z = v1.x*v2.y - v1.y*v2.x;
        return this;
    }

    public static Vec3 cross(final Vec3 v1, final Vec3 v2) {
        return new Vec3(v1.y*v2.z - v1.z*v2.y, v1.z*v2.x - v1.x*v2.z, v1.x*v2.y - v1.y*v2.x);
    }

    public Vec3 cross(Vec3 v) {
        return new Vec3(y*v.z - z*v.y, z*v.x - x*v.z, x*v.y - y*v.x);
    }

    public Vec3 unit() {
        float temp = QuickMath.invSqrt(x * x + y * y + z * z);
        x *= temp;y *= temp;z *= temp;
        return this;
    }

    public Vec3 scale(float scalar) {
        x *= scalar;
        y*=scalar;
        z*=scalar;
        return this;
    }

    public Vec3 scale(final Vec3 scalar) {
        x *= scalar.x;
        y *= scalar.y;
        z *= scalar.z;
        return this;
    }

    public Vec3 rotate_X(int angle){
        float sin = QuickMath.getSin(angle);
        float cos = QuickMath.getCos(angle);
        float old_Y = y;
        float old_Z = z;
        y = cos*old_Y + sin*old_Z;
        z = -sin*old_Y + cos*old_Z;
        return this;
    }

    public Vec3 rotate_X(float sin, float cos) {
        float old_Y = y;
        float old_Z = z;
        y = cos*old_Y + sin*old_Z;
        z = -sin*old_Y + cos*old_Z;
        return this;
    }

    public Vec3 rotate_Y(int angle) {
        float sin = QuickMath.getSin(angle);
        float cos = QuickMath.getCos(angle);
        float old_X = x;
        float old_Z = z;
        x = cos*old_X + sin*old_Z;
        z = - sin*old_X + cos*old_Z;
        return this;
    }

    public Vec3 rotate_Y(float sin, float cos){
        float old_X = x;
        float old_Z = z;
        x = cos*old_X + sin*old_Z;
        z = -sin*old_X + cos*old_Z;
        return this;
    }

    public Vec3 rotate_Z(int angle){
        float sin = QuickMath.getSin(angle);
        float cos = QuickMath.getCos(angle);
        float old_X = x;
        float old_Y = y;
        x = cos*old_X + sin*old_Y;
        y = -sin*old_X + cos*old_Y;
        return this;
    }

    public Vec3 rotate_Z(float sin, float cos){
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
