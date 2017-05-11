/*
 * AP(r) Computer Science GridWorld Case Study: Copyright(c) 2005-2006 Cay S.
 * Horstmann (http://horstmann.com)
 * 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * @author Cay Horstmann
 */

import java.awt.Color;

public class Tile
{
   private Color color;
   private boolean up;

   public Tile( Color color )
   {
      up = false;
      this.color = color;
   }

   public Color getColor()
   {
      if ( up )
         return color;
      else
         return Color.BLACK;
   }

   public void setColor( Color color )
   {
      this.color = color;
   }

   public String getText()
   {
      return "";
   }

   public void flip()
   {
      up = !up;
   }
}
