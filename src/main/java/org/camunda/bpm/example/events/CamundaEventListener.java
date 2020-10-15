package org.camunda.bpm.example.events;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.example.slack.SlackClient;
import org.camunda.bpm.spring.boot.starter.event.TaskEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * On the execution event stream, DelegateExecutions (mutable) and ExecutionEvents (immutable) can be received.
 * The task event stream offers DelegateTasks (mutable) and TaskEvents (immutable).
 * On the history event stream, only HistoryEvents (mutable) are published.
 *
 * @see <a href="https://docs.camunda.org/manual/latest/user-guide/spring-boot-integration/the-spring-event-bridge/"</a>
 */
@Slf4j
@Component
public class CamundaEventListener {

    private final SlackClient slackClient;

    public CamundaEventListener(SlackClient slackClient) {
        this.slackClient = slackClient;
    }

    @EventListener
    public void onTaskEvent(TaskEvent taskEvent) {
        // handle immutable task event
        log.info("TaskEvent listener processing immutable task event. \n TaskDefinitionKey: {} TaskId: {} \n >>> Event Name: {}  \n",
                taskEvent.getTaskDefinitionKey(), taskEvent.getId(), taskEvent.getEventName());
        String message =MessageFormat.format("Task Event - key: {0}, id: {1}, name: {2}", taskEvent.getTaskDefinitionKey(), taskEvent.getId(), taskEvent.getEventName());

        slackClient.sendToChannel(message);
    }

/*
    @EventListener
    public void onExecutionEvent(DelegateExecution executionDelegate) {
        // handle mutable execution event
        log.info("ExecutionEvent listener processing mutable event. \n Current ActivityId: {} ExecutionId: {} \n >>> Event Name: {}  \n",
                executionDelegate.getCurrentActivityId(), executionDelegate.getId(), executionDelegate.getEventName());
    }

    @EventListener
    public void onHistoryEvent(HistoryEvent historyEvent) {
        // handle history event
        log.info("HistoryEvent listener processing mutable event. \n ProcessDefinitionKey: {} ProcessInstanceId: {} \n EventId: {} \n >>> EventType: {} \n",
                historyEvent.getProcessDefinitionKey(), historyEvent.getProcessInstanceId(), historyEvent.getId(), historyEvent.getEventType());
    }

    @EventListener
    public void onExecutionEvent(ExecutionEvent executionEvent) {
        // handle immutable execution event
        log.info("ExecutionEvent listener processing immutable event. \n Current ActivityId: {} ExecutionId: {} \n >>> Event Name: {}  \n",
                executionEvent.getCurrentActivityId(), executionEvent.getId(), executionEvent.getEventName());
    }

    @EventListener
    public void firstTaskEventListener(DelegateTask taskDelegate) {
        // handle mutable task event
        log.info("1st TaskEvent listener processing mutable event. \n TaskDefinitionKey: {} TaskId: {} \n >>> Event Name: {}  \n",
                taskDelegate.getTaskDefinitionKey(), taskDelegate.getId(), taskDelegate.getEventName());
    }*/
}
