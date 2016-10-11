import org.math.plot.Plot2DPanel;
import org.math.plot.canvas.PlotCanvas;
import org.math.plot.render.AbstractDrawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created by barto on 9/27/2016.
 */
public class RunThis{

        String filePath = "C:\\Users\\barto\\OneDrive\\Documents\\School Fall 2016\\Research\\Study 2\\Processed\\5SecRandom";

        String savelocation = filePath + "5SecRandom" + "MarkedNotBlink.csv";
        double[] timestamps;
        double[] leftPoints;
        double[] rightPoints;
        int[] markedBlinks;
        int prevBlinkTime;


        Plot2DPanel plot = new Plot2DPanel();


        public static void main(String[] args) {
            // TODO Auto-generated method stub
            RunThis data = new RunThis();


        }


        public RunThis(){
            filePath += ".xls";
            System.out.println(filePath);
            readFile(filePath);
            final Plot2DPanel panel = new Plot2DPanel();
            final int[] mouseCurrent = new int[2];
            final KeyboardFocusManager keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            keyboardFocusManager.getFocusOwner();


            markedBlinks = new int[timestamps.length];

            double[] shortTime = new double[6000];
            double[] shortLeft = new double[6000];
            double[] shortRight = new double[6000];
            double[] shortLeftN = new double[6000];
            double[] shortRightN = new double[6000];


            for(int i=0;i<6000;i++){
                shortTime[i]=timestamps[i];
                shortLeftN[i]=leftPoints[i];
                if(i % 100 > 50){

                    shortLeft[i]=leftPoints[i];
                    shortRight[i]=rightPoints[i];
                }
            }

            double[] NAleft = leftPoints;
            //average10();
            //panel.addScatterPlot("LEFT", Color.red,timestamps,rightPoints);
            panel.addScatterPlot("Right", Color.blue, timestamps, leftPoints);
            //panel.addLinePlot
            //panel.addScatterPlot("Right", Color.blue, shortTime, shortLeftN);
            leftPoints = NAleft;



            panel.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    System.out.println("HERE?");
                    mouseCurrent[0] = (int)panel.getMousePosition().getX();
                    mouseCurrent[1] = (int)panel.getMousePosition().getY();
                    PlotCanvas pc = panel.getPlotCanvas();
                    //AbstractDrawer draw = pc.getDraw();
                    //double[] _coordNoted = panel.getPlot(0).isSelected(mouseCurrent, draw);
                    //System.out.println(_coordNoted[0]+ " "+ _coordNoted[1]);
                    //System.out.println(pc.lastClosePoint[0] + " " + pc.lastClosePoint[1]);


                    if(e.getKeyChar() == 'm'){
                        markBlink(pc.lastClosePoint[0],pc.lastClosePoint[1]);
                    }
                    if(e.getKeyChar() == 's'){
                        saveMarkedBlinks();
                    }
                    if(e.getKeyChar() == 'r'){
                        removeLast();
                    }
                    if(e.getKeyChar() == 'f'){// print current object with focus
                        System.out.println(keyboardFocusManager.getFocusOwner().toString().substring(14,30));
                    }
                    if(e.getKeyChar() == 'n'){
                        markNOTBlink(pc.lastClosePoint[0],pc.lastClosePoint[1]);
                    }

                }

                @Override
                public void keyPressed(KeyEvent e) {

                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });
            panel.getMousePosition();

        /*
            panel.getInputMap().put(KeyStroke.getKeyStroke("k"),"DoA");
            panel.getActionMap().put("DoA",
                    DoA);
        */
            panel.setFocusable(true);
            JFrame frame= new JFrame("Histogram");
            frame.setContentPane(panel);
            frame.setSize(500, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            keyboardFocusManager.getFocusOwner();
            Object a = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();


        }

        public void markBlink(double time, double point){
            int intTime = (int) (time * 1000);
            markedBlinks[intTime] = 1;
            prevBlinkTime = intTime;
            System.out.println("Marked at: " + prevBlinkTime );
        }
        public void markNOTBlink(double time, double point) {
            int intTime = (int) (time * 1000);
            markedBlinks[intTime] = 2;
            prevBlinkTime = intTime;
            System.out.println("Maked not Blink : " + time);
        }

        public void removeLast(){
            markedBlinks[prevBlinkTime] = 0;
            System.out.println("Removed blink at: " + prevBlinkTime);
        }
        public void saveMarkedBlinks(){
            String line;
            try(Writer writer = new BufferedWriter(new OutputStreamWriter
                    (new FileOutputStream(savelocation), StandardCharsets.UTF_8))){
                writer.write("Left," + "Right," + "Mark,Time \n");

                for(int i=0;i<timestamps.length;i++){

                    line = String.format(leftPoints[i] + "," + rightPoints[i] + ","
                            + markedBlinks[i]+"," +timestamps[i]+"\n");
                    writer.write(line);
                }
                System.out.println("Saved");
            }catch (Exception e){
                System.out.println("failed here " + e.getMessage());
            }

            System.out.println("Saved");
        }
        public RunThis(String path, boolean display){
            readFile(path);
            Plot2DPanel panel = new Plot2DPanel();

            //double[] diff = combine("subtract",leftPoints,rightPoints);
            //double[] abs = combine("abs",leftPoints,rightPoints);
            //double[] absDiff = combine("absDiff",leftPoints,rightPoints);

            //processData(shortTime,shortLeft,shortRight)

            if(display){
                panel.addLinePlot("Left", Color.red, timestamps, leftPoints);
                panel.addLinePlot("Right", Color.blue, timestamps, rightPoints);
                //panel.addLinePlot("Right", Color.orange, timestamps, absDiff);

                JFrame  frame= new JFrame("Histogram");
                frame.setContentPane(panel);
                frame.setSize(500, 600);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        }

        public  void average10(){
            double[] current = new double[10];
            for(int i =0;i<timestamps.length;i++){
                current[i%10] = leftPoints[i];

                if(i>10){
                    double avg = 0;
                    for(int j=0;j<10;j++){
                        avg += current[j];
                    }
                    avg = avg /10.0;
                    leftPoints[i] = avg;
                }
            }
        }

	/* Combines the Left and Right Arrays:
	 * Always Left first then Right eye
	 * subtract
	 * abs = sum abs(a) + abs(b)
	 * absDiff = abs(a) - abs(b)
	 *
	 */
	/*
	public double[] combine(String key, double[] a, double[] b){
		double[] rv = new double[a.length];
		switch (key){
			case "subtract":
				for(int i=0;i<a.length;i++)
					rv[i] = a[i] - b[i];
				break;
			case "abs":
				for(int i=0;i<a.length;i++){
					double absA = Math.abs(a[i]);
					double absB = Math.abs(b[i]);
					rv[i] = absA + absB;
				}
				break;
			case "absDiff":
				for(int i=0;i<a.length;i++){
					double absA = Math.abs(a[i]);
					double absB = Math.abs(b[i]);
					rv[i] = absA - absB;
				}
				break;


		}
		return rv;

	}
	*/

        public void readFile(String path){
            File f = new File(path);
            Scanner scanner = null;
            ArrayList<Double> time = new ArrayList<Double>();
            ArrayList<Double> left = new ArrayList<Double>();
            ArrayList<Double> right = new ArrayList<Double>();

            try {
                scanner = new Scanner(f);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            scanner.nextLine(); //skips the headers

            while(scanner.hasNextLine()) {
                String thisInfoString = scanner.nextLine();
                String[] pieces = thisInfoString.split("\t");

                time.add(Double.parseDouble(pieces[0]));
                left.add(Double.parseDouble(pieces[1]));
                right.add(Double.parseDouble(pieces[2]));

                //System.out.printf("P: %s %s %s \n", pieces[0], pieces[1], pieces[2]);
            }
            timestamps= new double[time.size()];
            leftPoints = new double[time.size()];
            rightPoints= new double[time.size()];
            for(int i=0;i<time.size();i++){
                timestamps[i]= time.get(i);
                leftPoints[i]= left.get(i);
                rightPoints[i] = right.get(i);
            }

            //System.out.println(time);
            //System.out.println(left);
            //System.out.println(right);
            //System.out.println(timestamps[4]);
            //System.out.println(leftPoints[4]);
            //System.out.println(rightPoints[2354]);


        }


    }


