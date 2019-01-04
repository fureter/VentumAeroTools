/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools;

import ventumaerotools.Aerodynamics.Airfoil;
import ventumaerotools.Aircraft.Aircraft;
import ventumaerotools.Aircraft.Wing;
import ventumaerotools.Design.ConstraintAnalysis;
/**
 *
 * @author FurEt
 */
public class VentumAeroTools {

    /**
     * @param args the command line arguments
     */
    private static double range;                   //max Aircraft range
    private static double payload;                 //max Aircraft Payload
    
    public static void main(String[] args) {
        range = 20;
        payload = 1;
        double span = 1.35;
        double[] chord = new double[]{0.11,0.11};
        double[] position = new double[]{0.18,0.0,0.05};
        double[] twist = new double[]{0.0,0.0};
        double[] sweep = new double[]{0.0,0.0};
        double[] dihedral = new double[]{0.0,0.0};
        Airfoil[] airfoils = new Airfoil[2];
        Wing w = new Wing(2,span,chord,twist,position,sweep,dihedral,airfoils,0.9);
        
        System.out.println(w.toString());
        double[] WS = new double[200];
        for(int i = 0; i <WS.length;i++){
            WS[i] = (i+1)*0.25;
        }
        ConstraintAnalysis cs = new ConstraintAnalysis();
        cs.initilize(WS);
        Aircraft a = cs.createPropAircraft();
        cs.graphConstraints();
        
        
    }
    
    public static double max(double[] a){
        double max = 0;
        for(int i = 0; i < a.length;i++){
            if(a[i] > max){
                max = a[i];
            }
        }
        return max;
    }
    
}
