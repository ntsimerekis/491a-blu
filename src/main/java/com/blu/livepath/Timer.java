package com.blu.livepath;

public class Timer {

    private long startTime = 0;
    private long pauseTime = 0;
    private long pausedDuration = 0;
    private boolean isRunning = false;
    private boolean isPaused = false;

    public void start() {
        if (!isRunning) {
            startTime = System.currentTimeMillis();
            pausedDuration = 0;
            isRunning = true;
            isPaused = false;
        }
    }

    public void pause() {
        if (isRunning && !isPaused) {
            pauseTime = System.currentTimeMillis();
            isPaused = true;
        }
    }

    public void resume() {
        if (isRunning && isPaused) {
            pausedDuration += System.currentTimeMillis() - pauseTime;
            isPaused = false;
        }
    }

    public void stop() {
        isRunning = false;
        isPaused = false;
    }

    public long getElapsedTime() {
        if (!isRunning) {
            return 0;
        } else if (isPaused) {
            return pauseTime - startTime - pausedDuration;
        } else {
            return System.currentTimeMillis() - startTime - pausedDuration;
        }
    }
}
