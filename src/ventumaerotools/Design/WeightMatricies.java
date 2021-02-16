/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Design;

import ventumaerotools.Aerodynamics.Airfoil;
import ventumaerotools.Numerical.Numerical;

/**
 *
 * @author FurEter
 */
public class WeightMatricies {
    
    public double[] weights;
    public double[][] inputs;
    public String[] inputNames;
    public String[] category;
    public double[] results;
    public int bestResult;

    public WeightMatricies(double[] weights, double[][] inputs, String[] inputNames, String[] cat) {
        if(weights.length != inputs[0].length){
            System.out.println("Invalid weight/input length: Must have same number of weights as input characterisitics");
        }else{
            this.weights = weights;
            this.inputs = inputs;
            this.inputNames = inputNames;
            results = new double[inputs.length];
            int max = 0;
            for(int i = 0; i < inputs.length;i++){
                results[i] = 0;
                for(int j = 0; j < inputs[i].length;j++){
                    results[i] += inputs[i][j];
                }
                if(results[max] <= results[i]){
                    max = i;
                }
            }
            bestResult = max;
        }
        this.category = cat;
        
    }
    
    public static Airfoil airfoilSelectionMatrix(double clCruise, double clStall, double RE, double liftWeight,double dragWeight, double stallLiftWeight,double effWeight, double stallQualityWeight,double structuralWeight, int reflex){
        double[] weights = new double[]{liftWeight,dragWeight,stallLiftWeight,effWeight,stallQualityWeight,structuralWeight,reflex};
        String[] inputNames = new String[Airfoil.airfoils.size()];
        String[] cat = new String[]{"Lift score","Drag Score","Stall Lift Score","Efficency Score","Stall Qual Score","Structural Score","Reflex Score"};
        double[][] inputs = new double[Airfoil.airfoils.size()][weights.length];
        int reIndex = Numerical.nearestInArray(((Airfoil)Airfoil.airfoils.get((String)Airfoil.airfoils.keySet().toArray()[0])).RE, RE);
        double[] min = new double[]{Double.MAX_VALUE,Double.MAX_VALUE,Double.MAX_VALUE,Double.MAX_VALUE,Double.MAX_VALUE,Double.MAX_VALUE};
        double[] max = new double[]{0,0,0,0,0,0};
        
        for(int i = 0; i < inputs.length;i++){
            
            inputNames[i] = (String)Airfoil.airfoils.keySet().toArray()[i];
            Airfoil a = (Airfoil)Airfoil.airfoils.get((String)Airfoil.airfoils.keySet().toArray()[i]);
            //System.out.println(inputNames[i]);
            int clCruiseAOAIndex = (int)Numerical.nearestInArray(a.cl.get(reIndex),clCruise);
            int clStallAOAIndex = (int)Numerical.nearestInArray(a.cl.get(reIndex),clStall);
            if(a.cl.get(reIndex)[clCruiseAOAIndex-1] < clCruise){
                clCruiseAOAIndex++;
            }
            if(a.cl.get(reIndex)[clStallAOAIndex-1] < clStall){
                clStallAOAIndex++;
            }
            System.out.println("ReIndex: " + reIndex + " " + "CL CRuise AOA Index: " + clCruiseAOAIndex + " a AOA size: " + a.AOA.size() + " array len: " + a.AOA.get(reIndex).length);
            if(a.AOA.get(reIndex)[clCruiseAOAIndex-1] == 0){
                inputs[i][0] = (10.0/0.5);
            }else{
                System.out.println("num: " + 10.0/Math.abs(a.AOA.get(reIndex)[clCruiseAOAIndex-1]+5));
                System.out.println("den: " + Math.abs(a.AOA.get(reIndex)[clCruiseAOAIndex-1]+5));
                inputs[i][0] = (10.0/Math.abs(a.AOA.get(reIndex)[clCruiseAOAIndex-1]+5));
            }
            if(inputs[i][0] < min[0]){
                min[0] = inputs[i][0];
            }
            if(inputs[i][0] > max[0]){
                max[0] = inputs[i][0];
            }
            
            inputs[i][1] = (.1/(a.cd.get(reIndex)[clCruiseAOAIndex-1]));
            
            if(inputs[i][1] < min[1]){
                min[1] = inputs[i][1];
            }
            if(inputs[i][1] > max[1]){
                max[1] = inputs[i][1];
            }
            
            if(a.AOA.get(reIndex)[clStallAOAIndex-1] == 0){
                inputs[i][2] = (10.0/0.5);;
            }else{
                if(a.AOA.get(reIndex)[clStallAOAIndex-1] > a.stallAOA){
                    inputs[i][2] = 0;
                }else{
                    inputs[i][2] = (10.0/Math.abs(a.AOA.get(reIndex)[clStallAOAIndex-1]+5));
                }
            }
            
            if(inputs[i][2] < min[2]){
                min[2] = inputs[i][2];
            }
            if(inputs[i][2] > max[2]){
                max[2] = inputs[i][2];
            }
            
            inputs[i][3] = ((a.cl.get(reIndex)[Numerical.nearestInArray(a.AOA.get(reIndex),a.a0+clCruise/a.CLalpha)])/a.cd.get(reIndex)[Numerical.nearestInArray(a.AOA.get(reIndex),a.a0+clCruise/a.CLalpha)]);
            
            if(inputs[i][3] < min[3]){
                min[3] = inputs[i][3];
            }
            if(inputs[i][3] > max[3]){
                max[3] = inputs[i][3];
            }
            
            inputs[i][4] = (a.stallAOA/10.0);
            
            if(inputs[i][4] < min[4]){
                min[4] = inputs[i][4];
            }
            if(inputs[i][4] > max[4]){
                max[4] = inputs[i][4];
            }
            
            inputs[i][5] = a.tcRatio*10.0;
            
            if(inputs[i][5] < min[5]){
                min[5] = inputs[i][5];
            }
            if(inputs[i][5] > max[5]){
                max[5] = inputs[i][5];
            }
            inputs[i][6] = 10*a.reflex;
            //System.out.println("cruise AOA EST: " + (a.a0+clCruise/a.CLalpha) +" cruise AOA: " + a.AOA.get(reIndex)[clCruiseAOAIndex] + " closest cruise cl: " + a.cl.get(reIndex)[Numerical.nearestInArray(a.cl.get(reIndex),clCruise)] +" stall AOA: " + a.AOA.get(reIndex)[clStallAOAIndex] + " closest stall cl: " + a.cl.get(reIndex)[Numerical.nearestInArray(a.cl.get(reIndex),clStall)] + " .1/cd: " + .1/a.cd.get(reIndex)[Numerical.nearestInArray(a.AOA.get(reIndex),a.a0+clCruise/a.CLalpha)] + " Stall speed AOA: " + (a.a0+clStall/a.CLalpha) + " cl/cd: " + (a.cl.get(reIndex)[Numerical.nearestInArray(a.AOA.get(reIndex),a.a0+clCruise/a.CLalpha)])/a.cd.get(reIndex)[Numerical.nearestInArray(a.AOA.get(reIndex),a.a0+clCruise/a.CLalpha)] + " stall AOA: " + a.stallAOA + " tc ratio*10: " + a.tcRatio);
        }
        for(int i = 0; i < inputs.length;i++){
            System.out.println("lift input: " + inputs[i][0]);
            if((max[0]-min[0]) != 0){
                inputs[i][0] = liftWeight*(inputs[i][0]-min[0])/(max[0]-min[0]);
            }else{
                inputs[i][0] = liftWeight*(inputs[i][0]-min[0]);
            }
            if((max[1]-min[1]) != 0){
                inputs[i][1] = dragWeight*(inputs[i][1]-min[1])/(max[1]-min[1]); 
            }else{
                inputs[i][1] = dragWeight*(inputs[i][1]-min[1]); 
            }
            if((max[2]-min[2]) != 0){
                inputs[i][2] = stallLiftWeight*(inputs[i][2]-min[2])/(max[2]-min[2]); 
            }else{
                inputs[i][2] = stallLiftWeight*(inputs[i][2]-min[2]); 
            }
            inputs[i][3] = effWeight*(inputs[i][3]-min[3])/(max[3]-min[3]); 
            inputs[i][4] = stallQualityWeight*(inputs[i][4]-min[4])/(max[4]-min[4]); 
            inputs[i][5] = structuralWeight*(inputs[i][5]-min[5])/(max[5]-min[5]); 
            inputs[i][6] = reflex*inputs[i][6]; 
        }
        
        
        System.out.println();
        
        WeightMatricies airfoilSelec = new WeightMatricies(weights,inputs,inputNames,cat);
        airfoilSelec.print();
        System.out.println("Airfoil Selected is: " + airfoilSelec.inputNames[airfoilSelec.bestResult]);
        return (Airfoil)Airfoil.airfoils.get(airfoilSelec.inputNames[airfoilSelec.bestResult]);
    }
    
    //Reference: Mohammad H. Sadraey: Aircraft Design a Systems Engineering Approach
    public static double[] wingPlacement(double stabilityWeight, double controlWeight, double complexityWeight, double takeoffWeight, double groundClearanceWeight){
        double[] placement = new double[]{0,0,0};
        double[][] inputs = new double[4][5];
        String[] cat = new String[]{"Stability Score","Control Score","Complexity Score","Takeoff Score","Ground Clearance Score"};
        double[] weights = new double[]{stabilityWeight,controlWeight,complexityWeight,takeoffWeight,groundClearanceWeight};
        inputs[0][0] =.1*stabilityWeight; inputs[0][1] = .3*controlWeight;inputs[0][2] =.7*complexityWeight;inputs[0][3] =1*takeoffWeight;inputs[0][4] =.1*groundClearanceWeight;
        inputs[1][0] =.3*stabilityWeight; inputs[1][1] = .7*controlWeight;inputs[1][2] =.1*complexityWeight;inputs[1][3] =.6*takeoffWeight;inputs[1][4] =.3*groundClearanceWeight;
        inputs[2][0] =.7*stabilityWeight; inputs[2][1] = .3*controlWeight;inputs[2][2] =.7*complexityWeight;inputs[2][3] =.3*takeoffWeight;inputs[2][4] =.8*groundClearanceWeight;
        inputs[3][0] =.9*stabilityWeight; inputs[3][1] = .1*controlWeight;inputs[3][2] =.4*complexityWeight;inputs[3][3] =.1*takeoffWeight;inputs[3][4] =1*groundClearanceWeight;
        String[] names = new String[]{"low","mid","high","parasol"};
        WeightMatricies wingPlace = new WeightMatricies(weights,inputs,names,cat);
        
        String n = names[wingPlace.bestResult];
        if(n.equals(names[0])){
            placement = new double[]{0.36,0.0,0.0};
        }else if(n.equals(names[1])){
            placement = new double[]{0.36,0.0,.4};
        }else if(n.equals(names[2])){
            placement = new double[]{0.36,0.0,1};
        }else{
            placement = new double[]{0.36,0.0,1.2};
        }
        wingPlace.print();
        System.out.println("Wing placement choice is: " + n + " " + wingPlace.bestResult);
        return placement;
    }
    
    public void print(){
        System.out.format("%10s","");
        for(int i = 0; i < this.inputs[0].length;i++){
            System.out.format("%20s",this.category[i]);
        }
        System.out.format("%20s","result");
        System.out.println();
        for(int i = 0; i < this.inputs.length;i++){
            System.out.print(this.inputNames[i] + " ");
            for(int j = 0; j < this.inputs[i].length;j++){
            System.out.format("%20f",this.inputs[i][j]);
            }
            System.out.format("%20f\n",results[i]);
        }
        System.out.println();
    }
    
}
