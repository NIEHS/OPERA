package libpadeljobs;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Worker class to work on a job.
 * 
 * @author yapchunwei
 */
public abstract class Worker<J extends Job> extends Thread
{
    protected boolean toStop;
    protected LinkedBlockingQueue<J> jobsWaiting;
    protected LinkedBlockingQueue<J> jobsRunning;
    protected LinkedBlockingQueue<J> jobsCompleted;
    protected J job;

    public Worker(LinkedBlockingQueue<J> jobsWaiting, LinkedBlockingQueue<J> jobsRunning, LinkedBlockingQueue<J> jobsCompleted)
    {
        toStop = false;
        this.jobsWaiting = jobsWaiting;
        this.jobsRunning = jobsRunning;
        this.jobsCompleted = jobsCompleted;
    }

    @Override
    public void run()
    {
        try
        {
            while (true)
            {
                // Retrieve a job; block if the queue is empty.
                job = jobsWaiting.take();

                // Terminate if no more jobs in queue or requested to stop.
                if (toStop || job.getId()==Master.NO_MORE_JOBS) break;

                // Add job to running jobs list.
                job.setStarttime(System.nanoTime());
                jobsRunning.add(job);
                
                // Do job
                DoJob();

                // Remove job from running jobs list and add job to completed jobs list.
                job.setEndtime(System.nanoTime());
                jobsRunning.remove(job);
                jobsCompleted.add(job);
            }
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger("global").log(Level.SEVERE, Integer.toString(job.getId()), ex);
        }
    }

    public void StopWorker()
    {
        toStop = true;
    }

    public abstract void DoJob();
}
