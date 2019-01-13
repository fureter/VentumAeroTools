/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools;

import ventumaerotools.Aircraft.Aircraft;
import ventumaerotools.Design.ConstraintAnalysis;
import ventumaerotools.Design.PreliminaryDesign;
import ventumaerotools.Design.WeightEstimation;
/**
 *
 * @author FurEt
 */
public class VentumAeroTools {

    /**
     * @param args the command line arguments
     */
    private static double range;                   //max Aircraft range
    private static double payload;                 //max Aircraft Payload
    
    public static void main(String[] args) {
        range = 200000;
        payload = 1;
        
        //Create wing loading design space
        double[] WS = new double[500];
        for(int i = 0; i <WS.length;i++){
            WS[i] = (i+1)*0.1;
        }
        
        //create Constraint Analysis
        ConstraintAnalysis cs = new ConstraintAnalysis();
        //Initialize Constraint Analysis
        cs.tester(WS);
        //Create Aircraft from Constraint Analysis Design space
        Aircraft a = cs.createPropAircraft();
        //Graph the Constraint Design Space
        cs.graphConstraints();
        a.designPayload = payload;
        a.designRange = range;
        PreliminaryDesign pd = new PreliminaryDesign();
        a.addTopologyUserInput();
        WeightEstimation.preliminaryWeightEstiamte(a);
        System.out.println("Preliminary Mass Estimate = " + a.mass + "kg");
        System.out.println(a.topologyToString());
        
        
    }
    
    /**
     * <h2> Finds the maximum value from the given array and returns it</h2>
     * 
     * @Author FurEter
     * @param a double[] 
     * @return double max value
    **/
    public static double max(double[] a){
        double max = 0;
        for(int i = 0; i < a.length;i++){
            if(a[i] > max){
                max = a[i];
            }
        }
        return max;
    }
    
}
