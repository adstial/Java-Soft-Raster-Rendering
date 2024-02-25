package code.mygL;


import code.dependence.math.Vector3D;

import static code.mygL.RenderCore.*;

/**
 * M V P
 *
 *
 */
public class Transformation {
    private static final int X = 0, Y = 1, Z = 2;
    private static final int Sin = 0, Cos = 1;
    private final Vector3D vertexNormal = new Vector3D(),
            lightDirection = new Vector3D(),
            reflectionDirection = new Vector3D(),
            viewDirection = new Vector3D();
    public void transformation(VBO vbo, final float[][] localTrigonometric, final float[][] globalTrigonometric) {
        for (int i = 0; i < vbo.vertexCount; i++) {
            var nowVertexes = vbo.updateVertexes[i];
            nowVertexes.set(vbo.vertexes[i])
                    .scale(vbo.scale)
                    .rotate_X(localTrigonometric[X][Sin], localTrigonometric[X][Cos])
                    .rotate_Y(localTrigonometric[Y][Sin], localTrigonometric[Y][Cos])
                    .rotate_Z(localTrigonometric[Z][Sin], localTrigonometric[Z][Cos])
                    .add(vbo.localTranslation);

            if (vbo.hasLight) {
                vertexNormal.set(vbo.normals[i])
                        .rotate_X(localTrigonometric[X][Sin], localTrigonometric[X][Cos])
                        .rotate_Y(localTrigonometric[Y][Sin], localTrigonometric[Y][Cos])
                        .rotate_Z(localTrigonometric[Z][Sin], localTrigonometric[Z][Cos]);
                vbo.vertexLightLevels[i] = 0;
                for (var light : vbo.lightSource) {
                    lightDirection.set(light.position)
                            .sub(nowVertexes)
                            .unit();
                    var ld = vbo.kd * (Math.max(vertexNormal.dot(lightDirection), 0));

                    reflectionDirection.set(vertexNormal)
                            .scale(2 * vertexNormal.dot(lightDirection))
                            .sub(lightDirection);

                    viewDirection.set(Camera.position)
                            .sub(nowVertexes)
                            .unit();

                    var ls = viewDirection.dot(reflectionDirection);
                    ls = Math.max(ls,0);
                    ls = ls*ls*ls*ls;
                    ls = ls*ls*ls*ls;     //n = 16;
                    ls = vbo.ks * ls;
                    vbo.vertexLightLevels[i] += light.la + ld + ls;
                }
            }
            nowVertexes.sub(Camera.position)
                    .rotate_Y(globalTrigonometric[Y][Sin], globalTrigonometric[Y][Cos]).rotate_X(globalTrigonometric[X][Sin], globalTrigonometric[X][Cos]);

        }
    }




    public void buildTriangle(boolean hasLight, final VBO vbo, int index, final Triangle triangle) {
        if (hasLight) {
            for (int i = 0; i < 3; i++) {
                var j = vbo.indexes[index * 3 + 2 - i];
                triangle.vertices[i] = vbo.updateVertexes[j];
                triangle.vertexLight[i] = vbo.vertexLightLevels[j];
            }
        } else {
            for (int i = 0; i < 3; i++) {
                triangle.vertices[i] = vbo.updateVertexes[vbo.indexes[index * 3 + 2 - i]];
            }
        }

    }





    public void getVertices2D(final Vector3D[] vertices, final float[][] vertices2D, final float[] vertexDepth ,int halfWidth, int halfHeight, int distance) {
        for (int i = 0; i < 3; i++) {
            vertices2D[i][0] = halfWidth + vertices[i].x * distance / vertices[i].z;
            vertices2D[i][1] = halfHeight - vertices[i].y * distance / vertices[i].z;

            vertexDepth[i] = 1f / vertices[i].z;
        }
    }




}
