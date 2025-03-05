package org.klozevitz.util;

import org.klozevitz.enitites.appUsers.MessageSent;

import java.util.Comparator;

public class MessageComparator implements Comparator<MessageSent> {
    @Override
    public int compare(MessageSent o1, MessageSent o2) {
        return Integer.compare(o1.getMessage().getMessageId(), o2.getMessage().getMessageId());
    }
}
