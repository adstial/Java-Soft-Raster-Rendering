package code.mygL;


import code.dependence.utils.Vector3D;
import java.util.Arrays;

public class Camera {
    private int MoveType;
    private static final float moveSpeed = 0.03f;
    private static final int turnRate = 1;
    private Vector3D position;
    private Vector3D waitMove;
    private Vector3D viewDirection;
    private int[] rate;            // (x: up/down, y: left/right, z)
    public int[]  waitRate;
    public boolean
            MOVE_FORWARD, MOVE_BACKWARD,
            SLIDE_LEFT, SLIDE_RIGHT,
            LOOK_UP, LOOK_DOWN,
            LOOK_RIGHT, LOOK_LEFT;

    public Camera() {
        position = new Vector3D();
        waitMove = new Vector3D();
        viewDirection = new Vector3D(0,0,1);
        rate = new int[] {0, 0, 0};
        waitRate = new int[] {0, 0, 0};
        MoveType = 1;
    }
    public void update(boolean move, boolean turn) {
        if (turn) turn();
        if (move) move();
    }



    private void turn() {
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

    private void move() {
        if (MoveType == 1) {
            if (MOVE_FORWARD) position.add(viewDirection, moveSpeed);
            if (MOVE_BACKWARD) position.sub(viewDirection, moveSpeed);

            if (SLIDE_LEFT)
                position.sub(viewDirection.cross(new Vector3D(0,-1,0)).unit(), moveSpeed);
            if (SLIDE_RIGHT)
                position.sub(viewDirection.cross(new Vector3D(0,1,0)).unit(), moveSpeed);
        }

    }

    public  int[] getRate() {
        return rate;
    }
    public final Vector3D getPosition() {
        return position;
    }
    public final Vector3D getViewDirection() {
        return viewDirection;
    }

    @Override
    public String toString() {
        return "Camera(" +
                "position: " + position +
                ", dir: " + viewDirection +
                ", rate: " + Arrays.toString(rate) +
                ')';
    }
}
