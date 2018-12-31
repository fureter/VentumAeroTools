/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools;

import org.jfree.ui.RefineryUtilities;
import ventumaerotools.Aerodynamics.Airfoil;
import ventumaerotools.Aerodynamics.Atmosphere;
import ventumaerotools.Aircraft.Wing;
import ventumaerotools.Design.Constraints.MaxSpeed;
import ventumaerotools.Design.Constraints.RateOfClimb;
import ventumaerotools.Design.Constraints.StallSpeed;
import ventumaerotools.Design.Constraints.TakeOff;
import ventumaerotools.Graph.XYLineChart_AWT;
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
        range = 20;
        payload = 1;
        double span = 1.35;
        double[] chord = new double[]{0.11,0.11};
        double[] position = new double[]{0.18,0.0,0.05};
        double[] twist = new double[]{0.0,0.0};
        double[] sweep = new double[]{0.0,0.0};
        double[] dihedral = new double[]{0.0,0.0};
        Airfoil[] airfoils = new Airfoil[2];
        Wing w = new Wing(2,span,chord,twist,position,sweep,dihedral,airfoils,0.9);
        
        System.out.println(w.toString());
        double[] WS = new double[200];
        for(int i = 0; i <WS.length;i++){
            WS[i] = (i+1)*0.25;
        }
        StallSpeed vS = new StallSpeed(0,1,8,.6);
        MaxSpeed vM = new MaxSpeed(0,1,20,.04,0.6,w.K);
        double WS_stall = vS.wingLoading();
        double[] WPmaxSpeed = vM.weightToPowerProp(WS);
        double mu = 0.1;
        double CD_TO = 0.041;
        double CL_TO = 0.8;
        double CL_R = 2*0.4*Atmosphere.g/(Atmosphere.getDensity(0)*w.area*Math.pow(vS.velocity*1.2,2));
        double CD_G = (CD_TO-mu*CL_TO);
        double sTO = 15;
        TakeOff to = new TakeOff(mu,0.5,CD_G,sTO,CL_R,0,1,vS.velocity);
        double[] WPTakeOff = to.weightToPowerProp(WS);
        double ROCv = 1.5;
        RateOfClimb ROC = new RateOfClimb(0.65,0.045,11,w.K,50,1,ROCv);
        double[] WPROC = ROC.weightToPowerProp(WS);
        double mass = WS_stall*w.area/9.81;
        
        System.out.println("\nStall Speed Wing loading for Vs = " + vS.velocity + " is " + WS_stall + "N/m^2" + "\nNeeded mass = " + mass + "kg");
        
        XYLineChart_AWT a = new XYLineChart_AWT("Graph","graph","W/S (N/m^2)","W/P (N/w)");
        a.addToDataset(WS,WPmaxSpeed,"Max Speed V = " + 5);
        double[] c;
        double[] b = {WS_stall,WS_stall};
        if(max(WPmaxSpeed) > max(WPTakeOff)){
            c = new double[]{0,max(WPmaxSpeed)};
        }else{
            c = new double[]{0,max(WPTakeOff)};
        }
        
        a.addToDataset(b, c, "Stall V = " + 5);
        a.addToDataset(WS,WPTakeOff,"TakeOff sTO = " + sTO);
        a.addToDataset(WS,WPROC,"Rate Of Climb = " + ROCv);
        a.pack( );          
        RefineryUtilities.centerFrameOnScreen( a );          
        a.setVisible( true ); 
        
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
    
}
