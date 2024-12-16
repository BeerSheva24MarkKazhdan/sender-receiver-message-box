package telran.producer.consumer;

public class Main {
    private static final int N_MESSAGES = 20;
    static final int N_RECEIVERS = 10;
    public static void main(String[] args) throws InterruptedException {
        MessageBox messageBox = new SimpleMessageBox();
        Sender sender = new Sender(N_MESSAGES, messageBox);

        Receiver[] receivers = new Receiver[10];
        for (int i = 0; i < N_RECEIVERS; i++) {
            receivers[i] = new Receiver(messageBox);
            receivers[i].start();
        }

        class BackupReceiver extends Thread {
            private MessageBox messageBox;

            public BackupReceiver(MessageBox messageBox) {
                this.messageBox = messageBox;
            }

            @Override
            public void run() {
                while (true) {
                    try {
                        String message = messageBox.take();
                        System.out.printf("Backup Thread processed message: %s\n", message);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }

        BackupReceiver backupReceiver = new BackupReceiver(messageBox);
        backupReceiver.start();

        sender.start();
        sender.join();

        for (Receiver receiver : receivers) {
            receiver.interrupt();
            receiver.join();
        }

        backupReceiver.interrupt();
        backupReceiver.join();

        System.out.println("All threads stopped. Program finished.");
    }
}
