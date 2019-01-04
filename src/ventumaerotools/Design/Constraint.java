/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Design;

/**
 *
 * @author FurEt
 */
public abstract class Constraint {
    public double altitude;
    public double velocity;
    public String title;
    
    public Constraint(double altitude, double velocity, String title){
        this.altitude = altitude;
        this.velocity = velocity;
        this.title = title;
    }
    
    public abstract double[] weightToPowerProp(double[] WS);
}
