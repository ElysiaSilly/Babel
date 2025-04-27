package com.elysiasilly.babel.api.task;

import com.elysiasilly.babel.Babel;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TaskQueue {

    // TODO, more testing, this feels fragile

    private Long previousTick = null;
    private final Level level;
    private final List<Task> que = new ArrayList<>();
    private final HashMap<UUID, Task> uuidTaskMap = new HashMap<>();

    public TaskQueue(Level level) {
        this.level = level;
    }

    private List<Task> que() {
        return this.que;
    }

    private HashMap<UUID, Task> map() {
        return this.uuidTaskMap;
    }

    private Level level() {
        return this.level;
    }

    private void add(Task task) {
        que().add(task);
        map().put(task.uuid(), task);
    }

    private UUID scheduleMinutes(int minutes, Runnable task) {
        Task temp = new Task(task, this, (minutes * 20) * 60);
        add(temp);
        return temp.uuid();
    }

    private UUID scheduleSeconds(int seconds, Runnable task) {
        Task temp = new Task(task, this, seconds * 20);
        add(temp);
        return temp.uuid();
    }

    private UUID scheduleTicks(int ticks, Runnable task) {
        Task temp = new Task(task, this, ticks);
        add(temp);
        return temp.uuid();
    }

    public static UUID scheduleTicks(Level level, int ticks, Runnable task) {
        return TaskQueueLevelMap.que(level).scheduleTicks(ticks, task);
    }

    public static UUID scheduleSeconds(Level level, int seconds, Runnable task) {
        return TaskQueueLevelMap.que(level).scheduleSeconds(seconds, task);
    }

    public static UUID scheduleMinutes(Level level, int minutes, Runnable task) {
        return TaskQueueLevelMap.que(level).scheduleMinutes(minutes, task);
    }

    public void tick() {
        long currentTick = level().getGameTime();

        if(this.previousTick == null) this.previousTick = currentTick;

        long tickDifference = currentTick - this.previousTick;

        for(int t = 0; t < tickDifference; t++) {
            for(Task task : new ArrayList<>(que())) {
                task.tick();
            }
        }

        this.previousTick = currentTick;
    }

    public static boolean taskStillInQue(Level level, UUID uuid) {
        return TaskQueueLevelMap.que(level).map().containsKey(uuid);
    }

    public static int remainingTicksForTask(Level level, UUID uuid) {
        return TaskQueueLevelMap.que(level).map().get(uuid).remainingTicks();
    }

    private void remove(Task task) {
        que().remove(task);
        map().remove(task.uuid());
        Babel.LOGGER.info("Removed Task, {} remaining.", que.size());
    }

    public static class Task {
        private final Runnable task;

        private final TaskQueue que;
        private final UUID uuid = UUID.randomUUID();

        private final int ticks;
        private int currentTicks = 0;

        public Task(Runnable task, TaskQueue que, int ticks) {
            this.task = task;
            this.que = que;
            this.ticks = ticks;
        }

        public void tick() {
            if(this.currentTicks >= this.ticks) {
                run();
                remove();
            }

            this.currentTicks++;
        }

        private int remainingTicks() {
            return this.ticks - this.currentTicks;
        }

        private void run() {
            task.run();
        }

        private void remove() {
            que.remove(this);
        }

        private UUID uuid() {
            return this.uuid;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Task other && uuid.equals(other.uuid);
        }
    }
}
