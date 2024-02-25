
package code.mygL;

public class Shader {
    public void scanTriangleWithoutLight(VBO.TriangleFillStyle tfs, final Triangle triangle, int width, int height) {
        var verticesCount = triangle.verticesCount;

        for (int i = 0; i < verticesCount; i++) {
            float[] v1 = triangle.vertices2D[i];
            float[] v2;

            float d1 = triangle.vertexDepth[i];
            float d2;
            if (i == verticesCount - 1) {
                v2 = triangle.vertices2D[0];
                d2 = triangle.vertexDepth[0];
            } else {
                v2 = triangle.vertices2D[i + 1];
                d2 = triangle.vertexDepth[i + 1];
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

            if (startY < triangle.scanUpperPosition) triangle.scanUpperPosition = startY;
            if (endY > triangle.scanLowerPosition) triangle.scanLowerPosition = endY;

            float gradient = (v2[0] - v1[0]) / dy;
            float dz_y = (d2 - d1) / dy;
            float startX = ((v1[0]) + (startY - v1[1]) * gradient);
            if (startX < 0 || startX > width) triangle.isClippingRightOrLeft = true;
            float tempZ = d1 - v1[1] * dz_y + startY * dz_y;
            for (var y = startY; y <= endY; y++, startX += gradient, tempZ += dz_y) {
                if (downwards) {
                    triangle.xRight[y] = (int)startX;
                    triangle.zRight[y] = tempZ;
                } else {
                    triangle.xLeft[y] = (int)startX;
                    triangle.zLeft[y] = tempZ;
                }
            }
        }

        if (triangle.isClippingRightOrLeft) {
            int x_left, x_right;
            boolean xLeftInView, xRightInView;
            for(int y = triangle.scanUpperPosition; y <= triangle.scanLowerPosition; y++){

                x_left = triangle.xLeft[y];
                x_right = triangle.xRight[y];

                xLeftInView = x_left >=0 && x_left < width;
                xRightInView = x_right >0 && x_right < width;

                if(xLeftInView && xRightInView && x_right >= x_left)
                    continue;

                if(x_left >= width  || x_right <= 0 || x_right < x_left ){
                    triangle.xLeft[y] = 0;
                    triangle.xRight[y] = 0;
                    continue;
                }



                float dx =  x_right - x_left;
                float dz = triangle.zRight[y] - triangle.zLeft[y];

                if(!xLeftInView){
                    triangle.xLeft[y] = 0;
                    triangle.zLeft[y] = (triangle.zLeft[y] + dz /dx * (-x_left) );

                }

                if(!xRightInView){
                    triangle.xRight[y] = width;
                    triangle.zRight[y] = (triangle.zRight[y] - dz /dx * (x_right - width));
                }
            }
        }
    }
    public void scanTriangleWithLight(VBO.TriangleFillStyle tfs, final Triangle triangle, int width, int height) {

    }

    public void renderTriangleWithoutLight(final Triangle triangle, final int[] screen, final float[] zBuffer, int width, final VBO vbo, int triangleIndex) {
        for (int i = triangle.scanUpperPosition; i <= triangle.scanLowerPosition; i++) {
            int x_left = triangle.xLeft[i];
            int x_right = triangle.xRight[i];
            if (x_right > width) x_right = width - 1;

            if (x_left > width) x_left = width - 1;

            float z_Left = triangle.zLeft[i];
            float z_Right = triangle.zRight[i];

            float dz = (z_Right- z_Left)/(x_right - x_left);

            x_left += i * width;
            x_right += i * width;

            if (vbo.triangleFillStyle == VBO.TriangleFillStyle.SingleColor) {
                for (int j = x_left; j < x_right; j++, z_Left+= dz) {
                    if (zBuffer[j] < z_Left) {
                        zBuffer[j] = z_Left;
                        screen[j] = vbo.triangleColor;
                    }
                }
            } else if (vbo.triangleFillStyle == VBO.TriangleFillStyle.ColorList) {
                for (int j = x_left; j < x_right; j++, z_Left+= dz) {
                    if (zBuffer[j] < z_Left) {
                        zBuffer[j] = z_Left;
                        screen[j] = vbo.colors[triangleIndex];
                    }
                }
            }


        }
    }
}