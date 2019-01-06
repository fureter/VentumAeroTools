/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Design;

/**
 * <h1>Abstract class defining a basic constraint and its constituent values</h1>
 * @author FurEter
 */
public abstract class Constraint {
    //Altitude at which Constraint is evaluated (m)
    public double altitude;
    //Velocity for the constraint e.g Stall velocity, rate of climb (m/s)
    public double velocity;
    //Title of the constraint, used when graphing the constraints
    public String title;
    
    public Constraint(double altitude, double velocity, String title){
        this.altitude = altitude;
        this.velocity = velocity;
        this.title = title;
    }
    
    /**
     * <h2>Constraint definition is done within this class for the given constraint. Weight to Power is for Propeller aircraft</h2>
     * @param WS array containing Wing Loading design space
     * @return Array containing power loading required to meet constraint at given Wing Loading
     */
    public abstract double[] weightToPowerProp(double[] WS);
}
