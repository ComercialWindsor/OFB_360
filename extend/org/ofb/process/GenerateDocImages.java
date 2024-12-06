/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
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
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.ofb.process;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.compiere.model.MAttachment;
import org.compiere.model.MAttachmentEntry;
import org.compiere.model.MClient;
import org.compiere.model.X_PM_SupervisionObs;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: GenerateDocImages.java $
 */
public class GenerateDocImages extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{
		
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		MAttachment att = new MAttachment(Env.getCtx(), this.getTable_ID(), this.getRecord_ID(), this.get_TrxName());
		X_PM_SupervisionObs obs = new X_PM_SupervisionObs(Env.getCtx(), this.getRecord_ID(), this.get_TrxName());
		
		MClient client = MClient.get(getCtx());
		String ExportDir = client.getWindowsAttachmentPath();
		List<String> files = new ArrayList<String>();
		ExportDir = ExportDir.replace("\\", "/");
		if (!ExportDir.endsWith("/"))
			ExportDir += "/";
		for (MAttachmentEntry e:att.getEntries())
		{
			/*try {
			Image image = Toolkit.getDefaultToolkit().createImage(e.getData());
			BufferedImage bi = (BufferedImage)image;
			//ImageIO.write(bi, "jpg",new File(ExportDir + this.getRecord_ID()+"_" + e.getName()));
			files.add(ExportDir + this.getRecord_ID()+"_" + e.getName());
			
			if(e.getName().substring(e.getName().length()-3).equalsIgnoreCase("jpg"))
				ImageIO.write(bi, "jpg",new File(ExportDir + this.getRecord_ID()+"_" + e.getName()));
			if(e.getName().substring(e.getName().length()-3).equalsIgnoreCase("gif"))
				ImageIO.write(bi, "gif",new File(ExportDir + this.getRecord_ID()+"_" + e.getName()));
			if(e.getName().substring(e.getName().length()-3).equalsIgnoreCase("png"))
				ImageIO.write(bi, "png",new File(ExportDir + this.getRecord_ID()+"_" + e.getName()));
			} catch (Exception ex) {
	        	ex.printStackTrace();
	        }*/
			File source = e.getFile ();
			File destFile = new File(ExportDir + this.getRecord_ID()+"_" + e.getName());
			files.add(ExportDir + this.getRecord_ID()+"_" + e.getName());
			   if (!destFile.exists()) {
			       destFile.createNewFile();
			   }
			
			FileChannel inputChannel = null;
			FileChannel outputChannel = null;

			 try {
				         inputChannel = new FileInputStream(source).getChannel();
				         outputChannel = new FileOutputStream(destFile).getChannel();
				         outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
			  } 
			 finally {
				   inputChannel.close();
				   outputChannel.close();
				     }
			 
			 source.delete();

		}
		
		obs.setimg_desc1(null);
		obs.setimg_desc2(null);
		obs.setimg_desc3(null);
		obs.setimg_desc4(null);
		obs.setimg_desc5(null);
		obs.setimg_desc6(null);
		obs.setimg_desc7(null);
		for (String f:files){
			if(obs.isuseImgDesc1() && obs.getimg_desc1()==null )
			{
				obs.setimg_desc1(f);
				continue;
			}
			if(obs.isuseImgDesc2() && obs.getimg_desc2()==null )
			{
				obs.setimg_desc2(f);
				continue;
			}
			if(obs.isuseImgDesc3() && obs.getimg_desc3()==null )
			{
				obs.setimg_desc3(f);
				continue;
			}
			if(obs.isuseImgDesc4() && obs.getimg_desc4()==null )
			{
				obs.setimg_desc4(f);
				continue;
			}
			if(obs.isuseImgDesc5() && obs.getimg_desc5()==null )
			{
				obs.setimg_desc5(f);
				continue;
			}
			if(obs.isuseImgDesc6() && obs.getimg_desc6()==null )
			{
				obs.setimg_desc6(f);
				continue;
			}
			if(obs.isuseImgDesc7() && obs.getimg_desc7()==null )
			{
				obs.setimg_desc7(f);
				continue;
			}
		}
		obs.save();
		
		//PM_SupervisionObs
		
	   return "Imagenes Generadas";
	}	//	doIt


	
}	//	ResetAcct
