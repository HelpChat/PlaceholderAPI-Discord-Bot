package at.helpch.papibot.core.objects.tasks;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------

import at.helpch.papibot.PapiBot;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Class containing util methods to schedule async and sync tasks.
 */
@Singleton
public final class Task {
    @Inject private static PapiBot main;

    private static final ExecutorService executor = Executors.newFixedThreadPool(10);
    private static final ScheduledExecutorService scheuledExecutor = Executors.newScheduledThreadPool(10);

    /**
     * Schedule an async task on the thread pool.
     * @param task Consumer containing a GRunnable instance.
     * @param threadName Optional parameter to specify a thread name.
     */
    public static void async(final Consumer<GRunnable> task, String... threadName) {
        executor.submit(new GRunnable() {
            @Override
            public void run() {
                if (threadName.length >= 1) Thread.currentThread().setName(threadName[0]);

                task.accept(this);
            }
        });
    }

    /**
     * Schedule a task on the main thread.
     * @param task Consumer containing a GRunnable instance.
     */
    public static void sync(final Consumer<GRunnable> task) {
        main.queue(new GRunnable() {
            @Override
            public void run() {
                task.accept(this);
            }
        });
    }

    /**
     * Schedule an async task on the thread pool that will run after a specific amount of time.
     * @param task Consumer containing a GRunnable instance.
     * @param delay The delay before the task will run.
     * @param timeUnit The TimeUnit the delay will be read as.
     */
    public static void scheduleAsync(final Consumer<GRunnable> task, long delay, TimeUnit timeUnit) {
        scheuledExecutor.schedule(new GRunnable() {
            @Override
            public void run() {
                async(task);
            }
        }, delay, timeUnit);
    }

    /**
     * Schedule an async task on the thread pool that will run after a specific amount of time, then on an interval forever.
     * @param task Consumer containing a GRunnable instance.
     * @param initialDelay Delay before the task will first run.
     * @param delay Interval between the task repeating itself.
     * @param timeUnit The TimeUnit the delay and initialDelay will be read as.
     */
    public static void scheduleAsyncRepeating(final Consumer<GRunnable> task, long initialDelay, long delay, TimeUnit timeUnit) {
        scheuledExecutor.scheduleWithFixedDelay(new GRunnable() {
            @Override
            public void run() {

            }
        }, initialDelay, delay, timeUnit);
    }

    /**
     * Schedule a task on the main thread that will run after a specific amount of time.
     * @param task Consumer containing a GRunnable instance.
     * @param delay Delay before the task will run.
     * @param timeUnit The TimeUnit the delay will be read as.
     */
    public static void scheduleSync(final Consumer<GRunnable> task, long delay, TimeUnit timeUnit) {
        scheduleAsync(r -> sync(task), delay, timeUnit);
    }

    /**
     * Schedule a task on the main thread that will run after a specific amount of time, then on an interval forever.
     * @param task Consumer containing a GRunnable instance.
     * @param initialDelay Delay before the task will first run.
     * @param delay Interval between the task repeating itself.
     * @param timeUnit The TimeUnit the delay and initialDelay wil be read as.
     */
    public static void scheduleSyncRepeating(final Consumer<GRunnable> task, long initialDelay, long delay, TimeUnit timeUnit) {
        scheduleAsyncRepeating(r -> sync(task), initialDelay, delay, timeUnit);
    }

    public static void shutdown() {
        executor.shutdownNow();
    }
}
