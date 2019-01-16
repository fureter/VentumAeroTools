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
 * <h1>Performs constraint analysis for finding Wing loading and Power loading Design space for aircraft.</h2>
 * @author FurEter
 */
public class ConstraintAnalysis {
    
    //All given constraints in ArrayList
    public ArrayList<Constraint> constraints;
    //Wing loading design space
    public double[] WS;
    
    //aircraft CD at zero lift estimate
    public double CD_0;
    // aircraft L/D max estimate
    public double LDmax;
    // Propeller efficency
    public double nuProp;
    // Oswald efficency factor
    public double e;
    // Main wing Aspect Ratio
    public double AR;
    // wing planforam efficiency coefficent
    public double K;
    
    // Ground coefficent of friction for take off
    public double mu;
    // Aircraft take off CL
    public double CL_TO;
    // Aircraft take off CD
    public double CD_TO;
    // take off ground distance
    public double sTO;
    // Aircraft CL at rotation
    public double CL_R;
    // Aircraft CD on ground
    public double CD_G;
    
    // if intercept if avaliable to graph, depends on order of operations
    public boolean interceptAvaliable = false;
    // intercept point for design choice [0] = WS [1] = WP
    public double[] interceptPoint;
    
    public double maxVel;
    
    
    /**
     * <h2>initialization of Constraints, polls user for input, currently supports 4 constraints (max speed, stall speed, rate of climb, take off distance)</h2>
     * @param WS input wing loading design space
     */
    public void initialize(double[] WS){
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
        System.out.println("Enter max velocity for dive");
        maxVel = input.nextDouble();
        input.nextLine();
        while(add){
            System.out.println("Add Constraint(Y/N)");
            String s = input.nextLine();
            if(s.equalsIgnoreCase("y")){
                System.out.println("Select Constraint(Stall speed(ST), Rate of Climb(ROC), Take Off Distance(TO), Max Speed(VM))");
                s = input.nextLine();
                if(s.equalsIgnoreCase("ST")){
                    System.out.println("Enter stall altitude (-1000m-80000m)");
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
                    System.out.println("Enter climbing altitude (-1000m-80000m)");
                    double altitude = input.nextDouble();
                    System.out.println("Enter desired rate of climb m/s");
                    double velocity = input.nextDouble();
                    String title = "Rate of Climb = " + velocity + " at " + altitude + "m";
                    RateOfClimb climb = new RateOfClimb(nuProp,CD_0,LDmax,K,altitude,velocity,title);
                    constraints.add(climb);
                    input.nextLine();
                }else if(s.equalsIgnoreCase("TO")){
                    System.out.println("Enter takeoff altitude (-1000m-80000m)");
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
                    System.out.println("Enter altitude (-1000m-80000m)");
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
    
    public void tester(double[] WS){
        constraints = new ArrayList<>();
        this.WS = WS;
        boolean add = true;
        CD_0 = 0.05;
        LDmax = 11;
        nuProp = 0.6;;
        e = 0.8;
        AR = 12;
        K = 1/(Math.PI*AR*e);
        
        double altitude = 0;
        double velocity = 5;
        CD_TO = 0.08;
        CL_TO = 1.4;
        mu = 0.1;
        sTO = 20;
        CL_R = 2*0.4*Atmosphere.g/(Atmosphere.getDensity(altitude)*1*Math.pow(velocity*1.2, 2));
        CD_G = CD_TO-mu*CL_TO;
        String title = "Take off distance = " + sTO + " at " + altitude + "m";
        TakeOff to = new TakeOff(mu,nuProp,CD_G,sTO,CL_R,altitude,velocity,title);
        constraints.add(to);
        
        altitude = 1000;
        velocity = 5;
        mu = 0.05;
        sTO = 20;
        CL_R = 2*0.4*Atmosphere.g/(Atmosphere.getDensity(altitude)*1*Math.pow(velocity*1.2, 2));
        CD_G = CD_TO-mu*CL_TO;
        title = "Take off distance = " + sTO + " at " + altitude + "m";
        to = new TakeOff(mu,nuProp,CD_G,sTO,CL_R,altitude,velocity,title);
        constraints.add(to);
        
        altitude = 0;
        velocity = 3;
        title = "Rate of Climb = " + velocity + " at " + altitude + "m";
        RateOfClimb climb = new RateOfClimb(nuProp,CD_0,LDmax,K,altitude,velocity,title);
        constraints.add(climb);
        
        altitude = 1000;
        velocity = 1;
        title = "Rate of Climb = " + velocity + " at " + altitude + "m";
        climb = new RateOfClimb(nuProp,CD_0,LDmax,K,altitude,velocity,title);
        constraints.add(climb);
        
        altitude = 0;
        velocity = 30;
        title = "Max speed = " + velocity + " at " + altitude + "m";
        MaxSpeed vm = new MaxSpeed(altitude,velocity,CD_0,nuProp,K,title);
        constraints.add(vm);
                    
        altitude = 1000;
        velocity = 15;
        title = "Max speed = " + velocity + " at " + altitude + "m";
        vm = new MaxSpeed(altitude,velocity,CD_0,nuProp,K,title);
        constraints.add(vm);
        
        altitude = 100;
        velocity = 10;
        title = "Stall Speed = " + velocity + " at " + altitude + "m";
        double CLmax = 1.6;
        StallSpeed st = new StallSpeed(altitude,velocity,CLmax,title);
        constraints.add(st);
        
        maxVel = 30;
    }
    
    /**
     * <h2>Finds the design Wing loading and Power loading for a propeller aircraft with the constraints created from initialization, and creates an aircraft</h2>
     * @return propeller aircraft with only design wing loading and power loading
     */
    public Aircraft createPropAircraft(){
        Aircraft aircraft = new Aircraft();
        
        aircraft.type = "prop";
        double WSLanding = 0;
        double WPDesign = Integer.MAX_VALUE;
        double WSDesign = Integer.MIN_VALUE;
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
        }else{
            for(int i = 0; i < constraints.size()-1;i++){
                double[] a = constraints.get(i).weightToPowerProp(WS);
                for(int j = i+1; j < constraints.size();j++){
                    double[] b = constraints.get(j).weightToPowerProp(WS);
                    int intercept = intercept(a,b);
                    if(intercept == -1){
                        break;
                    }
                    
                    if(WS[intercept] > WSDesign && !lineUnderPoint(intercept,a[intercept])){
                        WPDesign = a[intercept];
                        WSDesign = WS[intercept];
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
        
        aircraft.CLCD = LDmax;
        
        return aircraft;
    }
    
    public boolean lineUnderPoint(int index, double WP){
        boolean lineUnderPoint = false;
            for(int i = 0; i < constraints.size()-1;i++){
                double[] a = constraints.get(i).weightToPowerProp(WS);
                if(a[index] < (WP-0.01)){
                    lineUnderPoint = true;
                }
            }
        return lineUnderPoint;
    }
    
    /**
     * <h2> Returns the closest index position of the intersection of a and b</h2>
     * @param a
     * @param b
     * @return closest index position of intersection of lines a and b
     */
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
        if( dist > 0.1){
            index = -1;
        }
        return index;
    }
    
    /**
     * <h2> Returns the closest index positions of b in array a</h2>
     * @param a
     * @param b
     * @return closest index position of b in a
     */
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
    
    /**
     * <h2> Graphs the constraint lines from the constraint analysis</h2>
     */
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
