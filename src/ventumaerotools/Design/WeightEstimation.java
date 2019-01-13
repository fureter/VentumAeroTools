/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Design;

import java.util.Scanner;
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
            double eFrac = 0;
            double bFrac = 0;
            
            do{
                bFrac = ((a.designRange*Atmosphere.g)/((a.fuel.specificEnergy*3600)*averageEfficency))/a.CLCD;

                System.out.println("Battery Mass Fraction = " + bFrac);
                eFrac = 0.65;    //Start with large empty mass estimate
                System.out.println("Empty Mass Fraction = " + eFrac);
                if(bFrac > (1-eFrac)){
                    System.out.println("Range requirements cannot be fullfilled with specific Energy of battery, average Efficiency of powerplant, and Estimated CL/CD of the aircraft");
                    System.out.println("Please Enter Lower Range");
                    Scanner input = new Scanner(System.in);
                    a.designRange = input.nextDouble();
                }
            }while(bFrac > (1-eFrac));
            double mass = a.designPayload/(1-(eFrac+bFrac));
            a.mass = mass;
            
        }else{
                    
        }
    }
    
    public static void raymerWeightEstimation(Aircraft a){
        
    }
    
    public static void gundlachWeightEstimation(Aircraft a){
        
    }
}
