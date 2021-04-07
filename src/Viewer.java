import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class Viewer{
    Vector<String> viewDropdownList;
    Boolean[] GraphAlreadySet;
    ProgramUI root;
    int method;
    public Viewer(ProgramUI root, Analysis strategy, String[] analysisNames, int method, String finalMessage, String[] seriesName, String[] viewerTypes){
        /*
        Create a vector to populate the potential options for your strategy.
         */
        this.root = root;
        this.method = method;
        viewDropdownList = new Vector<>();
        GraphAlreadySet = new Boolean[viewerTypes.length];
        for (int i = 0; i < viewerTypes.length; i++){
            viewDropdownList.add(viewerTypes[i]);
            GraphAlreadySet[i] = false;
        }

       for(int i = 0; i<viewDropdownList.size(); i++){
            root.viewDropdown.addItem(viewDropdownList.get(i));
        }
        root.add_view.removeActionListener(root.add_view.getActionListeners()[0]);
        root.add_view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("add view clicked");
                if(root.viewDropdown.getItemCount()==0){
                    JOptionPane.showMessageDialog(root,"You have not analyzed  a country yet, please do so before adding any viewers");
                }
                else{


                    if(root.viewDropdown.getSelectedItem().equals("Line Chart") && !GraphAlreadySet[0]){
                        strategy.CreateLineChart();
                        GraphAlreadySet[0]=true;
                    }
                    else if(root.viewDropdown.getSelectedItem().equals("Scatter Plot") && !GraphAlreadySet[1]){
                        strategy.createScatter();
                        GraphAlreadySet[1]=true;
                    }
                    else if(root.viewDropdown.getSelectedItem().equals("Bar Chart") && !GraphAlreadySet[2]){
                        strategy.createBar();
                        GraphAlreadySet[2]=true;
                    }
                    else if(root.viewDropdown.getSelectedItem().equals("Time Series") && !GraphAlreadySet[3]){
                        strategy.createTimeSeries();
                        GraphAlreadySet[3]=true;

                    }
                    else if(root.viewDropdown.getSelectedItem().equals("Report") && !GraphAlreadySet[4]){
                        strategy.createReport(finalMessage);
                        GraphAlreadySet[4]=true;

                    }
                    else{
                        JOptionPane.showMessageDialog(root,"Graph already displayed!");
                    }

                }
            }
        });
    }
    public void addToUI(JFreeChart chart){
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.addChartMouseListener( new removeGraphClicked(root,chartPanel,viewDropdownList,method,GraphAlreadySet)) ;
        chartPanel.setPreferredSize(new Dimension(600, 400));
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        root.getCenter().add(chartPanel);
        root.validate();
    }
    public Boolean[] getGraphAlreadySet(){
        return GraphAlreadySet;
    }


}
class removeGraphClicked implements ChartMouseListener {
    int index; // Index for removal of chart to set to false
    Vector<String> viewDropdownList;
    ProgramUI root;
    ChartPanel chartPanel;
    Boolean[] graphAlreadySet;
    public removeGraphClicked(ProgramUI root, ChartPanel chartPanel, Vector<String> viewDropdownList, int index, Boolean[] graphAlreadySet) {
        this.root = root;
        this.viewDropdownList = viewDropdownList;
        this.chartPanel = chartPanel;
        this.index = index;
        this.graphAlreadySet=graphAlreadySet;
    }

    @Override
    public void chartMouseClicked(ChartMouseEvent chartMouseEvent) {
        if (root.minusButtonClicked) {
            if(viewDropdownList.size()==0){
                JOptionPane.showMessageDialog(root,"No charts clicked");
            }
            else {
                chartPanel.removeAll();
                graphAlreadySet[index] = false;
                root.minusButtonClicked = false;
                System.out.println("Removed graph");
                root.getCenter().remove(chartPanel);
                root.validate();
                root.pack();
            }
        } else
            System.out.println("+ not clicked yet");
    }
    @Override
    public void chartMouseMoved(ChartMouseEvent chartMouseEvent) {

    }
}
