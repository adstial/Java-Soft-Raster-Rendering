package code.mygL;

import code.dependence.logger.Logger;
import code.dependence.math.QuickMath;
import code.dependence.math.Vector3D;

import java.util.ArrayList;

public class RenderCore extends Thread{

    public final Object lock;
    public boolean work;
    public boolean close;

    public int width, height, halfWidth, halfHeight, distance;

    public ArrayList<VBO> vboList;
    public int VboStart, VboEnd;
    public VBO vbo;




    public static final float nearClipDistance = 0.01f;
    public int[] screen;
    public float[] zBuffer;

    public static final int X = 0, Y = 1, Z = 2;
    public static final int Sin = 0, Cos = 1;
    public float [][] localTrigonometric;
    public float[][] globalTrigonometric;

    public Transformation tf;


    public int[] xRight, xLeft;
    public float[] zRight, zLeft;
    public Vector3D[] updatedVertices;
    public float[][] vertices2D;
    public float[] vertexDepth;


    public RenderCore(String name) {
        setName(name);
        lock = new Object();

        tf = new Transformation();

        updatedVertices = new Vector3D[] {
                new Vector3D(), new Vector3D(),
                new Vector3D(), new Vector3D()
        };

        edge1 = new Vector3D(); edge2 = new Vector3D();
        surfaceNormal = new Vector3D();

        dv = new Vector3D();

        vertices2D = new float[4][2];
        vertexDepth = new float[4];
        clippedVertices = new Vector3D[]{
                new Vector3D(), new Vector3D(),
                new Vector3D(), new Vector3D()
        };

        localTrigonometric = new float[3][2];
        globalTrigonometric = new float[3][2];

    }


    @Override
    public void run() {
        while (!close) {
            sync();
            rasterize();
        }
    }


    public int triangleCount;

    private void rasterize() {
        if (VboStart == -1) return;
        for (int j = VboStart; j <= VboEnd; j++) {
            vbo = vboList.get(j);
            getQuickTrigonometric();
            tf.transformation((byte) 1, vbo, localTrigonometric, globalTrigonometric);

            triangleCount = -1;
            for (int i = 0; i < vbo.triangleCount; i++) {
                buildTriangle(i);
                triangleCount++;
                if (testAllBehindClippingPlane() |
                        testNotTowardView() |
                        testOutSide())
                    continue;
                if (clipZNearPlane()) continue;
                scanTriangle();
                renderTriangle();
            }

        }
    }




    private void getQuickTrigonometric() {
        for (int i = 0; i < 3; i++) {
            var r = vbo.localRotation[i];
            localTrigonometric[i][Sin] = QuickMath.getSin(r);
            localTrigonometric[i][Cos] = QuickMath.getCos(r);
            globalTrigonometric[i][Sin] = QuickMath.getSin(-Camera.rate[i]);
            globalTrigonometric[i][Cos] = QuickMath.getCos(Camera.rate[i]);
        }
    }

    private void buildTriangle(int i) {
        updatedVertices[0] = (vbo.updateVertexes[vbo.indexes[i*3+2]]);
        updatedVertices[1] = (vbo.updateVertexes[vbo.indexes[i*3+1]]);
        updatedVertices[2] = (vbo.updateVertexes[vbo.indexes[i*3]]);
    }


    public Vector3D edge1, edge2, surfaceNormal;


    private boolean testAllBehindClippingPlane() {
        for(int i = 0; i < 3; i++)
            if (updatedVertices[i].z >= nearClipDistance)
                return false;
        return true;
    }

    private boolean testNotTowardView() {
        edge1.set(updatedVertices[1])
                .sub(updatedVertices[0]);
        edge2.set(updatedVertices[2])
                .sub(updatedVertices[0]);
        surfaceNormal.crossAs(edge1, edge2);
        var dorProduct = surfaceNormal.dot(updatedVertices[0]);
        return dorProduct >= 0;
    }

    public boolean isClippingRightOrLeft;
    public float leftMostPosition, rightMostPosition, upperMostPosition, lowerMostPosition;

    private boolean testOutSide() {
        for (int i = 0; i < 3; i++) {
            vertices2D[i][0] = halfWidth + updatedVertices[i].x * distance / updatedVertices[i].z;
            vertices2D[i][1] = halfHeight - updatedVertices[i].y * distance / updatedVertices[i].z;

            vertexDepth[i] = 1f / updatedVertices[i].z;
        }
        leftMostPosition = width;
        rightMostPosition = -1;
        upperMostPosition = height;
        lowerMostPosition = -1;
        for (int i = 0; i < 3; i++) {
            if (vertices2D[i][0] <= leftMostPosition)
                leftMostPosition = vertices2D[i][0];
            if (vertices2D[i][0] >= rightMostPosition)
                rightMostPosition = vertices2D[i][0];
            if (vertices2D[i][1] <= upperMostPosition)
                upperMostPosition = vertices2D[i][1];
            if (vertices2D[i][1] >= lowerMostPosition)
                lowerMostPosition = vertices2D[i][1];
        }
        if(leftMostPosition == width || rightMostPosition == -1 || upperMostPosition == height || lowerMostPosition == -1)
            return true;
        isClippingRightOrLeft = leftMostPosition < 0 || rightMostPosition >= width;

        return false;
    }

    public int verticesCount;
    public boolean neededToBeClipped;
    public Vector3D[] clippedVertices;
    private boolean clipZNearPlane() {
        if (!vbo.hasLight) {
            return clipZNearPlane1();
        } else {
            return true;
        }
    }
    private boolean clipZNearPlane1() {
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
            leftMostPosition = width;
            rightMostPosition = -1;
            upperMostPosition = height;
            lowerMostPosition = -1;
            for(int i = 0; i < verticesCount; i++) {
                vertices2D[i][0] = halfWidth + clippedVertices[i].x * distance / clippedVertices[i].z;
                vertices2D[i][1] = halfHeight - clippedVertices[i].y * distance / clippedVertices[i].z;

                vertexDepth[i] = 1f / clippedVertices[i].z;

                leftMostPosition = Math.min(leftMostPosition, vertices2D[i][0]);
                rightMostPosition = Math.max(rightMostPosition, vertices2D[i][0]);
                upperMostPosition = Math.min(upperMostPosition, vertices2D[i][1]);
                lowerMostPosition = Math.max(lowerMostPosition, vertices2D[i][1]);
            }

            if(leftMostPosition == width ||  rightMostPosition == -1 || upperMostPosition == height || lowerMostPosition == -1) {
                return true;
            }
            isClippingRightOrLeft = leftMostPosition < 0 || rightMostPosition >= width;
        }
        return false;
    }

    public Vector3D dv;
    private void approximatePoint(int index, Vector3D behindPoint, Vector3D frontPoint) {
        dv.set(frontPoint.x - behindPoint.x, frontPoint.y - behindPoint.y, frontPoint.z - behindPoint.z);
        var ratio = (frontPoint.z - nearClipDistance) / dv.z;
        dv.scale(ratio);
        clippedVertices[index].set(frontPoint.x, frontPoint.y, frontPoint.z);
        clippedVertices[index].sub(dv);
    }

    public int scanUpperPosition;
    public int scanLowerPosition;

    private void scanTriangle() {
        switch (vbo.tfs) {
            case ColorList, SingleColor -> scanTriangle1();
        }
    }
    private void scanTriangle1() {
        scanUpperPosition = height;
        scanLowerPosition = -1;
        for (int i = 0; i < verticesCount; i++) {
            float[] v1 = vertices2D[i];
            float[] v2;
            float d1 = vertexDepth[i];
            float d2;
            if (i == verticesCount - 1) {
                v2 = vertices2D[0];
                d2 = vertexDepth[0];
            } else {
                v2 = vertices2D[i + 1];
                d2 = vertexDepth[i + 1];
            }
            boolean downwards = true;
            if (v1[1] > v2[1]) {
                downwards = false;
                var tempV = v1;
                v1 = v2; v2 = tempV;

                var tempD = d1;
                d1 = d2; d2 = tempD;
            }
            float dy = v2[1] - v1[1];
            if (dy == 0) continue;

            var startY = Math.max((int)(v1[1]) + 1, 0);
            var endY = Math.min((int) v2[1], height - 1);

            if (startY < scanUpperPosition) scanUpperPosition = startY;
            if (endY > scanLowerPosition) scanLowerPosition = endY;

            float gradient = (v2[0] - v1[0]) / dy;
            float dz_y = (d2 - d1) / dy;
            float startX = ((v1[0]) + (startY - v1[1]) * gradient);
            if (startX < 0 || startX > width) isClippingRightOrLeft = true;
            float tempZ = d1 - v1[1] * dz_y + startY * dz_y;
            for (var y = startY; y <= endY; y++, startX += gradient, tempZ += dz_y) {
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
                xRightInView = x_right > 0 && x_right < width;
                if (xLeftInView && xRightInView) continue;
                if (x_left >= width || x_right <= 0) {
                    xLeft[y] = 0;
                    xRight[y] = 0;
                    continue;
                }
                float dx =  x_right - x_left;
                float dz = zRight[y] - zLeft[y];
                if(!xLeftInView) {
                    xLeft[y] = 0;
                    zLeft[y] = (zLeft[y] + dz / dx * (-x_left) ) ;

                }
                if(!xRightInView) {
                    xRight[y] = width;
                    zRight[y] = (zRight[y] - dz /dx * (x_right - width));
                }
            }
        }
    }

    private void renderTriangle() {
        switch (vbo.tfs) {
            case SingleColor -> renderTriangle1();
            case ColorList -> renderTriangle2();
        }
    }

    private void renderTriangle1() {
        for (int i = scanUpperPosition; i <= scanLowerPosition; i++) {
            var x_left = xLeft[i];
            var x_right = xRight[i];
            if (x_right > width) x_right = width - 1;
            if (x_left > width) x_left = width - 1;
            var z_Left = zLeft[i];
            var z_Right = zRight[i];
            var dz = (z_Right- z_Left)/(x_right - x_left);
            x_left += i * width;
            x_right += i * width;
            for (int j = x_left; j < x_right; j++, z_Left+= dz) {
                if (zBuffer[j] < z_Left) {
                    zBuffer[j] = z_Left;
                    screen[j] = vbo.triangleColor;
                }
            }
        }
    }
    private void renderTriangle2() {
        for (int i = scanUpperPosition; i <= scanLowerPosition; i++) {
            var x_left = xLeft[i];
            var x_right = xRight[i];
            if (x_right > width) x_right = width - 1;
            if (x_left > width) x_left = width - 1;
            var z_Left = zLeft[i];
            var z_Right = zRight[i];
            var dz = (z_Right- z_Left)/(x_right - x_left);
            x_left += i * width;
            x_right += i * width;
            for (int j = x_left; j < x_right; j++, z_Left+= dz) {
                if (zBuffer[j] < z_Left) {
                    zBuffer[j] = z_Left;
                    screen[j] = vbo.colors[triangleCount];
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
                Logger.getGlobal().fatal(RenderCore.class, "error in sync");
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


    public void setContext(ArrayList<VBO> vboList) {
        this.vboList = vboList;
    }


    public void setVboStartAndEnd(int start, int end) {
        this.VboStart = start; this.VboEnd = end;
    }
}
