/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Design.Constraints;

import ventumaerotools.Aerodynamics.Atmosphere;
import ventumaerotools.Design.Constraint;

/**
 *
 * @author FurEt
 */
public class RateOfClimb extends Constraint{
    
    public double nuPropeller;
    public double CD_0;
    public double LDmax;
    public double K;
    
    public RateOfClimb(double nuPropeller, double CD_0, double LDmax, double K, double altitude, double velocity) {
        super(altitude, velocity);
        this.nuPropeller = nuPropeller;
        this.CD_0 = CD_0;
        this.LDmax = LDmax;
        this.K = K;
    }
    
    public double[] weightToPowerProp(double[] WS){
        double[] WP = new double[WS.length];
        for(int i = 0; i< WS.length;i++){
            WP[i] = 1/((velocity/nuPropeller)+Math.sqrt((2/(Atmosphere.getDensity(altitude)*Math.sqrt(3*CD_0/K)))*WS[i])*(1.155/(LDmax*nuPropeller)));
        }
        return WP;
    }
    
}
