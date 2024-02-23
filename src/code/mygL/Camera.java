package code.mygL;

import code.dependence.math.Vector3D;

import java.util.Arrays;

public final class Camera {
    public static final float moveSpeed = 0.03f;
    public static final int turnRate = 1;
    public static int MoveType = 1;
    public static Vector3D position = new Vector3D();
    public static Vector3D waitMove = new Vector3D();
    public static Vector3D viewDirection = new Vector3D();
    public static int[] rate = new int[3];            // (x: up/down, y: left/right, z)
    public static int[] waitRate = new int[3];
    public static boolean
            MOVE_FORWARD, MOVE_BACKWARD,
            SLIDE_LEFT, SLIDE_RIGHT,
            LOOK_UP, LOOK_DOWN,
            LOOK_RIGHT, LOOK_LEFT;

    public static void update(boolean move, boolean turn) {
        if (turn) turn();
        if (move) move();
    }

    public static void turn() {
        if (MoveType == 1) {
            if (LOOK_UP) {
                rate[0] += turnRate;
                if (rate[0] > 89)
                    rate[0] = 89;
            }
            if (LOOK_DOWN) {
                rate[0] -= turnRate;
                if (rate[0] < -89) {
                    rate[0] = -89;
                }
            }
            if (LOOK_LEFT) rate[1] -= turnRate;
            if (LOOK_RIGHT) rate[1] += turnRate;

            rate[1] = (rate[1] + 360) % 360;

            viewDirection.set(0,0,1)
                    .rotate_X(rate[0])
                    .rotate_Y(rate[1])
                    .unit();
        }
    }

    public static void move() {
        if (MoveType == 1) {
            if (MOVE_FORWARD) position.add(viewDirection, moveSpeed);
            if (MOVE_BACKWARD) position.sub(viewDirection, moveSpeed);

            if (SLIDE_LEFT)
                position.sub(viewDirection.cross(new Vector3D(0,-1,0)).unit(), moveSpeed);
            if (SLIDE_RIGHT)
                position.sub(viewDirection.cross(new Vector3D(0,1,0)).unit(), moveSpeed);
        }
    }

    public static String ToString() {
        return "CameraA(" +
                "position: " + position +
                ", dir: " + viewDirection +
                ", rate: " + Arrays.toString(rate) +
                ')';
    }
}
