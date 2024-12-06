/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 Adempiere, Inc. All Rights Reserved.               *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/

package org.compiere.pos;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.border.TitledBorder;

import org.compiere.grid.ed.VNumber;
import org.compiere.model.MOrderLine;
import org.compiere.swing.CButton;
import org.compiere.swing.CLabel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;


/**
 *	Sales Rep Sub Panel
 *	
 *  @author Comunidad de Desarrollo OpenXpertya 
 *         *Basado en Codigo Original Modificado, Revisado y Optimizado de:
 *         *Copyright (c) Jorg Janke
 *  @version $Id: SubSalesRep.java,v 1.1 2004/07/12 04:10:04 jjanke Exp $
 */
public class SubSalesRep extends PosSubPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 840666209988831145L;

	/**
	 * 	Constructor
	 *	@param posPanel POS Panel
	 */
	public SubSalesRep (PosPanel posPanel)
	{
		super (posPanel);
	}	//	PosSubSalesRep
	
	private CLabel f_label = null;
	private CButton f_button = null;
	private CButton DiscountBtn; //faaguilar OFB
	private VNumber f_discount;  //faaguilar OFB 
	private BigDecimal discount;  //faaguilar OFB 
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(SubSalesRep.class);
	
	/**
	 * 	Initialize
	 */
	public void init()
	{
		//	Title
		TitledBorder border = new TitledBorder(Msg.translate(Env.getCtx(), "C_POS_ID"));
		setBorder(border);
		
		//	Content
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = INSETS2;
		//	--
		f_label = new CLabel(p_pos.getName(), CLabel.LEADING);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		add (f_label, gbc);
		//
		
		//faaguilar OFB discount begin
		DiscountBtn = new CButton("Descontar");
		DiscountBtn.addActionListener(this);
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		add (DiscountBtn, gbc);
		
		f_discount = new VNumber("PriceActual", false, false, true,
				DisplayType.Amount, Msg.translate(Env.getCtx(), "PriceActual"));
		f_discount.setColumns(8, 20);
		f_discount.setValue(Env.ZERO);
		f_discount.addActionListener(this);
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		add (f_discount, gbc);
		//faaguilar OFB discount end
		
		f_button = new CButton (Msg.getMsg(Env.getCtx(), "Logout"));
		f_button.setActionCommand("LogOut");
		f_button.setFocusable(false);
		f_button.addActionListener(this);
		gbc.gridx = 4; //faaguilar OFB original value 1
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add (f_button, gbc);
	}	//	init

	/**
	 * 	Get Panel Position
	 */
	public GridBagConstraints getGridBagConstraints()
	{
		GridBagConstraints gbc = super.getGridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		return gbc;
	}	//	getGridBagConstraints
	
	/**
	 * 	Dispose - Free Resources
	 */
	public void dispose()
	{
		super.dispose();
	}	//	dispose

	/**
	 * 	Action Listener
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		String action = e.getActionCommand();
		if (action == null || action.length() == 0)
			return;
		log.info( "PosSubSalesRep - actionPerformed: " + action);
		//faaguilar OFB begin
		log.info( "PosSubSalesRep - actionPerformed: " + action);
		if(action.equals("Descontar"))
		{
			JPasswordField pf = new JPasswordField();
			Object[] message = new Object[] {"Ingrese Password", pf};
			Object[] options = new String[] {"OK", "Cancel"};
			JOptionPane op = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, options);
			JDialog dialog = op.createDialog(null, "Password");
			dialog.setVisible(true);

			String seleccion = String.valueOf(pf.getPassword()); 
			
			if(p_pos.getPassword().equals(seleccion) &&  discount.signum()>0){
				BigDecimal appliedDiscount=discount.divide(new BigDecimal("1.19"),0, BigDecimal.ROUND_HALF_EVEN);
				MOrderLine discountLine= new MOrderLine(p_posPanel.f_curLine.getOrder());
				discountLine.setC_Charge_ID(DB.getSQLValue(null, "select C_Charge_ID from C_charge where name='DESCUENTO'"));
				discountLine.setQty(Env.ONE);
				discountLine.setPrice(appliedDiscount.negate());
				discountLine.save();
				p_posPanel.updateInfo();
				f_discount.setValue(Env.ZERO);
			}
			else
				JOptionPane.showMessageDialog(
						   this,
						   "Password invalida");

		}
		else if (action.equals("VNumber")){
			f_discount.setValue(f_discount.getValue());
			discount=(BigDecimal)f_discount.getValue();
		}
		else if (e.getSource() == f_button)//	Logout
		//faaguilar OFB end
		//	Logout
		p_posPanel.dispose();
	}	//	actinPerformed
	
}	//	PosSubSalesRep
