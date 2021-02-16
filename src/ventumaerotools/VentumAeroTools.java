/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools;

import ventumaerotools.Aerodynamics.Airfoil;
import ventumaerotools.Aircraft.Aircraft;
import ventumaerotools.Design.ConstraintAnalysis;
import ventumaerotools.Design.PreliminaryDesign;
import ventumaerotools.Design.WeightEstimation;
import ventumaerotools.Design.WeightMatricies;
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
        range = 40000;
        payload = 0.35;
        Airfoil.constructAirfoilMapFromFile();
        
        //Create wing loading design space
        double[] WS = new double[1000];
        for(int i = 0; i <WS.length;i++){
            WS[i] = (i+1)*0.5;
        }
        
        //create Constraint Analysis
        ConstraintAnalysis cs = new ConstraintAnalysis();
        //Initialize Constraint Analysis
        cs.initialize(WS);
        //Create Aircraft from Constraint Analysis Design space
        Aircraft a = cs.createPropAircraft();
        //Graph the Constraint Design Space
        cs.graphConstraints();
        a.constraint = cs;
        a.designPayload = payload;
        a.designRange = range;
        PreliminaryDesign pd = new PreliminaryDesign();
        a.addTopologyTester();
        WeightEstimation.preliminaryWeightEstiamte(a);
        System.out.println("Preliminary Mass Estimate = " + a.mass + "kg");
        pd.preliminaryDesignLoop(a);
        System.out.println(a.topologyToString());
        System.out.println("motor Mass: " + a.propulsion.get(0).mMotor + a.propulsion.get(1).mMotor);
        /*
        int seg = 5;
        double[] chord = new double[]{.2,.19,.17,.12,0.06};
        double[] twist = new double[]{0,0,0,-0.5,-1};
        double[] sweep = new double[]{0,0,0,0,0};
        double span = 3;
        double[] pos = new double[]{0,0,0};
        double[] di = new double[]{0,0,0,0,0};
        double e = 0.8;
        Airfoil[] airfoils = new Airfoil[]{new Airfoil(6.5,-1.1), new Airfoil(6.5,-1.1), new Airfoil(6.5,-1.1), new Airfoil(6.5,-1.1), new Airfoil(6.5,-1.1)};
        
        Wing w  = new Wing(seg,span,chord,twist,pos,sweep,di,airfoils,e);
        Performance.plotLiftDist(w,2,seg-1,16);
        */
        
        
        // clCruise clStall RE liftWeight dragWeight stallLiftWeight effWeight stallQualityWeight structuralWeight
        //WeightMatricies.airfoilSelectionMatrix(.53, 1.3, 100000, 1,4,5,2,2,4,1);
        //double stabilityWeight, double controlWeight, double complexityWeight, double takeoffWeight, double groundClearanceWeight
        //WeightMatricies.wingPlacement(4,2,5,2,2);
        
    }
    
    /**
     * <h2> Finds the maximum value from the given array and returns it</h2>
     * 
     * @Author FurEter
     * @param a double[] 
     * @return double max value
    **/
    
    
}
