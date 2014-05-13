
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import java.util.Arrays;

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
 * Localização de Placa de Automóveis Brasileiros.
 *
 * @author Marcilio Leite
 */
public class BrazilianLicensePlateLocation_ implements PlugInFilter {

    @Override
    public int setup ( String string , ImagePlus ip ) {

        return DOES_8G;
    }

    @Override
    public void run ( ImageProcessor ip ) {

        for ( int y = 5 ; y < ip.getWidth () ; y += 5 ) {

            /**
             * TODO: Substituir Thresholds esporádicos por números
             *        obtidos através de informaçoes da imagem, como
             *        por exemplo, histograma.
             */
            
            final int threshold = 50; //Esporádico
            final int distanceThreshold = 30; //Esporádico
            double[] currLine = ip.getLine ( 0 , y , ip.getWidth () , y );
            int reference = 0;
            int direction = 0;
            double[] refPoints = new double[currLine.length];

            Arrays.fill ( refPoints , -1 );
            
            /**
             * Encontrar os pontos de referência.
             *
             * São os pontos relativamente máximos em intervalos de oscilações.
             */
            for ( int x = 0 ; x < currLine.length - 1 ; x++ ) {

                double current = currLine[x],
                        next = currLine[x + 1],
                        tmpReference = currLine[reference],
                        difference = Math.abs ( tmpReference - current );

                int newDirection = next > current ? 1 : 0;

                if ( newDirection != direction ) {
                    
                    if ( difference >= threshold ) {
                        
                        reference = x;
                        tmpReference = currLine[reference];
                        refPoints[x] = tmpReference;
                    }
                }
                direction = newDirection;
            }

            /**
             * Agrupar os pontos de referência.
             *
             * Agrupa a partir de uma distância definida.
             */
            int firstPoint = -1, lastPoint = -1;

            for ( int x = 0 ; x < refPoints.length ; x++ ) {

                if ( refPoints[x] >= 0 ) {
                    
                    if ( firstPoint < 0 ) {
                        
                        firstPoint = x;
                    }
                    lastPoint = x;
                }
                else {
                    
                    if ( x - lastPoint > distanceThreshold ) {
                        
                        if ( (lastPoint - firstPoint) >= ip.getHeight () / 3 ) {
                            
                            ip.drawLine ( firstPoint , y , lastPoint , y );
                            // Ao invés de pintar uma linha, adicionar a um 
                            // array de linhas
                        }
                        firstPoint = -1;
                        lastPoint = -1;
                    }
                }
            }
        }
    }

}
