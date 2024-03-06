package code.mygL;

import code.dependence.logger.Logger;
import code.dependence.math.QuickMath;

import java.util.ArrayList;


/**
 * RenderCore类：
 * 渲染VBO至屏幕缓冲中
 *
 *
 *
 */
public class RenderCore extends Thread{

    private final Logger log = Logger.getGlobal();;

    // 锁住线程
    public final Object lock;


    public static final float nearClipDistance = 0.01f;

    public boolean work;
    public boolean close;

    public int width, height, halfWidth, halfHeight, distance;

    public ArrayList<VBO> vboList;              // 引用
    public int VboStart, VboEnd;                // 渲染Vbo的起始、结束的下标
    public VBO vbo;                             // 正在渲染的VBO的引用





    public int[] screen;            // 屏幕缓冲
    public float[] zBuffer;         // z深度缓冲

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

        localTrigonometric = new float[3][2];
        globalTrigonometric = new float[3][2];
        triangle = new Triangle();

        transformation = new Transformation();
        shader = new Shader();
        crop = new Crop();

        log.info(RenderCore.class, name + " has created");
    }


    @Override
    public void run() {
        while (!close) {
            // 等待唤醒
            sync();

            //锁被打开后, 运行渲染任务
            rasterize();
        }
    }


    public int triangleIndex;
    private void rasterize() {
        // 无需渲染，返回
        if (VboStart == -1) return;

        // 遍历渲染对象
        for (int j = VboStart; j <= VboEnd; j++) {

            vbo = vboList.get(j);
            var hasLight = vbo.hasLight;

            // 获得所需的三角值
            getQuickTrigonometric();

            // 施加全局变换、局部变换
            transformation.transformation(vbo, localTrigonometric, globalTrigonometric);

            // 渲染VBO中的每个三角形
            for (triangleIndex = 0; triangleIndex < vbo.triangleCount; triangleIndex++) {

                // 构建三角形
                transformation.buildTriangle(hasLight, vbo, triangleIndex, triangle);

                //测试三角形是否该被渲染出来
                var isHidden = crop.testHidden(triangle, mostPosition);
                if (isHidden) continue;

                //将在三角形在z裁键平面的部分裁剪掉
                isHidden = crop.clipZNearPlane(triangle, mostPosition, vbo.hasLight);
                if (isHidden) continue;


                triangle.scanUpperPosition = height;
                triangle.scanLowerPosition = -1;
                if (hasLight) {
                    shader.scanTriangleWithLight(vbo.triangleFillStyle, triangle, width, height, mostPosition[Left]);
                    shader.renderTriangleWithLight(triangle, screen, zBuffer, width, vbo, triangleIndex);
                } else {
                    shader.scanTriangleWithoutLight(vbo.triangleFillStyle, triangle, mostPosition[Left]);
                    shader.renderTriangleWithoutLight(triangle, screen, zBuffer, width, vbo, triangleIndex);
                }
            }
        }
    }




    private void getQuickTrigonometric() {

        //从预先算好的三角函数表中获取变换角度所需的函数值
        for (int i = 0; i < 3; i++) {
            var r = vbo.localRotation[i];
            localTrigonometric[i][Sin] = QuickMath.getSin(r);
            localTrigonometric[i][Cos] = QuickMath.getCos(r);

            // 全局变换与视角变换相反
            globalTrigonometric[i][Sin] = QuickMath.getSin(-Camera.rate[i]);

            // cos函数无需取反
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
                //等待渲染器发布新的命令
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
        triangle.lLeft = new float[height]; triangle.lRight = new float[height];

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
