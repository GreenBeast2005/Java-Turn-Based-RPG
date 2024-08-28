import java.util.ArrayList;
import java.util.List;

public class MessageBus {
    private static final List<MessageListener> listeners = new ArrayList<>();

    public static void addListener(MessageListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(MessageListener listener) {
        listeners.remove(listener);
    }

    public static void sendMessage(String message, String data) {
        for (MessageListener listener : listeners) {
            listener.onMessageReceived(message, data);
        }
    }
}
