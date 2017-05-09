package ru.nsu.ccfit.pyataev.gui;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import ru.nsu.ccfit.pyataev.factory.Factory;

public class SliderTestFrame extends JFrame{
	public static void start(Factory factory){
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				SliderTestFrame frame = new SliderTestFrame(factory);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.repaint();
			}
		});
	}

	@Override
	public void paint(Graphics g){
		sliderPanel.repaint();
	}

	public SliderTestFrame(Factory factory){
		this.factory = factory;
		setResizable(false);
		setTitle("Factory Emulator");
		setSize(500, 500);

		sliderPanel = new JPanel(){
			Thread thread = new Thread(new Runnable(){
				@Override
				public void run(){
					while(true){
						repaint();
					}
				}
			});
			{
				thread.setDaemon(true);
				thread.start();
			}
			@Override
			public void paint(Graphics g){
				super.paint(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.drawString("Body storage: " + String.valueOf(factory.bodyStorageDetNum() + " All time: " + String.valueOf(factory.bodyAllNum())), 15, 250);
				g2d.drawString("Engine storage: " + String.valueOf(factory.engineStorageDetNum() + " All time: " + String.valueOf(factory.engineAllNum())), 15, 270);
				g2d.drawString("Accessory storage: " + String.valueOf(factory.accessoryStorageDetNum() + " All time: " + String.valueOf(factory.accessoryAllNum())), 15, 290);
				g2d.drawString("Car storage: " + String.valueOf(factory.carStorageDetNum() + " All time: " + String.valueOf(factory.carAllNum())), 15, 310);
				g2d.drawString("Tasks number: " + String.valueOf(factory.tasksNum()), 15, 330);
			}
		};

		sliderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JSlider dealers = new JSlider(JSlider.HORIZONTAL, 1, 11, 10);
		dealers.setMajorTickSpacing(5);
		dealers.setMinorTickSpacing(1);
		dealers.setPaintTicks(true);
		dealers.setSnapToTicks(true);
		addSlider(dealers, " Dealers time", new ChangeListener(){
			public void stateChanged(ChangeEvent event){
				JSlider source = (JSlider) event.getSource();
				factory.setDealersTime(source.getValue());
			}
		});

		JSlider EP = new JSlider(JSlider.HORIZONTAL, 1, 11, 3);
		EP.setMajorTickSpacing(5);
		EP.setMinorTickSpacing(1);
		EP.setPaintTicks(true);
		EP.setSnapToTicks(true);
		addSlider(EP, " Engine Providers time", new ChangeListener(){
			public void stateChanged(ChangeEvent event){
				JSlider source = (JSlider) event.getSource();
				factory.setEPTime(source.getValue());
			}
		});

		JSlider BP = new JSlider(JSlider.HORIZONTAL, 1, 11, 2);
		BP.setMajorTickSpacing(5);
		BP.setMinorTickSpacing(1);
		BP.setPaintTicks(true);
		BP.setSnapToTicks(true);
		addSlider(BP, " Body Providers time", new ChangeListener(){
			public void stateChanged(ChangeEvent event){
				JSlider source = (JSlider) event.getSource();
				factory.setBPTime(source.getValue());
			}
		});

		JSlider AP = new JSlider(JSlider.HORIZONTAL, 1, 11, 5);
		AP.setMajorTickSpacing(5);
		AP.setMinorTickSpacing(1);
		AP.setPaintTicks(true);
		AP.setSnapToTicks(true);
		addSlider(AP, " Accessory Providers time", new ChangeListener(){
			public void stateChanged(ChangeEvent event){
				JSlider source = (JSlider) event.getSource();
				factory.setAPTime(source.getValue());
			}
		});

		add(sliderPanel, BorderLayout.CENTER);
	}

	public void addSlider(JSlider s, String description, ChangeListener listen){
		s.addChangeListener(listen);
		JPanel panel = new JPanel();
		panel.add(s);
		panel.add(new JLabel(description));
		sliderPanel.add(panel);
	}


	private JPanel sliderPanel;
	private Factory factory;
}
