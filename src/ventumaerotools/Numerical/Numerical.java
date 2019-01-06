/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Numerical;

/**
 *
 * @author FurEter
 */
public class Numerical {
    public static double interpolate(double point, double x1, double x2, double y1, double y2){
        return y1 + (point-x1)*((y2-y1)/(x2-x1));
    }
}
