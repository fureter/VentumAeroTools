/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Design;

import ventumaerotools.Aerodynamics.Atmosphere;
import ventumaerotools.Aircraft.Aircraft;
import ventumaerotools.Aircraft.Propulsion;

/**
 *
 * @author FurEter
 */
public class WeightEstimation {
    public static void preliminaryWeightEstiamte(Aircraft a){
        double averageEfficency = 0;
        for(int i = 0; i < a.propulsion.size();i++){
            Propulsion p = a.propulsion.get(i);
            averageEfficency += p.totalEfficency;
        }
        averageEfficency /= a.propulsion.size();
        if(a.fuel.fuelType.equals("battery")){
            double bFrac = ((a.designRange*Atmosphere.g)/(a.fuel.specificEnergy*averageEfficency))/a.CLCD;
            double eFrac = 0.545547449;
            double mass = a.designPayload/(1-(eFrac+bFrac));
            a.mass = mass;
        }else{
                    
        }
    }
}
