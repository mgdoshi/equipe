package ingen.html.frame;

import ingen.html.*;
import java.util.*;

public class NoFrame extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of NoFrames Html Tag, which mark a no-frames section.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;FRAMESET ROWS="rows" COLS="cols" Attribs&gt; and &lt;/FRAMESET&gt;<BR>
      */

      public NoFrame()
      {
         startTag="NOFRAMES";
         endTag="/NOFRAMES";
      }  
}