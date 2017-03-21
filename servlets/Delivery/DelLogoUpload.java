/*
-------------------------------------------------------------------------------------------------
Warning : This Computer Program is protected by copyright law and international treaties.
Unauthorised reproduction or distribution of this program , or any portion of it, may result in severe civil and criminal penalties , and will be prosecuted to the maximum extent possible under the law.

Revision : 
By 					Date								Defect No.	Reasons
Kishore Godse(SMD)	02-12-99    Tuesday 5:12:30 PM		00001	CRE:Created.
CRE : Created
BFX : Bug fix
RAC : Requirement anlaysis changing

------------------------------------------------------------------------------------------------
*/

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.head.*;
import ingen.html.para.*;
import ingen.html.character.*;

public class DelLogoUpload extends HttpServlet
{
	String boundary="";
	String subboundary="";
 	String fileName="";
	String user="";
	public void doGet(HttpServletRequest request,HttpServletResponse responce)
	throws IOException ,ServletException
	{
	    responce.setContentType("text/html");
 	    PrintWriter out=responce.getWriter();
            Page page = new Page();  
            Head head = new Head();
            Script scr = new Script( "JavaScript", null );
            HtmlTag scrdata = new HtmlTag();
            scrdata.add("<!-- Start Hidding" +       "\n"); 
            scrdata.add("  function submit_form() {   \n"+
                        "  if(confirm(\"Do you want to continue?\"))\n"+
                        "    document.forms[0].submit()            \n"+             
                        "  }                                       \n");
            scrdata.add("// End Hidding -->");
            scr.add(scrdata);
            Title title = new Title("Sample File Upload Form");   
            head.add(title);
            head.add(scr);  
            Body body = new Body("/ordimg/BACKGR2.GIF",null); 
            Form form = new Form("/JOrder/servlets/DelLogoUpload", "POST", "_parent", "multipart/form-data", null);
            form.add(new NL());
            Center cen = new Center();
            cen.add("<B>File to upload:</B>");
            cen.add(new NL(2));
            cen.add("<input type=file name=upfile value=\"\"><BR>");
            cen.add( new FormButton( null, " Attach File ", "onClick=\"submit_form()\"" ) );
            form.add(cen);
            body.add(form);
            page.add(head);
            page.add(body);
            out.println(page);
	}
	public synchronized void doPost(HttpServletRequest request,HttpServletResponse response)
	throws IOException,ServletException
	{
		
		PrintWriter out=response.getWriter();

		// Create input stream with the requester to read data  send by that requester.
		ServletInputStream sin=request.getInputStream();
		try
		{
			byte buffer [] =new byte[2048];
			int totalLength=request.getContentLength();
			int bytesRead=0;
			boolean fileEndflag=false;
			int len=sin.read(buffer);
		
			//Get bytes required to read header information .Variable bytesread contatins no of bytes 
			//required to read header

			int bytesread =readHeader(buffer);
			
			//Check for whether fileName is null or not 
			if(fileName == null)
			{
                                out.println("<HTML>");		
				out.println("<H2>File Name Selcted by you is empty </H2>");		
				out.println("<H2>Please Enter Correct File Name </H2>");		
				out.println("</HTML>");		
				sin.close();
				out.close();
				return;
			}
			else if(fileName.trim().indexOf(".") < 0)
			{
                                out.println("<HTML>");		
				out.println("<H2>You might have selected Directory insted of File  </H2>");
				out.println("<H2>You can not upload full directory you have to select one file at a time</H2>");
				out.println("<H2>Please Check it out </H2>");
				out.println("</HTML>");
				sin.close();
				out.close();
				return;
			}
			
			//As File Name is not null then create object of the file provided by user .Also create 
			// Output Stream to that file .
			
			File f = createFile(fileName.substring(0,fileName.indexOf('.')),
                                  fileName.substring(fileName.indexOf('.')+1,fileName.length()));
                        if(f!=null)
                        {
  			  FileOutputStream fout=new FileOutputStream(f); 
      			  
                          // Read Header and if length of file is less than or equals to the bytes read then 
			  // write it into the file  .If length of file is greater than the bytes read then write full bytes 
  			  if(len >= totalLength )
			  {
				Vector v=readFooter(buffer,bytesread,boundary);
				if(((Boolean)v.elementAt(0)).booleanValue())
				{
					//Check whether file is empty or not ?.
					//If file is empty then send the message that file is empty and return . 
					if(bytesread > ((Integer)v.elementAt(1)).intValue() )
					{
                                                out.println("<HTML>");		
						out.println("<H2>File transfer by you does not contain any data it is  Empty File </H2>");		
						out.println("<H2>Please choose another file </H2>");		
						out.println("</HTML>");	
						if(f.exists())
						{
							fout.close();
							f.delete();
						}	
						sin.close();
						out.close();
						return;
					
					}

					// Else write bytes to the file .
					writeBytes(buffer,bytesread,((Integer)v.elementAt(1)).intValue(),fout); 
					fileEndflag=true;
				}
				
  			  }
			  else 
			  {
				writeBytes(buffer,bytesread,len,fout); 
			  }
	 	          // Until Footer, write directly the total bytes array read

     			  while(len < (totalLength-4096) && !fileEndflag)
			  {
				int lenx=sin.read(buffer);
				len+=lenx;
				writeBytes(buffer,0,lenx,fout) ;
				//System.out.println("Total bytes write are ="+len);	

                          }
  			  //System.out.println(" 2 ) After while(len <= (totalLength-2048) ) Total bytes write are ="+len);
			  //From Last 4096 bytes read upto the Footer and then write upto the footer value

   			  while(!fileEndflag && len <= totalLength && !fileEndflag)
			  {
				//System.out.println("						<----"+(j++)+"----->");
				//System.out.println("Reading last "+ (totalLength - len)+"  total read = "+len +" out of "+totalLength);
				int lenx=sin.read(buffer);
			
				//String s=new String(buffer);
				//System.out.println("----"+lenx+"----->"+s);
				
				Vector v=readFooter(buffer,0,boundary);
				if(((Boolean)v.elementAt(0)).booleanValue())
				{
					//System.out.println("Footer starts From "+((Integer)v.elementAt(1)).intValue());
					writeBytes(buffer,0,((Integer)v.elementAt(1)).intValue(),fout); 
					fileEndflag=true;
				}
				else
				{
					writeBytes(buffer,0,lenx,fout) ;
				}
				len+=lenx;
			  }
                          response.setContentType("text/html");
                	  Script scr = new Script( "JavaScript", null );
                          HtmlTag scrdata = new HtmlTag();
                          scrdata.add("<!-- Start Hidding" + "\n"); 
                          scrdata.add(" var oldWin = this.opener.document.forms[0]   \n");
                          scrdata.add(" oldWin.logoname.value = '"+f.getName()+"'    \n"+
                          " with( this.opener.document.ilayer) {         \n"+
                          "  document.open();                           \n"+
                          "  document.writeln(\"<A href='JavaScript:getLogo()'><img SRC='/ordimg/"+f.getName()+"' border='0' height=75 width=75 align=ABSCENTER></A>\")\n"+
                          "  document.close();                           \n"+
                          "  }                                           \n");
                          scrdata.add(" top.close()\n");
                          scrdata.add("// End Hidding -->");
                          scr.add(scrdata);
                          out.println(scr);
  		          fout.close();
			  sin.close();
			  out.close();
			  fout=null;
			  sin=null;
			  out=null;
                       }
                       else
                         out.println("Error in upload");
                       
		}
		catch(FileNotFoundException fne)
		{
                        out.println("<HTML>");
			out.println("<H2> File "+fileName +" is not available</H2>");
			out.println("<H2>Please Check it out </H2>");
			out.println("</HTML>");
			sin.close();
			out.close();
		}
		catch(Exception e)
		{
			out.println("<H2> Exception Occure while uploading file "+fileName +"</H2>");
			out.println("<H2>Exception is "+e+" </H2>");
			out.println("</HTML>");
			sin.close();
			out.close();
		}
	}
	
	
	// This Function write total byte array to the file specified by the FileOutputStream object.
	private  synchronized void writeBytes(byte [] arr,FileOutputStream fout) throws IOException
	{
		fout.write(arr);
	}

	// This Function write specified no ot bytes  to the file specified by the FileOutputStream object.
	private synchronized boolean writeBytes(byte [] arr,int start,int end,FileOutputStream fout) throws IOException
	{
		if(start > end)
		{
			return false;
		}
		for(int i=start;i< end;i++)
		{
			fout.write(arr[i]);
		}
		return true;
	}

	// This Function read the Footer information and return Vector object which contains 
	// 1) End of flag at position no 0 i.e v.elementAt(0) is boolean (This value true if end of File is find else it is false);
	// 2) Position from where Footer information starts  i.e v.elementAt(1) is int;
	private synchronized Vector readFooter(byte [] inputbuffer,int start,String boundary)
	{
			
			int pos1=start;
			boolean done =true;
			boolean isendoffile=false;
			String boundary1=boundary.substring(3,boundary.length());
			while(done)
			{
				byte [] line=readLine(inputbuffer,pos1);
				String ttt=new String(line);
				if(ttt.trim().equals(boundary.trim())|| ttt.indexOf(boundary1.trim()) > 0)// || ttt.trim().indexOf(subboundary.trim()) > 0)
				{
					// End of File Here.
					isendoffile=true;
					done=false;
					break;
				}
				pos1+=line.length;
				if(pos1 >= inputbuffer.length)
				{
					done=false;
					break;
				}
				
			}
			Vector v=new Vector();
			v.addElement(new Boolean(isendoffile));
			v.addElement(new Integer(pos1-2));
			return v;
	}	

	// This Function read the Header information and return int value
	// Return value is Number of bytes required to read Header .
	private synchronized int readHeader(byte [] inputbuffer)
	{
			int pos=0;

			byte [] line=readLine(inputbuffer,pos);
			pos+=line.length;

			String ttt=new String(line);
			boundary=ttt.trim();
			subboundary=boundary.substring(boundary.lastIndexOf("-"),boundary.length());
			
			//	Read FileName /////////////////////////////////
			line=readLine(inputbuffer,pos);
			pos+=line.length;
			ttt=new String(line);
			if(ttt.trim().startsWith("Content-Disposition:"))
			{
				if(ttt.trim().indexOf("filename=") < 0)
				{
					String username=ttt.substring(ttt.indexOf("name=\"")+6,ttt.lastIndexOf("\""));
					user=username;
					System.out.println("UserName ="+username);
				}
				else
				{
					String Length=ttt.substring(ttt.indexOf("filename=\""),ttt.lastIndexOf("\""));
					fileName=Length.substring(Length.lastIndexOf("\\")+1,Length.length());
					if(fileName.trim().equals("filename=\""))
					{
						fileName=null;
					}
				}
	    	}
			//	Read Content Type /////////////////////////////////
			line=readLine(inputbuffer,pos);
			pos+=line.length;
			ttt=new String(line);

			//	Read Black Line  /////////////////////////////////
			line=readLine(inputbuffer,pos);
			pos+=line.length;
			ttt=new String(line);
			if(ttt.trim().equals(""))
			{
				return pos;
			}
			else
			{
				return (pos-line.length);
			}
	}

	// This function read line from the array passed to .
	// second argument to this function is pos which indicates starting position from where line which is tobe read should start.

	private synchronized byte [] readLine(byte arr[],int pos) 
	{
		int len=1;
		// If pos is greater than length of arr then return null;
		if( arr.length < pos)
		{
			return null;
		}

		Vector v=new Vector();
		boolean b=false;
		
		//Read upto the end of line .End of line is indicated by the '\n'(13) and '\r' (10)
		for(int i=pos ; i < arr.length ; i++,len++)
		{
			v.addElement(new Byte(arr[i]));
			if(i > 0 && arr[i] == 10 && arr[i-1] == 13 )
			{
				b=true;
				break;
			}
		}

		//Create byte array from the bytes read for that line and return that byte array.
		byte [] ret=new byte[v.size()];	
		for(int i=0;i< v.size();i++)
		{
			ret[i]=((Byte)v.elementAt(i)).byteValue();
		}
		return ret;
	}

       public synchronized File createFile(String filename,String fileext)
       {
         String dir = "/www/jorder/imgs/"; 
         File f = new File(dir); 
         File f1=null;
         if(f!=null && f.exists()) 
         {
           String temp1=new String(filename+"."+fileext);
           int tcount=1;
           while((f1=new File(f,temp1)).exists())
           {
             temp1 = filename + tcount + "." +fileext;
             tcount++;     
           }     
         }
         return f1;
      } 
}
