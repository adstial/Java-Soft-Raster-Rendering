package code.mygL;

import code.app.AppConfig;
import code.dependence.math.Vector3D;

import static code.mygL.RenderCore.*;
import static code.mygL.RenderCore.Lower;

public class Crop {

    public static int width = AppConfig.pw, height = AppConfig.ph;

    public static int halfWidth = width / 2, halfHeight = height / 2;

    public static int distance = AppConfig.distance;

    private final Vector3D
            edge1 = new Vector3D(),
            edge2 = new Vector3D(),
            surfaceNormal = new Vector3D();



    // 测试隐藏面
    public boolean testHidden(final Triangle triangle, final float[] mostPosition) {
        //测试 1: 如果三角形的顶点全部在Z裁剪平面后面，则这个三角形可视为隐藏面
        boolean allBehindClippingPlane = true;
        for(int i = 0; i < 3; i++) {
            if(triangle.vertices[i].z >= nearClipDistance) {
                allBehindClippingPlane = false;
                break;
            }
        }
        if(allBehindClippingPlane)
            return true;

        //测试 2: 计算三角形表面法线向量并检查其是否朝向视角
        edge1.set(triangle.vertices[1])
                .sub(triangle.vertices[0]);
        edge2.set(triangle.vertices[2])
                .sub(triangle.vertices[0]);
        surfaceNormal.crossAs(edge1, edge2);
        float dotProduct  = surfaceNormal.dot(triangle.vertices[0]);
        //如果不朝向视角， 则这个三角形可视为隐藏面
        if(dotProduct >= 0)
            return true;


        //测试 3: 判断三角形是否在屏幕外
        mostPosition[Left] = width;
        mostPosition[Right] = -1;
        mostPosition[Upper] = height;
        mostPosition[Lower] = -1;

        for(int j = 0; j < 3; j++) {
            //获得顶点的深度值
            triangle.vertexDepth[j] = 1f/triangle.vertices[j].z;

            //用投影公式计算顶点在屏幕上的2D坐标
            triangle.vertices2D[j][0] = halfWidth + triangle.vertices[j].x* distance *triangle.vertexDepth[j];
            triangle.vertices2D[j][1] = halfHeight - triangle.vertices[j].y* distance *triangle.vertexDepth[j];

            if(triangle.vertices[j].z >= nearClipDistance) {
                mostPosition[Left] = Math.min(mostPosition[Left], triangle.vertices2D[j][0]);
                mostPosition[Right] = Math.max(mostPosition[Right], triangle.vertices2D[j][0]);
                mostPosition[Upper] = Math.min(mostPosition[Upper], triangle.vertices2D[j][1]);
                mostPosition[Lower] = Math.max(mostPosition[Lower], triangle.vertices2D[j][1]);
            }else {
                float screenX = halfWidth + triangle.vertices[j].x* distance /nearClipDistance;
                float screenY = halfHeight - triangle.vertices[j].y* distance /nearClipDistance;
                mostPosition[Left] = Math.min(mostPosition[Left], screenX);
                mostPosition[Right] = Math.max(mostPosition[Right], screenX);
                mostPosition[Upper] = Math.min(mostPosition[Upper], screenY);
                mostPosition[Lower] = Math.max(mostPosition[Lower], screenY);
            }
        }

        //如果这个三角形的最左边或最右或最上或最下都没有被重新赋值，那么这个三角形肯定在屏幕范围之外，所以不对其进行渲染。
        if(mostPosition[Left] == width ||  mostPosition[Right] == -1 || mostPosition[Upper] == height || mostPosition[Lower] == -1) {
            return true;
        }

        //判断三角形是否和屏幕的左边和右边相切
        triangle.isClippingRightOrLeft = mostPosition[Left] < 0 || mostPosition[Right] >= width;

        return false;
    }

    public boolean clipZNearPlane(final Triangle triangle, final float[] mostPosition,boolean light) {
        // 一般情况下三角形顶点数为3，裁剪后有可能变为4。
        triangle.verticesCount = 0;

        triangle.needToBeClipped = false;

        if (light) {
            for (int i = 0; i < 3; i++) {
                // 如果顶点在裁剪平面之前，则不做任何改动
                if(triangle.vertices[i].z >= nearClipDistance){
                    triangle.clippedVertices[triangle.verticesCount].set(triangle.vertices[i]);
                    triangle.clippedLight[triangle.verticesCount] = triangle.vertexLight[i];
                    triangle.verticesCount++;
                }
                //如果顶点在裁剪平面之后，则需要对三角形进行裁剪
                else{
                    triangle.needToBeClipped = true;

                    //找到前一个顶点（即三角形边上与当前顶点相邻的顶点）
                    int index = (i + 2) % 3;
                    if(triangle.vertices[index].z >= nearClipDistance){
                        //如果前一个顶点在裁剪平面的前面，  就找出当前顶点和前一个顶点之间的线段在裁剪平面的交点
                        approximatePoint(triangle, triangle.verticesCount, i, index, true);
                        triangle.verticesCount++;
                    }
                    //找到后一个顶点（即三角形边上与当前顶点相邻的另一个顶点）
                    index = (i + 1) % 3;
                    if(triangle.vertices[index].z >= nearClipDistance){
                        //如果后一个顶点在裁剪平面的前面，  就找出当前顶点和后一个顶点之间的线段在裁剪平面的交点
                        approximatePoint(triangle, triangle.verticesCount, i, index, true);
                        triangle.verticesCount++;
                    }
                }
            }
        }
        else {
            for (int i = 0; i < 3; i++) {
                // 如果顶点在裁剪平面之前，则不做任何改动
                if(triangle.vertices[i].z >= nearClipDistance){
                    triangle.clippedVertices[triangle.verticesCount].set(triangle.vertices[i]);
                    triangle.verticesCount++;
                }
                //如果顶点在裁剪平面之后，则需要对三角形进行裁剪
                else{
                    triangle.needToBeClipped = true;

                    //找到前一个顶点（即三角形边上与当前顶点相邻的顶点）
                    int index = (i + 2) % 3;
                    if(triangle.vertices[index].z >= nearClipDistance){
                        //如果前一个顶点在裁剪平面的前面，  就找出当前顶点和前一个顶点之间的线段在裁剪平面的交点
                        approximatePoint(triangle, triangle.verticesCount, i, index, false);
                        triangle.verticesCount++;
                    }
                    //找到后一个顶点（即三角形边上与当前顶点相邻的另一个顶点）
                    index = (i + 1) % 3;
                    if(triangle.vertices[index].z >= nearClipDistance){
                        //如果后一个顶点在裁剪平面的前面，  就找出当前顶点和后一个顶点之间的线段在裁剪平面的交点
                        approximatePoint(triangle, triangle.verticesCount, i, index, false);
                        triangle.verticesCount++;
                    }
                }
            }

            //如果三角形被裁剪平面裁剪则要重新计算一遍和顶点的有关的参数
            if(triangle.needToBeClipped) {
                mostPosition[Left] = width;
                mostPosition[Right] = -1;
                mostPosition[Upper] = height;
                mostPosition[Lower] = -1;

                for(int j = 0; j < triangle.verticesCount; j++) {
                    //获得顶点的深度值
                    triangle.vertexDepth[j] = 1f/triangle.clippedVertices[j].z;

                    //用投影公式计算顶点在屏幕上的2D坐标
                    triangle.vertices2D[j][0] = halfWidth + triangle.clippedVertices[j].x* distance *triangle.vertexDepth[j];
                    triangle.vertices2D[j][1] = halfHeight - triangle.clippedVertices[j].y* distance *triangle.vertexDepth[j];

                    mostPosition[Left] = Math.min(mostPosition[Left], triangle.vertices2D[j][0]);
                    mostPosition[Right] = Math.max(mostPosition[Right], triangle.vertices2D[j][0]);
                    mostPosition[Upper] = Math.min(mostPosition[Upper], triangle.vertices2D[j][1]);
                    mostPosition[Lower] = Math.max(mostPosition[Lower], triangle.vertices2D[j][1]);

                }

                //重新判断三角形是否在屏幕外
                if(mostPosition[Left] == width ||  mostPosition[Right] == -1 || mostPosition[Upper] == height || mostPosition[Lower] == -1) {
                    return true;
                }

                //重新判断三角形是否和屏幕的左边和右边相切
                triangle.isClippingRightOrLeft = mostPosition[Left] < 0 || mostPosition[Right] >= width;

            }
        }


        return false;
    }


    private final Vector3D dv = new Vector3D();




    // 找出两点之间的线段在裁剪平面的交点
    private void approximatePoint(final Triangle triangle, int vc, int i, int index, boolean hasLight) {
        var frontPoint = triangle.vertices[index];
        var behindPoint = triangle.vertices[i];

        // 交点在线段间位置的比例
        dv.set(frontPoint.x - behindPoint.x, frontPoint.y - behindPoint.y, frontPoint.z - behindPoint.z);
        float ratio = (frontPoint.z - nearClipDistance) / dv.z;

        // 线段方向矢量乘以这个比例，就可以得到交点的位置
        dv.scale(ratio);
        triangle.clippedVertices[vc].set(frontPoint)
                .sub(dv);

        if (hasLight) {
            triangle.clippedLight[vc] =
                    triangle.vertexLight[index] -
                            (triangle.vertexLight[index] - triangle.vertexLight[i]) * ratio;
        }
    }


}
