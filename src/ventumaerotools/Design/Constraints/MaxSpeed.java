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
public class MaxSpeed extends Constraint{
    public double CD0;
    public double nuPropulsion;
    public double K;
    
    public MaxSpeed(double alt, double massFrac, double vel, double CD0, double nu, double K){
        super(alt,massFrac,vel);
        this.CD0 = CD0;
        this.nuPropulsion = nu;
        this.K = K;
    }
    
    public double[] weightToPowerProp(double[] WS){
        double[] WP = new double[WS.length];
        for(int i = 0; i< WS.length;i++){
            WP[i] = (nuPropulsion*(Atmosphere.getDensity(altitude)/Atmosphere.getDensity(0)))/(
                    .5*Atmosphere.getDensity(altitude)*Math.pow(velocity,3)*CD0*(1/WS[i]) + 2*K*WS[i]/(Atmosphere.getDensity(altitude)*velocity));
        }
        return WP;
    }
}
