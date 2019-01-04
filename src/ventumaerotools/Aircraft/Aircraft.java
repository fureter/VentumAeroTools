/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Aircraft;

import java.util.ArrayList;

/**
 *
 * @author FurEt
 */
public class Aircraft {
        public String type;
        public double WSDesign;
        public double WPDesign;
        public double TWDesign;
    
        public double mass;
        public double[][] inertia;
        public Wing mainWing;
        public Wing horzStab;
        public Wing vertStab;
        public Fuselage fuselage;
        public ArrayList<Propulsion> propulsion;
        
        
        //Add weightchart 
        public void addTopology(){
            
        }
        
}
