/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Design;

import java.util.ArrayList;
import java.util.Scanner;
import org.jfree.ui.RefineryUtilities;
import ventumaerotools.Aerodynamics.Airfoil;
import ventumaerotools.Aerodynamics.Atmosphere;
import ventumaerotools.Aerodynamics.Performance;
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
            //System.out.println("Wing area: " + wingArea);
            
            for(int i = 0; i < a.mainWing.size();i++){
                a.mainWing.get(i).area = wingArea/a.mainWing.size();
                a.mainWing.get(i).span = Math.sqrt(a.mainWing.get(i).aspectRatio*a.mainWing.get(i).area);
                a.mainWing.get(i).MAC = a.mainWing.get(i).area/a.mainWing.get(i).span;
                a.mainWing.get(i).twist = new double[2];
                a.mainWing.get(i).twist[0] = 0;
                a.mainWing.get(i).twist[1] = 0;
                a.mainWing.get(i).chord = new double[2];
                a.mainWing.get(i).chord[0] = a.mainWing.get(i).MAC;
                a.mainWing.get(i).chord[1] = a.mainWing.get(i).MAC;
                a.mainWing.get(i).airfoils = new Airfoil[2];
                double clCruiseSection = 2*(a.mass*Atmosphere.g)/(a.mainWing.get(i).area*Math.pow(a.constraint.cruiseVel,2)*Atmosphere.getDensity(a.constraint.cruiseAlt));
                double clStallSection = 2*(a.mass*Atmosphere.g)/(a.mainWing.get(i).area*Math.pow(a.constraint.stallVel,2)*Atmosphere.getDensity(0));
                System.out.println("Cruise Section CL: " + clCruiseSection);
                System.out.println("Stall Section CL: " + clStallSection);
                Airfoil airfoil = WeightMatricies.airfoilSelectionMatrix(clCruiseSection, clStallSection, 100000, 2, 4, 1, 5, 1, 2, 0);
                a.mainWing.get(i).airfoils[0] = airfoil;
                a.mainWing.get(i).airfoils[1] = airfoil;
                a.mainWing.get(i).tc = a.mainWing.get(i).airfoils[0].tcRatio;
                a.mainWing.get(i).taperRatio = a.mainWing.get(i).chord[1]/a.mainWing.get(i).chord[0];
                //System.out.println("Main Wing Span: " + a.mainWing.get(i).span);
            }
            if(a.fuselage != null){
                a.fuselage.length = a.mainWing.get(0).span*.7;      //Initial fuselage length estimated as 70% of wing span.
                //System.out.println("Fuselage Length: " + a.fuselage.length);
            }
            if(a.horzStab != null){
                for(int i = 0; i < a.horzStab.size();i++){
                    a.horzStab.get(i).aspectRatio = hAR;
                    a.horzStab.get(i).area = cHT*a.mainWing.get(0).area*a.mainWing.get(0).MAC/(a.fuselage.length*.6);   //Initial tail length = 60% fuselage length
                    a.horzStab.get(i).span = Math.sqrt(a.horzStab.get(i).area*hAR);
                    a.horzStab.get(i).MAC = a.horzStab.get(i).area/a.horzStab.get(i).span;
                    //System.out.println("horzStab Area: " + a.horzStab.get(i).area);
                    //System.out.println("horzStab Span: " + a.horzStab.get(i).span);
                }
            }
            if(a.vertStab != null){
                for(int i = 0; i < a.vertStab.size();i++){
                    a.vertStab.get(i).aspectRatio = vAR;
                    a.vertStab.get(i).area = cVT*a.mainWing.get(0).area*a.mainWing.get(0).span/(a.fuselage.length*.6);   //Initial tail length = 60% fuselage length
                    a.vertStab.get(i).span = Math.sqrt(a.vertStab.get(i).area*vAR);
                    a.vertStab.get(i).MAC = a.vertStab.get(i).area/a.vertStab.get(i).span;
                    //System.out.println("vertStab Area: " + a.vertStab.get(i).area);
                    //System.out.println("vertStab Span: " + a.vertStab.get(i).span);
                }
            }
            double m  = a.mass;
            WeightEstimation.gundlachWeightEstimation(a);
            tol = Math.abs(m-a.mass);
            weight.add(a.mass);
            System.out.println();
        
            
        }
        topologyDesign(a);
        System.out.println("\nMTOW = " + a.mass + "kg");
        System.out.println("MTOP = " + Atmosphere.g*a.mass/a.WPDesign + "W");
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
    
    public void topologyDesign(Aircraft a){
        //mainWingDesign(a);
    }
    
    public void mainWingDesign(Aircraft a){
        double s = 0;
        for(int i = 0; i < a.mainWing.size();i++){
            s+=a.mainWing.get(i).area;
        }
        double clCruiseSection = 2*(a.mass*Atmosphere.g)/(s*Math.pow(a.constraint.cruiseVel,2)*Atmosphere.getDensity(a.constraint.cruiseAlt));
        double clStallSection = 2*(a.mass*Atmosphere.g)/(s*Math.pow(a.constraint.stallVel,2)*Atmosphere.getDensity(0));
        
        double clCruiseWing = clCruiseSection/0.85;
        double clStallWing = clStallSection/0.85;
        
        System.out.println("CLw Cruise = " +clCruiseWing);
        
        double M = a.constraint.cruiseVel/342;
        if(M < 0.3){//uncompressible subsonic
            ArrayList m = new ArrayList<>();
            ArrayList cl = new ArrayList<>();
            ArrayList cd = new ArrayList<>();
            if(a.mainWing.size() == 1){
                if(a.horzStab == null){//flying wing or delta with no canard
                    a.mainWing.get(0).sweep[0] = 30;
                    a.mainWing.get(0).sweep[1] = 30;
                }
                m.add(a.mass);
                for(int h = 0; h < 6; h++){
                    double[][] clx;
                    clx = Performance.liftDistributionLLT(a.mainWing.get(0), -8, a.mainWing.get(0).chord.length-1, 18);
                    int i = -4;
                    for(;i < 12;i++){
                        clx = Performance.liftDistributionLLT(a.mainWing.get(0), i, a.mainWing.get(0).chord.length-1, 18);
                        if(clx[4][0] >= clCruiseWing){
                            break;
                        }
                    }
                    if(i == 12){
                        System.out.println("Wing cannot provide sufficent lift");
                        //If wing is not providing enough lift at 12 AOA or above, increase span by 5%
                        a.mainWing.get(0).span *= 1.05;
                    }else{
                        //If lift is good, decrease taper ratio
                        a.mainWing.get(0).chord[1] *=.9;
                    }
                    
                    a.mainWing.get(0).reCalc();
                    
                    WeightEstimation.gundlachWeightEstimation(a);
                    m.add(a.mass);
                    cl.add(clx[4][0]);
                    cd.add(clx[4][1]);
                    System.out.println("cl = " + clx[4][0]);
                    System.out.println("cdi = " + clx[4][1]);
                    
                }
            }
            
            XYLineChart_AWT graph = new XYLineChart_AWT("Wing Opt","Wing Opt","iter","Val");
            double[] iter = new double[m.size()];
            double[] mass = new double[m.size()];
            for(int i = 0; i < m.size();i++){
                iter[i] = i;
                mass[i] = (double)m.get(i);
            }
            graph.addToDataset(iter,mass,"Mass");
            iter = new double[cl.size()];
            double[] CL = new double[cl.size()];
            for(int i = 0; i < cl.size();i++){
                iter[i] = i;
                CL[i] = (double)cl.get(i);
            }
            graph.addToDataset(iter,CL,"cl");
            iter = new double[cd.size()];
            double[] CDi = new double[cd.size()];
            for(int i = 0; i < cd.size();i++){
                iter[i] = i;
                CDi[i] = (double)cd.get(i);
            }
            graph.addToDataset(iter,CDi,"cd_i");
            
            graph.pack();
            RefineryUtilities.centerFrameOnScreen( graph );          
            graph.setVisible( true ); 
            
        }else if(M > 0.8 && M < 1.2){//transonic
            
        }else if(M >= 1.2){//supersonic
            
        }else{//compressible subsonic
            
        }
        Performance.plotLiftDist(a.mainWing.get(0), 2, 1, 18);
        //System.out.println("Cruise Section cl: " + clCruiseSection);
        //System.out.println("Stall Section cl: " + clStallSection);
    }
}
