package org.klozevitz.services.interfaces.updateProcessors;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@FunctionalInterface
public interface UpdateProcessor<V, W> {
    SendMessage processUpdate(V v, W w);
}
