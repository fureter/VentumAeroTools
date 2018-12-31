/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Design;

import java.util.ArrayList;
import java.util.Scanner;
import ventumaerotools.Design.Constraints.RateOfClimb;
import ventumaerotools.Design.Constraints.StallSpeed;

/**
 *
 * @author FurEt
 */
public class ConstraintAnalysis {
    
    public ArrayList<Constraint> constraints;
    
    public void initilize(){
        constraints = new ArrayList<>();
        boolean add = true;
        Scanner input = new Scanner(System.in);
        while(add){
            System.out.println("Add Constraint(Y/N)");
            String s = input.nextLine();
            if(s.equalsIgnoreCase("y")){
                System.out.println("Select Constraint(Stall speed(ST), Rate of Climb(ROC), Take Off Distance(TO), Max Speed(VM))");
                s = input.nextLine();
                if(s.equalsIgnoreCase("ST")){
                    System.out.println("Enter stall altitude (0m-3000m)");
                    double altitude = input.nextDouble();
                    System.out.println("Enter stall Velocity m/s");
                    double velocity = input.nextDouble();
                    System.out.println("Enter Aircraft CLmax estimate");
                    double CLmax = input.nextDouble();
                    StallSpeed stall = new StallSpeed(altitude,velocity,CLmax);
                    constraints.add(stall);
                }else if(s.equalsIgnoreCase("ROC")){
                    System.out.println("Enter stall altitude (0m-3000m)");
                    double altitude = input.nextDouble();
                    System.out.println("Enter stall Velocity m/s");
                    double velocity = input.nextDouble();
                    System.out.println("Enter Aircraft CLmax estimate");
                    double CD_0 = input.nextDouble();
                    System.out.println("Enter Aircraft CLmax estimate");
                    double LDmax = input.nextDouble();
                    System.out.println("Enter Aircraft CLmax estimate");
                    double CD_0 = input.nextDouble();
                    RateOfClimb climb = new RateOfClimb(altitude,velocity,CLmax);
                    constraints.add(climb);
                }else if(s.equalsIgnoreCase("TO")){
                    
                }else if(s.equalsIgnoreCase("VM")){
                    
                }else{
                    System.out.println("Please enter valid constraint type");
                }
            }else{
                add = false;    
            }
        }
    }
}
