/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Design;

import java.util.ArrayList;
import java.util.Scanner;
import org.jfree.ui.RefineryUtilities;
import ventumaerotools.Aerodynamics.Atmosphere;
import ventumaerotools.Aircraft.Aircraft;
import ventumaerotools.Graph.XYLineChart_AWT;

/**
 *
 * @author FurEter
 */
public class PreliminaryDesign {
    public void preliminaryDesignLoop(Aircraft a){
        Scanner input = new Scanner(System.in);
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
        input.nextLine();
        if(a.vertStab != null){
            System.out.println("Enter lateral stability estimate (low, medium, high) [lower = less weight, higher = more stable");
            s = input.nextLine();
            if(s.equalsIgnoreCase("low")){
                cVT = 0.02;
            }else if(s.equalsIgnoreCase("medium")){
                cVT = 0.05;
            }else if(s.equalsIgnoreCase("high")){
                cVT = 0.1;
            }else{
                System.out.println("Not recognised entry, using default medium");
                cVT = 0.05;
            }
            System.out.println("Enter vertical Tail Aspect Ratio (usually lower than hAR)");
            vAR = input.nextDouble();
        }
        
        for(int i = 0; i < a.mainWing.size();i++){
            a.mainWing.get(i).tc = 0.15;
            a.mainWing.get(i).taperRatio = 1;
        }
            
        double tol = 1;
        ArrayList<Double> weight = new ArrayList<>();
        weight.add(a.mass);
        while(tol > Math.pow(10,-4)){
            a.fuel.mBattery = a.mass*a.fuel.bFrac;
            double wingArea = (a.mass*Atmosphere.g)/a.WSDesign;
            System.out.println("Wing area: " + wingArea);
            
            for(int i = 0; i < a.mainWing.size();i++){
                a.mainWing.get(i).area = wingArea/a.mainWing.size();
                a.mainWing.get(i).span = Math.sqrt(a.mainWing.get(i).aspectRatio*a.mainWing.get(i).area);
                a.mainWing.get(i).MAC = a.mainWing.get(i).area/a.mainWing.get(i).span;
                System.out.println("Main Wing Span: " + a.mainWing.get(i).span);
            }
            if(a.fuselage != null){
                a.fuselage.length = a.mainWing.get(0).span*.7;      //Initial fuselage length estimated as 70% of wing span.
                System.out.println("Fuselage Length: " + a.fuselage.length);
            }
            if(a.horzStab != null){
                for(int i = 0; i < a.horzStab.size();i++){
                    a.horzStab.get(i).aspectRatio = hAR;
                    a.horzStab.get(i).area = cHT*a.mainWing.get(0).area*a.mainWing.get(0).MAC/(a.fuselage.length*.6);   //Initial tail length = 60% fuselage length
                    a.horzStab.get(i).span = Math.sqrt(a.horzStab.get(i).area*hAR);
                    a.horzStab.get(i).MAC = a.horzStab.get(i).area/a.horzStab.get(i).span;
                    System.out.println("horzStab Area: " + a.horzStab.get(i).area);
                    System.out.println("horzStab Span: " + a.horzStab.get(i).span);
                }
            }
            if(a.vertStab != null){
                for(int i = 0; i < a.vertStab.size();i++){
                    a.vertStab.get(i).aspectRatio = vAR;
                    a.vertStab.get(i).area = cVT*a.mainWing.get(0).area*a.mainWing.get(0).span/(a.fuselage.length*.6);   //Initial tail length = 60% fuselage length
                    a.vertStab.get(i).span = Math.sqrt(a.vertStab.get(i).area*vAR);
                    a.vertStab.get(i).MAC = a.vertStab.get(i).area/a.vertStab.get(i).span;
                    System.out.println("vertStab Area: " + a.vertStab.get(i).area);
                    System.out.println("vertStab Span: " + a.vertStab.get(i).span);
                }
            }
            double m  = a.mass;
            WeightEstimation.gundlachWeightEstimation(a);
            tol = Math.abs(m-a.mass);
            weight.add(a.mass);
            System.out.println();
        }
        System.out.println("\nMTOW = " + a.mass);
        double[] mass = new double[weight.size()];
        double[] index = new double[weight.size()];
        for(int i = 0; i < weight.size();i++){
            index[i] = i;
            mass[i] = weight.get(i);
        }
        XYLineChart_AWT chart = new XYLineChart_AWT("Preliminary Mass","graph","Iteration","Mass [kg]");
        chart.addToDataset(index,mass,"MTOW");
        chart.pack();
        RefineryUtilities.centerFrameOnScreen( chart );          
        chart.setVisible( true ); 
    }
}
