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
    
    public static int nearestInArray(double[] array, double value){
        double dist = Math.abs(value - array[0]);
        int index = 0;
        
        for(int i = 1; i < array.length; i++){
            if( Math.abs(value - array[i]) <= dist){
                dist = Math.abs(value - array[i]);
                index = i;
            }
        }
        
        return index;
        
    }
    public static double max(double[] a){
        double max = 0;
        for(int i = 0; i < a.length;i++){
            if(a[i] > max){
                max = a[i];
            }
        }
        return max;
    }
    public static double min(double[] a){
        double min = Double.MAX_VALUE;
        for(int i = 0; i < a.length;i++){
            if(a[i] < min){
                min = a[i];
            }
        }
        return min;
    }
}
