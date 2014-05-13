
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import static ij.plugin.filter.PlugInFilter.DOES_8G;
import static ij.plugin.filter.PlugInFilter.NO_UNDO;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

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

/**
 *
 * @author Marcilio
 */
public class EsticamentoHistograma_ implements PlugInFilter {

    @Override
    public void run ( ImageProcessor ip ) {
        
        ByteProcessor processador = (ByteProcessor) ip;
        byte[] pixelsLinear = (byte[]) processador.getPixels ();
        short maximo = 0;
        short minimo = Short.MAX_VALUE;
        short temp;
        for ( int i = 0 ; i < pixelsLinear.length ; i++ ) {
            temp = (short) (pixelsLinear[i] & 0xff);
            if ( temp > maximo ) {
                maximo = temp;
            }

            if ( temp < minimo ) {
                minimo = temp;
            }
        }
        for ( int i = 0 ; i < pixelsLinear.length ; i++ ) {
            pixelsLinear[i] = (byte) ((((pixelsLinear[i] & 0xff) - minimo)
                    / (float) (maximo - minimo)) * 255);
        }
        processador.setPixels ( pixelsLinear );
    }

    @Override
    public int setup ( String arg0 , ImagePlus arg1 ) {
        return DOES_8G + NO_UNDO;
    }

}
