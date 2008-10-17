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
package org.cspoker.common.api.chat.listener;

import java.util.List;

import org.cspoker.common.api.chat.event.MessageEvent;
import org.cspoker.common.api.shared.listener.ForwardingListener;

public class ForwardingChatListener extends ForwardingListener<ChatListener> implements ChatListener {

	public ForwardingChatListener() {
		super();
	}
	
	public ForwardingChatListener(List<ChatListener> listeners) {
		super(listeners);
	}
	
	public void onMessage(MessageEvent messageEvent) {
		for(ChatListener listener:listeners){
			listener.onMessage(messageEvent);
		}
	}
}
