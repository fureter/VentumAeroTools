/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Aircraft;

import java.util.ArrayList;

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
    //Arraylist of Aircraft fuselage (not necessary for some topologies e.g. flying Wing) 
    public ArrayList<Fuselage> fuselage;
    //Arraylist of Aircraft propulsion powerplants
    public ArrayList<Propulsion> propulsion;
    // Power source for the aircraft, battery, gasoline, et cetera
    public Fuel fuel;

    public double CLCD;
    
    public void addTopology(){
        //TO-DO Add weightchart 
    }
        
}
