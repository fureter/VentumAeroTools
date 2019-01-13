/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Design;

import java.util.Scanner;
import ventumaerotools.Aerodynamics.Atmosphere;
import ventumaerotools.Aircraft.Aircraft;

/**
 *
 * @author FurEter
 */
public class PreliminaryDesign {
    public void preliminaryDesignLoop(Aircraft a){
        Scanner input = new Scanner(System.in);
        a.addTopologyUserInput();
        WeightEstimation.preliminaryWeightEstiamte(a);
        
        System.out.println("Preliminary Mass Estimate = " + a.mass + "kg");
        System.out.println(a.topologyToString());
        
        double cHT = 0;
        double cVT = 0;
        
        double hAR = 0;
        double vAR = 0;
        String s = "";
        if(a.horzStab != null){
            System.out.println("Enter longitudinal stability estimate (low, medium, high) [lower = less weight, higher = more stable");
            s = input.nextLine();
            if(s.equalsIgnoreCase("low")){
                cHT = 0.5;
            }else if(s.equalsIgnoreCase("medium")){
                cHT = 0.75;
            }else if(s.equalsIgnoreCase("high")){
                cHT = 1;
            }else{
                System.out.println("Not recognised entry, using default medium");
                cHT = 0.75;
            }
            System.out.println("Enter horizontal Tail Aspect Ratio (if canard hAR > AR)");
            hAR = input.nextDouble();
        }
        if(a.vertStab != null){
            System.out.println("Enter lateral stability estimate (low, medium, high) [lower = less weight, higher = more stable");
            s = input.nextLine();
            if(s.equalsIgnoreCase("low")){
                cVT = 0.02;
            }else if(s.equalsIgnoreCase("medium")){
                cVT = 0.05;
            }else if(s.equalsIgnoreCase("high")){
                cVT = 0.01;
            }else{
                System.out.println("Not recognised entry, using default medium");
                cVT = 0.05;
            }
            System.out.println("Enter vertical Tail Aspect Ratio (usually lower than hAR)");
            vAR = input.nextDouble();
        }
            
        double tol = 1;
        
        while(tol > Math.pow(1,-4)){
            double wingArea = (a.mass*Atmosphere.g)/a.WSDesign;
            
            for(int i = 0; i < a.mainWing.size();i++){
                a.mainWing.get(i).area = wingArea/a.mainWing.size();
                a.mainWing.get(i).span = Math.sqrt(a.mainWing.get(i).aspectRatio*a.mainWing.get(i).area);
                a.mainWing.get(i).MAC = a.mainWing.get(i).area/a.mainWing.get(i).span;
            }
            if(a.fuselage != null){
                a.fuselage.length = a.mainWing.get(0).span*.7;      //Initial fuselage length estimated as 70% of wing span.
            }
            if(a.horzStab != null){
                for(int i = 0; i < a.horzStab.size();i++){
                    a.horzStab.get(i).aspectRatio = hAR;
                }
            }
            if(a.vertStab != null){
                for(int i = 0; i < a.vertStab.size();i++){

                }
            }
            
            
        }
    }
}
