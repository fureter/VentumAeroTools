/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Aerodynamics;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.jfree.ui.RefineryUtilities;
import ventumaerotools.Aircraft.Wing;
import ventumaerotools.Graph.XYLineChart_AWT;

/**
 *
 * @author FurEter
 */
public class Performance {
    
    public static double[][] liftDistributionLLT(Wing w, double alpha,int n, int m){
        double s = w.area;
        double AR = w.aspectRatio;
        double iw = w.twist[0];
        double b = w.span;
        double MAC = w.MAC;
        double cRoot = w.chord[0];
        double[][] matrix = new double[n*m][n*m];
        double[] theta = new double[n*m];
        double[] mu = new double[n*m];
        double[] LHS = new double[n*m];
        double[] cl = new double[n*m];
        double[] x = new double[n*m];
        double[][] clx = new double[5][n*m];
        
        for(int i = 0; i < n*m;i++){
            theta[i] = Math.PI/(n*m*2) + (Math.PI/(n*m*2))*(i);
            //theta[i] = (Math.PI/2)*(i+1)/(n*m+1);
            //System.out.println(theta[i]);
        }
        for(int i = 0; i < n*m;i++){
            x[i] = (b/2)*Math.cos(theta[i]);
            //System.out.println("x" + x[i]);
        }
        
        for(int i = 0; i < n*m; i++){
            int segment = (int)(x[i]/((b/2)/(n)));
            double len = (b/2)/n;
            
            //System.out.println(segment);
            //System.out.println(len);
            
            
            
            double C =((((len-(x[i]-(segment*len)))/len)*w.chord[segment] + ((x[i]-(segment*len))/len)*w.chord[segment+1]));
            //double C = w.chord[segment]*(1-(1-w.chord[segment+1]/w.chord[segment])*Math.cos(theta[i]));
            clx[2][i] = C;
            //System.out.println("Chord "+i+" = "+C);
            double CLalpha =((((len-(x[i]-(segment*len)))/len)*w.airfoils[segment].CLalpha + ((x[i]-(segment*len))/len)*w.airfoils[segment+1].CLalpha));
            double a0 =((((len-(x[i]-(segment*len)))/len)*w.airfoils[segment].a0 + ((x[i]-(segment*len))/len)*w.airfoils[segment+1].a0));
            double twist =((((len-(x[i]-(segment*len)))/len)*w.twist[segment] + ((x[i]-(segment*len))/len)*w.twist[segment+1]));
            clx[3][i] = twist;
            //double CLalpha = w.airfoils[segment+1].CLalpha*(1-(1-w.airfoils[segment].CLalpha/w.airfoils[segment+1].CLalpha)*Math.cos(theta[i]));
            //double a0 = w.airfoils[segment+1].a0*(1-(1-w.airfoils[segment].a0/w.airfoils[segment+1].a0)*Math.cos(theta[i]));
            //double twist = w.twist[segment+1]*(1-(1-w.twist[segment]/w.twist[segment+1])*Math.cos(theta[i]));
            
            
            mu[i] = C*CLalpha/(4*b);
            LHS[i] = mu[i]*Math.sin(theta[i])*(alpha-a0+twist)/(Math.PI/180.0);
            //LHS[i] = mu[i]*(alpha-a0+twist)*(Math.PI/180);
            //System.out.println("LHS " + i+" = " + LHS[i]);
            //System.out.println("mu " + i+" = " + mu[i]);
            //System.out.println("twist " + i+" = " + twist);
            //System.out.println("Chord " + i+" = " + C);
            //System.out.println("CLalpha " + i+" = " + CLalpha);
            //System.out.println("a0 " + i+" = " + a0+"\n");
        }
        
        for(int i = 0; i < n*m;i++){
            for(int j = 0; j < n*m; j++){
                matrix[i][j] = Math.sin((j+1)*theta[i])*(Math.sin(theta[i]) + (j+1)*mu[i]);
                //matrix[i][j] = Math.sin((2*(j+1)-1)*theta[i])*(1+(mu[i]*(2*(j+1)-1)/Math.sin(theta[i])));
                //if(Math.abs(matrix[i][j]) < Math.pow(10,-11)){
                //    matrix[i][j] = 0;
                //}
                //System.out.print(matrix[i][j] + " ");
            }
            //System.out.println();
        }
        RealMatrix coefficients = new Array2DRowRealMatrix(matrix,
                       false);
        DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
        RealVector constants = new ArrayRealVector(LHS, false);
        RealVector solution = solver.solve(constants);
        //System.out.println(solution.toString());
        double CLw = solution.getEntry(0)*Math.PI*AR;
        double CDi = 0;
        for(int i = 0; i < n*m;i++){
            CDi += (2*(i+1)-1)*Math.pow(solution.getEntry(i),2);
        }
        CDi = CDi*Math.PI*AR;
        //System.out.println("CLw = " +CLw);
        //System.out.println("CDi = " +CDi);
        
        for(int i  = 0; i < n*m;i++){
            double sum = 0;
            int segment = (int)(x[i]/((b/2)/(n)));
            double len = (b/2)/n;
            for(int j = 0; j < n*m;j++){
                sum += solution.getEntry(j)*Math.sin((2*(j+1)-1)*theta[i]);
            }
            cl[i] = sum*4*b/((((len-(x[i]-(segment*len)))/len)*w.chord[segment] + ((x[i]-(segment*len))/len)*w.chord[segment+1]));
            //System.out.println("CL" + i+ " " + cl[i]);
        }
        //cl[cl.length-1] = 0;
        for(int i = 0; i < n*m;i++){
            clx[0][i] = x[i];
            clx[1][i] = cl[i];
        }
        clx[4][0] = CLw;
        clx[4][1] = CDi;
        return clx;
    }
    
    public static void plotLiftDist(Wing w, double alpha,int n, int m){
        double[][] clx = liftDistributionLLT(w,alpha,n,m);
        XYLineChart_AWT chart = new XYLineChart_AWT("Lift Distribution","Lift Distribution alpha = " + alpha+ "°","x/b","cl");

        double[] c = clx[2];
        double[] t = clx[3];
        
        double[] x = clx[0];
        double[] cl = new double[n*m];
        for(int i = 0; i < n*m;i++){
            //cl[i] = clx[1][m*n-(i+1)];
            cl[i] = clx[1][i];
        }
        //double[] cl = clx[2];

        //System.out.println("cl size = " + cl.length + " x size = " + x.length);
        chart.addToDataset(x, cl, "alpha = "+alpha+"°");
        chart.addToDataset(x, c, "chord");
        //chart.addToDataset(x, t, "twist");
        chart.pack();
        RefineryUtilities.centerFrameOnScreen( chart );          
        chart.setVisible( true ); 
        
    }
}
