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
public class Constraint {
    public double altitude;
    public double massFraction;
    public double velocity;
    
    public Constraint(double altitude,double massFraction, double velocity){
        this.altitude = altitude;
        this.massFraction = massFraction;
        this.velocity = velocity;
    }
}
