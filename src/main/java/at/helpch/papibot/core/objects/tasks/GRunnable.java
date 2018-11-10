package at.helpch.papibot.core.objects.tasks;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------

/**
 * Runnable implementation that provide's easy-to-access utils to specific runnable functions.
 */
public abstract class GRunnable implements Runnable {
    /**
     * Util method to sleep without needing a try/catch.<br/>
     * <i>WARNING: This method is unreliable, may not wake up.</i>
     * @param ms Milliseconds to sleep for before waking up.
     */
    public void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
