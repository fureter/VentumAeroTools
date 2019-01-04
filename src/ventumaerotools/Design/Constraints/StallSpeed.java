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
public class StallSpeed extends Constraint{
    
    public double CLmax;
    
    public StallSpeed(double alt, double vel, double CLmax,String title){
        super(alt,vel,title);
        this.CLmax =  CLmax;
    }
    
    public double wingLoading(){
        return 0.5*Atmosphere.getDensity(super.altitude)*Math.pow(super.velocity,2)*this.CLmax;
    }
    
    public double[] weightToPowerProp(double[] WS){
        double[] WP = new double[WS.length];
        for(int i = 0; i< WS.length;i++){
            WP[i] = 0.5*Atmosphere.getDensity(super.altitude)*Math.pow(super.velocity,2)*this.CLmax;
        }
        return WP;
    }
}
