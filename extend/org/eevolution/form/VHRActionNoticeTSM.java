/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Contributor(s): Oscar Gomez & Victor Perez www.e-evolution.com             *
 * Copyright (C) 2003-2007 e-Evolution,SC. All Rights Reserved.               *
 *****************************************************************************/
package org.eevolution.form;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.compiere.apps.ADialog;
import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.ed.VComboBox;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.grid.ed.VNumber;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MLookupFactory;
import org.compiere.model.X_HR_Concept_TSM;
import org.compiere.model.X_HR_Prebitacora;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.eevolution.model.X_HR_Concept;

/**
 *  @author Oscar Gomez
 * 			<li>BF [ 2936481 ] no show employee into action notice
 * 			<li>https://sourceforge.net/tracker/?func=detail&aid=2936481&group_id=176962&atid=934929
 *  @author Cristina Ghita, www.arhipac.ro
 *  @version $Id: VHRActionNotice.java
 *  
 *  Contributor: Carlos Ruiz (globalqss) 
 *    [ adempiere-Libero-2840048 ] Apply ABP to VHRActionNotice  
 *    [ adempiere-Libero-2840056 ] Payroll Action Notice - concept list wrong
 */
public class VHRActionNoticeTSM extends HRActionNoticeTSM implements FormPanel, ActionListener, VetoableChangeListener
{
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel parameterPanel = new CPanel();

	//nuevos campos ininoles
	public static VComboBox fieldTSMCmbType = new VComboBox();
	public static VComboBox fieldTSMEmployeeTSM = new VComboBox();
	public static VComboBox fieldTSMAssetTSM = new VComboBox();
	public static VDate fieldTSMDateTrx = new VDate();
	public static VNumber fieldTSMDays = new VNumber();
	public static VComboBox fieldTSMOrg = new VComboBox();
	public static VComboBox fieldTSMWorkshift = new VComboBox();
	public static VComboBox fieldTSMHRConceptTSM = new VComboBox();
	
	

	/**	Window No			*/
	private int           m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 	  m_frame;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VHRActionNoticeTSM.class);
	//
	private JLabel dataStatus = new JLabel();
	private JScrollPane dataPane = new JScrollPane();
	private static MiniTable miniTable = new MiniTable();
	private CPanel commandPanel = new CPanel();
	private FlowLayout commandLayout = new FlowLayout();
	private JButton bOk = ConfirmPanel.createOKButton(true);

	//
	//labels ininoles
	private CLabel labelTSMCmbType = new CLabel();
	private CLabel labelTSMEmployeeTSM = new CLabel();
	private CLabel labelTSMAssetTSM = new CLabel();
	private CLabel labelTSMDateTrx = new CLabel();
	private CLabel labelTSMDays = new CLabel();
	private CLabel labelTSMOrg = new CLabel();
	private CLabel labelTSMWorkshift = new CLabel();
	private CLabel labelTSMHRConceptTSM= new CLabel();
	

	
	private GridBagLayout parameterLayout = new GridBagLayout();	

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		log.info("");
		m_WindowNo = WindowNo;
		m_frame = frame;
		Env.setContext(Env.getCtx(), m_WindowNo, "IsSOTrx", "Y");
		try
		{	
			super.dynInit();
			dynInit();
			jbInit();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			frame.setSize(1000, 400);
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "init", ex);
		}
	}	//	init


	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose

	
	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit()
	{
		CompiereColor.setBackground(mainPanel);
		mainPanel.setLayout(mainLayout);
		///mainPanel.setSize(500, 500);
		mainPanel.setPreferredSize(new Dimension(1000, 400));
		parameterPanel.setLayout(parameterLayout);
		
		//tipo
		labelTSMCmbType.setLabelFor(fieldTSMCmbType);
		labelTSMCmbType.setText("Tipo");
		//empleado
		labelTSMEmployeeTSM.setLabelFor(fieldTSMEmployeeTSM);
		labelTSMEmployeeTSM.setText("Empleado");
		//activo
		labelTSMAssetTSM.setLabelFor(fieldTSMAssetTSM);
		labelTSMAssetTSM.setText("Activo");
		//fecha
		labelTSMDateTrx.setLabelFor(fieldTSMDateTrx);
		labelTSMDateTrx.setText("Fecha Inicio");
		//Dias
		labelTSMDays.setLabelFor(fieldTSMDays);
		labelTSMDays.setText("Dias");
		//turno
		labelTSMWorkshift.setLabelFor(fieldTSMWorkshift);
		labelTSMWorkshift.setText("Turno");
		//organización
		labelTSMOrg.setLabelFor(fieldTSMOrg);
		labelTSMOrg.setText("Organización");
		//type
		labelTSMHRConceptTSM.setLabelFor(fieldTSMCmbType);
		labelTSMHRConceptTSM.setText("Concepto");	
		//TSM
		
		mainPanel.add(parameterPanel, BorderLayout.NORTH);
		// Tipo
		parameterPanel.add(labelTSMCmbType,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldTSMCmbType,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		//Organizacion
		parameterPanel.add(labelTSMOrg,  new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldTSMOrg,   new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		//Socio de negocio
		parameterPanel.add(labelTSMEmployeeTSM,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldTSMEmployeeTSM,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		//Activo en misma posicion que empleado
		parameterPanel.add(labelTSMAssetTSM,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldTSMAssetTSM,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));		
		//Concepto
		parameterPanel.add(labelTSMHRConceptTSM,  new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldTSMHRConceptTSM,  new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//Fecha
		parameterPanel.add(labelTSMDateTrx,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldTSMDateTrx,  new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//Dias
		parameterPanel.add(labelTSMDays,  new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldTSMDays,  new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));		
		// Turno
		parameterPanel.add(labelTSMWorkshift,  new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldTSMWorkshift,  new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// Refresh
		parameterPanel.add(bOk, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// Agree
		mainPanel.add(dataStatus, BorderLayout.SOUTH);
		mainPanel.add(dataPane, BorderLayout.CENTER);
		dataPane.getViewport().add(miniTable, null);
		//
		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.RIGHT);
		commandLayout.setHgap(10);

	}   //  jbInit


	/**
	 *	Fill Picks.
	 *		Column_ID from C_Order
	 *  @throws Exception if Lookups cannot be initialized
	 */
	public void dynInit() throws Exception
	{
		//tipo
		fieldTSMCmbType = new VComboBox(getTypeTSM());
		fieldTSMCmbType.addActionListener(this);
		fieldTSMCmbType.setMandatory(true);
		//empleado
		fieldTSMEmployeeTSM.addActionListener(this);
		fieldTSMEmployeeTSM.setReadWrite(false);
		fieldTSMEmployeeTSM.setMandatory(true);
		fieldTSMEmployeeTSM.setVisible(false);
		labelTSMEmployeeTSM.setVisible(false);
		
		//activo
		fieldTSMAssetTSM.addActionListener(this);
		fieldTSMAssetTSM.setReadWrite(false);
		fieldTSMAssetTSM.setMandatory(true);
		fieldTSMAssetTSM.setVisible(false);
		labelTSMAssetTSM.setVisible(false);
		
		//fecha
		fieldTSMDateTrx.setReadWrite(false);
		fieldTSMDateTrx.setMandatory(true);
		fieldTSMDateTrx.addVetoableChangeListener(this);
		//dias
		fieldTSMDays.setReadWrite(false);
		fieldTSMDays.setDisplayType(DisplayType.Quantity);
		fieldTSMDays.setValue(1);
		//turno
		fieldTSMWorkshift = new VComboBox(getWorkshiftTSM());
		fieldTSMWorkshift.setReadWrite(false);
		fieldTSMWorkshift.setMandatory(false);
		//organización
		fieldTSMOrg.removeAllItems();		
		for(KeyNamePair ppt : getOrgTSM())
		{
			fieldTSMOrg.addItem(ppt);
		}
		fieldTSMOrg.setReadWrite(true);
		fieldTSMOrg.setMandatory(true);
		fieldTSMOrg.addActionListener(this);
		
		//concepto
		fieldTSMHRConceptTSM = new VComboBox(getConceptTSM());
		fieldTSMHRConceptTSM.setReadWrite(false);
		fieldTSMHRConceptTSM.setMandatory(true);
		//
		bOk.addActionListener(this);
		miniTable = new MiniTable();
		configureMiniTable(miniTable);
		
	}	//	fillPicks
	
	public static void executeQueryTSM(String type)
	{
		executeQueryTSM(Env.getCtx(),miniTable, type);
	}   //  executeQuery


	/**************************************************************************
	 *  vetoableChange
	 *  @param e event
	 */
	public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException 
	{
		/**esto es para cuando cambia la fecha, se podrian agregar aqui validaciones 
		 * de fecha
		 * */
	
		/*fieldConcept.setReadWrite(true);
		log.fine("Event"+ e);
		log.fine("Event Source "+ e.getSource());
		log.fine("Event Property "+ e.getPropertyName());
		Integer   HR_Period_ID = new MHRProcess(Env.getCtx(),(Integer)fieldProcess.getValue(),null).getHR_Period_ID(); 
		String date = DB.TO_DATE((Timestamp)fieldValidFrom.getValue());
		int existRange = DB.getSQLValueEx(null,"SELECT HR_Period_ID FROM HR_Period WHERE " +date+
				" >= StartDate AND "+date+	" <= EndDate AND HR_Period_ID = "+HR_Period_ID);
		// Exist of Range Payroll
		if ( existRange < 0){
			fieldConcept.setReadWrite(false);
			return;
		}
		if (fieldConcept != null)*/
		//	sHR_Movement_ID = seekMovement((Timestamp)fieldValidFrom.getValue()); // exist movement record to date actual
	}   //  vetoableChange


	/**************************************************************************
	 *	Action Listener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		
		
		log.fine("Event "+ e);
		log.fine("Event Source "+ e.getSource());
		if (e.getSource().equals(fieldTSMCmbType)) 
		{	// tipo
			KeyNamePair pp = (KeyNamePair)fieldTSMCmbType.getSelectedItem();
			if (pp != null)
			{
				m_TSM_ValueType = DB.getSQLValueString(null, "SELECT value " +
						"FROM AD_Ref_List WHERE AD_Ref_List_ID = ? ",pp.getKey());
				
			/*	if(m_TSM_ValueType.compareToIgnoreCase("A")==0)
				{
					labelTSMAssetTSM.setVisible(true);
					fieldTSMAssetTSM.setVisible(true);
					fieldTSMAssetTSM.removeAllItems();
					//escondemos campo
					labelTSMEmployeeTSM.setVisible(false);
					fieldTSMEmployeeTSM.setVisible(false);
					fieldTSMEmployeeTSM.removeAllItems();
					
					for(KeyNamePair ppt : getAssetTSM())
					{
						fieldTSMAssetTSM.addItem(ppt);
					}
					fieldTSMAssetTSM.setSelectedIndex(0);
					fieldTSMAssetTSM.setReadWrite(true);					
					
				}else if(m_TSM_ValueType.compareToIgnoreCase("B")==0)
				{
					labelTSMEmployeeTSM.setVisible(true);
					fieldTSMEmployeeTSM.setVisible(true);
					fieldTSMEmployeeTSM.removeAllItems();
					//escondemos campos 
					labelTSMAssetTSM.setVisible(false);
					fieldTSMAssetTSM.setVisible(false);
					fieldTSMAssetTSM.removeAllItems();
					
					for(KeyNamePair ppt : getEmployeeTSM())
					{
						fieldTSMEmployeeTSM.addItem(ppt);
					}
					fieldTSMEmployeeTSM.setSelectedIndex(0);
					fieldTSMEmployeeTSM.setReadWrite(true);								
				}*/
			}
		}
		else if ( e.getSource().equals(fieldTSMEmployeeTSM) )
		{	// Employee
			KeyNamePair pp = (KeyNamePair)fieldTSMEmployeeTSM.getSelectedItem();
			if ( pp != null )
				m_TSM_BPartner_ID = pp.getKey();
			if ( m_TSM_BPartner_ID > 0)
			{	
				fieldTSMDateTrx.setReadWrite(true);
				fieldTSMDays.setReadWrite(true);
				fieldTSMWorkshift.setReadWrite(true);
				fieldTSMOrg.setReadWrite(true);
				fieldTSMHRConceptTSM.setReadWrite(true);				
				executeQueryTSM("B");
				fieldTSMHRConceptTSM.removeAllItems();
				KeyNamePair[] conceptValues = getConceptTSM("B");
				for(int a = 0; a < conceptValues.length; a++)
				{
					fieldTSMHRConceptTSM.addItem(conceptValues[a]);
				}					
			}else
			{
				//miniTable = new MiniTable();
				//configureMiniTable(miniTable);
				fieldTSMDateTrx.setReadWrite(false);
				fieldTSMDays.setReadWrite(false);
				fieldTSMWorkshift.setReadWrite(false);
				//fieldTSMOrg.setReadWrite(false);
				fieldTSMHRConceptTSM.setReadWrite(false);
			}
		}		
		else if ( e.getSource().equals(fieldTSMAssetTSM) ) 
		{	// asset
			KeyNamePair pp = (KeyNamePair)fieldTSMAssetTSM.getSelectedItem();
			if (pp != null)
				m_TSM_Asset_ID = pp.getKey();			
			if (m_TSM_Asset_ID > 0)
			{	
				fieldTSMDateTrx.setReadWrite(true);
				fieldTSMDays.setReadWrite(true);
				fieldTSMWorkshift.setReadWrite(true);
				fieldTSMOrg.setReadWrite(true);
				fieldTSMHRConceptTSM.setReadWrite(true);				
				executeQueryTSM("A");
				fieldTSMHRConceptTSM.removeAllItems();
				KeyNamePair[] conceptValues = getConceptTSM("A");
				for(int a = 0; a < conceptValues.length; a++)
				{
					fieldTSMHRConceptTSM.addItem(conceptValues[a]);
				}			
			}
			else
			{
				//miniTable = new MiniTable();
				//configureMiniTable(miniTable);
				fieldTSMDateTrx.setReadWrite(false);
				fieldTSMDays.setReadWrite(false);
				fieldTSMWorkshift.setReadWrite(false);
				//fieldTSMOrg.setReadWrite(false);
				fieldTSMHRConceptTSM.setReadWrite(false);
			}
		} // Concept
		//se agrega accion al seleccionar org
		else if ( e.getSource().equals(fieldTSMOrg)) 
		{	// asset
			KeyNamePair pp = (KeyNamePair)fieldTSMOrg.getSelectedItem();
			if (pp != null)
				m_TSM_Org_ID = pp.getKey();
			if(m_TSM_ValueType.compareToIgnoreCase("A")==0)
			{
				labelTSMAssetTSM.setVisible(true);
				fieldTSMAssetTSM.setVisible(true);
				fieldTSMAssetTSM.removeAllItems();
				//escondemos campo
				labelTSMEmployeeTSM.setVisible(false);
				fieldTSMEmployeeTSM.setVisible(false);
				fieldTSMEmployeeTSM.removeAllItems();
				
				for(KeyNamePair ppt : getAssetTSMOrg(m_TSM_Org_ID))
				{
					fieldTSMAssetTSM.addItem(ppt);
				}
				fieldTSMAssetTSM.setSelectedIndex(0);
				fieldTSMAssetTSM.setReadWrite(true);					
				
			}else if(m_TSM_ValueType.compareToIgnoreCase("B")==0)
			{
				labelTSMEmployeeTSM.setVisible(true);
				fieldTSMEmployeeTSM.setVisible(true);
				fieldTSMEmployeeTSM.removeAllItems();
				//escondemos campos 
				labelTSMAssetTSM.setVisible(false);
				fieldTSMAssetTSM.setVisible(false);
				fieldTSMAssetTSM.removeAllItems();
				
				for(KeyNamePair ppt : getEmployeeTSMOrg(m_TSM_Org_ID))
				{
					fieldTSMEmployeeTSM.addItem(ppt);
				}
				fieldTSMEmployeeTSM.setSelectedIndex(0);
				fieldTSMEmployeeTSM.setReadWrite(true);								
			}
		}
		
		else if (e instanceof ActionEvent && e.getSource().equals(bOk))
		{	//validacion de campos llenos
			if( fieldTSMCmbType.getValue() == null || ((Integer)fieldTSMCmbType.getValue()).intValue() <= 0
				|| fieldTSMDateTrx.getValue() == null				
				|| fieldTSMOrg.getValue() == null || ((Integer)fieldTSMOrg.getValue()).intValue() <= 0
				|| fieldTSMHRConceptTSM.getValue() == null || ((Integer)fieldTSMHRConceptTSM.getValue()).intValue() <= 0) 
			{  // required fields
				ADialog.error(m_WindowNo, this.mainPanel, Msg.translate(Env.getCtx(), "FillMandatory")
						+ " Tipo, Fecha, Organización, Concepto");
			} 
			else	// SAVE prebitacora
				saveMovement();
		}
		//executeQuery();
		return;
	}   //  actionPerformed

	public static void saveMovement()
	{
		//MHRConcept conceptOK   = MHRConcept.get(Env.getCtx(),m_HR_Concept_ID);
		int ID_preBitacora = sTSMPreBitacora_ID > 0 ? sTSMPreBitacora_ID : 0;
		X_HR_Prebitacora prebitacora = new X_HR_Prebitacora(Env.getCtx(),ID_preBitacora,null);
		prebitacora.setAD_Org_ID((Integer)fieldTSMOrg.getValue());		
		KeyNamePair pp = (KeyNamePair)fieldTSMCmbType.getSelectedItem();
		if (pp != null)
		{
			m_TSM_ValueType = DB.getSQLValueString(null, "SELECT value " +
					"FROM AD_Ref_List WHERE AD_Ref_List_ID = ? ",pp.getKey());
			prebitacora.setColumnType(m_TSM_ValueType);
		}
		if (fieldTSMAssetTSM.getValue() != null && (Integer)fieldTSMAssetTSM.getValue() > 0)
			prebitacora.setA_Asset_ID((Integer)fieldTSMAssetTSM.getValue());
		if (fieldTSMEmployeeTSM.getValue() != null && (Integer)fieldTSMEmployeeTSM.getValue() > 0)
			prebitacora.setC_BPartner_ID((Integer)fieldTSMEmployeeTSM.getValue());
		//validamos si es sabado o domingo	
		//obtenemos fecha en calendario
		Calendar calCalendario = Calendar.getInstance();		
		Timestamp dateField = (Timestamp)fieldTSMDateTrx.getTimestamp();
		calCalendario.setTimeInMillis(dateField.getTime());
		//buscar si es fecha seguida o sin fin de semana
		X_HR_Concept_TSM con = new X_HR_Concept_TSM(Env.getCtx(),(Integer)fieldTSMHRConceptTSM.getValue(),null);
		Boolean IsBDay = con.get_ValueAsBoolean("OnlyBusinessDay"); 
		if(IsBDay)
		{		        
	        if(calCalendario.get(Calendar.DAY_OF_WEEK) == 1)
	        	calCalendario.add(Calendar.DATE,1);
	        else if(calCalendario.get(Calendar.DAY_OF_WEEK) == 7)
	        	calCalendario.add(Calendar.DATE,2);
		}
        dateField = new Timestamp(calCalendario.getTimeInMillis());
		prebitacora.setDateTrx(dateField);
		prebitacora.setdays(fieldTSMDays.getValue() != null ? (BigDecimal)fieldTSMDays.getValue() : Env.ONE);
		
		KeyNamePair ppWS = (KeyNamePair)fieldTSMWorkshift.getSelectedItem();
		String vWorkshift = null;
		if (ppWS.getKey() > 0)
		{
			vWorkshift = DB.getSQLValueString(null, "SELECT value " +
				"FROM AD_Ref_List WHERE AD_Ref_List_ID = ? ",ppWS.getKey());
			prebitacora.setWorkshift(vWorkshift);
		}
		prebitacora.setHR_Concept_TSM_ID((Integer)fieldTSMHRConceptTSM.getValue());
		prebitacora.setProcessed(false);
		prebitacora.setIsActive(true);
		prebitacora.saveEx();
		//generamos demas registros dependiendo de los dias
		BigDecimal qty = (BigDecimal)fieldTSMDays.getValue();
		int indice = 0; 
		if(qty != null && qty.compareTo(Env.ZERO) > 0)
		{
			indice = qty.intValue();
			indice--;
			X_HR_Prebitacora prebitacoraSub = null;
			//obtenemos fecha en calendario			
	        //calCalendario.setTimeInMillis(prebitacora.getDateTrx().getTime());			
			while(indice > 0)
			{
				//calculamos nueva fecha sumandole 1 dia
				calCalendario.add(Calendar.DATE,1);
				//ininoles antes de guardar nueva prebitacora se valida que no sea sabado o domingo
				if(IsBDay)
				{
					if(calCalendario.get(Calendar.DAY_OF_WEEK) == 1)
			        	calCalendario.add(Calendar.DATE,1);
			        else if(calCalendario.get(Calendar.DAY_OF_WEEK) == 7)
			        	calCalendario.add(Calendar.DATE,2);
				}
				prebitacoraSub = new X_HR_Prebitacora(Env.getCtx(),0,null);
				prebitacoraSub.setAD_Org_ID(prebitacora.getAD_Org_ID());		
				prebitacoraSub.setColumnType(prebitacora.getColumnType());
				prebitacoraSub.setA_Asset_ID(prebitacora.getA_Asset_ID());
				prebitacoraSub.setC_BPartner_ID(prebitacora.getC_BPartner_ID());				
				prebitacoraSub.setWorkshift(prebitacora.getWorkshift());
				prebitacoraSub.setHR_Concept_TSM_ID(prebitacora.getHR_Concept_TSM_ID());
				prebitacoraSub.setProcessed(false);
				prebitacoraSub.setIsActive(true);				
				prebitacoraSub.setDateTrx(new Timestamp(calCalendario.getTimeInMillis()));				
				prebitacoraSub.saveEx();
				indice--;
			}
			
			prebitacora.setdays(fieldTSMDays.getValue() != null ? (BigDecimal)fieldTSMDays.getValue() : Env.ONE);
		}
		executeQueryTSM(m_TSM_ValueType);
		fieldTSMDateTrx.setValue(null);
		fieldTSMDays.setValue(Env.ONE);
		fieldTSMHRConceptTSM.setSelectedIndex(0);
		sTSMPreBitacora_ID = 0;
		
		// clear fields
	}
}   //  VHRActionNotice