//package org.carleton;
//
//import org.springframework.stereotype.Service;
//import java.util.concurrent.LinkedBlockingQueue;
//
//@Service
//public class GameService {
//    private final LinkedBlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
//    private final StringBuilder outputBuffer = new StringBuilder();
//
//    public void handleInput(String input) {
//        inputQueue.offer(input);
//    }
//
//    public String getNextInput() throws InterruptedException {
//        return inputQueue.take();
//    }
//
//    public void appendOutput(String message) {
//        synchronized (outputBuffer) {
//            outputBuffer.append(message).append("\n");
//        }
//    }
//
//    public String getOutput() {
//        synchronized (outputBuffer) {
//            String output = outputBuffer.toString();
//            outputBuffer.setLength(0);
//            return output;
//        }
//    }
//}
