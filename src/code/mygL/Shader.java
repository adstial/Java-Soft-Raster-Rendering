package code.mygL;

import code.dependence.logger.Logger;
import code.dependence.math.QuickMath;
import code.dependence.math.Vector3D;

import java.util.ArrayList;

public class Shader extends Thread{

    public final Object lock;
    public boolean work;
    public boolean close;
    public ArrayList<VBO> vboList;
    public int width, height, halfWidth, halfHeight, distance;
    public int VboStart, VboEnd;
    public VBO vbo;




    public static final float nearClipDistance = 0.01f;
    public Camera camera;
    public int[] screen;
    public float[] zBuffer;
    public float local_sinX, local_cosX, local_sinY, local_cosY, local_sinZ, local_cosZ, global_sinY, global_cosY, global_sinX, global_cosX;
    public int[] xRight, xLeft;
    public float[] zRight, zLeft;
    public Vector3D[] updatedVertices;
    public float[][] vector2d;
    public float[] zDepth;


    public Shader(String name) {
        setName(name);
        lock = new Object();
        updatedVertices = new Vector3D[] {
                new Vector3D(), new Vector3D(),
                new Vector3D(), new Vector3D()
        };
        edge1 = new Vector3D(); edge2 = new Vector3D(); surfaceNormal = new Vector3D();
        vector2d = new float[4][2];
        zDepth = new float[4];
        clippedVertices = new Vector3D[]{
                new Vector3D(), new Vector3D(),
                new Vector3D(), new Vector3D()
        };
        dv = new Vector3D();
    }


    @Override
    public void run() {
        while (!close) {
            sync();
            rasterize();
        }
    }


    private void rasterize() {
        if (VboStart == -1) return;
        for (int i = VboStart; i <= VboEnd; i++) {
            vbo = vboList.get(i);
            getQuickTrigonometric();
            transformVertices(vbo);
            for (int j = 0; j < vbo.triangleCount; j++) {
                buildTriangle(j);
                if (testHidden()) continue;
                clipZNearPlane();
                scanTriangle();
                renderTriangle();
            }

        }
    }



    private void transformVertices(VBO vbo) {
        if (vbo.hasLight) {

        } else {
            transformVertices1(vbo);
        }
    }

    private void transformVertices1(VBO vbo) {
        for (int i = 0; i < vbo.vertexCount; i++) {
            vbo.updateVertexes[i].set(vbo.vertexes[i])
                    .scale(vbo.scale)
                    .rotate_X(local_sinX, local_cosX)
                    .rotate_Y(local_sinY, local_cosY)
                    .rotate_Z(local_sinZ, local_cosZ)
                    .add(vbo.localTranslation)
                    .sub(camera.getPosition())
                    .rotate_Y(global_sinY, global_cosY)
                    .rotate_X(global_sinX, global_cosX);
        }
    }


    private void getQuickTrigonometric() {
        local_sinX = QuickMath.getSin(vbo.localRotationX);
        local_cosX = QuickMath.getCos(vbo.localRotationX);
        local_sinY = QuickMath.getSin(vbo.localRotationY);
        local_cosY = QuickMath.getCos(vbo.localRotationY);
        local_sinZ = QuickMath.getSin(vbo.localRotationZ);
        local_cosZ = QuickMath.getCos(vbo.localRotationZ);

        global_sinY = QuickMath.getSin(-camera.getRate()[1]);
        global_cosY = QuickMath.getCos(camera.getRate()[1]);
        global_sinX = QuickMath.getSin(-camera.getRate()[0]);
        global_cosX = QuickMath.getCos(camera.getRate()[0]);
    }

    private void buildTriangle(int i) {
        updatedVertices[0].set(vbo.updateVertexes[i * 3 + 2]);
        updatedVertices[1].set(vbo.updateVertexes[i * 3 + 1]);
        updatedVertices[2].set(vbo.updateVertexes[i * 3]);
    }


    public Vector3D edge1, edge2, surfaceNormal;
    public boolean isClippingRightOrLeft;
    public boolean isClippingTopOrButton;

    private boolean testHidden() {
        //test 1
        boolean allBehindClippingPlane = true;
        for (int i = 0; i < 3; i++) {
            if (updatedVertices[i].z >= nearClipDistance) {
                allBehindClippingPlane = false;
                break;
            }
        }
        if (allBehindClippingPlane) return true;

        // test 2
        edge1.set(updatedVertices[1])
                        .sub(updatedVertices[0]);
        edge2.set(updatedVertices[2])
                .sub(updatedVertices[0]);
        surfaceNormal.crossAs(edge1, edge2);
        var dotProduct = surfaceNormal.dot(updatedVertices[0]);
        if (dotProduct >= 0) return true;

        // test 3
        for (int i = 0; i < 3; i++) {
            // x
            vector2d[i][0] = halfWidth + distance * updatedVertices[i].z / halfWidth;
            // y
            vector2d[i][1] = halfHeight - distance * updatedVertices[i].z / halfHeight;
            // z depth
            zDepth[i] = 1f / updatedVertices[i].z;
        }
        var left_x = (float) width; var right_x = -1f;
        var top_y = (float) height; var bottom_y = -1f;
        for (int i = 0; i < 3; i++) {
            left_x = Math.min(left_x, vector2d[i][0]);
            right_x = Math.max(right_x, vector2d[i][0]);
            top_y = Math.min(top_y, vector2d[i][1]);
            bottom_y = Math.max(bottom_y, vector2d[i][1]);
        }
        if (left_x == width | right_x == -1 | top_y == height | bottom_y == -1)
            return true;
        isClippingRightOrLeft = left_x < 0 || right_x >= width;
        isClippingTopOrButton = top_y < 0 || bottom_y >= height;
        return false;
    }


    public int verticesCount;
    public boolean neededToBeClipped;
    public Vector3D[] clippedVertices;
    private void clipZNearPlane() {
        if (!vbo.hasLight) {
            clipZNearPlane1();
        }
    }
    private void clipZNearPlane1() {
        verticesCount = 0;
        neededToBeClipped = false;
        for (int i = 0; i < 3; i++) {
            if (updatedVertices[i].z >= nearClipDistance) {
                clippedVertices[verticesCount].set(updatedVertices[i]);
                verticesCount++;
            } else {
                neededToBeClipped = true;
                var index = (i + 2) % 3;
                if (updatedVertices[index].z >= nearClipDistance) {
                        approximatePoint(verticesCount, updatedVertices[i], updatedVertices[index]);
                    verticesCount++;
                }
                index = (i + 1) %3;
                if (updatedVertices[index].z >= nearClipDistance) {
                        approximatePoint(verticesCount, updatedVertices[i], updatedVertices[index]);
                    verticesCount++;
                }
            }
        }

        if (neededToBeClipped) {
            for (int i = 0; i < verticesCount; i++) {
                vector2d[i][0] = halfWidth + clippedVertices[i].x * distance / clippedVertices[i].z;
                vector2d[i][1] = halfHeight - clippedVertices[i].y * distance / clippedVertices[i].z;

                zDepth[i] = 1f / clippedVertices[i].z;
            }
        }
        
    }

    public Vector3D dv;
    private void approximatePoint(int index, Vector3D behindPoint, Vector3D frontPoint) {
        dv.set(frontPoint.x - behindPoint.x, frontPoint.y - behindPoint.y, frontPoint.z - behindPoint.z);
        var ratio = (frontPoint.z - nearClipDistance) / dv.z;

        dv.scale(ratio);
        clippedVertices[index].set(frontPoint);
        clippedVertices[index].sub(dv);
    }

    public int scanUpperPosition;
    public int scanLowerPosition;

    private void scanTriangle() {
        switch (vbo.tfs) {
            case ColorList -> {}
            case SingleColor -> scanTriangle1();
        }
    }
    private void scanTriangle1() {
        scanUpperPosition = height;
        scanLowerPosition = -1;
        for (int i = 0; i < verticesCount; i++) {
            var v1 = vector2d[i];
            float[] v2;

            var d1 = zDepth[i];
            float d2;
            if (i == verticesCount - 1) {
                v2 = vector2d[0];
                d2 = zDepth[0];
            } else {
                v2 = vector2d[i+1];
                d2 = zDepth[i+1];
            }
            var downwards = true;

            if (v1[1] > v2[1]) {
                downwards = false;
                var temp = v1;
                v1 = v2; v2 = temp;

                var tempDepth = d1;
                d1 = d2; d2 = tempDepth;
            }

            var dy = v2[1] - v1[1];
            if (dy == 0) continue;

            var startY = Math.max((int)(v1[1]) + 1, 0);
            var endY =Math.min((int) (v2[1]), height - 1);

            if (startY < scanUpperPosition) scanUpperPosition = startY;
            if (endY > scanLowerPosition) scanLowerPosition = endY;

            var gradient = (v2[0] - v1[0]) / dy;

            var dy_z = (d2 - d1) /dy;

            var startX = ((v1[0]) + (startY - v1[1]) * gradient);
            if (startX < 0) isClippingRightOrLeft = true;

            var tempZ = d1 - v1[1] * dy_z + startY * dy_z;
            for (int y = startY; y < endY; y++, startX += gradient, tempZ += dy_z) {
                if (downwards) {
                    xRight[y] = (int)startX;
                    zRight[y] = tempZ;
                } else {
                    xLeft[y] = (int)startX;
                    zLeft[y] = tempZ;
                }
            }
        }

        if (isClippingRightOrLeft) {
            int x_left, x_right;
            boolean xLeftInView, xRightInView;
            for(int y = scanUpperPosition; y <= scanLowerPosition; y++) {
                x_left = xLeft[y];
                x_right = xRight[y];

                xLeftInView = x_left >= 0 && x_left < width;
                xRightInView = x_right >0 && x_right < width;

                if (xLeftInView && xRightInView) continue;

                if (x_left >= width || x_right <= 0) {
                    xLeft[y] = 0;
                    xRight[y] = 0;
                    continue;
                }

                var dx = x_right - x_left;
                var dz = zRight[y] - zLeft[y];

                if (!xLeftInView) {
                    xLeft[y] = 0;
                    zLeft[y] = (zLeft[y] + dz /dx * (-x_left) ) ;
                }

                if (!xRightInView) {
                    xRight[y] = width;
                    zRight[y] = (zRight[y] - dz /dx * (x_right - width));
                }
            }
        }
    }

    private void renderTriangle() {
        switch (vbo.tfs) {
            case SingleColor -> renderTriangle1();
        }
    }

    private void renderTriangle1() {
        for (int i = scanUpperPosition; i < scanLowerPosition; i++) {
            var x_left = xLeft[i] ;
            var x_right = xRight[i];

            var z_Left = zLeft[i];
            var z_Right = zRight[i];

            var dz = (z_Right- z_Left)/(x_right - x_left);

            x_left +=i * width;
            x_right +=i * width;

            for (int j = x_left; j < x_right ; j++, z_Left += dz) {
                if (zBuffer[j] < z_Left) {
                    zBuffer[j] = z_Left;
                    screen[j] = vbo.triangleColor;
                }
            }
        }
    }


    private void sync() {
        synchronized (this) {
            try {
                synchronized (lock) {
                    lock.notify();
                    work = false;
                }
                wait();
            } catch (InterruptedException e) {
                Logger.getGlobal().fatal(e.toString());
            }
        }
    }

    public void setContext(int width, int height, int distance) {
        this.width = width; halfWidth = width / 2;
        this.height = height; halfHeight = height / 2;
        xLeft = new int[height]; xRight = new int[height];
        zLeft = new float[height]; zRight = new float[height];
//        lightLeft = new float[height]; lightRight = new float[height];
        this.distance = distance;
    }

    public void setContext(int[] screen, float[] zBuffer) {
        this.screen = screen;
        this.zBuffer = zBuffer;
    }

    public void setContext(Camera camera) {
        this.camera = camera;
    }


    public void setVboStartAndEnd(int start, int end) {
        this.VboStart = start; this.VboEnd = end;
    }
}
