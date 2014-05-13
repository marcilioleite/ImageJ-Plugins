/*
 * The MIT License
 *
 * Copyright 2014 SciJava.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

/**
 * Filtro para Ajuste de Brilho em Imagens.
 *
 * @author Marcilio Leite
 */
public class Brilho_ implements PlugInFilter {

    @Override
    public int setup ( String string , ImagePlus ip ) {
        return DOES_RGB;
    }

    @Override
    public void run ( ImageProcessor ip ) {

        ColorProcessor cp = (ColorProcessor) ip;
        int[] pixels = (int[]) cp.getPixels ();

        for ( int i = 0 ; i < pixels.length ; i++ ) {
            
            int color = pixels[i];

            int r = Math.min ( 3 * ((color & 0xff0000) >> 16) , 255 );
            int g = Math.min ( 3 * ((color & 0x00ff00) >> 8) , 255 );
            int b = Math.min ( 3 * ((color & 0x0000ff)) , 255 );

            pixels[i] = (r << 16) + (g << 8) + b;
        }

        cp.setPixels ( pixels );
    }

}
