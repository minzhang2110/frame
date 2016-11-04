package com.zm.frame.thread.task;

import com.zm.frame.conf.Definition;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.msg.ThreadMsgBody;
import com.zm.frame.thread.server.Server;
import com.zm.frame.thread.thread.BasicThread;

/**
 * Created by Administrator on 2016/11/4.
 */
public abstract class Task {

    private Server server = Server.getInstance();

    private final int taskId;
    private final BasicThread thread;

    public Task(int taskId, BasicThread thread) {
        this.taskId = taskId;
        this.thread = thread;
    }

    public abstract void processMsg(ThreadMsg msg);

    // 所有参数，从task发送，发往task
    protected void sendThreadMsgTo
    (int msgType, ThreadMsgBody msgBody, int desThreadType, int desThreadId, int desTaskId) {
        ThreadMsg msg = new ThreadMsg(thread.getThreadType(), thread.getThreadId(), this.taskId,
                desThreadType, desThreadId, desTaskId, msgType, msgBody);
        server.sendThreadMsgTo(msg);
    }

    //发送消息，发往非task
    protected void sendThreadMsgTo
    (int msgType, ThreadMsgBody msgBody, int desThreadType, int desThreadId) {
        this.sendThreadMsgTo(msgType, msgBody, desThreadType, desThreadId, Definition.NONE);
    }

    //发送消息，发往非task，不指定thread id
    protected void sendThreadMsgTo
    (int msgType, ThreadMsgBody msgBody, int desThreadType) {
        this.sendThreadMsgTo(msgType, msgBody, desThreadType, Definition.NONE, Definition.NONE);
    }

    //回包
    protected void replayThreadMsg(ThreadMsg msg, int msgType, ThreadMsgBody msgBody) {
        ThreadMsg replyMsg = new ThreadMsg(thread.getThreadType(), thread.getThreadId(), this.taskId,
                msg.srcThreadType, msg.srcThreadId, msg.srcTaskId, msgType, msgBody);
        server.sendThreadMsgTo(replyMsg);
    }

    protected void remove() {
        thread.removeTask(this.taskId);
    }
}
