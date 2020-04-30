package libpadeljobs;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Master class to add jobs to queue.
 * 
 * @author yapchunwei
 */
public abstract class Master<J extends Job, W extends Worker<J> >
{
    public static int NO_MORE_JOBS = -1;

    protected int maxThreads;
    protected int maxJobsWaiting;
    protected boolean hasWork;
    protected String status;
    protected String[] messages; // Will be cleared by Master.
    protected String[] messagesWorker; // Will be cleared by Worker.
    protected LinkedBlockingQueue<J> jobsWaiting;
    protected LinkedBlockingQueue<J> jobsRunning;
    protected LinkedBlockingQueue<J> jobsCompleted;
    protected ArrayList<W> workers;

    public Master()
    {
        maxThreads = -1;
        maxJobsWaiting = -1;
    }

    public final boolean HasWork()
    {
        return hasWork;
    }

    public final void DoWork()
    {
        // Add new jobs to queue
        AddJobsToQueue();

        // Process running jobs
        ProcessRunningJobs();

        // Process completed jobs
        ProcessCompletedJobs();
    }

    // To be override by extending class but extending class should call super.Initialize();
    public String Initialize()
    {
        if (maxThreads<=0) maxThreads = Runtime.getRuntime().availableProcessors();
        if (maxJobsWaiting<=0) maxJobsWaiting = maxThreads * 50;

        hasWork = true;
        jobsWaiting = new LinkedBlockingQueue<J>(maxJobsWaiting);
        jobsRunning = new LinkedBlockingQueue<J>();
        jobsCompleted = new LinkedBlockingQueue<J>();
        workers = new ArrayList<W>();
        status = new String();
        messages = new String[maxThreads];
        messagesWorker = new String[maxThreads];
        for (int i=0; i<maxThreads; ++i)
        {
            messages[i] = new String();
            messagesWorker[i] = new String();
        }
        return null;
    }

    protected abstract void AddJobsToQueue();

    protected abstract void ProcessRunningJobs();

    protected abstract void ProcessCompletedJobs();

    public void StopAllWorkers()
    {
        if (workers==null) return;
        for (int i=0; i<workers.size(); ++i)
        {
            if (workers.get(i)!=null) workers.get(i).StopWorker();
        }
    }

    public synchronized String getMessage(int channelID)
    {
        if (messages[channelID].isEmpty()) return messages[channelID];
        else return messages[channelID] + "\n";
    }
    
    public synchronized String getMessages()
    {
        String temp = new String();
        for (String message : messages)
        {
            if (!message.isEmpty())
            {
                temp += message + "\n";
            }
        }
        return temp;
    }

    public synchronized String clearMessage(int channelID)
    {
        String temp = new String(messages[channelID]);
        if (!temp.isEmpty()) temp += "\n";
        messages[channelID] = new String();
        return temp;
    }

    public synchronized String clearMessages()
    {
        String temp = new String();
        for (int i=0; i<messages.length; ++i)
        {
            if (!messages[i].isEmpty())
            {
                temp += messages[i] + "\n";
                messages[i] = new String();
            }
        }
        return temp;
    }

    public synchronized void setMessage(int channelID, String messagesFromWorkers)
    {
        messages[channelID] = messagesFromWorkers;
        messagesWorker[channelID] = messagesFromWorkers;
    }

    public synchronized void addMessage(int channelID, String messagesFromWorkers)
    {
        if (!messages[channelID].isEmpty()) messages[channelID] += "\n";
        messages[channelID] += messagesFromWorkers;

        messagesWorker[channelID] = messagesFromWorkers;
    }

    public synchronized String getMessageWorker(int channelID)
    {
        if (messagesWorker[channelID].isEmpty()) return messagesWorker[channelID];
        else return messagesWorker[channelID] + "\n";
    }

    public synchronized String getMessagesWorker()
    {
        String temp = new String();
        for (String message : messagesWorker)
        {
            if (!message.isEmpty())
            {
                temp += message + "\n";
            }
        }
        return temp;
    }

    public String getStatus()
    {
        return status;
    }

    public int getMaxJobsWaiting()
    {
        return maxJobsWaiting;
    }

    public void setMaxJobsWaiting(int maxJobsWaiting)
    {
        this.maxJobsWaiting = maxJobsWaiting;
    }

    public int getMaxThreads()
    {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads)
    {
        this.maxThreads = maxThreads;
    }
}
