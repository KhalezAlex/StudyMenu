package org.klozevitz.services.interfaces.updateProcessors;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface UpdateProcessor<T> {
    SendMessage processUpdate(T t);
}
