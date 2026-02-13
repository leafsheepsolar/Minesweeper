package com.leafsheepsolar;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

public class AnotherExperimentFileBecauseImStupid extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AnotherExperimentFileBecauseImStupid frame = new AnotherExperimentFileBecauseImStupid();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AnotherExperimentFileBecauseImStupid() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel nPanel = new JPanel();
		nPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		Dimension nPanelDim = new Dimension(100,50);
		nPanel.setPreferredSize(new Dimension(nPanelDim));
		contentPane.add(nPanel, BorderLayout.NORTH);
		nPanel.setLayout(null);
		
		JLabel lbl = new JLabel(nPanelDim.toString());
		lbl.setBounds(137, 11, 294, 28);
		nPanel.add(lbl);
		
		JPanel cPanel = new JPanel();
		cPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(cPanel, BorderLayout.CENTER);
		cPanel.setLayout(new GridBagLayout());
		
		JLabel lbl1 = new JLabel(getPreferredSize().toString());
		GridBagConstraints gbc_lbl1 = new GridBagConstraints();
		gbc_lbl1.gridwidth = 7;
		gbc_lbl1.gridheight = 3;
		gbc_lbl1.gridx = 0;
		gbc_lbl1.gridy = 0;
		cPanel.add(lbl1, gbc_lbl1);
		
		
		

	}
}
