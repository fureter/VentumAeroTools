/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventumaerotools.Graph;

/**
 *
 * @author FurEter
 */
import java.awt.Color; 
import java.awt.BasicStroke; 

import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.data.xy.XYDataset; 
import org.jfree.data.xy.XYSeries; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.chart.plot.XYPlot; 
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.plot.PlotOrientation; 
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.data.xy.XYSeriesCollection; 
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class XYLineChart_AWT extends ApplicationFrame {
   private XYSeriesCollection dataset;
   public XYLineChart_AWT( String applicationTitle, String chartTitle,String x, String y ) {
      super(applicationTitle);
      JFreeChart xylineChart = ChartFactory.createXYLineChart(
         chartTitle ,
         x ,
         y ,
         createDataset(),
         PlotOrientation.VERTICAL ,
         true , true , false);
         
      ChartPanel chartPanel = new ChartPanel( xylineChart );
      chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
      final XYPlot plot = xylineChart.getXYPlot( );
      
      plot.setSeriesRenderingOrder(SeriesRenderingOrder.FORWARD);
      
      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
      plot.setRenderer( renderer ); 
      setContentPane( chartPanel ); 
   }
   
   private XYDataset createDataset() {
        this.dataset = new XYSeriesCollection( );                    
        return dataset;
   }
   
   /**
    * 
    * @param WS Wing loading design space
    * @param WP power loading for constraint corresponding to wing loading
    * @param title title of the constraint e.g. stall speed = 5 m/s
    */
   public void addToDataset(double[] WS, double[] WP,String title ) {
      final XYSeries data = new XYSeries( title );          
      for(int i = 0; i < WS.length;i++){
          data.add(WS[i],WP[i]);
      }                
      this.dataset.addSeries( data );          
   }
}
