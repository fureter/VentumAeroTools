/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools;

import ventumaerotools.Aerodynamics.Airfoil;
import ventumaerotools.Aircraft.Wing;
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
        Wing w = new Wing(2,span,chord,twist,position,sweep,dihedral,airfoils);
        
        System.out.println(w.toString());
        
    }
    
}
