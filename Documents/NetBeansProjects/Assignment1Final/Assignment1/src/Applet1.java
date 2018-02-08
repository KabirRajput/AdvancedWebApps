/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import javax.swing.JApplet;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Kabir
 */
public class Applet1 extends JApplet implements ActionListener {
    
    String AxisTitleH, AxisTitleV, ChartTitle;
    int NumOfDataSeries;
    boolean ShowChartTitle, ShowAxisTitles, ShowDataLabels, ShowLegend; 
    int[][] ChartInputData;
    String[] AxisValueH, AxisValueV;
    JComboBox chartType = new JComboBox();
    JComboBox colorScheme = new JComboBox();
    JLabel chartLabel=new JLabel("Chart Type:");
    JLabel colorLabel=new JLabel("Color Scheme:");
    JLabel dataLabel=new JLabel("Data Labels:");
    JCheckBox labelBox;   
   
    JPanel inputBar;

    Color[][] myColors;
    
    
    public void HTMLData(){
         
        AxisTitleH = getParameter("AxisTitleH");
        AxisTitleV = getParameter("AxisTitleV");
        ChartTitle = getParameter("ChartTitle");
        NumOfDataSeries = Integer.parseInt(getParameter("NumOfDataSeries"));
        ShowChartTitle = (Integer.parseInt(getParameter("ShowChartTitle")) != 0);
        ShowAxisTitles = (Integer.parseInt(getParameter("ShowAxisTitles")) != 0);
        ShowDataLabels = (Integer.parseInt(getParameter("ShowDataLabels")) != 0);
        ShowLegend = (Integer.parseInt(getParameter("ShowLegend")) != 0);
        AxisValueH = getParameter("AxisValueH").split(",");
        AxisValueV = getParameter("AxisValueV").split(",");
        
        
        String dataParameter = "Data1";
        ChartInputData = new int[NumOfDataSeries][AxisValueH.length];
        for(int i=2; i<=NumOfDataSeries+1; i++){
            String readData = getParameter(dataParameter);
            dataParameter = dataParameter.substring(0,4)+i+dataParameter.substring(5); 
            String[] temp = readData.split(",");
            
            for(int j = 0; j < temp.length; j++){
                ChartInputData[i-2][j] = Integer.parseInt(temp[j]);
            }
        }
        
        myColors = new Color[5][AxisValueH.length];  //giving 5 color options 
        for(int i=0; i<myColors[0].length; i++){
            int Red = (int)(Math.random()*256);
            int Green = (int)(Math.random()*256);
            int Blue= (int)(Math.random()*256);
            myColors[0][i] = new Color(Red, Green, Blue); //randomly using RGB
        }
        for(int i=0; i<myColors[0].length; i++){
            myColors[1][i] = new Color(255, 0, 0);    //R
        }
        for(int i=0; i<myColors[0].length; i++){
            myColors[2][i] = new Color(0, 255, 0);   //G
        }
        for(int i=0; i<myColors[0].length; i++){
            myColors[3][i] = new Color(0, 0, 255);   //B
        }
        for(int i=0; i<myColors[0].length; i++){
            myColors[4][i] = new Color(255, 255, 255);   //RGB @255
        }
        
    }

    
    @Override 
    public void init() {
        // TODO start asynchronous download of heavy resources
        HTMLData();
        chartType.addItem("Line-Chart");                 //drop down for type of chart
        chartType.addItem("Bar-Chart(Horizontal)");
        chartType.addItem("Bar-Chart(Vertical)");
        
        colorScheme.addItem("Erratic");        //drop down for colors
        colorScheme.addItem("Red");
        colorScheme.addItem("Green");
        colorScheme.addItem("Blue");
        colorScheme.addItem("RGB");
        
        
        if(ShowDataLabels)
            labelBox= new JCheckBox("Show",true);     //checkbox
        else
            labelBox= new JCheckBox("Show"); 
        
        inputBar = new JPanel();
        inputBar.setLayout(new FlowLayout());  
        inputBar.add(chartLabel);
        inputBar.add(chartType);
        inputBar.add(colorLabel);
        inputBar.add(colorScheme);
        inputBar.add(dataLabel);
        inputBar.add(labelBox);
        setLayout(new BorderLayout());
        add(inputBar,BorderLayout.SOUTH);
        
        labelBox.addActionListener(this);
        chartType.addActionListener(this);
        colorScheme.addActionListener(this);
        
    }

  
    @Override
    public void paint(Graphics g)
   {
      super.paint(g);
      Graphics2D graphics2D = (Graphics2D) g;

      int minValue = 0;
      int maxValue = 0;
     
        for (int[] Data1 : ChartInputData) {            //loop to find the min, max value of 2D array
            for (int j = 0; j < Data1.length; j++) {
                if (minValue > Data1[j]) {
                    minValue = Data1[j];
                }
                if (maxValue < Data1[j]) {
                    maxValue = Data1[j];
                }
            }
        }
                    
      String chartName = String.valueOf(chartType.getSelectedItem());
      String colourScheme = String.valueOf(colorScheme.getSelectedItem());
      ShowDataLabels = labelBox.isSelected();
      int colorNumber = 0;
    
      if("Erratic".equals(colourScheme)){
          colorNumber = 0;
      }
      else if("Red".equals(colourScheme)){
          colorNumber = 1;
      }
      else if("Green".equals(colourScheme)){
          colorNumber = 2;
      }
      else if("Blue".equals(colourScheme)){
          colorNumber = 3;
      }
       else if("White".equals(colourScheme)){
          colorNumber = 4;
      }
      
      
      if("Bar-Chart(Vertical)".equals(chartName)){
          
        int panelWidth = getWidth()-60;
        int panelHeight = getHeight()-85;

        Font titleFont = new Font("Calibri", Font.BOLD, 20);
        Font labelFont = new Font("Calibri", Font.PLAIN, 10);

        FontRenderContext context = graphics2D.getFontRenderContext();
        Rectangle2D titleBounds = titleFont.getStringBounds(ChartTitle, context);  //rectangle big enough to fit in the chart title 
        graphics2D.setFont(titleFont);
        double titleWidth = titleBounds.getWidth();
        double y = -titleBounds.getY();          
        double x = (panelWidth - titleWidth) / 2;
        double top = titleBounds.getHeight()+20;    // leave 20 extra spaces on top

       
        if(ShowChartTitle)
          graphics2D.drawString(ChartTitle, (float) x, (float) y);

        if(ShowAxisTitles){
            graphics2D.drawString("Series", (float) getWidth()/2, (float) getHeight()-50);
            graphics2D.drawString("Data", (float) 0, (float) getHeight()/2);
        }
        
        if(ShowLegend){
            for(int i=0; i<ChartInputData[0].length; i++){
                Rectangle2D legend = new Rectangle2D.Double(getWidth()-70, 100+i*25, 20, 20);
                graphics2D.setPaint(myColors[colorNumber][i]);
                graphics2D.fill(legend);
                graphics2D.draw(legend);
                graphics2D.setFont(labelFont);
                graphics2D.drawString(AxisValueH[i], getWidth()-45, 115+i*25);
            }
        }
        
        // compute the extent of the bar labels
        LineMetrics labelMetrics = labelFont.getLineMetrics("", context);
        double bottom = labelMetrics.getHeight();

        y = panelHeight - labelMetrics.getDescent();
        graphics2D.setFont(labelFont);

        // get the scale factor and width for the bars
        double scale = (panelHeight - top - bottom) / (maxValue - minValue);
        int barWidth = panelWidth / ((ChartInputData.length+1)*ChartInputData[0].length);

        // draw the bars
        int index = 3;                                             //2d array to access all data values
        for (int i = 0; i < ChartInputData.length; i++){

            index++;
            for(int j=0; j<ChartInputData[i].length; j++){
                 // get the coordinates of the bar rectangle
                 double x1 = index * barWidth + 1;      //x1- place where we staart drawing the rectangle
                 index++;
                 double y1 = top;                       //starting point of the rectangle
                 double height = ChartInputData[i][j] * scale;
                 y1 += (maxValue - ChartInputData[i][j]) * scale;
//                 else
//                 {
//                    y1 += maxValue * scale;
//                    height = -height;                   //if data is negative
//                 }

                 // fill the bar and draw the bar outline
                 Rectangle2D dataRect = new Rectangle2D.Double(x1, y1, barWidth - 2, height);
                
                 graphics2D.setPaint(myColors[colorNumber][j]);
                 graphics2D.fill(dataRect);
                 graphics2D.draw(dataRect);

                 if(ShowDataLabels){
                    graphics2D.setPaint(Color.BLACK);
                    graphics2D.drawString(Integer.toString(ChartInputData[i][j]),(float)(x1),(float)(y1-10));
                 }

                 // draw the centered label below the bar
                 if(j == ChartInputData[0].length/2 && ShowAxisTitles){

                    Rectangle2D labelBounds = labelFont.getStringBounds(AxisValueH[i], context);
                    double labelWidth = labelBounds.getWidth();
                    x = x1 + (barWidth - labelWidth) / 2;
                    graphics2D.setPaint(Color.BLACK);
                    graphics2D.drawString(AxisValueV[i], (float) x, (float) y);
                 }
              }
        }
      }
      else if("Bar-Chart(Horizontal)".equals(chartName)){
          int panelWidth = getWidth()-60;
                int panelHeight = getHeight()-85;
                Font titleFont = new Font("Calibri", Font.BOLD, 20);
                Font labelFont = new Font("Calibri", Font.PLAIN, 10);
                FontRenderContext context = graphics2D.getFontRenderContext();
                Rectangle2D titleBounds = titleFont.getStringBounds(ChartTitle, context);
                double titleWidth = titleBounds.getWidth();
                double top = titleBounds.getHeight()+20;
                double y = -titleBounds.getY();
                double x = (panelWidth - titleWidth) / 2;
                graphics2D.setFont(titleFont);
                if(ShowChartTitle)
                    graphics2D.drawString(ChartTitle, (float) x, (float) y);
                if(ShowAxisTitles){
                    graphics2D.drawString("Data", (float) getWidth()/2, (float) getHeight()-50);
                    graphics2D.drawString("Series", (float) 0, (float) getHeight()/2);
                }           if(ShowLegend){
                    for(int i=ChartInputData[0].length-1; i>=0; i--){
                        Rectangle2D legend = new Rectangle2D.Double(getWidth()-70, 100+i*25, 20, 20);
                        graphics2D.setPaint(myColors[colorNumber][i]);
                        graphics2D.fill(legend);
                        graphics2D.draw(legend);
                        graphics2D.setFont(labelFont);
                        graphics2D.drawString(AxisValueH[i], getWidth()-45, 115+i*25);
                    }
                }           
                LineMetrics labelMetrics = labelFont.getLineMetrics("", context);
                double bottom = labelMetrics.getHeight();
                graphics2D.setFont(labelFont);
                
                double scale = (panelWidth - top - bottom -100) / (maxValue - minValue);
                int barHeight = panelHeight / ((ChartInputData.length+1)*ChartInputData[0].length);
                
                int c = 4;  for (int i =ChartInputData.length-1; i>=0; i--){                    
                    c++;
                    for(int j=ChartInputData[i].length-1; j>=0; j--){
                        
                        double x1 = c++ * barHeight + 1;
                        double height = ChartInputData[i][j] * scale;   
                     
                        Rectangle2D rect = new Rectangle2D.Double(100, x1, height, barHeight - 2);
                        
                        graphics2D.setPaint(myColors[colorNumber][j]);
                        graphics2D.fill(rect);
                        graphics2D.draw(rect);
                        
                        if(ShowDataLabels){
                            graphics2D.setPaint(Color.BLACK);
                            graphics2D.drawString(Integer.toString(ChartInputData[i][j]),(float)(110+height), (float) x1+7);
                        }
                        
                        if(j == ChartInputData[0].length/2 && ShowAxisTitles){
                            
                            Rectangle2D labelBounds = labelFont.getStringBounds(AxisValueH[i], context);
                            double labelWidth = labelBounds.getWidth();
                            x = x1 + (barHeight - labelWidth) / 2;
                            graphics2D.setPaint(Color.BLACK);
                            graphics2D.drawString(AxisValueV[i], (float) (50), (float) x);
                        }
                    }
                        
            }
                
          
      }
      else if("Line-Chart".equals(chartName)){
        int panelWidth = getWidth()-50;
        int panelHeight = getHeight()-75;

        Font titleFont = new Font("SansSerif", Font.BOLD, 20);
        Font labelFont = new Font("SansSerif", Font.PLAIN, 10);

        FontRenderContext context = graphics2D.getFontRenderContext();
        Rectangle2D titleBounds = titleFont.getStringBounds(ChartTitle, context);
        double titleWidth = titleBounds.getWidth();
        double top = titleBounds.getHeight()+20;

        double y = -titleBounds.getY(); 
        double x = (panelWidth - titleWidth) / 2;
        graphics2D.setFont(titleFont);
        if(ShowChartTitle)
          graphics2D.drawString(ChartTitle, (float) x, (float) y);

        if(ShowAxisTitles){
            graphics2D.drawString("Series", (float) getWidth()/2, (float) getHeight()-50);
            graphics2D.drawString("Data", (float) 0, (float) getHeight()/2);
        } 
        
        if(ShowLegend){
            for(int i=0; i<ChartInputData[0].length; i++){
                Rectangle2D legend = new Rectangle2D.Double(getWidth()-70, 100+i*25, 20, 5);
                graphics2D.setPaint(myColors[colorNumber][i]);
                graphics2D.fill(legend);
                graphics2D.draw(legend);
                graphics2D.setFont(labelFont);
                graphics2D.drawString(AxisValueH[i], getWidth()-45, 105+i*25);
            }
        }
        
        // compute the extent of the bar labels
        LineMetrics labelMetrics = labelFont.getLineMetrics("", context);
        double bottom = labelMetrics.getHeight();

        y = panelHeight - labelMetrics.getDescent();
        graphics2D.setFont(labelFont);

        // get the scale factor and width for the bars
        double scale = (panelHeight - top - bottom) / (maxValue - minValue);
        int barWidth = panelWidth / (ChartInputData.length+1);

        // draw the bars
        for (int i = 0; i < ChartInputData[0].length; i++){
            for(int j=0; j<ChartInputData.length-1; j++){
                 // get the coordinates of the bar rectangle
                 int x1 = (j+1) * barWidth;
                 int y1 = (int) top;
                 
                 if (ChartInputData[j][i] >= 0) y1 += (maxValue - ChartInputData[j][i]) * scale;
                 else
                 {
                    y1 += maxValue * scale;
                 }
                 
                 int x2 = (j+2) * barWidth;
                 int y2 = (int) top;
                 if (ChartInputData[j+1][i] >= 0) y2 += (maxValue - ChartInputData[j+1][i]) * scale;
                 else
                 {
                    y2 += maxValue * scale;
                 }
                 
                 graphics2D.setPaint(myColors[colorNumber][i]);
                 graphics2D.setStroke(new BasicStroke(3));
                 graphics2D.drawLine(x1, y1, x2, y2);

                 if(ShowDataLabels){
                    graphics2D.setPaint(Color.BLACK);
                    graphics2D.drawString(Integer.toString(ChartInputData[j][i]),(float)(x1),(float)(y1));
                    graphics2D.drawString(Integer.toString(ChartInputData[j+1][i]),(float)(x2),(float)(y2));
                 }

                 // draw the centered label below the bar
                 if(ShowAxisTitles){
                    graphics2D.setPaint(Color.BLACK);
                    graphics2D.drawString(AxisValueV[j], (float) x1, (float) y);
                    graphics2D.drawString(AxisValueV[j+1], (float) x2, (float) y);
                 }
              }
        }
      }
   }

    // TODO overwrite start(), stop() and destroy() methods

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()== labelBox || e.getSource()== chartType || e.getSource()== colorScheme){
            repaint();
        }
    }


    }

    // TODO overwrite start(), stop() and destroy() methods

