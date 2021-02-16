/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Design;

import java.util.Scanner;
import ventumaerotools.Aerodynamics.Atmosphere;
import ventumaerotools.Aircraft.Aircraft;
import ventumaerotools.Aircraft.Propulsion;

/**
 *
 * @author FurEter
 */
public class WeightEstimation {
    public static void preliminaryWeightEstiamte(Aircraft a){
        double averageEfficency = 0;
        for(int i = 0; i < a.propulsion.size();i++){
            Propulsion p = a.propulsion.get(i);
            averageEfficency += p.totalEfficency;
        }
        averageEfficency /= a.propulsion.size();
        if(a.fuel.fuelType.equals("battery")){
            double eFrac = 0;
            double bFrac = 0;
            
            do{
                bFrac = ((a.designRange*Atmosphere.g)/((a.fuel.specificEnergy*3600)*averageEfficency))/a.CLCD;
                a.fuel.bFrac = bFrac;

                System.out.println("Battery Mass Fraction = " + bFrac);
                eFrac = 0.65;    //Start with large empty mass estimate
                System.out.println("Empty Mass Fraction = " + eFrac);
                if(bFrac > (1-eFrac)){
                    System.out.println("Range requirements cannot be fullfilled with specific Energy of battery, average Efficiency of powerplant, and Estimated CL/CD of the aircraft");
                    System.out.println("Please Enter Lower Range");
                    Scanner input = new Scanner(System.in);
                    a.designRange = input.nextDouble();
                }
            }while(bFrac > (1-eFrac));
            double mass = a.designPayload/(1-(eFrac+bFrac));
            a.mass = mass;
            
        }else{
                    
        }
    }
    
    public static void raymerWeightEstimation(Aircraft a){
        
    }
    
    public static void gundlachWeightEstimation(Aircraft a){
        double mWing = 0;
        double mFuselage = 0;
        double mHorzStab = 0;
        double mVertStab = 0;
        double mProp = 0;
        double mControl = 0;
        double mPayload = a.designPayload;
        double mBattery = a.fuel.mBattery;
        double fudge = 0.4;
        
        for(int i = 0; i < a.mainWing.size();i++){
            // metric to imperial back to metric
            double mW = (0.0038*Math.pow((a.Nz*a.mass*2.20462),1.06)*Math.pow(a.mainWing.get(i).aspectRatio,0.38)*Math.pow(a.mainWing.get(i).area*10.7639,0.25)
                    *Math.pow((1+a.mainWing.get(i).taperRatio),0.21)*Math.pow(a.mainWing.get(i).tc,0.14))*0.453592;
            mWing += fudge*mW;
            a.mainWing.get(i).mass = fudge*mW;
        }
        
        //Howe Method
        if(a.horzStab != null){
            for(int i = 0; i < a.horzStab.size();i++){
                //Metric
                double mH = (0.047*a.constraint.maxVel*Math.pow((a.horzStab.get(i).area),1.24));
                mHorzStab +=  fudge*mH;
                a.horzStab.get(i).mass = fudge*mH;
            }
        }
        
        //Howe method
        if(a.vertStab != null){
            for(int i = 0; i < a.vertStab.size();i++){
                //todo add k for tail types
                //metric
                double mV =(0.065*a.constraint.maxVel*Math.pow(a.vertStab.get(i).area,1.15));
                mVertStab +=  fudge*mV;
                a.vertStab.get(i).mass = fudge*mV;
            }
        }
        
        if(a.fuselage != null){
            //System.out.println("Fuselage Length: " + a.fuselage.length);
            double fm = 1.07;
            double fn = 1.04;
            double fv = 1;
            double fp = 1;
            double ft = 1;
            // metric to imperial back to metric
            mFuselage += fudge*(0.5257*fm*fn*fp*fv*ft*Math.pow(a.fuselage.length*3.28084, 0.3796)*Math.pow((mPayload*2.20462+mControl*2.20462+mBattery*2.20462)*a.Nz,0.4863)*Math.pow(1.2*a.constraint.maxVel*1.94384*1.2/100,2))*0.453592;
            a.fuselage.mass = mFuselage;
        }
        
        
        double power = (a.mass/a.WPDesign);
        
        mProp = fudge*0.0002*power+0.0194;    //From excel sheet
        System.out.println("mass Prop pre fudge: " + 0.0002*power+0.0194 + "kg");
        //System.out.println("\nmFuselage: " + mFuselage);
        //System.out.println("mMainWing: " + mWing);
        //System.out.println("mHorzStab: " + mHorzStab);
        //System.out.println("mVertStab: " + mVertStab);
        //System.out.println("mProp " + mProp+"\n");
        
        
        double mEmpty = mWing + mFuselage + mHorzStab + mVertStab + mProp + mControl;
        //System.out.println("mEmpty: " + mEmpty);
        //System.out.println("mBattery: " + mBattery);
        //System.out.println("mPayload: " + mPayload);
        a.mass = mEmpty + mPayload + mBattery;
    }
}
