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
public class TakeOff extends Constraint{
    public double mu;
    public double nuPropeller;
    public double CD_G;
    public double sTO;
    public double CL_R;

    public TakeOff(double mu, double nuPropeller, double CD_G, double sTO, double CL_R, double altitude, double velocity,String title) {
        super(altitude, velocity,title);
        this.mu = mu;
        this.nuPropeller = nuPropeller;
        this.CD_G = CD_G;
        this.sTO = sTO;
        this.CL_R = CL_R;
    }
    
    public double[] weightToPowerProp(double[] WS){
        double[] WP = new double[WS.length];
        for(int i = 0; i< WS.length;i++){
            WP[i] = ((1-Math.exp(0.6*Atmosphere.getDensity(altitude)*Atmosphere.g*CD_G*sTO*(1/WS[i])))/(mu-(mu+CD_G/CL_R)*(Math.exp(0.6*Atmosphere.getDensity(altitude)*Atmosphere.g*CD_G*sTO*(1/WS[i])))))*nuPropeller/velocity;
        }
        return WP;
    }
    
    
}
