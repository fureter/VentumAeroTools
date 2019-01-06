/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Design.Constraints;

import ventumaerotools.Aerodynamics.Atmosphere;
import ventumaerotools.Design.Constraint;

/**
 * @reference  Aircraft Design: A systems Engineering Approach, Mohammad H Sadraey
 * @author FurEter
 */
public class MaxSpeed extends Constraint{
    //Zero lift coefficient of drag
    public double CD0;
    //Propeller efficency
    public double nuPropeller;
    //Main wing planform efficency coefficent
    public double K;
    
    public MaxSpeed(double alt, double vel, double CD0, double nu, double K, String title){
        super(alt,vel,title);
        this.CD0 = CD0;
        this.nuPropeller = nu;
        this.K = K;
    }
    
    /**
     * 
     * @param WS Wing loading design space
     * @return array holding required Power loading to meet the constraint at the given wing loading
     */
    public double[] weightToPowerProp(double[] WS){
        double[] WP = new double[WS.length];
        for(int i = 0; i< WS.length;i++){
            WP[i] = (nuPropeller*(Atmosphere.getDensity(altitude)/Atmosphere.getDensity(0)))/(
                    .5*Atmosphere.getDensity(altitude)*Math.pow(velocity,3)*CD0*(1/WS[i]) + 2*K*WS[i]/(Atmosphere.getDensity(altitude)*velocity));
        }
        return WP;
    }
}
