package code.mygL;


/**
 * M V P
 *
 *
 */
public class Transformation {
    public static final int X = 0, Y = 1, Z = 2;
    public static final int Sin = 0, Cos = 1;
    public void transformation(byte type, VBO vbo, final float[][] localTrigonometric, final float[][] globalTrigonometric) {
        if (type == 1) {
            for (int i = 0; i < vbo.vertexCount; i++) {
                vbo.updateVertexes[i].set(vbo.vertexes[i])
                        .scale(vbo.scale)
                        .rotate_X(localTrigonometric[X][Sin], localTrigonometric[X][Cos])
                        .rotate_Y(localTrigonometric[Y][Sin], localTrigonometric[Y][Cos])
                        .rotate_Z(localTrigonometric[Z][Sin], localTrigonometric[Z][Cos])
                        .add(vbo.localTranslation)
                        .sub(Camera.position)
                        .rotate_Y(globalTrigonometric[Y][Sin], globalTrigonometric[Y][Cos])
                        .rotate_X(globalTrigonometric[X][Sin], globalTrigonometric[X][Cos]);
            }
        }
    }

}
