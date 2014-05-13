
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import java.awt.Point;
import java.util.Stack;

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
 * A Simple Single Seeded Region Growing Algorithm for Color Image Segmentation
 * using Adaptive Thresholding.
 *
 * Verma, O. P.; Hanmandlu, M.; Susan, S.; Kulkarni, M.; Jain, P. K.
 *
 * http://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=5966497
 *
 * @author Marcilio Leite
 */
public class SeededRegionGrowing_ implements PlugInFilter {

    private ImageProcessor processor;
    private int rows;
    private int columns;
    private Point seed;
    private int rCount;
    private Stack<Point> pg;
    private Stack<Point> bp;
    private int[][] region;
    private int threshold;

    @Override
    public int setup ( String string , ImagePlus ip ) {

        return DOES_RGB;
    }

    @Override
    public void run ( ImageProcessor ip ) {

        processor = ip;
        rows = ip.getHeight ();
        columns = ip.getWidth ();
        seed = new Point ( rows / 2 , columns / 2 ); //position of seed (x, y)
        rCount = 1; // Counter to keep track of current region being grown
        pg = new Stack<> (); // stack to store pixels to grow
        bp = new Stack<> (); // stack to store boundary pixels of grown region
        region = new int[rows][columns];

        pg.push ( seed );
        threshold = 20; // Tenho que calcular o Otsu’s adaptive threshold

        runStepOne ();
        runStepTwo ();
        
        for (int i = 0; i < region.length-1; i++) {
            for (int j = 0; j < region.length-1; j++) {
                System.out.print ( region[i][j] );
            }
            System.out.println ();
        }
    }

    public void runStepOne () {

        while ( !pg.empty () ) {

            Point cp = pg.pop ();

            for ( int k = cp.x - 1 ; k <= cp.x + 1 ; k++ ) {

                for ( int z = cp.y - 1 ; z <= cp.y + 1 ; z++ ) {

                    try {
                        
                        if ( region[k][z] == 0 ) {

                            int curr = processor.getPixel ( cp.x , cp.y );//current
                            int nb = processor.getPixel ( k , z );//neighbor
                            int currR = (curr & 0xff0000) >> 16,
                                    currG = (curr & 0x00ff00) >> 8,
                                    currB = (curr & 0x0000ff);
                            int nbR = (nb & 0xff0000) >> 16,
                                    nbG = (nb & 0x00ff00) >> 8,
                                    nbB = (nb & 0x0000ff);

                            double distance = root ( quad ( nbR - currR ) +
                                                     quad ( nbG - currG ) + 
                                                     quad ( nbB - currB ) );

                            //System.out.println ( "Distance: " + distance );
                            if ( distance < threshold ) {
                                region[k][z] = rCount;
                                pg.push ( new Point ( k , z ) );
                            }
                            else {
                                bp.push ( new Point ( k , z ) );
                            }
                        }   
                    }
                    catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }
    }

    public void runStepTwo () {

        while ( !bp.empty () ) {
            seed = bp.pop ();
            rCount++;
            pg.push ( seed );
            runStepOne ();
        }
    }

    public double quad ( double num ) {
        return Math.pow ( num , 2 );
    }

    public double root ( double num ) {
        return Math.sqrt ( num );
    }
}
