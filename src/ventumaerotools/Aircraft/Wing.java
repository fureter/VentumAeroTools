/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Aircraft;

import ventumaerotools.Aerodynamics.Airfoil;

/**
 * <h1>Wing data and construction, includes geometric features of wings and control surface/flap</h1>
 * @author FurEter
 */
public class Wing {
    public String type;
    
    //Number of cross sections wing is broken into, minimum is 2 (root and tip)
    public int segments;
    
    //Total span of the wing (m)
    public double span;
    //Array giving chord at segment cross sections (m)
    public double[] chord;
    //Mean Aerodynamic Chord for the wing (m) 
    public double MAC;
    //Area of the wing (m^2)
    public double area;
    //Aspect ratio of the wing
    public double aspectRatio;
    
    //Taper ratio of the wing
    public double taperRatio;
    
    //Array giving wing twist relative to the fuselage x-axis at each segment cross section (deg°)
    public double[] twist;
    //Relative position of the front of the wing root from the nose of the aircraft
    public double[] position;
    //Sweep of the wing section for each segment, relative to c/4 (deg°)
    public double[] sweep;
    //Dihedral of each wing segment  for each segment (deg°)
    public double[] dihedral;
    
    //Oswald efficiency Factor
    public double e;
    // wing planforam efficiency coefficent
    public double K;
    
    //Airfoil at each wing cross section
    public Airfoil[] airfoils;
    //Control surfaces attached to the wing
    public ControlSurface controlSurface;
    //Flaps attached to the wing
    public Flap flap;
    //average t/c for wing
    public double tc;
    
    public double mass;
    
    
    /**
     *  <h2>Constructor for wing class, calculates wing area and MAC from input segments</h2>
     * @param seg
     * @param span
     * @param chord
     * @param twist
     * @param position
     * @param sweep
     * @param dihedral
     * @param airfoils
     * @param e 
     */
    public Wing(int seg, double span, double[] chord, double[] twist, double[] position, double[] sweep, double[] dihedral, Airfoil[] airfoils, double e){
        this.segments = seg;
        this.span = span;
        this.chord = chord;
        this.twist = twist;
        this.position = position;
        this.sweep = sweep;
        this.dihedral = dihedral;
        this.e = e;
        
        this.area = 0;
        this.MAC = 0;
        
        for(int i = 0; i < this.segments-1;i++){
            this.area += ((this.span/(this.segments-1))*(chord[i]+chord[i+1])/2);
            this.MAC +=  (((this.span/2)/(this.segments-1))*(Math.pow(chord[i],2)+Math.pow(chord[i+1],2))/2);
        }
        this.MAC = 2*this.MAC/this.area;
        this.MAC = this.area/this.span;
        this.aspectRatio = Math.pow(this.span,2)/this.area;
        this.taperRatio = chord[chord.length-1]/chord[0];
        
        this.airfoils = airfoils;
        
        this.K = 1/(Math.PI*this.e*this.aspectRatio);
    }
    
    public Wing(){
        
    }
    
    /**
     * <h2>Returns the wing values as a string for viewing</h2>
     * @return String comprising the main wing geometry values
     */
    public String toString(){
        String str = "";
        str = str + "Span = " + this.span+ "\nMAC = " + this.MAC + "\nArea = " + this.area + "\nAspectRatio = " + this.aspectRatio 
                + "\nTaper Ratio = " + this.taperRatio;
        
        
        return str;
    }
    
}
