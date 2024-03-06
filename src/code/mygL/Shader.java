package code.mygL;

import code.app.AppConfig;

public class Shader {
    public static int width = AppConfig.pw, height = AppConfig.ph;


    public void scanTriangleWithoutLight(VBO.TriangleFillStyle tfs, final Triangle triangle, float leftmostPosition) {
        s1(triangle, leftmostPosition);
    }

    private void s1(final Triangle triangle, float mostPosition) {
        var verticesCount = triangle.verticesCount;

        for (int i = 0; i < verticesCount; i++) {

            float[] v1 = triangle.vertices2D[i];
            float[] v2;

            float d1 = triangle.vertexDepth[i];
            float d2;

            // 如果已经处理到最后一个顶点
            // 则第二个点为第一个顶点
            // 否则第二个顶点为下一个顶点
            if (i == verticesCount - 1) {
                v2 = triangle.vertices2D[0];
                d2 = triangle.vertexDepth[0];
            } else {
                v2 = triangle.vertices2D[i + 1];
                d2 = triangle.vertexDepth[i + 1];
            }

            //默认是下降的边
            boolean downwards = true;

            // 如果第一个顶点低于第二个顶点
            // 则为上升的边
            if (v1[1] > v2[1]) {
                downwards = false;

                // 互换
                var tempV = v1;
                v1 = v2; v2 = tempV;

                var tempD = d1;
                d1 = d2; d2 = tempD;
            }


            float dy = v2[1] - v1[1];
            // 忽略水平边
            if (dy == 0) continue;

            var startY = Math.max((int)(v1[1]) + 1, 0);
            var endY = Math.min((int) v2[1], height - 1);

            if (startY < triangle.scanUpperPosition)
                triangle.scanUpperPosition = startY;

            if (endY > triangle.scanLowerPosition)
                triangle.scanLowerPosition = endY;

            float gradient = (v2[0] - v1[0]) / dy;

            float startX = ((v1[0]) + (startY - v1[1]) * gradient);
            if (startX < 0 && !triangle.isClippingRightOrLeft)
                startX = mostPosition;


            float dz_y = (d2 - d1) / dy;

            float tempZ = d1 - v1[1] * dz_y + startY * dz_y;

//            if (startX < 0 || startX > width) triangle.isClippingRightOrLeft = true;

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

                xLeftInView = x_left >= 0 && x_left < width;
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





    public void scanTriangleWithLight(VBO.TriangleFillStyle tfs, final Triangle triangle, int width, int height, float leftMost) {
        sl1(triangle, width, height, leftMost);
    }

    private void sl1(final Triangle triangle, int width, int height, float leftMost) {
        final var vc = triangle.verticesCount;

        for (int i = 0; i < vc; i++) {
            float[] v1 = triangle.vertices2D[i];
            float[] v2;

            float d1 = triangle.vertexDepth[i];
            float d2;

            float l1 = triangle.clippedLight[i];
            float l2;

            if (i == vc - 1) {
                v2 = triangle.vertices2D[0];
                d2 = triangle.vertexDepth[0];
                l2 = triangle.clippedLight[0];
            } else {
                v2 = triangle.vertices2D[i + 1];
                d2 = triangle.vertexDepth[i + 1];
                l2 = triangle.clippedLight[i + 1];
            }

            boolean downwards = true;

            if (v1[1] > v2[1]) {
                downwards = false;
                var tempV = v1;
                v1 = v2; v2 = tempV;

                var tempD = d1;
                d1 = d2; d2 = tempD;

                var tempL = l1;
                l1 = l2; l2 = tempL;
            }

            var dy = v2[1] - v1[1];
            if (dy == 0) continue;

            var startY = Math.max((int)(v1[1]) + 1, 0);
            var endY = Math.min((int) v2[1], height - 1);

            if (startY < triangle.scanUpperPosition) triangle.scanUpperPosition = startY;
            if (endY > triangle.scanLowerPosition) triangle.scanLowerPosition = endY;

            float gradient = (v2[0] - v1[0]) / dy;
            var startX = (v1[0] + (startY - v1[1]) * gradient);
            //if (startX < 0 || startX > width) triangle.isClippingRightOrLeft = true;

            if (startX < 0 && !triangle.isClippingRightOrLeft) {
                startX = leftMost;
            }

            var z_dy = (d2 - d1) / dy;
            var tempZ = d1 + (startY - v1[1]) * z_dy;

            var l_dy = (l2 - l1) / dy;
            var tempL = l1 + (startY - v1[1]) * l_dy;

            for (var y = startY; y <= endY; y++, startX += gradient, tempZ += z_dy, tempL += l_dy) {
                if(downwards){
                    triangle.xRight[y] = (int)startX;
                    triangle.zRight[y] = tempZ;
                    triangle.lRight[y] = tempL;
                }else{
                    triangle.xLeft[y] = (int)startX;
                    triangle.zLeft[y] = tempZ;
                    triangle.lLeft[y] = tempL;
                }
            }
        }

        if (triangle.isClippingRightOrLeft) {
            int x_left, x_right;
            boolean xLeftInView, xRightInView;

            for (var y = triangle.scanUpperPosition; y < triangle.scanLowerPosition; y++) {
                x_left = triangle.xLeft[y];
                x_right = triangle.xRight[y];

                xLeftInView = x_left >= 0 && x_left < width;
                xRightInView = x_right > 0 && x_right >= x_left;

                if(xLeftInView && xRightInView && x_right >= x_left)
                    continue;

                if(x_left >= width  || x_right <= 0 || x_right < x_left ){
                    triangle.xLeft[y] = 0;
                    triangle.xRight[y] = 0;
                    continue;
                }



                var dx =  x_right - x_left;
                var dz = triangle.zRight[y] - triangle.zLeft[y];
                var dl = triangle.lRight[y] - triangle.lLeft[y];

                if(!xLeftInView){
                    triangle.xLeft[y] = 0;
                    triangle.zLeft[y] = (triangle.zLeft[y] + dz /dx * (-x_left) );
                    triangle.lLeft[y] = (triangle.lLeft[y] + dl / dx * (-x_left));

                }

                if(!xRightInView){
                    triangle.xRight[y] = width;
                    triangle.zRight[y] = (triangle.zRight[y] - dz /dx * (x_right - width));
                    triangle.lRight[y] = (triangle.lRight[y] - dl / dx * (x_right - width));
                }
            }
        }
    }

    public void renderTriangleWithoutLight(final Triangle triangle, final int[] screen, final float[] zBuffer, int width, final VBO vbo, int triangleIndex) {
        for (int i = triangle.scanUpperPosition; i <= triangle.scanLowerPosition; i++) {
            int x_left = triangle.xLeft[i];
            int x_right = triangle.xRight[i];
//            if (x_right > width) x_right = width - 1;
//
//            if (x_left > width) x_left = width - 1;

            float z_Left = triangle.zLeft[i];
            float z_Right = triangle.zRight[i];

            float dz = (z_Right - z_Left) / (x_right - x_left);

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

    public void renderTriangleWithLight(final Triangle triangle, final int[] screen, final float[] zBuffer, int width, final VBO vbo, int triangleIndex) {
        for (int i = triangle.scanUpperPosition; i <= triangle.scanLowerPosition; i++) {
            var x_left = triangle.xLeft[i] ;
            var x_right = triangle.xRight[i];
            var z_Left = triangle.zLeft[i];
            var z_Right = triangle.zRight[i];
            var lightLevel = triangle.lLeft[i];
            var light_right = triangle.lRight[i];

            var dz = (z_Right - z_Left) / (x_right - x_left);
            float dLight = (light_right - lightLevel)/(x_right - x_left);

            x_left  += i * width;
            x_right += i * width;

            if (vbo.triangleFillStyle == VBO.TriangleFillStyle.SingleColor) {
                for (var j = x_left; j < x_right; j++, z_Left += dz, lightLevel += dLight) {
                    if (zBuffer[j] < z_Left) {
                        zBuffer[j] = z_Left;
                        var r = vbo.triangleColor >> 16;
                        var g = (vbo.triangleColor >> 8) & 0xff;
                        var b = vbo.triangleColor & 0xff;

                        r*= (int) lightLevel;
                        g*= (int) lightLevel;
                        b*= (int) lightLevel;

                        r = Math.min(r, 255);
                        g = Math.min(g, 255);
                        b = Math.min(b, 255);

                        screen[j] = (r << 16) | (g << 8) | b;
                    }
                }
            } else if (vbo.triangleFillStyle == VBO.TriangleFillStyle.ColorList) {
                for (var j = x_left; j < x_right; j++, z_Left += dz, lightLevel += dLight) {
                    if (zBuffer[j] < z_Left) {
                        zBuffer[j] = z_Left;
                        var color = vbo.colors[triangleIndex];
                        var r = color >> 16;
                        var g = color >> 8 & 0xff;
                        var b = color & 0xff;

                        r*= (int) lightLevel;
                        g*= (int) lightLevel;
                        b*= (int) lightLevel;

                        r = Math.min(r, 255);
                        g = Math.min(g, 255);
                        b = Math.min(b, 255);

                        screen[j] = (r << 16) | (g << 8) | b;
                    }
                }
            }
        }
    }
}