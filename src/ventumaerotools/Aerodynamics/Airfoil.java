/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Aerodynamics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FurEter
 */
public class Airfoil {
    public double CLalpha;
    public double a0;
    public double stallAOA;
    public double tcRatio;
    public double maxThickLoc;
    public double camber;
    public double camberLoc;
    public int reflex;
    
    public String name;
    

    public double[] RE;
    public ArrayList<double[]> AOA;
    public ArrayList<double[]> cl;
    public ArrayList<double[]> cd;
    public ArrayList<double[]> cm;

    public Airfoil(double CLalpha, double a0, double stallAOA, double tcRatio, double maxThickLoc, double camber, double camberLoc, double[] RE, ArrayList<double[]> AOA, ArrayList<double[]> cl, ArrayList<double[]> cd, ArrayList<double[]> cm,int reflex, String name) {
        this.CLalpha = CLalpha;
        this.a0 = a0;
        this.stallAOA = stallAOA;
        this.tcRatio = tcRatio;
        this.maxThickLoc = maxThickLoc;
        this.camber = camber;
        this.camberLoc = camberLoc;
        this.RE = RE;
        this.AOA = AOA;
        this.cl = cl;
        this.cd = cd;
        this.cm = cm;
        this.reflex = reflex;
        this.name = name;
    }
    
    

    public Airfoil(double CLalpha, double a0) {
        this.CLalpha = CLalpha;
        this.a0 = a0;
    }
    
    public static HashMap airfoils;
    
    public static void constructAirfoilMapFromFile(){
        airfoils = new HashMap<>();
        
        File[] dirs = new File("..//VentumAeroTools//Airfoils").listFiles(File::isDirectory);
        for(int i = 0; i < dirs.length;i++){
            //System.out.println(dirs[i].getName());
            File[] csvs = new File(dirs[i].getPath()).listFiles();
            ArrayList RE = new ArrayList<>();
            ArrayList allValues = new ArrayList<>();
            ArrayList data = new ArrayList<>();
            int dataIndex = 0;
            for(int j = 0; j < csvs.length;j++){
                //System.out.println(csvs[j].getName());
                BufferedReader br;
                ArrayList values = new ArrayList<>();
                    String line = "";
                    String split = ",";
                try {
                    br = new BufferedReader(new FileReader(csvs[j].getAbsolutePath()));
                    while ((line = br.readLine()) != null) {
                        String[] items = line.split(split);
                        if(items.length > 0){
                        // use comma as separator
                            if(items.length == 8){
                                data.add(items);
                            }else if(items.length == 10){
                                //for(int h = 0; h < items.length;h++){
                                //    System.out.print(items[h] + " ");
                                //}
                                //System.out.println();
                                values.add(items);
                            }if(items[0].indexOf("Re =") != -1){
                                String str = items[0].substring(items[0].indexOf("Re")+4,items[0].indexOf("Re")+20);
                                str = str.replaceAll("\\s+","");
                                RE.add(Double.parseDouble(str));
                            }
                            //for(int k = 0; k < items.length;k++){
                                //System.out.print(items[k] + " | ");
                            //}
                        }
                        //System.out.println();
                    }
                    br.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Airfoil.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Airfoil.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(!csvs[j].getName().equals("data.csvs")){
                    allValues.add(values);
                }else{
                    dataIndex = j;
                }
            }
            allValues.remove(dataIndex);
            ArrayList<double[]> cl = new ArrayList<>();
            ArrayList<double[]> cd = new ArrayList<>();
            ArrayList<double[]> cm = new ArrayList<>();
            double[] Re = new double[RE.size()];
            ArrayList<double[]> AOA = new ArrayList<>();
            for(int o = 0; o < RE.size();o++){
                Re[o] = (double)RE.get(o);
                ArrayList val = (ArrayList)allValues.get(o);
                if(!val.isEmpty()){
                    val.remove(0);
                    double[] aoaA = new double[val.size()];
                    double[] clA = new double[val.size()];
                    double[] cdA = new double[val.size()];
                    double[] cmA = new double[val.size()];
                    for(int p = 0; p < val.size();p++){
                        String[] items = (String[])val.get(p);
                        aoaA[p] = Double.parseDouble(items[0]);
                        clA[p] = Double.parseDouble(items[1]);
                        cdA[p] = Double.parseDouble(items[2]);
                        cmA[p] = Double.parseDouble(items[4]);
                    }
                    cl.add(clA);
                    cd.add(cdA);
                    cm.add(cmA);
                    AOA.add(aoaA);
                }
            }
            //System.out.println(data.get(0));
            String[] items = (String[])data.get(0);
            double CLalphaP = Double.parseDouble(items[0]);
            double a0P= Double.parseDouble(items[1]);
            double stallAOAP= Double.parseDouble(items[2]);
            double tcRatioP= Double.parseDouble(items[3]);
            double maxThickLocP= Double.parseDouble(items[4]);
            double camberP= Double.parseDouble(items[5]);
            double camberLocP= Double.parseDouble(items[6]);
            int reflex;
            if(Integer.parseInt(items[7]) == 1){
                reflex = 1;
            }else{
                reflex = 0;
            }
            airfoils.put(dirs[i].getName(),new Airfoil(CLalphaP,a0P,stallAOAP,tcRatioP,maxThickLocP,camberP,camberLocP,Re,AOA,cl,cd,cm,reflex,dirs[i].getName()));
        }
    }
    
    public static void constructAirfoilMap(){
        airfoils = new HashMap<>();
        /*
        
        airfoils.put("GOE614", new Airfoil(0.065231746, -6.25, 16, .188,.292,.062,.392,new double[]{50000,100000,200000,500000,1000000},new double[]{-16,-14,-12,-10,-8,-6,-4,-2,0,2,4,6,8,10,12,14,16},
                //cl
                new double[][]{{0,0,0,-0.133,-0.296,-0.1811,-0.0004,0.1784,0.3864,0.5029,0.626,0.68,0.732,0.765,0,0,0},
            {0,0,0,-0.361,-0.213,-0.032,0.1628,0.3622,0.5371,0.7505,0.9688,1.083,1.1803,1.2401,1.252,1.1746,0},
            {0,0,-0.41,-0.315,-0.128,0.033,0.2106,0.3931,0.6023,0.7486,0.9512,1.0956,1.2246,1.3263,1.3917,1.4337,1.4294},
            {0,-0.492,-0.358,-0.272,-0.13,0.0322,0.2082,0.3878,0.5594,0.7307,0.8941,1.1108,1.2396,1.34,1.4116,1.3792,1.3785},
            {-0.549,-0.518,-0.408,-0.31,-0.153,0.0246,0.2113,0.3945,0.5731,0.7486,0.9128,1.0349,1.2541,1.3238,1.4131,1.4614,1.497}},
                //cd
                new double[][]{{0,0,0,0.111,0.0551,0.0403,0.0344,0.0345,0.0345,0.0511,0.0658,0.0843,0.1031,0,0,0},
            {0,0,0,0.0423,0.0302,0.024,0.0204,0.0199,0.0202,0.0209,0.0225,0.0255,0.0302,0.0385,0.0538,0.0851,0},
            {0,0,0.0417,0.0292,0.0218,0.018,0.0159,0.0148,0.0151,0.0156,0.0163,0.0186,0.0221,0.0279,0.0378,0.0521,0.0739},
            {0,0.0498,030263,0.0204,0.0167,0.0141,0.0127,0.012,0.0116,0.0122,0.0131,0.0146,0.0187,0.0257,0.0366,0.06,0.083},
            {0.0681,0.0364,0.0202,0.0164,0.0137,0.0121,0.0108,0.0104,0.0101,0.0106,0.0115,0.0138,0.018,0.0274,0.0375,0.0531,0.0172}},
                //cm update later
                new double[][]{{0,0,0,-0.133,-0.296,-0.1811,-0.0004,0.1784,0.3864,0.5029,0.626,0.68,0.732,0.765,0,0,0},
            {0,0,0,-0.361,-0.213,-0.032,0.1628,0.3622,0.5371,0.7505,0.9688,1.083,1.1803,1.2401,1.252,1.1746,0},
            {0,0,-0.41,-0.315,-0.128,0.033,0.2106,0.3931,0.6023,0.7486,0.9512,1.0956,1.2246,1.3263,1.3917,1.4337,1.4294},
            {0,-0.492,-0.358,-0.272,-0.13,0.0322,0.2082,0.3878,0.5594,0.7307,0.8941,1.1108,1.2396,1.34,1.4116,1.3792,1.3785},
            {-0.549,-0.518,-0.408,-0.31,-0.153,0.0246,0.2113,0.3945,0.5731,0.7486,0.9128,1.0349,1.2541,1.3238,1.4131,1.4614,1.497}}));
        
        airfoils.put("E395", new Airfoil(0.09107, -6.25, 10, .123,.295,.052,.523,new double[]{50000,100000,200000,500000,1000000},new double[]{-16,-14,-12,-10,-8,-6,-4,-2,0,2,4,6,8,10,12,14,16},
                //cl
                new double[][]{{0,0,0,-0.339,-0.341,-0.262,-0.021,0.2114,0.4215,0.6062,0.8179,0.9783,1.162,1.3061,1.3294,1.3002,1.296},
            {0,0,0,0,-0.324,-0.086,0.1693,0.3989,0.6228,0.8213,1.0323,1.2332,1.3766,1.4122,1.4046,1.3871,0},
            {0,0,0,-0.42,-0.232,0.0048,0.2341,0.4612,0.6859,0.8935,1.096,1.2889,1.404,1.4294,1.4584,1.471,1.4504},
            {0,0,-0.462,-0.4,-0.193,0.0266,0.2529,0.4802,0.706,1.1216,1.304,1.4008,1.4703,1.5328,1.5763,1.5865},
            {0,0,-0.5928,-0.3982,-0.1908,0.0321,0.2598,0.4886,0.7156,0.9376,1.1471,1.312,1.4236,1.5131,1.5931,1.6542,1.6856}},
                //cd
                new double[][]{{0,0,0,0.1195,0.0941,0.0413,0.0318,0.0307,0.0315,0.0341,0.038,0.0437,0.0451,0.0467,0.0624,0},
            {0,0,0,0,0.0364,0.0228,0.0187,0.0177,0.0175,0.0175,0.0192,0.0205,0.0229,0.0323,0.0515,0.0787,0},//
            {0,0,0,0.049,0.0203,0.0148,0.0127,0.0119,0.0118,0.0118,0.0125,0.014,0.0181,0.0289,0.0448,0.067,0.0992},
            {0,0,0.0701,0.0198,0.0135,0.0106,0.0092,0.0086,0.0084,0.0085,0.0088,0.011,0.0166,0.0251,0.0371,0.0539,0.0789},
            {0,0,0.0306,0.0153,0.0112,0.009,0.0077,0.0071,0.007,0.0072,0.008,0.0104,0.0153,0.022,0.0315,0.0451,0.0652}},
                //cm update later
                new double[][]{{0,0,0,-0.133,-0.296,-0.1811,-0.0004,0.1784,0.3864,0.5029,0.626,0.68,0.732,0.765,0,0,0},
            {0,0,0,-0.361,-0.213,-0.032,0.1628,0.3622,0.5371,0.7505,0.9688,1.083,1.1803,1.2401,1.252,1.1746,0},
            {0,0,-0.41,-0.315,-0.128,0.033,0.2106,0.3931,0.6023,0.7486,0.9512,1.0956,1.2246,1.3263,1.3917,1.4337,1.4294},
            {0,-0.492,-0.358,-0.272,-0.13,0.0322,0.2082,0.3878,0.5594,0.7307,0.8941,1.1108,1.2396,1.34,1.4116,1.3792,1.3785},
            {-0.549,-0.518,-0.408,-0.31,-0.153,0.0246,0.2113,0.3945,0.5731,0.7486,0.9128,1.0349,1.2541,1.3238,1.4131,1.4614,1.497}}));
*/
    }
    
}
