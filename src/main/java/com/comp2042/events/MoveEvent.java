package com.comp2042.events;

/**
 * Encapsulates information about a user input event.
 * Contains the type of input action and whether it originated from
 * user interaction or automated game logic (like gravity).
 * 
 * <p>This design allows event handlers to distinguish between
 * player-initiated moves and automatic moves, enabling different
 * behaviors or scoring rules for each.
 * 
 * @see EventType
 * @see EventSource
 * @see InputEventListener
 */
public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Constructs a MoveEvent with specified type and source.
     * 
     * @param eventType the type of input action (left, right, down, etc.)
     * @param eventSource the origin of the event (user or automated)
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    public EventType getEventType() {
        return eventType;
    }

    /**
     * Returns the source indicating who initiated this event.
     * 
     * @return the event source (user input or automated timer)
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}
