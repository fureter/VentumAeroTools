/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Aerodynamics;

/**
 *
 * @author FurEt
 */
public class Atmosphere {
    public static double getDensity(double altitude){
        if(altitude < 3000){
            double density = 1.225 - (altitude-0)*(1.225-0.84)/(0-3000);
            return density;
        }else{
            double density = 0.0;
            return density;
        }
    }
}
