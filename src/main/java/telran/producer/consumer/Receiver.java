package telran.producer.consumer;

public class Receiver extends Thread {
    private MessageBox messageBox;

    public Receiver(MessageBox messageBox) {
        this.messageBox = messageBox;
        setDaemon(false);
    }

    public void setMessageBox(MessageBox messageBox) {
        this.messageBox = messageBox;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = messageBox.take();
                int messageNumber = Integer.parseInt(message.replace("Message", ""));
                int threadNumber = Integer.parseInt(getName().replace("Thread-", ""));

                if (messageNumber % 2 != threadNumber % 2) {
                    System.out.printf("Thread: %s cannot process message: %s. Returning back.\n", getName(), message);
                    messageBox.put(message);
                    continue;
                }

                System.out.printf("Thread: %s, processed message: %s\n", getName(), message);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

}