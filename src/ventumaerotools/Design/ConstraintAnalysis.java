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
import ventumaerotools.Design.Constraints.MaxSpeed;
import ventumaerotools.Design.Constraints.RateOfClimb;
import ventumaerotools.Design.Constraints.StallSpeed;
import ventumaerotools.Design.Constraints.TakeOff;
import ventumaerotools.Graph.XYLineChart_AWT;
import ventumaerotools.VentumAeroTools;

/**
 *
 * @author FurEt
 */
public class ConstraintAnalysis {
    
    public ArrayList<Constraint> constraints;
    public double[] WS;
    
    public double CD_0;
    public double LDmax;
    public double nuProp;
    public double e;
    public double AR;
    public double K;
    
    public double mu;
    public double CL_TO;
    public double CD_TO;
    public double sTO;
    public double CL_R;
    public double CD_G;
    
    public boolean interceptAvaliable = false;
    public double[] interceptPoint;
    
    public void initilize(double[] WS){
        constraints = new ArrayList<>();
        this.WS = WS;
        boolean add = true;
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Aircraft CD_0 estimate");
        CD_0 = input.nextDouble();
        System.out.println("Enter Aircraft L/Dmax estimate");
        LDmax = input.nextDouble();
        System.out.println("Enter Aircraft propeller efficency estimate");
        nuProp = input.nextDouble();
        System.out.println("Enter Aircraft e estimate");
        e = input.nextDouble();
        System.out.println("Enter desired Aspect Ratio");
        AR = input.nextDouble();
        K = 1/(Math.PI*AR*e);
        input.nextLine();
        while(add){
            System.out.println("Add Constraint(Y/N)");
            String s = input.nextLine();
            if(s.equalsIgnoreCase("y")){
                System.out.println("Select Constraint(Stall speed(ST), Rate of Climb(ROC), Take Off Distance(TO), Max Speed(VM))");
                s = input.nextLine();
                if(s.equalsIgnoreCase("ST")){
                    System.out.println("Enter stall altitude (0m-3000m)");
                    double altitude = input.nextDouble();
                    System.out.println("Enter stall Velocity m/s");
                    double velocity = input.nextDouble();
                    System.out.println("Enter Aircraft CLmax estimate");
                    double CLmax = input.nextDouble();
                    String title = "Stall speed = " + velocity+ " at " + altitude + "m";
                    StallSpeed stall = new StallSpeed(altitude,velocity,CLmax,title);
                    constraints.add(stall);
                    input.nextLine();
                }else if(s.equalsIgnoreCase("ROC")){
                    System.out.println("Enter climbing altitude (0m-3000m)");
                    double altitude = input.nextDouble();
                    System.out.println("Enter desired rate of climb m/s");
                    double velocity = input.nextDouble();
                    String title = "Rate of Climb = " + velocity + " at " + altitude + "m";
                    RateOfClimb climb = new RateOfClimb(nuProp,CD_0,LDmax,K,altitude,velocity,title);
                    constraints.add(climb);
                    input.nextLine();
                }else if(s.equalsIgnoreCase("TO")){
                    System.out.println("Enter takeoff altitude (0m-3000m)");
                    double altitude = input.nextDouble();
                    System.out.println("Enter stall Velocity m/s");
                    double velocity = input.nextDouble();
                    System.out.println("Enter Aircraft CD_TO estimate");
                    CD_TO = input.nextDouble();
                    System.out.println("Enter Aircraft CL_TO estimate");
                    CL_TO = input.nextDouble();
                    System.out.println("Enter Aircraft ground ceofficent of friction estimate");
                    mu = input.nextDouble();
                    System.out.println("Enter Aircraft desired take off distance (m)");
                    sTO = input.nextDouble();
                    CL_R = 2*0.4*Atmosphere.g/(Atmosphere.getDensity(altitude)*1*Math.pow(velocity*1.2, 2));
                    CD_G = CD_TO-mu*CL_TO;
                    String title = "Take off distance = " + sTO + " at " + altitude + "m";
                    TakeOff to = new TakeOff(mu,nuProp,CD_G,sTO,CL_R,altitude,velocity,title);
                    constraints.add(to);
                    input.nextLine();
                }else if(s.equalsIgnoreCase("VM")){
                    System.out.println("Enter altitude (0m-3000m)");
                    double altitude = input.nextDouble();
                    System.out.println("Enter max Velocity m/s");
                    double velocity = input.nextDouble();
                    String title = "Max speed = " + velocity + " at " + altitude + "m";
                    MaxSpeed vm = new MaxSpeed(altitude,velocity,CD_0,nuProp,K,title);
                    constraints.add(vm);
                    input.nextLine();
                }else{
                    System.out.println("Please enter valid constraint type");
                }
            }else if(s.equalsIgnoreCase("n")){
                add = false;    
            }else{
                System.out.println("Enter valid choise");
            }
        }
    }
    
    public Aircraft createPropAircraft(){
        Aircraft aircraft = new Aircraft();
        
        aircraft.type = "prop";
        double WSLanding = 0;
        double WPDesign = Integer.MAX_VALUE;
        double WSDesign = Integer.MAX_VALUE;
        for(int i = 0; i < constraints.size();i++){
            double[] a = constraints.get(i).weightToPowerProp(WS);
            if(a[0] == a[1] && a[1] == a[2]){
                WSLanding = a[0];
                WSDesign = WSLanding;
            }
        }
        if(WSLanding != 0){
            for(int i = 0; i < constraints.size();i++){
                double[] a = constraints.get(i).weightToPowerProp(WS);
                if(a[0] != a[1] && a[1] != a[2]){
                    double[] WP = constraints.get(i).weightToPowerProp(WS);
                    int intercept = intercept(WS,WSLanding);
                    if(WP[intercept] < WPDesign){
                        WPDesign = WP[intercept];
                    }
                }
            }
        }
        System.out.println("Design W/S: " +WSDesign +"N/m^2");
        System.out.println("Design W/P: " +WPDesign+"N/w");
        System.out.println("Design P/W: " +1/(WPDesign/9.81)+"w/kg");
        
        interceptAvaliable = true;
        interceptPoint = new double[]{WSDesign,WPDesign};
        
        aircraft.WPDesign = WPDesign;
        aircraft.WSDesign = WSDesign;
        
        return aircraft;
    }
    
    public int intercept(double[] a, double[] b){
        double dist = 1000;
        int index = 0;
        for(int i = 0; i < a.length;i++){
            double c = Math.abs(a[i]-b[i]);
            if( c <= dist){
                index = i;
                dist = c;
            }
        }
        return index;
    }
    
    public int intercept(double[] a, double b){
        double dist = 1000;
        int index = 0;
        for(int i = 0; i < a.length;i++){
            double c = Math.abs(a[i]-b);
            if( c <= dist){
                index = i;
                dist = c;
            }
        }
        if(a[index] > b && index > 0){
            index -= 1;
        }
        return index;
    }
    
    public void graphConstraints(){
        XYLineChart_AWT chart = new XYLineChart_AWT("Graph","graph","W/S (N/m^2)","W/P (N/w)");
        for(int i  = 0; i < constraints.size();i++){
            double[] WP = constraints.get(i).weightToPowerProp(WS);
            if(WP[1] == WP[2] && WP[1] == WP[0]){
                double[] w = new double[]{WP[1],WP[1]};
                double max = 0;
                for(int j  = 0; j < constraints.size();j++){
                    if(VentumAeroTools.max(constraints.get(j).weightToPowerProp(WS)) > max && VentumAeroTools.max(constraints.get(j).weightToPowerProp(WS)) != w[0]){
                        max = VentumAeroTools.max(constraints.get(j).weightToPowerProp(WS));
                        System.out.println(max);
                    }
                }
                double[] p = new double[]{0,max};
                chart.addToDataset(w,p,constraints.get(i).title);
            }else{
                chart.addToDataset(WS,constraints.get(i).weightToPowerProp(WS),constraints.get(i).title);
            }
        }
        if(interceptAvaliable){
            chart.addToDataset(new double[]{interceptPoint[0],interceptPoint[0]},new double[]{interceptPoint[1],interceptPoint[1]} , "Design Point");
        }
        chart.pack();
        RefineryUtilities.centerFrameOnScreen( chart );          
        chart.setVisible( true ); 
    }
}
