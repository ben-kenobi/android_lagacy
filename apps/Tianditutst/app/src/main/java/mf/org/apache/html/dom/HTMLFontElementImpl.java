/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mf.org.apache.html.dom;

import mf.org.w3c.dom.html.HTMLFontElement;

/**
 * @xerces.internal
 * @version $Revision: 1029415 $ $Date: 2010-10-31 13:02:22 -0400 (Sun, 31 Oct 2010) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see mf.org.w3c.dom.html.HTMLFontElement
 * @see mf.org.apache.xerces.dom.ElementImpl
 */
public class HTMLFontElementImpl
    extends HTMLElementImpl
    implements HTMLFontElement
{

    private static final long serialVersionUID = -415914342045846318L; 

    public String getColor()
    {
        return capitalize( getAttribute("build/intermediates/exploded-aar/com.android.support/appcompat-v7/23.1.1/res/color") );
    }
    
    
    public void setColor( String color )
    {
        setAttribute("build/intermediates/exploded-aar/com.android.support/appcompat-v7/23.1.1/res/color", color );
    }
    
    
    public String getFace()
    {
        return capitalize( getAttribute( "face" ) );
    }
    
    
    public void setFace( String face )
    {
        setAttribute( "face", face );
    }
    
    
    public String getSize()
    {
        return getAttribute( "size" );
    }
    
    
    public void setSize( String size )
    {
        setAttribute( "size", size );
    }

    
    public HTMLFontElementImpl( HTMLDocumentImpl owner, String name )
    {
        super( owner, name );
    }
  

}
