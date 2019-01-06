/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Aerodynamics;

import static ventumaerotools.Numerical.Numerical.interpolate;

/**
 *<h1>Provides atmospheric data for usage throughout the program, references data from https://www.engineeringtoolbox.com</h1>
 * @author FurEter
 */
public class Atmosphere {
    //Gravitational acceleration for earth
    public static double g = 9.81;
    
    /**
     * <h2>Uses linear interpolation from https://www.engineeringtoolbox.com to return atmospheric density at given altitude</h2>
     * @author FurEter
     * @param altitude Altitude given in m
     * @return returns the density at the given altitude in units kg/m^3
     * @reference Engineering ToolBox, (2003). U.S. Standard Atmosphere. [online] Available at: https://www.engineeringtoolbox.com/standard-atmosphere-d_604.html [Accessed 05 01 2019].
     */
    public static double getDensity(double altitude){
        if(-1000 <= altitude && altitude < 0){
            return interpolate(altitude,-1000,0,1.347,1.225);
        }else if(0 <= altitude && altitude < 1000){
            return interpolate(altitude,0,1000,1.225,1.112);
        }else if(1000 <= altitude && altitude < 2000){
            return interpolate(altitude,1000,2000,1.112,1.007);
        }else if(2000 <= altitude && altitude < 3000){
            return interpolate(altitude,2000,3000,1.007,0.9093);
        }else if(3000 <= altitude && altitude < 4000){
            return interpolate(altitude,3000,4000,0.9093,0.8194);
        }else if(4000 <= altitude && altitude < 5000){
            return interpolate(altitude,4000,5000,0.8194,0.7364);
        }else if(5000 <= altitude && altitude < 6000){
            return interpolate(altitude,5000,6000,0.7364,0.6601);
        }else if(6000 <= altitude && altitude < 7000){
            return interpolate(altitude,6000,7000,0.6601,0.5900);
        }else if(7000 <= altitude && altitude < 8000){
            return interpolate(altitude,7000,8000,0.5900,0.5258);
        }else if(8000 <= altitude && altitude < 9000){
            return interpolate(altitude,8000,9000,0.5258,0.4671);
        }else if(9000 <= altitude && altitude < 10000){
            return interpolate(altitude,9000,10000,0.4671,0.4135);
        }else if(10000 <= altitude && altitude < 15000){
            return interpolate(altitude,10000,15000,0.4135,0.1948);
        }else if(15000 <= altitude && altitude < 20000){
            return interpolate(altitude,15000,20000,0.1948,0.08891);
        }else if(20000 <= altitude && altitude < 25000){
            return interpolate(altitude,20000,25000,0.08891,0.04008);
        }else if(25000 <= altitude && altitude < 30000){
            return interpolate(altitude,25000,30000,0.04008,0.01841);
        }else if(30000 <= altitude && altitude < 40000){
            return interpolate(altitude,30000,40000,0.01841,0.003996);
        }else if(40000 <= altitude && altitude < 50000){
            return interpolate(altitude,40000,50000,0.003996,0.001027);
        }else if(50000 <= altitude && altitude < 60000){
            return interpolate(altitude,50000,60000,0.001027,0.0003097);
        }else if(60000 <= altitude && altitude < 70000){
            return interpolate(altitude,60000,70000,0.0003097,0.00008283);
        }else if(70000 <= altitude && altitude < 80000){
            return interpolate(altitude,70000,80000,0.00008283,0.00001846);
        }else{
            return 0.0000001;
        }
    }
}
