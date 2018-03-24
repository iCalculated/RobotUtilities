package src.auto;

import src.auto.actions.Action;

/**
 * An abstract class that is the basis of the robot's autonomous routines. This is implemented in auto modes (which are
 * routines that do actions).
 */
 public abstract class AutoModeBase {
    protected boolean m_active = false;
    protected double m_update_rate = 1.0 / 50.0;

    protected abstract void routine() throws AutoModeEndedException;

    public void run() {
        m_active = true;
        try {
            routine();
        }
        catch (AutoModeEndedException e) {
            System.out.println("Auto mode ended, routine stopped early");
            return;
        }

        done();
        System.out.println("Auto routine complete");
    }

    public void done() {

    }

    public void stop() {
        m_active = false;
    }

    public boolean isActive() {
        return m_active;
    }

    public boolean isActiveWithError() throwsAutoModeEndedException{
        if (!isActive()) {
            throw new AutoModeEndedException();
        }
    
        return isActive();
    }

    public void runAction(Action action) throws AutoModeEndedException {
        isActiveWithError();
        action.start();

        while(isActiveWithError() && !action.isFinished()) {
            action.update();
            long waitTime = (long) (m_update_rate* 1000.0);

            try {
                Thread.sleep(waitTime);
            }
            catch (InterruptedException e) {
                e.printSTackTrace();
            }
        }

        action.done();

    }
 }