/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Aircraft;

import java.util.ArrayList;
import java.util.Scanner;
import ventumaerotools.Design.ConstraintAnalysis;

/**
 * <h1>Aircraft topology and construction is stored here along with design specifications gathered from constraint analysis</h1>
 * @author FurEter
 */
public class Aircraft {
    
    //Aircraft propulsion type (Propeller, jet) Only propeller implemented currently
    public String type;
    //Wing loading design point
    public double WSDesign;
    //Power loading design point (Propeller aircraft)
    public double WPDesign;
    //Thrust to Weight design point (Jet Aircraft)
    public double TWDesign;
    
    public double designRange;
    
    public double designPayload;

    //Aircraft Estimated Mass
    public double mass;
    //Aircraft Estimated Inertia
    public double[][] inertia;
    //Arraylist of Aircraft Main Wings
    public ArrayList<Wing> mainWing;
    //Arraylist ofAircraft horizontal stabilizers (not necessary for some topologies e.g. flying Wing) 
    public ArrayList<Wing> horzStab;
    //Arraylist of Aircraft vertical stabilizers 
    public ArrayList<Wing> vertStab;
    //Aircraft fuselage (not necessary for some topologies e.g. flying Wing) 
    public Fuselage fuselage;
    //Aircraft Landing Gear
    public LandingGear landingGear;
    //Arraylist of Aircraft propulsion powerplants
    public ArrayList<Propulsion> propulsion;
    // Power source for the aircraft, battery, gasoline, et cetera
    public Fuel fuel;

    //Max CL/CD for aircraft
    public double CLCD;
    
    //Ultimate load Factor
    public double Nz;
    
    public ConstraintAnalysis constraint;
    
    public void addTopologyWeightMatrix(){
        //TO-DO Add weightchart 
    }
    
    public void addTopologyUserInput(){
        Nz = 5;
        
        
        Scanner input = new Scanner(System.in);
        String s = "";
        //fuel
        System.out.println("Select fuel Type (Only Battery currently)");
        s = input.nextLine();
        if(s.equalsIgnoreCase("battery")){
            fuel = new Fuel();
            fuel.fuelType = "battery";
            System.out.println("Enter Specific Energy for Battery (W*h/kg");
            double E = input.nextDouble();
            fuel.specificEnergy = E;
        }
        input.nextLine();
        
        //propulsion
        System.out.println("Enter number of propulsion Units");
        int num = input.nextInt();
        input.nextLine();
        propulsion = new ArrayList<>();
        for(int i = 0; i < num;i++){
            propulsion.add(new Propulsion());
            System.out.println("Enter propulsion Type (eg propeller, jet)");
            s = input.nextLine();
            propulsion.get(i).type = s;
            System.out.println("Enter estimated total propulsion efficency (from fuel to output) [0-1]");
            propulsion.get(i).totalEfficency = input.nextDouble();
        }
        input.nextLine();
        
        //fuselage
        System.out.println("Enter fuselage Type (Twin Boom, none, Lifting, conventional");
        s = input.nextLine();
        if(s.equalsIgnoreCase("none")){
            
        }else{  
            fuselage = new Fuselage();
            fuselage.type = s;
            System.out.println("Enter fuselage Cross Section Approximation (circle, ellipse, rectangular");
            s = input.nextLine();
            fuselage.crossSectionApproximation = s;
        }
        
        //flap
        System.out.println("Enter flap type (none, plain, slotted, split, fowler");
        s = input.nextLine();
        mainWing = new ArrayList<>();
        mainWing.add(new Wing());
        mainWing.get(0).flap = new Flap();
        mainWing.get(0).flap.type = s;
        mainWing.get(0).aspectRatio = constraint.AR;
        
        
        //tails
        System.out.println("Enter horizontal Stabilizer setup (canard, aft, both, none");
        s = input.nextLine();
        if(s.equalsIgnoreCase("both")){
            horzStab =  new ArrayList<>();
            horzStab.add(new Wing());
            horzStab.get(0).type = "canard";
            horzStab.add(new Wing());
            horzStab.get(1).type = "aft";
        }else if(s.equalsIgnoreCase("aft") || s.equalsIgnoreCase("canard")){
            horzStab =  new ArrayList<>();
            horzStab.add(new Wing());
            horzStab.get(0).type = s;
        }else{
            
        }
        
        System.out.println("Enter Vertical Stabilizer setup (single, double, wingMounted, wingTip");
        s = input.nextLine();
        if(s.equalsIgnoreCase("single")){
            vertStab =  new ArrayList<>();
            vertStab.add(new Wing());
            vertStab.get(0).type = "single";
        }else{
            vertStab = new ArrayList<>();
            vertStab.add(new Wing());
            vertStab.get(0).type = s;
            vertStab.add(new Wing());
            vertStab.get(1).type = s;
        }
        
        //Landing Gear
        System.out.println("Enter Landing Gear Setup (none,tricycle, tailsitter, bicycle, ski, monowheel)");
        s = input.nextLine();
        landingGear = new LandingGear();
        landingGear.type = s;
        
        
    }
    
    public void addTopologyTester(){
        Nz = 5;


            fuel = new Fuel();
            fuel.fuelType = "battery";
            double E = 120;
            fuel.specificEnergy = E;
        //propulsion

        propulsion = new ArrayList<>();

            propulsion.add(new Propulsion());

            propulsion.get(0).type = "propeller";
            propulsion.get(0).totalEfficency = 0.65;

        
        //fuselage

            fuselage = new Fuselage();
            fuselage.type = "conventional";
            fuselage.crossSectionApproximation = "circle";
        
        //flap
        mainWing = new ArrayList<>();
        mainWing.add(new Wing());
        mainWing.get(0).flap = new Flap();
        mainWing.get(0).flap.type = "plain";
        mainWing.get(0).aspectRatio = constraint.AR;
        
        
        //tails

            horzStab =  new ArrayList<>();
            horzStab.add(new Wing());
            horzStab.get(0).type = "aft";

        

            vertStab =  new ArrayList<>();
            vertStab.add(new Wing());
            vertStab.get(0).type = "single";
        
        //Landing Gear
        landingGear = new LandingGear();
        landingGear.type = "none";
        
        
    }
    
    public String topologyToString(){
        String s = "";
        s += "Number of Main Wings = " + mainWing.size() + "\n";
        if(horzStab != null){
            s += "Number of Horizontal Stabilizers = " + horzStab.size() + "\n";
            for(int i = 0; i < horzStab.size();i++){
                s += "Horiztal Stabilizer number " + i+1 + " is of " + horzStab.get(i).type + " Configuration\n";
            }
        }
        if(vertStab != null){
            s += "Number of Vertical Stabilizers = " + vertStab.size() + "\n";
            for(int i = 0; i < vertStab.size();i++){
                s += "Vertical Stabilizer number " + i+1 + " is of " + vertStab.get(i).type + " Configuration\n";
            }
        }
        
        
        if(fuselage == null){
            s += "No Fuselage\n";
        }else{
            s += "Fuselage configuration = " + fuselage.type + " with " + fuselage.crossSectionApproximation + " cross section approximation\n";
        }
        
        if(landingGear.type.equalsIgnoreCase("none")){
            s += "No Landing Gear\n";
        }else{
            s += "Landing Gear Configuration = " + landingGear.type;
        }
        return s;
    }
        
}
