package code.mygL;

import code.dependence.math.Vector3D;

import static code.mygL.RenderCore.*;
import static code.mygL.RenderCore.Lower;

public class Crop {
    public void clipZNearPlaneIfNeeded(boolean hasLight, final Triangle triangle) {
        if (hasLight) {
            for (int i = 0; i < 3; i++) {
                if (triangle.vertices[i].z >= nearClipDistance) {
                    triangle.clippedVertices[triangle.verticesCount].set(triangle.vertices[i]);
                    triangle.clippedLight[triangle.verticesCount] = triangle.vertexLight[i];
                    triangle.verticesCount++;
                } else {
                    triangle.clipped = true;
                    var before = (i + 2) % 3;
                    if (triangle.vertices[before].z >= nearClipDistance) {
                        approximatePoint(triangle.verticesCount, triangle.vertices[i], triangle.vertices[before], triangle.vertexLight[i], triangle.vertexLight[before], triangle.clippedVertices, triangle.clippedLight);
                        triangle.verticesCount++;
                    }
                    var behind = (i + 1) % 3;
                    if (triangle.vertices[behind].z >= nearClipDistance) {
                        approximatePoint(triangle.verticesCount, triangle.vertices[i], triangle.vertices[behind], triangle.vertexLight[i], triangle.vertexLight[behind], triangle.clippedVertices, triangle.clippedLight);
                        triangle.verticesCount++;
                    }
                }
            }
        } else {
            for (int i = 0; i < 3; i++) {
                if (triangle.vertices[i].z >= nearClipDistance) {
                    triangle.clippedVertices[triangle.verticesCount].set(triangle.vertices[i]);
                    triangle.verticesCount++;
                } else {
                    triangle.clipped = true;
                    var before = (i + 2) % 3;
                    if (triangle.vertices[before].z >= nearClipDistance) {

                        approximatePoint(triangle.verticesCount, triangle.vertices[i], triangle.vertices[before], triangle.clippedVertices);
                        triangle.verticesCount++;
                    }
                    var behind = (i + 1) % 3;
                    if (triangle.vertices[behind].z >= nearClipDistance) {
                        approximatePoint(triangle.verticesCount, triangle.vertices[i], triangle.vertices[behind], triangle.clippedVertices);
                        triangle.verticesCount++;
                    }
                }
            }
        }
    }

    private final Vector3D dv = new Vector3D();


    private void approximatePoint(int index, final Vector3D behindPoint, final Vector3D frontPoint, final Vector3D[] clippedVertices) {
        dv.set(frontPoint).sub(behindPoint);
        var ratio = (frontPoint.z - nearClipDistance) / dv.z;
        dv.scale(ratio);
        clippedVertices[index].set(frontPoint).sub(dv);
    }

    private void approximatePoint(int index, Vector3D behindPoint, Vector3D frontPoint, float behindLightLevel, float frontLightLevel, final Vector3D[] clippedVertices, final float[] clippedLight) {
        dv.set(frontPoint).sub(behindPoint);
        var ratio = (frontPoint.z - nearClipDistance) / dv.z;
        dv.scale(ratio);
        clippedVertices[index].set(frontPoint).sub(dv);
        clippedLight[index] = frontLightLevel - ((frontLightLevel - behindLightLevel)*ratio);
    }

    public boolean testAllBehindClippingPlane(final Triangle triangle) {
        for(int i = 0; i < 3; i++)
            if (triangle.vertices[i].z >= nearClipDistance)
                return false;
        return true;
    }

    private final Vector3D
            edge1 = new Vector3D(),
            edge2 = new Vector3D(),
            surfaceNormal = new Vector3D();
    public boolean testNotTowardView(final Triangle triangle) {
        edge1.set(triangle.vertices[1])
                .sub(triangle.vertices[0]);
        edge2.set(triangle.vertices[2])
                .sub(triangle.vertices[0]);
        surfaceNormal.crossAs(edge1, edge2);
        var dorProduct = surfaceNormal.dot(triangle.vertices[0]);
        return dorProduct >= 0;
    }


    public boolean testOutside(final float[][] vertices2D, final float[] mostPosition, int width, int height) {
        mostPosition[Left] = width;
        mostPosition[Right] = -1;
        mostPosition[Upper] = height;
        mostPosition[Lower] = -1;
        for (int i = 0; i < 3; i++) {
            if (vertices2D[i][0] <= mostPosition[Left])
                mostPosition[Left] = vertices2D[i][0];
            if (vertices2D[i][0] >= mostPosition[Right])
                mostPosition[Right] = vertices2D[i][0];
            if (vertices2D[i][1] <= mostPosition[Upper])
                mostPosition[Upper] = vertices2D[i][1];
            if (vertices2D[i][1] >= mostPosition[Lower])
                mostPosition[Lower] = vertices2D[i][1];
        }
        return mostPosition[Left] == width |
                mostPosition[Right] == -1 |
                mostPosition[Upper] == height |
                mostPosition[Lower] == -1;
    }

}
