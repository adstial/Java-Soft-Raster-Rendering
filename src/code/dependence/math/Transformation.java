package code.dependence.math;

public class Transformation {

    // 平移
    public Vec3 translation;

    // 旋转
    public int[] rotation;

    // 缩放
    public Vec3 scale;

    public Transformation(Vec3 translation, int[] rotation, Vec3 scale) {
        this.translation = translation;
        this.rotation = rotation;
        this.scale = scale;
    }


    public static Transformation Default() {
        return new Transformation(new Vec3(), new int[]{0, 0, 0}, new Vec3(1,1,1));
    }
}
