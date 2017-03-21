import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DelForm
{
  public static Script ShowDelScript(HttpServletRequest request,String pvMode, String vPID )
  {
    ConfigData config = new ConfigData();
    Message msg = new Message();
    DBConnect db = new DBConnect();
    Connection conn = null;
    Statement  stmt = null; 
    ResultSet  rs = null;
    String query = null;

    String nLangID  = Parse.GetValueFromString( vPID, "LangID" );
    String nUserID  = Parse.GetValueFromString( vPID, "UserID" );
    String vTemplName  = config.GetConfigValue( "TR_DELIVERY", nLangID, "BL_LABEL.B_DELIVERY_DELIVERY_TEMPL", "Select Delivery Order Template" );
    String vDelvNo  = config.GetConfigValue( "TR_DELIVERY", nLangID, "BL_LABEL.B_DELIVERY_DELIVERY_NR", "Delivery No" );
    String vDelvDt  = config.GetConfigValue( "TR_DELIVERY", nLangID, "BL_LABEL.B_DELIVERY_DELIVERY_DT", "Delivery Date" );
    String vPosNr   = config.GetConfigValue( "TR_DELVDTLS", nLangID, "BL_LABEL.B_DELIVERYDTLS_POS_NR", "Pos No" );
    String vQuantity  = config.GetConfigValue( "TR_DELVDTLS", nLangID, "BL_LABEL.B_DELIVERYDTLS_QUANTITY", "Quantity" );
    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    if( pvMode!=null && pvMode.equalsIgnoreCase("C") )
    {
     scrdata.add("  function submit_form()   {		  \n"+
                 "    var nIdx = 0	 		  \n"+
                 "    with( this.right_frame.document.forms[0] ) {    \n"+
              	 "     if ( vTemplName.options.length>1 {             \n"+ 
                 "       if( vTemplName.selectedIndex == 0 ) {        \n"+
              	 "          alert( \""+ msg.GetMsgDesc( 33, nLangID )+"\" ); vTemplName.focus(); return  \n"+
                 "       }					              	                         \n"+
            	 "       if ( nClientID.selectedIndex == 0 ) {                                           \n"+      	                           
            	 "         alert( \""+ msg.GetMsgDesc( 16, nLangID) +"\" ); nClientID.focus(); return  } \n"+
                 "       for( var i = 0; i < nOrderNo.length; i++)         	                         \n"+
                 "          if( top.IsNull( nOrderNo[i].value ) ){ nIdx++; continue }                    \n"+  
                 "       if ( nIdx == 10 ) { alert( \""+ msg.GetMsgDesc( 38, nLangID) +"\" ); nOrderNo[0].focus(); return  } \n"+
                 "         for( var l = 0; l < nOrderNo.length-1; l++)         	                      \n"+
                 "           for( var m = l+1; m < nOrderNo.length; m++)         	              \n"+
                 "         if( !top.IsNull( nOrderNo[l].value ) && nOrderNo[l].value == nOrderNo[m].value ) {   \n"+
                 "           alert("\"+ msg.GetMsgDesc( 39, nLangID) +"\"); nOrderNo[m].focus(); nOrderNo[m].select();return } \n"+
                 "    }					              	                            \n"+
            	 "    if ( confirm( \""+ msg.GetMsgDesc( 10, nLangID ) +"\" ) )                     \n"+
            	 "      submit()                                                                    \n"+
                 "   } } \n");
    }
    else if( pvMode!=null && ( pvMode.equalsIgnoreCase("I") ) )
    {
     scrdata.add("  function submit_form( pnOption )     {	                                      \n"+
                 "    var aFormFields	                                                              \n"+
                 "    var nIdx = 0	                                                              \n"+
                 "    var vErrMsg	                                                              \n"+
                 "    with( this.right_frame.document.forms[0] ) {                                    \n"+   
             	 "      aField = new Array( \""+vDelvNo+"\", vDeliveryNo.value, \""+vDelvDt+"\", dDeliveryDt.value, \""+nShipFromAddr+"\", shipaddr_id.value ) \n"+
            	 "      vErrMsg = top.check_fields( aField )                                          \n"+ 
            	 "      if ( vErrMsg != \"\" ) {                          	                      \n"+      
            	 "        vErrMsg = \""+ msg.GetMsgDesc( 9, nLangID ) +"\" + vErrMsg + \".\"          \n"+ 
            	 "         alert( vErrMsg ); return                                                   \n"+
            	 "      }                                                                             \n"+  
            	 "      for ( var i=0; i<nPosNo.length-1; i++ ) {                                     \n"+   
            	 "        aFormFields = new Array( \"A\", nPosNo[i].value, \"B\", nQuantity[i].value, \"C\", vBatchNo[i].value, \"D\", dDt1[i].value, \"E\", dDt2[i].value, \"F\", dDt3[i].value, \"G\", vText1[i].value, \"H\", vText2[i].value, \"I\", vText3[i].value, \"J\", vText4[i].value, \"K\", vText5[i].value, \"L\", nNum1[i].value, \"M\", nNum2[i].value, \"N\", nNum3[i].value, \"O\", nNum4[i].value, \"P\", nNum5[i].value, \"Q\", vRemarks1[i].value, \"R\", vRemarks2[i].value, \"S\", vRemarks3[i].value ) \n"+
            	 "        vErrMsg = top.check_fields( aFormFields )                                   \n"+
             	 "        if ( top.isRowBlank( vErrMsg, 19 ) ) { nIdx++; continue; }                  \n"+
             	 "        else {                                                                      \n"+
              	 "          aFormFields = new Array(\""+vPosNr+"\", nPosNo[i].value, \""+vQuantity+"\", nQuantity[i].value ) \n"+
             	 "          vErrMsg = top.check_fields( aFormFields )                                 \n"+  
             	 "          if ( vErrMsg != \"\" ) {                                                  \n"+                                
             	 "            vErrMsg = \""+ msg.GetMsgDesc( 9, nLangID ) +"\" + vErrMsg + \""+ msg.GetMsgDesc( 18, nLangID ) + "\"+(i+1)+\".\" \n"+
             	 "            alert( vErrMsg ); nPosNo[i].focus(); nPosNo[i].select(); return         \n"+
             	 "          }                                                                         \n"+
             	 "        }                                                                           \n"+                                                                      
             	 "     }                                                                              \n"+
             	 "     if ( nIdx == ( nPosNo.length-1 ) ) {                                           \n"+
             	 "       alert( \""+ msg.GetMsgDesc( 17, nLangID) +"\" ); nPosNo[0].focus(); nPosNo[0].select(); return \n"+
             	 "     }                                                                              \n"+
         	 "     for ( var m=1; m<nPosNo.length-1; m++ )                                        \n"+
            	 "       for ( var n=m+1; n<nPosNo.length; n++ )                                      \n"+
            	 "         if( !top.IsNull(nPosNo[m].value) && nPosNo[m].value == nPosNo[n].value ) { \n"+
            	 "           alert( \""+ msg.GetMsgDesc( 34, nLangID ) +"\" ); nPosNo[n].focus(); nPosNo[n].select(); return \n"+
                 "         }                                                                          \n"+
            	 "      if ( confirm( \""+ msg.GetMsgDesc( 10, nLangID )+"\" ) )                      \n"+
            	 "        submit()                                                                    \n"+
                 "  }  } \n" );
    }
    scrdata.add("// End Hidding -->");
    scr.add(scrdata);
    return scr;
  }
}

