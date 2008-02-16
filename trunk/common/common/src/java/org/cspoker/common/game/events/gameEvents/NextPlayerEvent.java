/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.cspoker.common.game.events.gameEvents;

import org.cspoker.common.game.player.Player;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * A class to represent new player events.
 * 
 * @author Kenzo
 * 
 */
public class NextPlayerEvent extends GameEvent {

    private static final long serialVersionUID = -2048233796443189725L;
    
    private final Player player;

    public NextPlayerEvent(Player player) {
	this.player = player;
    }

    public Player getPlayer() {
	return player;
    }

    @Override
    public String toString() {
	return "It's " + player.getName() + "'s turn.";
    }
    
    @Override
    public void toXml(ContentHandler handler) throws SAXException {
	AttributesImpl attrs = new AttributesImpl();
	attrs.addAttribute("", "type", "type", "CDATA", "nextplayer");
	attrs.addAttribute("", "player", "player", "CDATA", getPlayer().getName());
	handler.startElement("", "event", "event", attrs);

	handler.endElement("", "event", "event");
    }
}