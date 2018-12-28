/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Aircraft;

import ventumaerotools.Aerodynamics.Airfoil;

/**
 *
 * @author FurEt
 */
public class Wing {
    public int segments;
    
    public double span;
    public double[] chord;
    public double MAC;
    public double area;
    public double aspectRatio;
    
    public double taperRatio;
    
    public double[] twist;
    public double[] position;
    public double[] sweep;
    public double[] dihedral;
    
    public Airfoil[] airfoils;
    
    public Wing(int seg, double span, double[] chord, double[] twist, double[] position, double[] sweep, double[] dihedral, Airfoil[] airfoils){
        this.segments = seg;
        this.span = span;
        this.chord = chord;
        this.twist = twist;
        this.position = position;
        this.sweep = sweep;
        this.dihedral = dihedral;
        
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
    }
    
    public String toString(){
        String str = "";
        str = str + "Span = " + this.span+ "\nMAC = " + this.MAC + "\nArea = " + this.area + "\nAspectRatio = " + this.aspectRatio 
                + "\nTaper Ratio = " + this.taperRatio;
        
        
        return str;
    }
    
}
