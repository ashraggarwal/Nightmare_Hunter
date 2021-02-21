import java.io.IOException;

public class DataCheck {
    private DataListener dataListener;

    public void registerDataListener(DataListener dataListener) {
        this.dataListener = dataListener;
    }
    public void doDataEvent() {
        new Thread(new Runnable() {
            public void run() {
                if (dataListener != null) {
                    try {
                        while(true){
                            dataListener.receiveData();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}