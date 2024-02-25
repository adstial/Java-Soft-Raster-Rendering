package code.mygL;

import code.dependence.logger.Logger;
import code.dependence.math.QuickMath;

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

    public static final int Sin = 0, Cos = 1;
    public float [][] localTrigonometric;
    public float[][] globalTrigonometric;


    public Transformation transformation;
    public Shader shader;
    public Crop crop;

    public Triangle triangle;



    public RenderCore(String name) {
        setName(name);
        lock = new Object();

        transformation = new Transformation();
        shader = new Shader();

        localTrigonometric = new float[3][2];
        globalTrigonometric = new float[3][2];
        triangle = new Triangle();
        crop = new Crop();
    }


    @Override
    public void run() {
        while (!close) {
            sync();
            rasterize();
        }
    }


    public int triangleIndex;
    private void rasterize() {
        if (VboStart == -1) return;
        for (int j = VboStart; j <= VboEnd; j++) {
            vbo = vboList.get(j);
            var hasLight = vbo.hasLight;
            var tfs = vbo.triangleFillStyle;

            getQuickTrigonometric();

            transformation.transformation(vbo, localTrigonometric, globalTrigonometric);

            for (triangleIndex = 0; triangleIndex < vbo.triangleCount; triangleIndex++) {

                transformation.buildTriangle(hasLight, vbo, triangleIndex, triangle);

                if (crop.testNotTowardView(triangle)) continue;

                if (crop.testAllBehindClippingPlane(triangle)) continue;

                transformation.getVertices2D(triangle.vertices, triangle.vertices2D, triangle.vertexDepth, halfWidth, halfHeight, distance);

                if (crop.testOutside(triangle.vertices2D, mostPosition, width, height)) continue;
                else triangle.isClippingRightOrLeft = mostPosition[Left] < 0 || mostPosition[Right] >= width;

                triangle.clipped = false;
                triangle.verticesCount = 0;
                crop.clipZNearPlaneIfNeeded(hasLight, triangle);

                if (triangle.clipped) {
                    transformation.getVertices2D(triangle.clippedVertices, triangle.vertices2D, triangle.vertexDepth, halfWidth, halfHeight, distance);
                    if (crop.testOutside(triangle.vertices2D, mostPosition, width, height)) continue;
                    else triangle.isClippingRightOrLeft = mostPosition[Left] < 0 || mostPosition[Right] >= width;
                }

                triangle.scanUpperPosition = height;
                triangle.scanLowerPosition = -1;
                if (hasLight) {
                    shader.scanTriangleWithLight(vbo.triangleFillStyle, triangle, width, height);
                } else {
                    shader.scanTriangleWithoutLight(vbo.triangleFillStyle, triangle, width, height);
                    shader.renderTriangleWithoutLight(triangle, screen, zBuffer, width, vbo, triangleIndex);
                }
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

    public float[] mostPosition = new float[4];
    public static final int Left = 0, Right = 1, Upper = 2, Lower = 3;

    private void sync() {
        synchronized (this) {
            try {
                synchronized (lock) {
                    lock.notify();
                    work = false;
                }
                wait();
            } catch (InterruptedException e) {
                Logger.getGlobal().fatal(RenderCore.class, "error in sync" + e);
            }
        }
    }

    public void setContext(int width, int height, int distance) {
        this.width = width; halfWidth = width / 2;
        this.height = height; halfHeight = height / 2;

        triangle.xLeft = new int[height]; triangle.xRight = new int[height];
        triangle.zLeft = new float[height]; triangle.zRight =new float[height];

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
